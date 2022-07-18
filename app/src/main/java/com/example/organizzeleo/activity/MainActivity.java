package com.example.organizzeleo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.organizzeleo.R;
import com.example.organizzeleo.activity.CadastroActivity;
import com.example.organizzeleo.activity.LoginActivity;
import com.example.organizzeleo.activity.PrincipalActivity;
import com.example.organizzeleo.config.ConfiguracaoFirebase;
import com.example.organizzeleo.navigation.NavigationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainActivity extends IntroActivity {

    FirebaseAuth autenticacao;
    Animation animation;
   // Button botaoCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //botaoCadastro = findViewById(R.id.botaoCadastro);
        animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);


        verificarUsuarioLogado();

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setButtonCtaVisible(false);


        addSlide(new FragmentSlide.Builder()
                .background(R.color.startColorBackgroumd)
                .fragment(R.layout.slide_um)
                .build()

        );



        addSlide(new FragmentSlide.Builder()
                .background(R.color.startColorBackgroumd)
                .fragment(R.layout.slide_dois)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(R.color.startColorBackgroumd)
                .fragment(R.layout.slide_tres)
                .build()
        );


        addSlide(new FragmentSlide.Builder()
                .background(R.color.startColorBackgroumd)
                .fragment(R.layout.pre_login)
                .canGoForward(false)
                .build()

        );



    }

    public void btEntrar(View view){

        startActivity(new Intent(this, LoginActivity.class));

    }

    public void btCadastro(View view){


        Intent intentCadastro = new Intent(this, CadastroActivity.class);

        startActivity(intentCadastro);

    }


    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        if(autenticacao.getCurrentUser() != null){

            abrirTelaPrincipal();

        }
    }


    public void abrirTelaPrincipal(){

        startActivity(new Intent(this, NavigationActivity.class));


    }

}