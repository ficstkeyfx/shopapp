package com.example.shoppingapp.admin.activities.statistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shoppingapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StatRevenueActivity extends AppCompatActivity {

    ImageView goBack;
    BarChart bar;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_revenue);
        goBack = findViewById(R.id.goBack);

        firestore = FirebaseFirestore.getInstance();
        bar = findViewById(R.id.barChart);
        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> records = new ArrayList<>();

        YearMonth thisMonth = YearMonth.now();
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
        String current = thisMonth.format(monthYearFormatter);

        firestore.collection("Statistics")
                .document("SoldProducts")
                .collection("Revenue")
                .document("Monthly")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                        bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));

                        ArrayList<BarEntry> entries = new ArrayList<>();

                        Map<String, Object> stat = new HashMap<>();
                        int i = 0;
                        for(String month:months){
                            if(!month.equals(current)){
                                entries.add(new BarEntry(i,documentSnapshot.getLong(month)));
//                        stat.put(month,documentSnapshot.getLong(month));
                                i+=1;
                            }else {
                                entries.add(new BarEntry(i,documentSnapshot.getLong(month)));
//                        stat.put(month,documentSnapshot.getLong(month));
                                i+=1;
                                break;
                            };
                        }
                        bar.getXAxis().setLabelCount(i);
                        BarDataSet dataSet = new BarDataSet(entries, "Dữ liệu theo tháng");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        BarData barData = new BarData(dataSet);
                        bar.setData(barData);
                        bar.getDescription().setText("");
                        bar.invalidate();
                        firestore.collection("Statistics")
                                .document("SoldProducts")
                                .collection("Revenue")
                                .document("Type")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();
                                        Long adidas = document.getLong("adidas");
                                        Long nike = document.getLong("nike");
                                        Long converse = document.getLong("converse");
                                        Long newbalance = document.getLong("newbalance");
                                        Long gucci = document.getLong("gucci");
                                        records.add(new PieEntry(adidas, "Adidas"));
                                        records.add(new PieEntry(nike, "Nike"));
                                        records.add(new PieEntry(converse, "Converse"));
                                        records.add(new PieEntry(newbalance, "NewBalance"));
                                        records.add(new PieEntry(gucci, "Gucci"));
                                        PieDataSet pieDataSet = new PieDataSet(records,"");
                                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                        pieDataSet.setValueTextColor(Color.BLACK);
                                        pieDataSet.setValueTextSize(22f);
                                        ValueFormatter formatter = new ValueFormatter() {
                                            @Override
                                            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                                return String.valueOf((int) value);
                                            }
                                        };

                                        PieData pieData = new PieData(pieDataSet);
                                        pieData.setValueFormatter(formatter);
                                        pieChart.setData(pieData);
                                        pieChart.getDescription().setText("");
                                        pieChart.invalidate();
                                        pieChart.animate();
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