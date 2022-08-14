package com.example.organizzeleo.navigation.ui.minhaconta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.organizzeleo.activity.LoginActivity;
import com.example.organizzeleo.activity.MainActivity;
import com.example.organizzeleo.activity.PreLoginActivity;
import com.example.organizzeleo.config.ConfiguracaoFirebase;
import com.example.organizzeleo.helper.Base64Custom;
import com.example.organizzeleo.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FederatedAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MinhaContaFragment extends Fragment {

    private TextView textoNome,textoEmail;
    private Button botaoDeslogar;
    private Button botaoExcluir;



        private FirebaseAuth deslogar;
        private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    public MinhaContaFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        /*Intent intentSenha = new Intent();
        final String senha = intentSenha.getStringExtra("Nome");
        System.out.println(senha);*/


            final FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            String email = autenticacao.getCurrentUser().getEmail();
        textoNome = view.findViewById(R.id.textoNome);
        textoEmail = view.findViewById(R.id.textEmail);
        textoEmail.setText(email);


        String idUsuario = Base64Custom.codificarBase64( email );
        final DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);


        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

             Usuario usuario = snapshot.getValue(Usuario.class);



             textoNome.setText(usuario.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        botaoDeslogar = view.findViewById(R.id.ButtonLogout);
        botaoDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setTitle("Deseja mesmo encerrar sua sessão?");
                dialog.setMessage("Você terá de logar novamente");
                dialog.setPositiveButton("Deslogar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deslogar = ConfiguracaoFirebase.getFirebaseAutenticacao();
                        deslogar.signOut();
                        Toast.makeText(getContext(), "Deslogado com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                    dialog.show();
            }
        });
        botaoExcluir = view.findViewById(R.id.ButtonExcluir);
        botaoExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Deseja prosseguir com a ação?");
                dialog.setMessage("Se prosseguir com a ação sua conta será excluida e você " +
                        "deverá criar ou entrar com outra conta.");
                dialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                      /*  AuthCredential credential = EmailAuthProvider.getCredential(autenticacao.getCurrentUser().getEmail());
                        autenticacao.getCurrentUser().reauthenticate(credential).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Não foi possivel autenticar, faça lofin novamente", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                            }
                        });
*/

                        autenticacao.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Conta Deletada",
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent( getActivity(), MainActivity.class);
                                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getActivity(), "Por favor, encerre a sessão e logue novamente, depois exclua sua conta",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });





        return view;

    }



}

