package com.example.organizzeleo.navigation.ui.investir;

import android.app.AlertDialog;
import android.app.UiAutomation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.example.organizzeleo.navigation.ui.minhacarteira.MinhaCarteiraFragment;
import com.example.organizzeleo.navigation.ui.minhaconta.MinhaContaFragment;
import com.google.android.material.snackbar.Snackbar;
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

    private Spinner spinnerCoin;
    private TextView textoRespostaSpinner, textoNome,textoSaldo, textoQuantidadeMoedas;
    private EditText campoQuantidade;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private Button botaoComprar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        botaoComprar = root.findViewById(R.id.buttonComprar);
        textoNome = root.findViewById(R.id.textoNomeInvestir);
        textoSaldo = root.findViewById(R.id.textoSaldoInvestir);
        campoQuantidade = root.findViewById(R.id.editQuantidadeCompra);
        textoQuantidadeMoedas = root.findViewById(R.id.textQuantidadeMoeda);


        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String email = autenticacao.getCurrentUser().getEmail();

       final String idUsuario = Base64Custom.codificarBase64( email );
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
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                final String coin = parent.getItemAtPosition(position).toString();




                if(position ==0){
                        textoRespostaSpinner.setText("");
                }
                else {
                    campoQuantidade.setText(null);
                    try {
                        Resposta retorno = new HttpService(coin).execute().get();

                        final Double retornoDouble = Double.parseDouble(retorno.toString());
                        final DecimalFormat formato = new DecimalFormat("0.####");
                        String resultadoFormatado = formato.format(retornoDouble);

                        textoRespostaSpinner.setText("Preço Atual: R$" + resultadoFormatado);
                        System.out.println(retorno.toString());

                        campoQuantidade.addTextChangedListener(new TextWatcher() {



                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String quantiaString = campoQuantidade.getText().toString();
                                if (quantiaString.equals("") || quantiaString.isEmpty()) {
                                    textoQuantidadeMoedas.setText("");

                                } else {





                                    final Double quantiaDouble = Double.parseDouble(quantiaString);
                                    final Double moedasCompradas = (quantiaDouble / retornoDouble);

                                    final String moedasCompradasFormatado = formato.format(moedasCompradas);
                                    textoQuantidadeMoedas.setText(moedasCompradasFormatado + "" + coin+"?");


                                    DatabaseReference carteiraRef = firebaseRef.child("Carteira").child(idUsuario);

                                    carteiraRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            final Carteira carteira = snapshot.getValue(Carteira.class);

                                            // carteira.setSaldo(0.0);

                                                botaoComprar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if(campoQuantidade.getText().toString().equals("") || campoQuantidade.getText().toString().isEmpty())  {
                                                            Toast.makeText(getContext(),"Digite a Quantia Desejada",Toast.LENGTH_SHORT);
                                                        }else{
                                                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                                                        alertDialog.setTitle("Deseja Efetuar a compra?");
                                                        alertDialog.setMessage("O valor será subtraido do seu saldo, deseja continuar?");
                                                        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                if (carteira.getSaldo() >= quantiaDouble) {
                                                                    if (position == 1) {
                                                                        carteira.setBTC(moedasCompradas + carteira.getBTC());
                                                                    }
                                                                    if (position == 2) {
                                                                        carteira.setETH(moedasCompradas + carteira.getETH());
                                                                    }
                                                                    if (position == 3) {
                                                                        carteira.setSLP(moedasCompradas + carteira.getSLP());
                                                                    }
                                                                    if (position == 4) {
                                                                        carteira.setSOL(moedasCompradas + carteira.getSOL());
                                                                    }
                                                                    if (position == 5) {
                                                                        carteira.setMPL(moedasCompradas + carteira.getMPL());
                                                                    }
                                                                    if (position == 6) {
                                                                        carteira.setDOGE(moedasCompradas + carteira.getDOGE());
                                                                    }
                                                                    if (position == 7) {
                                                                        carteira.setAXS(moedasCompradas + carteira.getAXS());
                                                                    }
                                                                    if (position == 8) {
                                                                        carteira.setAPE(moedasCompradas + carteira.getAPE());
                                                                    }
                                                                    if (position == 9) {
                                                                        carteira.setADA(moedasCompradas + carteira.getADA());
                                                                    }
                                                                    if (position == 10) {
                                                                        carteira.setLTC(moedasCompradas + carteira.getLTC());
                                                                    }
                                                                    if (position == 11) {
                                                                        carteira.setSHIB(moedasCompradas + carteira.getSHIB());
                                                                    }
                                                                    if (position == 12) {
                                                                        carteira.setTRB(moedasCompradas + carteira.getTRB());
                                                                    }
                                                                    if (position == 13) {
                                                                        carteira.setXRP(moedasCompradas + carteira.getXRP());
                                                                    }
                                                                    if (position == 14) {
                                                                        carteira.setXTZ(moedasCompradas + carteira.getXTZ());
                                                                    }
                                                                    if (position == 15) {
                                                                        carteira.setUSDC(moedasCompradas + carteira.getUSDC());
                                                                    }

                                                                    Double saldoNovo = carteira.getSaldo() - quantiaDouble;
                                                                    carteira.setSaldo(saldoNovo);
                                                                    textoSaldo.setText("R$" + saldoNovo.toString());


                                                                    carteira.Salvar();


                                                                    Toast.makeText(getContext(), "Compra efetuada com sucesso", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(getContext(), "Saldo Insuficiente vá para sua carteira", Toast.LENGTH_LONG).show();
                                                                /*Snackbar mySnackbar = Snackbar.make(getView(),
                                                                        "Tal", Snackbar.LENGTH_SHORT);
                                                                mySnackbar.setAction("Ir para sua carteira?", new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        Intent intent = new Intent(getActivity(), MinhaCarteiraFragment.class);
                                                                        startActivity(intent);
                                                                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    }
                                                                });
                                                                mySnackbar.show();*/
                                                                }


                                                            }
                                                        });

                                                        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                        alertDialog.show();
                                                    }
                                                        }
                                                });


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });





                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        return root;

            }


    }
