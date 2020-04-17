package com.vlasovs.fastshop.app.background;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vlasovs.fastshop.app.activities.RegistrationActivity;
import com.vlasovs.fastshop.app.classes.LoadingDialog;

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

public class RegisterTask extends AsyncTask<String, Void, String> {

    private Context ctx;
    private LoadingDialog lD;

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://192.168.1.43/register.php";
        try {
            String email = params[0];
            String pass = params[1];
            String name = params[2];
            String surname = params[3];
            URL url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                              +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(pass, "UTF-8")+"&"
                              +URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"
                              +URLEncoder.encode("surname", "UTF-8")+"="+URLEncoder.encode(surname, "UTF-8");
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

    public RegisterTask(Context context) {
        ctx = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lD = new LoadingDialog((Activity) ctx);
        lD.startLoadingDialog();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null && result.equals("Thanks for Registration!")){
            Toast.makeText(ctx, "" + result, Toast.LENGTH_LONG).show();
            ((Activity)ctx).finish();
        } else {
            Toast.makeText(ctx, "Something went wrong :(", Toast.LENGTH_LONG).show();
        }
        lD.dismissLoadingDialog();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
