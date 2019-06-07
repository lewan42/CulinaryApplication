package com.example.lewan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    Button btn_back;
    Button btn_create_account;
    private TextView field_create_login;
    private TextView field_create_email;
    private TextView field_create_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        btn_back = (Button) findViewById(R.id.btn_back_LogIn);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_create_account = (Button) findViewById(R.id.btn_create_account);

        field_create_login = findViewById(R.id.field_create_login);
        field_create_password = findViewById(R.id.field_create_password);
        field_create_email = findViewById(R.id.field_create_email);

        field_create_login.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        field_create_password.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        field_create_email.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);


        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println(field_create_email);
                System.out.println(field_create_login);
                System.out.println(field_create_password);

                CallBack callBack = new CallBack();

                Thread t;
                if (!field_create_login.getText().toString().equals("") && !field_create_email.getText().toString().equals("") && !field_create_password.getText().toString().equals("")) {
                    if (!validateLogIn(field_create_login.getText().toString())) {
                        field_create_login.setError("Логин должен состоять только из букв латинского алфавита и цифр (первая заглавная, остальные строчные или цифры) и быть длинной более 3 символов.");
                        field_create_login.requestFocus();
                    } else if (!validateEmail(field_create_email.getText().toString())) {
                        field_create_email.setError("Введена некорректная почта");
                        field_create_email.requestFocus();
                    } else if (!validatePassword(field_create_password.getText().toString())) {
                        field_create_password.setError("Пароль должен состоять из не менее 4 символов.");
                        field_create_password.requestFocus();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "ВСЁ ОК", Toast.LENGTH_LONG);
                        toast.show();

                        t = new Thread(new CreateAccount(field_create_login.getText().toString(), field_create_password.getText().toString(), field_create_email.getText().toString(), callBack, "createAccount"));
                        t.start();

                        try {
                            t.join();

                            if (callBack.getStatus().equals("yes")) {
                                finish();
                                toast = Toast.makeText(getApplicationContext(), "Аккаунт успешно создан!", Toast.LENGTH_LONG);
                                toast.show();
                            }

                        } catch (Exception e) {
                            toast = Toast.makeText(getApplicationContext(), "Такой аккаунт уже создан!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });
    }

    private boolean validateLogIn(String str) {
        if (str != null && str.length() > 3) {
            String logInPattern = "^([A-Z][a-z0-9]+)+$";
            Pattern pattern = Pattern.compile(logInPattern);
            Matcher matcher = pattern.matcher(str);

            return matcher.matches();
        } else {
            return false;
        }

    }

    private boolean validateEmail(String str) {
        String logInPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,}";
        Pattern pattern = Pattern.compile(logInPattern);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    private boolean validatePassword(String str) {
        if (str != null && str.length() > 3) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onRestart() {
        super.onRestart();
        field_create_email.setText("");
        field_create_login.setText("");
        field_create_password.setText("");
    }
}


class CreateAccount extends Thread {
    private String login;
    private String password;
    private String email;
    private String proc;
    private CallBack callBack;


    public CreateAccount(String client_login, String client_password, String client_email, CallBack callBack, String proc) {
        login = client_login;
        password = client_password;
        email = client_email;
        this.proc = proc;
        this.callBack = callBack;
    }


    public void run() {

        try {

            StringBuilder sbURL = new StringBuilder().append("http://lewanov888.000webhostapp.com/?proc=").append(proc).append("&newLogin=").append(login).append("&newPassword=").append(password).append("&newEmail=").append(email);
            URL url = new URL(sbURL.toString());
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                sb.append(inputLine + "\n");
            }
            in.close();

            inputLine = sb.toString();
            JSONObject jObject = new JSONObject(inputLine);
            String aJsonString = (String) jObject.get("statusCreateAcc");
            callBack.setStatus(aJsonString);

            System.out.println(inputLine + " status createAcc");


        } catch (MalformedURLException e) {
            System.err.println("URL FAIL " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO FAIL " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("JSON FAIL " + e.getMessage());
        }
    }
}
