package com.example.organizzeleo.navigation.ui.minhaconta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.organizzeleo.R;
import com.example.organizzeleo.activity.LoginActivity;
import com.example.organizzeleo.config.ConfiguracaoFirebase;
import com.example.organizzeleo.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MinhaContaFragment extends Fragment {

    private Usuario usuario;

    private TextView textoNome,textoEmail;

    private Button botaoDeslogar;

    public MinhaContaFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

            final FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            String email = autenticacao.getCurrentUser().getEmail();



        usuario = new Usuario();
        usuario.getNome();

        textoNome = view.findViewById(R.id.textoNome);
        textoEmail = view.findViewById(R.id.textEmail);
        textoEmail.setText(email);




        botaoDeslogar = view.findViewById(R.id.ButtonLogout);
        botaoDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), LoginActivity.class);
               startActivity(intent);
            }
        });


        return view;

    }



    }

