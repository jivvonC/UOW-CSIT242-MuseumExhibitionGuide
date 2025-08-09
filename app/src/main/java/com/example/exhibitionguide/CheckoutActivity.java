package com.example.exhibitionguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 전달받은 리스트 꺼내기
        ArrayList<ticket_select.CartItem> cartList = (ArrayList<ticket_select.CartItem>) getIntent().getSerializableExtra("cartList");
        // 전달받은 텍스트 꺼내기
        String totalPrice = getIntent().getStringExtra("totalText");

        TextView totalPriceTextView = findViewById(R.id.totalPrice);
        totalPriceTextView.setText(totalPrice);

        if (cartList != null) {
            ListView listView = findViewById(R.id.listViewCheckout);
            ArrayAdapter<ticket_select.CartItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartList);
            listView.setAdapter(adapter);
        }

        Button confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(clickListener);
    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                   Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                   Toast.makeText(CheckoutActivity.this, "Tickets confirmed", Toast.LENGTH_SHORT).show();
                   startActivity(intent);
               }
            };
}