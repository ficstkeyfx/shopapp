package com.example.shoppingapp.admin.activities.statistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.shoppingapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StatAccountActivity extends AppCompatActivity {

    ImageView goBack;
    BarChart bar;
    FirebaseFirestore firestore;
    FirebaseDatabase database;
    LinearLayout layout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_stat_account);

        layout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progressbar);

        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        goBack = findViewById(R.id.goBack);
        bar = findViewById(R.id.barChart);

        YearMonth thisMonth = YearMonth.now();
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
        String current = thisMonth.format(monthYearFormatter);
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Statistics")
                .document("Account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        System.out.println(documentSnapshot.getLong("Jan"));
                        System.out.println(documentSnapshot.getLong("Feb"));
                        System.out.println(documentSnapshot.getLong("Mar"));
                        System.out.println(documentSnapshot.getLong("Apr"));
                        bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                        bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months){});

                        ArrayList<BarEntry> entries = new ArrayList<>();

                        Map<String, Object> stat = new HashMap<>();
                        int i = 0;
                        for(String month:months){
                            if(!month.equals(current)){
                                System.out.println(month);
                                entries.add(new BarEntry(i,documentSnapshot.getLong(month)));
                                i+=1;

                            }else break;
                        }
                        database = FirebaseDatabase.getInstance();
                        DatabaseReference emailRef = database.getReference().child("Users");

                        int finalI = i;
                        emailRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int q = 0;
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    q++;
                                }
                                entries.add(new BarEntry(finalI,q));
                                stat.put(current,q);
                                firestore.collection("Statistics")
                                        .document("Account")
                                        .update(stat);
                                bar.getXAxis().setLabelCount(finalI+1);
                                BarDataSet dataSet = new BarDataSet(entries, "Dữ liệu theo tháng");
                                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                BarData barData = new BarData(dataSet);
                                bar.setData(barData);
                                bar.getAxisLeft().setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {
                                        return String.valueOf((int) value);
                                    }
                                });
                                
                                bar.getDescription().setText("");
                                bar.invalidate();
                                layout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
            });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}