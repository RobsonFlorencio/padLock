/*
    Video: https://www.youtube.com/watch?v=oCMOYS71NIU
    Based on Neil Kolban example for IDF: https://github.com/nkolban/esp32-snippets/blob/master/cpp_utils/tests/BLE%20Tests/SampleNotify.cpp
    Ported to Arduino ESP32 by Evandro Copercini

   Create a BLE server that, once we receive a connection, will send periodic notifications.
   The service advertises itself as: 86225306-0968-11ea-8d71-362b9e155667
   Has a characteristic of: 6E400002-B5A3-F393-E0A9-E50E24DCCA9E - used for receiving data with "WRITE" 
   Has a characteristic of: 6E400003-B5A3-F393-E0A9-E50E24DCCA9E - used to send data with  "NOTIFY"

   The design of creating the BLE server is:
   1. Create a BLE Server
   2. Create a BLE Service
   3. Create a BLE Characteristic on the Service
   4. Create a BLE Descriptor on the characteristic
   5. Start the service.
   6. Start advertising.

   In this example rxValue is the data received (only accessible inside that function).
   And txValue is the data to be sent, in this example just a byte incremented every second. 
*/
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include <WiFi.h>
#include <HTTPClient.h>
 
const char* ssid = "RafaelArthur";
const char* password =  "meupia03";
//const char* ssid = "iPhone";
//const char* password =  "senhavila1";

// Motor A
int motor1Pin1 = 27; 
int motor1Pin2 = 26; 

BLECharacteristic *pCharacteristic;
bool deviceConnected = false;
float txValue = 0;
const int readPin = 32; // Use GPIO number. See ESP32 board pinouts
const int LED = 2; // Could be different depending on the dev board. I used the DOIT ESP32 dev board.

//std::string rxValue; // Could also make this a global var to access it in loop()

// See the following for generating UUIDs:
// https://www.uuidgenerator.net/

#define SERVICE_UUID           "86225306-0968-11ea-8d71-362b9e155667" // UART service UUID
#define CHARACTERISTIC_UUID_RX "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
#define CHARACTERISTIC_UUID_TX "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"

class MyServerCallbacks: public BLEServerCallbacks {
    void onConnect(BLEServer* pServer) {
      deviceConnected = true;
    };

    void onDisconnect(BLEServer* pServer) {
      deviceConnected = false;
    }
};

class MyCallbacks: public BLECharacteristicCallbacks {
    void onWrite(BLECharacteristic *pCharacteristic) {
      std::string rxValue = pCharacteristic->getValue();

      if (rxValue.length() > 0) {
        Serial.println("*********");
        Serial.print("Received Value: ");
        for (int i = 0; i < rxValue.length(); i++) {
          Serial.print(rxValue[i]);
        }
        Serial.print("Dados recebidos: ");
        Serial.println(rxValue.data());

        Serial.print("WiFi.status(): ");
        Serial.println(WiFi.status());
        if(WiFi.status()== WL_CONNECTED){
         // Do stuff based on the command received from the app
             HTTPClient http;   
 
             http.begin("http://192.168.1.15:8080/acesso/pessoa");  //Specify destination for HTTP request
             http.addHeader("Content-Type", "application/json");             //Specify content-type header
           
             int httpResponseCode = http.POST(rxValue.data());   //Send the actual POST request
           
             if(httpResponseCode>0){
           
              String response = http.getString();                       //Get the response to the request
           
              Serial.println(httpResponseCode);   //Print return code
              Serial.println(response);           //Print request answer
              if(response.equals("true")){
                // Acionar o motor
                Serial.print("Acionando o motor!");

                Serial.print("Abrindo!");
                  digitalWrite(motor1Pin1, LOW);
                  digitalWrite(motor1Pin2, HIGH);
                  delay(2000);
                
                Serial.print("Fechando!");
                  digitalWrite(motor1Pin1, HIGH);
                  digitalWrite(motor1Pin2, LOW); 
                  delay(2000);

                Serial.print("Fechada!");
                  digitalWrite(motor1Pin1, LOW);
                  digitalWrite(motor1Pin2, LOW);
              }else{
                Serial.println("Turning ON!");
                digitalWrite(LED, HIGH);

                delay(2000);

                Serial.println("Turning OFF!");
                digitalWrite(LED, LOW);
              }
           
             }else{
           
              Serial.print("Error on sending POST: ");
              Serial.println(httpResponseCode);

              Serial.println("Turning ON!");
              digitalWrite(LED, HIGH);

              delay(2000);

              Serial.println("Turning OFF!");
              digitalWrite(LED, LOW);
           
             }
           
             http.end();  //Free resources
        }
 // Teste para verificar a conexão, acender e apagar LED
/*        if (rxValue.find("A") != -1) { 
          Serial.print("Turning ON!");
          digitalWrite(LED, HIGH);
        }
        else if (rxValue.find("B") != -1) {
          Serial.print("Turning OFF!");
          digitalWrite(LED, LOW);
        }
*/
        Serial.println();
        Serial.println("*********");
      }
    }
};

