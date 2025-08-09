package com.example.exhibitionguide;

import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Button aboutBtn = (Button) findViewById(R.id.about);
        aboutBtn.setOnClickListener(clickListener);

        Button whatsOnBtn = (Button) findViewById(R.id.whatsOn);
        whatsOnBtn.setOnClickListener(clickListener);

        Button sessionsBtn = (Button) findViewById(R.id.sessions);
        sessionsBtn.setOnClickListener(clickListener);

        Button ticketsBtn = (Button) findViewById(R.id.tickets);
        ticketsBtn.setOnClickListener(clickListener);

    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.about:
                            Intent intent = new Intent(MainActivity.this, About.class);
                            startActivity(intent);
                            break;
                        case R.id.whatsOn:
                            Intent intentA = new Intent(MainActivity.this, WhatsOn.class);
                            startActivity(intentA);
                            break;

                        case R.id.sessions:
                            Intent intentB = new Intent(MainActivity.this, Sessions.class);
                            startActivity(intentB);
                            break;

                        case R.id.tickets:
                            Intent intentC = new Intent(MainActivity.this, ticket_select.class);
                            startActivity(intentC);
                            break;
                    }
                }
            };}