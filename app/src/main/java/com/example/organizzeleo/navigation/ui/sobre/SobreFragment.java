package com.example.organizzeleo.navigation.ui.sobre;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.organizzeleo.R;

import mehdi.sakout.aboutpage.AboutPage;


public class SobreFragment extends Fragment {

    private ShareViewModel shareViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        String descricao = "Somos jovens desenvolvedores e este é o nosso primeiro projeto";

    return new AboutPage(getActivity())
            .setImage(R.drawable.logomenor)
            .setDescription(descricao)
            .addGroup("Entre em contato conosco")
            .addEmail("AvetaSoftware@gmail.com","Email de contato")
            .addGroup("Redes sociais")
            .addInstagram("avetasoftwares","Visite nossa página")
            .create();
    }
}