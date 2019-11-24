package com.example.letimendes.padlock.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.letimendes.padlock.R;
import com.example.letimendes.padlock.util.ViewDialog;

public class SincronizarFragment extends Fragment implements View.OnClickListener {

    ViewDialog viewDialog;
    Button btnSicronizar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sincronizacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDialog = new ViewDialog(getActivity());
        btnSicronizar = view.findViewById(R.id.btnSincronizar);
        btnSicronizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, new HomeFragment());
                ft.commit();
                viewDialog.hideDialog();
            }
        }, 6000);
    }
}
