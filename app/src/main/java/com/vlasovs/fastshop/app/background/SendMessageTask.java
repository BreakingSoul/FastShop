package com.vlasovs.fastshop.app.background;

import android.os.AsyncTask;

import com.vlasovs.fastshop.app.classes.CartItem;
import com.vlasovs.fastshop.app.classes.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.RequestBody.create;

public class SendMessageTask extends AsyncTask <Message, Void, Boolean> {

    private boolean success;
    public SendDataResponse delegate;

        @Override
        protected Boolean doInBackground(Message... messages) {

            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            JSONObject idForQuery = new JSONObject();

            try{
                idForQuery.put("userid", String.valueOf(messages[0].getUserID()));
                idForQuery.put("tosupport", String.valueOf(messages[0].getToSupport()));
                idForQuery.put("message", String.valueOf(messages[0].getMessage()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            RequestBody body = create(JSON, idForQuery.toString());
            Request request = new Request.Builder()
                    .url("http://192.168.1.43/sendmessage.php")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();

                success = response.isSuccessful();

           /*     JSONArray array = new JSONArray(response.body().string());

                for (int i = 0; i < array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    description = object.getString("Description");
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            delegate.operationStatus(success);
        }
    }