void setup() {
  // sets the pins as outputs:
  pinMode(motor1Pin1, OUTPUT);
  pinMode(motor1Pin2, OUTPUT);
  
  Serial.begin(115200);
  delay(4000);   //Delay needed before calling the WiFi.begin
 
  WiFi.begin(ssid, password); 
 
  while (WiFi.status() != WL_CONNECTED) { //Check for the connection
    delay(1000);
    Serial.println("Connecting to WiFi..");
  }
 
  Serial.println("Connected to the WiFi network");

  pinMode(LED, OUTPUT);

  // Create the BLE Device
  BLEDevice::init("ESP32 UART Test"); // Give it a name

  // Create the BLE Server
  BLEServer *pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());

  // Create the BLE Service
  BLEService *pService = pServer->createService(SERVICE_UUID);

  // Create a BLE Characteristic
  pCharacteristic = pService->createCharacteristic(
                      CHARACTERISTIC_UUID_TX,
                      BLECharacteristic::PROPERTY_NOTIFY
                    );
                      
  pCharacteristic->addDescriptor(new BLE2902());

  BLECharacteristic *pCharacteristic = pService->createCharacteristic(
                                         CHARACTERISTIC_UUID_RX,
                                         BLECharacteristic::PROPERTY_WRITE
                                       );

  pCharacteristic->setCallbacks(new MyCallbacks());

  // Start the service
  pService->start();

  // Start advertising
  pServer->getAdvertising()->start();
  Serial.println("Waiting a client connection to notify...");
}

void loop() {
  if (deviceConnected) {
    // Fabricate some arbitrary junk for now...
    txValue = analogRead(readPin) / 3.456; // This could be an actual sensor reading!

    // Let's convert the value to a char array:
    char txString[8]; // make sure this is big enuffz
    dtostrf(txValue, 1, 2, txString); // float_val, min_width, digits_after_decimal, char_buffer
    
//    pCharacteristic->setValue(&txValue, 1); // To send the integer value
//    pCharacteristic->setValue("Hello!"); // Sending a test message
    pCharacteristic->setValue(txString);
    
    pCharacteristic->notify(); // Send the value to the app!
    Serial.print("*** Sent Value: ");
    Serial.print(txString);
    Serial.println(" ***");

    // You can add the rxValue checks down here instead
    // if you set "rxValue" as a global var at the top!
    // Note you will have to delete "std::string" declaration
    // of "rxValue" in the callback function.
//    if (rxValue.find("A") != -1) { 
//      Serial.println("Turning ON!");
//      digitalWrite(LED, HIGH);
//    }
//    else if (rxValue.find("B") != -1) {
//      Serial.println("Turning OFF!");
//      digitalWrite(LED, LOW);
//    }
  }
  delay(1000);
}
