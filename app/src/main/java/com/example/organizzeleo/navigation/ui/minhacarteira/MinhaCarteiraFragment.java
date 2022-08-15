package com.example.organizzeleo.navigation.ui.minhacarteira;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.organizzeleo.config.ConfiguracaoFirebase;
import com.example.organizzeleo.helper.Base64Custom;
import com.example.organizzeleo.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class MinhaCarteiraFragment extends Fragment {

    private Usuario usuario;

    private TextInputEditText campoSaldo;
    private TextView textoSaldo,textoNome;
    private Button botaoSalvar;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textoNome = view.findViewById(R.id.textNomeMinhaCarteira);

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String email = autenticacao.getCurrentUser().getEmail();

        String idUsuario = Base64Custom.codificarBase64( email );
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue(Usuario.class);



                textoNome.setText("Olá, "+usuario.getNome());
                usuario.setSaldo(campoSaldo.getText().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        campoSaldo = view.findViewById(R.id.campoSaldo);
        textoSaldo = view.findViewById(R.id.textoSaldo);
        botaoSalvar = view.findViewById(R.id.botaoSalvarSaldo);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Deseja alterar seu saldo?");
                        dialog.setMessage("Saldo atual será substituido");
                    dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String valorSaldo = campoSaldo.getText().toString();

                            if(valorSaldo == null || valorSaldo == "" || valorSaldo.isEmpty()){
                                Toast.makeText(getContext(),"Digite um Saldo Primeiro",Toast.LENGTH_SHORT).show();
                                //salvarSaldo();
                            }else{
                                textoSaldo.setText("R$"+valorSaldo);
                                Toast.makeText(getContext(),"Saldo alterado com sucesso",Toast.LENGTH_SHORT).show();

                            }


                        }
                    });

                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }) ;


                    dialog.show();
            }
        });




        return view;
        }





    }
