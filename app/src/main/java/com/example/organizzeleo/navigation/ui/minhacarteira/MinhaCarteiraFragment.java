package com.example.organizzeleo.navigation.ui.minhacarteira;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.organizzeleo.R;
import com.example.organizzeleo.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;


public class MinhaCarteiraFragment extends Fragment {

    private Usuario usuario;

    private TextInputEditText campoSaldo;
    private TextView textoSaldo;
    private Button botaoSalvar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);




        campoSaldo = view.findViewById(R.id.campoSaldo);
        textoSaldo = view.findViewById(R.id.textoSaldo);
        botaoSalvar = view.findViewById(R.id.botaoSalvarSaldo);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valorSaldo = campoSaldo.getText().toString();

                if(valorSaldo == null || valorSaldo == "" || valorSaldo.isEmpty()){
                    Toast.makeText(getContext(),"Digite um Saldo Primeiro",Toast.LENGTH_SHORT).show();
                }else{
                    textoSaldo.setText("R$"+valorSaldo);
                    Toast.makeText(getContext(),"Saldo alterado com sucesso",Toast.LENGTH_SHORT).show();
                }

            }
        });




        return view;
        }
    }
