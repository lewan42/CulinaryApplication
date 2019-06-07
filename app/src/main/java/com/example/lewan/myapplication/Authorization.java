package com.example.lewan.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Authorization extends Thread {

    private String login;
    private String password;
    private String proc;
    private CallBack callBack;


    public Authorization(String client_login, String client_password, CallBack callBack, String proc) {
        login = client_login;
        password = client_password;
        this.proc = proc;
        this.callBack = callBack;
    }

    public void run() {

        try {
            StringBuilder sbURL = new StringBuilder().append("http://lewanov888.000webhostapp.com/?proc=").append(proc).append("&login=").append(login).append("&password=").append(password);
            URL url = new URL(sbURL.toString());

            //URL url = new URL("https://edadeal.ru/perm/offers");
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine + "\n");
            }
            in.close();

            inputLine = sb.toString();

            System.err.println("Authorization JSON= " + inputLine);

            JSONObject jObject = new JSONObject(inputLine);
            String aJsonString = (String) jObject.get("status");
            callBack.setStatus(aJsonString);

            System.out.println(inputLine.toString() + "Authorization success");


        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }


}