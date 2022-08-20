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
import com.example.organizzeleo.model.Carteira;
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
    private DatabaseReference firebaseRef2 = ConfiguracaoFirebase.getFirebaseDatabase();
    Carteira carteira;


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

                Usuario usuarioBD = snapshot.getValue(Usuario.class);



                textoNome.setText("Olá, "+usuarioBD.getNome());

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
                            }else{
                                salvarSaldo();
                                //textoSaldo.setText("R$"+carteira.getSaldo());
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


        DatabaseReference usuarioRef2 = firebaseRef.child("Carteira").child(idUsuario);

        usuarioRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               Carteira carteira = snapshot.getValue(Carteira.class);



                           // carteira.setSaldo(0.0);

                            textoSaldo.setText("R$" + carteira.getSaldo().toString());
                            carteira.setETH(carteira.getETH());
                            carteira.setBTC(carteira.getBTC());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;


        }

    public void salvarSaldo(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String email = autenticacao.getCurrentUser().getEmail();

        String idUsuario = Base64Custom.codificarBase64( email );

        DatabaseReference usuarioRef2 = firebaseRef.child("Carteira").child(idUsuario);

        usuarioRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Carteira carteira = snapshot.getValue(Carteira.class);



                // carteira.setSaldo(0.0);

                textoSaldo.setText("R$" + carteira.getSaldo().toString());
                carteira.setETH(carteira.getETH());
                carteira.setBTC(carteira.getBTC());
                carteira.setADA(carteira.getADA());
                carteira.setAPE(carteira.getAPE());
                carteira.setAXS(carteira.getAXS());
                carteira.setDOGE(carteira.getDOGE());
                carteira.setLTC(carteira.getLTC());
                carteira.setMPL(carteira.getMPL());
                carteira.setSHIB(carteira.getSHIB());
                carteira.setSLP(carteira.getSLP());
                carteira.setSOL(carteira.getSOL());
                carteira.setTRB(carteira.getTRB());
                carteira.setUSDC(carteira.getUSDC());
                carteira.setXRP(carteira.getXRP());
                carteira.setXTZ(carteira.getXTZ());
                carteira.setSaldo(Double.parseDouble(campoSaldo.getText().toString()));
                carteira.Salvar();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //carteira = new Carteira();

    }
        }
