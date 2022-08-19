package com.example.organizzeleo.navigation.ui.investir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.organizzeleo.API.HttpService;
import com.example.organizzeleo.API.Resposta;
import com.example.organizzeleo.R;
import com.example.organizzeleo.config.ConfiguracaoFirebase;
import com.example.organizzeleo.helper.Base64Custom;
import com.example.organizzeleo.model.Carteira;
import com.example.organizzeleo.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Double.parseDouble;


public class InvestirFragment extends Fragment {

    Spinner spinnerCoin;
    TextView textoRespostaSpinner, textoNome,textoSaldo, textoQuantidadeMoedas;
    EditText campoQuantidade;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        textoNome = root.findViewById(R.id.textoNomeInvestir);
        textoSaldo = root.findViewById(R.id.textoSaldoInvestir);
        campoQuantidade = root.findViewById(R.id.editQuantidadeCompra);
        textoQuantidadeMoedas = root.findViewById(R.id.textQuantidadeMoeda);


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


        DatabaseReference usuarioRef2 = firebaseRef.child("Carteira").child(idUsuario);

        usuarioRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Carteira carteira = snapshot.getValue(Carteira.class);

                // carteira.setSaldo(0.0);

                textoSaldo.setText("R$" + carteira.getSaldo().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        spinnerCoin = root.findViewById(R.id.spinnerMoedas);
        textoRespostaSpinner = root.findViewById(R.id.textRespostaSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.spinner_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoin.setAdapter(adapter);
        spinnerCoin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String coin = parent.getItemAtPosition(position).toString();
                try {
                    Resposta retorno = new HttpService(coin).execute().get();

                    Double retornoDouble = Double.parseDouble(retorno.toString());
                    DecimalFormat formato = new DecimalFormat("0.###");
                  String resultadoFormatado = formato.format(retornoDouble);

                    textoRespostaSpinner.setText("Preço Atual: R$"+resultadoFormatado);
                    System.out.println(retorno.toString());

                    String quantiaString = campoQuantidade.getText().toString();
                    if(quantiaString.equals("") || quantiaString.isEmpty() ){
                        Toast.makeText(getContext(), "Escolha uma quantidade", Toast.LENGTH_SHORT).show();
                    }else {
                        Double quantiaDouble = Double.parseDouble(quantiaString);
                        Double moedasCompradas = (quantiaDouble / retornoDouble);

                        String moedasCompradasFormatado = formato.format(moedasCompradas);
                        textoQuantidadeMoedas.setText(moedasCompradasFormatado + " "+ coin);
                    }


                }catch(InterruptedException e){e.printStackTrace();}
                catch (ExecutionException e){e.printStackTrace();}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        return root;

            }


    }
