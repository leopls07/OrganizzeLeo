package com.example.organizzeleo.navigation.ui.minhaconta;

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
import com.example.organizzeleo.config.ConfiguracaoFirebase;
import com.example.organizzeleo.helper.Base64Custom;
import com.example.organizzeleo.model.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MinhaContaFragment extends Fragment {

    private TextView textoNome,textoEmail;
    private Button botaoDeslogar;



        private FirebaseAuth deslogar;
        private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    public MinhaContaFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

            FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            String email = autenticacao.getCurrentUser().getEmail();
        textoNome = view.findViewById(R.id.textoNome);
        textoEmail = view.findViewById(R.id.textEmail);
        textoEmail.setText(email);


        String idUsuario = Base64Custom.codificarBase64( email );
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

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
                deslogar = ConfiguracaoFirebase.getFirebaseAutenticacao();
                deslogar.signOut();
                startActivity(new Intent(getContext(), MainActivity.class));

            }
        });





        return view;

    }



}

