package com.example.shoppingapp.user.activities;

import static android.content.ContentValues.TAG;
import static android.provider.ContactsContract.Intents.Insert.EMAIL;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.example.shoppingapp.user.models.UserModel;
import com.example.shoppingapp.user.util.PasswordHash;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    int RC_SIGN_IN = 01;
    Button signIn;
    TextView signUp;
    EditText email,password;
    ProgressBar progressBar;
    LinearLayout ggBtn, phoneBtn;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth;
    FirebaseDatabase database;
    CheckBox checkBox;
    GoogleApiClient googleApiClient;
    boolean captcha = false;
    String SiteKey = "6LehhDgmAAAAAEW6UI7k0w8l9s5tjfgPz8XYfER4";

    TextView forgotPassword;

//    <com.google.android.gms.common.SignInButton
//    android:id="@+id/gg_btn"
//    android:layout_width="wrap_content"
//    android:layout_height="40dp"/>

//    <com.facebook.login.widget.LoginButton
//    xmlns:fb="http://schemas.android.com/apk/res-auto"
//    android:id="@+id/fb_btn"
//    android:layout_width="35dp"
//    android:layout_height="wrap_content"
//    android:gravity="center"/>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);
        checkBox = findViewById(R.id.checkBox);

        signIn = findViewById(R.id.login_btn);
        signUp = findViewById(R.id.sign_up);
        email = findViewById(R.id.email_log);
        password = findViewById(R.id.password_log);
        progressBar = findViewById(R.id.progressbar);

        ggBtn = findViewById(R.id.gg_btn);

        forgotPassword=findViewById(R.id.forgot_password);


        callbackManager = CallbackManager.Factory.create();

        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Yêu cầu cung cấp thông tin cơ bản: email, tên, avatar
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        // captcha

        googleApiClient = new GoogleApiClient.Builder(this).addApi(SafetyNet.API)
                .addConnectionCallbacks(LoginActivity.this).build();
        googleApiClient.connect();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    System.out.println(status.isSuccess());
                                    if(status != null && status.isSuccess()) {
                                        Toast.makeText(LoginActivity.this,"Xác nhận thành công",Toast.LENGTH_SHORT).show();
                                        captcha = true;
                                        checkBox.setChecked(true);
                                        checkBox.setTextColor(Color.BLUE);
                                    }else{
                                        Toast.makeText(LoginActivity.this,"Xác nhận không thành công",Toast.LENGTH_SHORT).show();
                                        checkBox.setTextColor(Color.RED);
                                    }
                                }
                            });
                }else {
                    captcha = false;
                    checkBox.setTextColor(Color.BLACK);
                }

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!captcha){
                    Toast.makeText(LoginActivity.this,"Hãy xác nhận captcha trước",Toast.LENGTH_SHORT).show();
                }else loginUser();

                progressBar.setVisibility(View.VISIBLE);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                View dialogView= getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox= dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog=builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail= emailBox.getText().toString();
                        if(TextUtils.isEmpty(userEmail)&&!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(LoginActivity.this, "Nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Kiểm tra email của bạn", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(LoginActivity.this, "L", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

        ggBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.gg_btn:
                        signIn();
                        break;
                }
            }

            private void signIn() {
                Intent signInIntent = ((GoogleSignInClient) mGoogleSignInClient).getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void loginUser() {
        String userPassword = password.getText().toString();
        String userEmail = email.getText().toString();
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Mật khẩu trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.length()<6){
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
        }
        String hashPass = PasswordHash.encryptThisString(userPassword);
        auth.signInWithEmailAndPassword(userEmail,hashPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if (userEmail.equals("shopapp216@gmail.com"))
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Bạn là admin", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MenuAdminActivity.class));
                    }

                    else
                    {
                        if (auth.getCurrentUser().isEmailVerified()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            String saveCurrentDate, saveCurrentTime;
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy" , Locale.ENGLISH);
                            saveCurrentDate = currentDate.format(calendar.getTime());
                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                            saveCurrentTime = currentTime.format(calendar.getTime());
                            database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("lastOnline").setValue(saveCurrentTime + " " + saveCurrentDate);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            //Toast.makeText(LoginActivity.this, "Vui lòng xác thực email!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    //Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu chưa đúng", Toast.LENGTH_SHORT).show();
                    auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if (userEmail.equals("shopapp216@gmail.com"))
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Bạn là admin", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MenuAdminActivity.class));
                                }

                                else
                                {
                                    if (auth.getCurrentUser().isEmailVerified()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        String saveCurrentDate, saveCurrentTime;
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy" , Locale.ENGLISH);
                                        saveCurrentDate = currentDate.format(calendar.getTime());
                                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                                        saveCurrentTime = currentTime.format(calendar.getTime());
                                        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("lastOnline").setValue(saveCurrentTime + " " + saveCurrentDate);
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Vui lòng xác thực email!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu chưa đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        UserModel userModel = new UserModel();
                        userModel.setEmail(user.getEmail());
                        userModel.setName(user.getDisplayName());
                        userModel.setAvatar(user.getPhotoUrl().toString());
                        database.getReference().child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.child("name").exists())
                                {
                                    database.getReference().child("Users").child(user.getUid()).setValue(userModel);
                                }
                                String saveCurrentDate, saveCurrentTime;
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy" , Locale.ENGLISH);
                                saveCurrentDate = currentDate.format(calendar.getTime());
                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                                saveCurrentTime = currentTime.format(calendar.getTime());
                                database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("lastOnline").setValue(saveCurrentTime + " " + saveCurrentDate);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}