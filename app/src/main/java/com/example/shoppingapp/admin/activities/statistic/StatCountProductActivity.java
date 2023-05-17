package com.example.shoppingapp.admin.activities.statistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.models.ViewAllModel;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StatCountProductActivity extends AppCompatActivity {

    BarChart bar;
    ImageView goBack;
    FirebaseFirestore firebaseFirestore;
    int adidas,nike,converse,newbalance,gucci;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_count_product);
        bar = findViewById(R.id.barChart);

        YearMonth thisMonth = YearMonth.now();
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
        String current = thisMonth.format(monthYearFormatter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> records = new ArrayList<>();
        firebaseFirestore.collection("Statistics")
                .document("CountProducts")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    }else break;
                }



                bar.getXAxis().setLabelCount(i+1);
                int finalI = i;
                firebaseFirestore.collection("AllProducts").whereEqualTo("type","adidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        adidas = task.getResult().getDocuments().size();
                        //System.out.println(task.getResult().getDocuments().size());
                        firebaseFirestore.collection("AllProducts").whereEqualTo("type","nike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                nike = task.getResult().getDocuments().size();
                                firebaseFirestore.collection("AllProducts").whereEqualTo("type","converse").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        converse = task.getResult().getDocuments().size();
                                        firebaseFirestore.collection("AllProducts").whereEqualTo("type","new balance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                newbalance = task.getResult().getDocuments().size();
                                                firebaseFirestore.collection("AllProducts").whereEqualTo("type","gucci").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        gucci = task.getResult().getDocuments().size();
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


                                                        entries.add(new BarEntry(finalI,adidas+converse+gucci+newbalance+nike));

                                                        int sum = adidas+converse+gucci+newbalance+nike;

                                                        stat.put(current,sum);
                                                        firebaseFirestore.collection("Statistics")
                                                                .document("CountProducts")
                                                                .update(stat);
                                                        BarDataSet dataSet = new BarDataSet(entries, "Dữ liệu theo tháng");
                                                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                                        BarData barData = new BarData(dataSet);
                                                        bar.setData(barData);
                                                        bar.getDescription().setText("");
                                                        bar.invalidate();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}