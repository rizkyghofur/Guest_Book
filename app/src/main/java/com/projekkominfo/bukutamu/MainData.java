package com.projekkominfo.bukutamu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainData extends AppCompatActivity {

    Button input1, lihat1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_data);

        input1=findViewById(R.id.input);
        lihat1=findViewById(R.id.lihat);

        input1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainData.this, MainMenu.class);
                startActivity(i);
                finish();

            }
        });

        lihat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(MainData.this, MyListData.class);
                startActivity(j);
            }
        });
    }
}
