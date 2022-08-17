package com.example.organizzeleo.model;

import com.example.organizzeleo.config.ConfiguracaoFirebase;
import com.example.organizzeleo.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Carteira {

    public Double saldo;

    public Carteira() {
    }

    public void Salvar(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        String email = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());

        DatabaseReference firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseDatabase.child("Carteira")
                .child(email)
                .setValue(this);


    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}
