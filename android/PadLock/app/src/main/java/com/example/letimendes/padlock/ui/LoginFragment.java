package com.example.letimendes.padlock.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.letimendes.padlock.R;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements View.OnClickListener {

    TextInputLayout txtEmail;
    TextInputEditText edtEmail;
    TextInputLayout txtSenha;
    TextInputEditText edtSenha;
    Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtEmail = view.findViewById(R.id.txtEmail);
        edtEmail = view.findViewById(R.id.edtEmail);
        txtSenha = view.findViewById(R.id.txtSenha);
        edtSenha = view.findViewById(R.id.edtSenha);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (edtSenha.getText() != null && edtSenha.getText().toString() != ""
        && edtEmail.getText() != null && edtEmail.getText().toString() != "") {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("PADLOCK", MODE_PRIVATE).edit();
            editor.putString("email", edtEmail.getText().toString());
            editor.putString("senha", edtSenha.getText().toString());
            editor.apply();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new SincronizarFragment());
            ft.commit();
        }else{
            Toast.makeText(getActivity(), "Favor preencher os campos obrigatórios", Toast.LENGTH_SHORT).show();
        }

    }
}
