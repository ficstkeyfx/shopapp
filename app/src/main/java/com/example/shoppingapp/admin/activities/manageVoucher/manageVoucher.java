package com.example.shoppingapp.admin.activities.manageVoucher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.example.shoppingapp.admin.activities.addProduct.AddProduct;
import com.example.shoppingapp.user.models.VoucherModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class manageVoucher extends AppCompatActivity {
    TextView reduce, minimum, quantity, date, txtDate;
    EditText txtReduce, txtMinimum, txtQuantity;
    Button add;
    DatePicker datePicker;
    ImageView goback;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_voucher);

        reduce = findViewById(R.id.reduce);
        minimum = findViewById(R.id.minimum);
        quantity = findViewById(R.id.quantity);
        date = findViewById(R.id.date);
        txtDate = findViewById(R.id.txtDate);
        txtReduce = findViewById(R.id.txtReduce);
        txtMinimum = findViewById(R.id.txtMinimum);
        txtQuantity = findViewById(R.id.txtQuantity);
        add = findViewById(R.id.add);
        goback = findViewById(R.id.goBack);
        datePicker = findViewById(R.id.datePicker);

        firebaseFirestore = FirebaseFirestore.getInstance();

        txtReduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtReduce != null && !txtReduce.getText().equals(""))
                {
                    reduce.setText("Giảm " + txtReduce.getText() + "k");
                }
                else
                {
                    reduce.setText("Giảm ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtMinimum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtMinimum != null && !txtMinimum.getText().equals(""))
                {
                    minimum.setText("Đơn tối thiểu " + txtMinimum.getText() +"k");
                }
                else
                {
                    minimum.setText("Đơn tối thiểu " );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtQuantity != null && !txtQuantity.getText().equals(""))
                {
                    quantity.setText("Số lượng: " + txtQuantity.getText());

                }
                else
                {
                    quantity.setText("Số lượng: ");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        String saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        txtDate.setText(saveCurrentDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
//                datePicker.init(
//                        datePicker.getYear(),
//                        datePicker.getMonth(),
//                        datePicker.getDayOfMonth(),
//                        new DatePicker.OnDateChangedListener() {
//                            @Override
//                            public void onDateChanged(DatePicker view,
//                                                      int year, int monthOfYear, int dayOfMonth) {
//                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//                                txtDate.setText(selectedDate);
//                                date.setText("Ngày hết hạn: " + txtDate.getText());
//                            }
//                        });
                datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        txtDate.setText(selectedDate);
                        date.setText("Ngày hết hạn: " + txtDate.getText());
                    }
                });
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Voucher").add(new VoucherModel(Integer.parseInt(txtReduce.getText().toString()),
                                                Integer.parseInt(txtMinimum.getText().toString()),
                                                Integer.parseInt(txtQuantity.getText().toString()),
                                                txtDate.getText().toString()));
                Toast.makeText(manageVoucher.this, "Bạn đã thêm mã giảm giá thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}