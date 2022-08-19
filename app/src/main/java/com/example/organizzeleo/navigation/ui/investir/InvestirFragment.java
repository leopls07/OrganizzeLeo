package com.example.organizzeleo.navigation.ui.investir;

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

import com.example.organizzeleo.API.HttpService;
import com.example.organizzeleo.API.Resposta;
import com.example.organizzeleo.R;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InvestirFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        String coin = "BTC";
    try {
    Resposta retorno = new HttpService(coin).execute().get();

    System.out.println(retorno.toString());

    }catch(InterruptedException e){e.printStackTrace();}
    catch (ExecutionException e){e.printStackTrace();}





        return root;
            }


    }
