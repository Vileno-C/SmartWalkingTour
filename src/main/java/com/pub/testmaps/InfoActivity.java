package com.pub.testmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InfoActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        textView = findViewById(R.id.textViewDescription);
        textView.setText(getAppDescription(this));
    }

    private String getAppDescription(Context context){
        String linha = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.description)))) {

            linha = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG_ERROR", "Erro ao ler arquivo: " + e.getMessage());
        }

        return linha;
    }
}