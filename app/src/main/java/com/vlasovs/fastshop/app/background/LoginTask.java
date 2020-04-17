package com.vlasovs.fastshop.app.background;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginTask extends AsyncTask<String, Void, String> {

    public LoginResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://192.168.1.43/login.php";
        try {
            String email = params[0];
            String pass = params[1];
            URL url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                    +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(pass, "UTF-8");
            bw.write(post_data);
            bw.flush();
            bw.close();
            os.close();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1));
            String result = "";
            String line="";
            while ((line = br.readLine()) != null){
                result += line;
            }
            br.close();
            is.close();
            httpURLConnection.disconnect();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LoginTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
