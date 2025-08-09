package com.example.exhibitionguide;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.CalendarView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ImageView;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


public class ticket_select extends AppCompatActivity {

    CalendarView calendarView;
    int selectYear, selectMonth, selectDay;
    String selectDay2;
    String caldate;
    String caldate2;

    ArrayAdapter<CharSequence> adapter = null;
    Spinner spinner = null;
    String[] list = {"Art Gallery", "WWI Exhibition", "Exploring the space", "Visual show"};
    String exhibition = "";

    ArrayAdapter<CharSequence> adapter2 = null;
    Spinner spinner2 = null;
    String[] list2 = {"09:30~11:00", "11:30~13:00", "13:30~15:00", "15:30~17:00", "17:30~19:00"};

    ArrayAdapter<CharSequence> adapter3 = null;
    Spinner spinner3 = null;
    String[] list3 = {"15:00", "17:00", "19:00"};
    String session1 = "";
    String session2 = "";


    int numppl;
    TextView dateTv;
    TextView exTv;
    TextView sessionTv;
    TextView numpplTv;
    TextView totalamt;

    int v;
    int v2;
    int v3;

    NumberPicker npker;

    int price = 0;
    int totalPrice;
    ArrayList<CartItem> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_select);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendarView = (CalendarView) findViewById(R.id.calendar);
        dateTv = (TextView)findViewById(R.id.dateTv);
        exTv = (TextView)findViewById(R.id.exTv);
        sessionTv = (TextView)findViewById(R.id.sessionTv);
        npker = findViewById(R.id.npker);
        numpplTv = (TextView)findViewById(R.id.numpplTv);
        totalamt = (TextView)findViewById(R.id.totalamt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate now = LocalDate.now();
            selectYear = now.getYear();
            selectMonth = now.getMonthValue();
            selectDay = now.getDayOfMonth();
            DayOfWeek dayOfWeek = now.getDayOfWeek();
            selectDay2 = dayOfWeek.toString().substring(0,1).toUpperCase() + dayOfWeek.toString().substring(1).toLowerCase();
            caldate = selectYear + "-" + selectMonth + "-" + selectDay + "(" + selectDay2 + ")";
            dateTv.setText(caldate);
            calendarView.setDate(System.currentTimeMillis()); // UI 반영
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selectYear = year;
                //달은 month+1이라고 해줘야됨!!!주의(안드로이드 기본 설정)
                selectMonth = month +1;
                selectDay = dayOfMonth;
                LocalDate date = LocalDate.of(selectYear, selectMonth, selectDay);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                int dayOfWeekNumber = dayOfWeek.getValue();
                switch (dayOfWeekNumber){
                    case 1:
                        selectDay2 = "Monday";
                        break;
                    case 2:
                        selectDay2 = "Tuesday";
                        break;
                    case 3:
                        selectDay2 = "Wednesday";
                        break;
                    case 4:
                        selectDay2 = "Thursday";
                        break;
                    case 5:
                        selectDay2 = "Friday";
                        break;
                    case 6:
                        selectDay2 = "Saturday";
                        break;
                    case 7:
                        selectDay2 = "Sunday";
                        break;
                }
                caldate = Integer.toString(selectYear)+ "-" + Integer.toString(selectMonth) + "-" +Integer.toString(selectDay)+"("+selectDay2+")";
                dateTv.setText(caldate);
                calculateTotalPrice();

            }
        });
        adapter = ArrayAdapter.createFromResource(this, R.array.array_exhibition, android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spinnerEx);
        spinner.setAdapter(adapter);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.array_sessions, android.R.layout.simple_spinner_dropdown_item);
        spinner2 = findViewById(R.id.spinnerTime);
        spinner2.setAdapter(adapter2);

        adapter3 = ArrayAdapter.createFromResource(this, R.array.array_visualShow, android.R.layout.simple_spinner_dropdown_item);
        spinner3 = findViewById(R.id.spinnerTime2);
        spinner3.setAdapter(adapter3);

        //default
        spinner2.setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.GONE);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                v = parentView.getSelectedItemPosition();
                exhibition = spinner.getSelectedItem().toString();

                //your code here
                exTv.setText(exhibition);

                if (Objects.equals(exhibition, "Visual show")){
                    spinner2.setVisibility(View.GONE);
                    spinner3.setVisibility(View.VISIBLE);
                }
                else {
                    spinner2.setVisibility(View.VISIBLE);
                    spinner3.setVisibility(View.GONE);
                }

                //default date setting
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (Objects.equals(exhibition, "Visual show")) {
                        String[] sessionArray = list3;
                        spinner3.post(() -> {
                            java.time.LocalTime currentTime = java.time.LocalTime.now();
                            for (int i = 0; i < sessionArray.length; i++) {
                                String sessionStr = sessionArray[i].trim();
                                java.time.LocalTime sessionStart = java.time.LocalTime.parse(sessionStr);
                                if (sessionStart.isAfter(currentTime)) {
                                    forceSpinnerSelection(spinner3, i);
                                    break;
                                }
                            }
                        });
                    }
                    else {
                        String[] sessionArray = list2;
                        spinner2.post(() -> {
                            java.time.LocalTime currentTime = java.time.LocalTime.now();
                            for (int i = 0; i < sessionArray.length; i++) {
                                String timeRange = sessionArray[i].trim();
                                String sessionStartStr = timeRange.split("~")[0].trim();

                                try {
                                    java.time.LocalTime sessionStart = java.time.LocalTime.parse(sessionStartStr);
                                    if (sessionStart.isAfter(currentTime)) {
                                        forceSpinnerSelection(spinner2, i);
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }

                calculateTotalPrice();
            }
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //return;
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                v2 = parentView.getSelectedItemPosition();
                session1= spinner2.getSelectedItem().toString();

                //your code here
                sessionTv.setText(session1);
            }
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //return;
            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                v3 = parentView.getSelectedItemPosition();
                session2= spinner3.getSelectedItem().toString();

                //your code here
                sessionTv.setText(session2);
            }
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //return;
            }
        });

        npker.setMaxValue(100); //최대값
        npker.setMinValue(0); //최소값
        npker.setValue(1);// 초기값

        npker.setOnLongPressUpdateInterval(10); //길게 눌렀을 때 몇 초부터 반응?
        npker.setWrapSelectorWheel(true); //최대값 or 최소값에서 멈출지 넘어갈지

        npker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                numppl = newVal;
                numpplTv.setText(String.valueOf(numppl));
                calculateTotalPrice();
            }
        });

        Button addCartBtn = findViewById(R.id.addCartbtn);
        addCartBtn.setOnClickListener(v -> {
            String selectedExhibition = spinner.getSelectedItem().toString();
            String selectedSession = selectedExhibition.equals("Visual show") ? spinner3.getSelectedItem().toString()
                    : spinner2.getSelectedItem().toString();
            int selectedPeople = npker.getValue();
            String selectedDate = dateTv.getText().toString();

            int pricePerPerson = 0;
            if (selectedExhibition.equals("Art Gallery")) {
                pricePerPerson = (selectDay2.equals("Saturday") || selectDay2.equals("Sunday")) ? 30 : 25;
            } else if (selectedExhibition.equals("WWI Exhibition")) {
                pricePerPerson = (selectDay2.equals("Saturday") || selectDay2.equals("Sunday")) ? 25 : 20;
            } else if (selectedExhibition.equals("Exploring the space")) {
                pricePerPerson = (selectDay2.equals("Saturday") || selectDay2.equals("Sunday")) ? 35 : 30;
            } else if (selectedExhibition.equals("Visual show")) {
                pricePerPerson = 40;
            }

            double totalPrice = pricePerPerson * selectedPeople;
            if(selectedPeople > 3){
                totalPrice = totalPrice * 0.9;
            }

            CartItem item = new CartItem(selectedDate,selectedExhibition, selectedSession, selectedPeople, totalPrice);
            cartList.add(item);

            Toast.makeText(ticket_select.this, "Added to cart", Toast.LENGTH_SHORT).show();
        });

        ImageView shoppingCartIcon = findViewById(R.id.shoppingCart);
        shoppingCartIcon.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ticket_select.this);
            builder.setTitle("Your Cart");

            // 레이아웃 세팅
            LinearLayout layout = new LinearLayout(ticket_select.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            // 리스트뷰 생성
            ListView listView = new ListView(ticket_select.this);
            ArrayAdapter<CartItem> adapter = new ArrayAdapter<>(ticket_select.this, android.R.layout.simple_list_item_1, cartList);
            listView.setAdapter(adapter);
            layout.addView(listView);

            // 총합 텍스트뷰
            TextView totalText = new TextView(ticket_select.this);
            totalText.setPadding(30, 20, 30, 30);
            layout.addView(totalText);
            totalText.setText("Total: " + calculateCartTotal() + " AUD");

            // 항목 클릭 시 수량 수정
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                CartItem item = cartList.get(position);

                AlertDialog.Builder editDialog = new AlertDialog.Builder(ticket_select.this);
                editDialog.setTitle("Edit Quantity");

                NumberPicker picker = new NumberPicker(ticket_select.this);
                picker.setMinValue(1);
                picker.setMaxValue(100);
                picker.setValue(item.numPeople);

                editDialog.setView(picker);

                editDialog.setPositiveButton("Update", (dialog, which) -> {
                    int newPeople = picker.getValue();
                    double unitPrice = item.totalPrice / item.numPeople;
                    item.numPeople = newPeople;
                    item.totalPrice = unitPrice * newPeople;

                    adapter.notifyDataSetChanged();
                    totalText.setText("Total: " + calculateCartTotal() + " AUD");
                });

                editDialog.setNegativeButton("Cancel", null);
                editDialog.show();
            });

            builder.setView(layout);

            // 결제 버튼
            builder.setNeutralButton("Checkout", (dialog, which) -> {
                Intent intent = new Intent(ticket_select.this, CheckoutActivity.class);
                intent.putExtra("cartList", (Serializable) cartList);
                String totalValue = totalText.getText().toString();
                intent.putExtra("totalText", totalValue);
                startActivity(intent);
            });

            builder.setNegativeButton("Close", null);

            // 항목 길게 누를 시 삭제 다이얼로그
            listView.setOnItemLongClickListener((parent, view12, position, id12) -> {
                CartItem itemToDelete = cartList.get(position);

                new AlertDialog.Builder(ticket_select.this)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to remove this item from the cart?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            cartList.remove(position);
                            adapter.notifyDataSetChanged();
                            totalText.setText("Total: " + calculateCartTotal() + " AUD");
                            Toast.makeText(ticket_select.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            });
            builder.show();
        });



    }//onCreate끝
    private void calculateTotalPrice() {
        if (exhibition == null || selectDay2 == null || numppl == 0) return;

        int price = 0;

        if (exhibition.equals("Art Gallery")) {
            price = (selectDay2.equals("Saturday") || selectDay2.equals("Sunday")) ? 30 : 25;
        } else if (exhibition.equals("WWI Exhibition")) {
            price = (selectDay2.equals("Saturday") || selectDay2.equals("Sunday")) ? 25 : 20;
        } else if (exhibition.equals("Exploring the space")) {
            price = (selectDay2.equals("Saturday") || selectDay2.equals("Sunday")) ? 35 : 30;
        } else if (exhibition.equals("Visual show")) {
            price = 40;
        }

        double totalPrice = price * numppl;

        if(numppl > 3){
            totalPrice = totalPrice * 0.9;
        }
        totalamt.setText(String.valueOf(totalPrice));
    }

    public static class CartItem implements Serializable {
        String date;
        String exhibition;
        String session;
        int numPeople;
        double totalPrice;

        public CartItem(String date, String exhibition, String session, int numPeople, double totalPrice) {
            this.date = date;
            this.exhibition = exhibition;
            this.session = session;
            this.numPeople = numPeople;
            this.totalPrice = totalPrice;
        }

        @Override
        public String toString() {
            return exhibition + " | " + session + "\n" +
                    date + " | People: " + numPeople + " | AUD " + totalPrice;
        }
    }

    private double calculateCartTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.totalPrice;
        }
        return total;
    }

    private void forceSpinnerSelection(Spinner spinner, int position) {
        spinner.setSelection(position); // setSelection으로 선택
        AdapterView.OnItemSelectedListener listener = spinner.getOnItemSelectedListener(); // 리스너 가져오기
        if (listener != null) {
            listener.onItemSelected(spinner, null, position, spinner.getItemIdAtPosition(position)); // onItemSelected 호출
        }
    }



    }









