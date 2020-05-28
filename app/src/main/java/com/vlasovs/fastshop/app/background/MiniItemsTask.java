package com.vlasovs.fastshop.app.background;

import android.os.AsyncTask;

import com.vlasovs.fastshop.app.classes.MiniItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;

import static okhttp3.RequestBody.*;

public class MiniItemsTask extends AsyncTask <Integer, Void, ArrayList<MiniItem>> {

        private ArrayList<MiniItem> itemList = new ArrayList<>();
        public MiniItemResponse delegate = null;

        @Override
        protected ArrayList<MiniItem> doInBackground(Integer... integers) {

            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            JSONObject idForQuery = new JSONObject();

            try{
                idForQuery.put("id", String.valueOf(integers[0]));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            RequestBody body = create(JSON, idForQuery.toString());
            Request request = new Request.Builder()
                    .url("http://192.168.1.43/getminiitems.php")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();

                JSONArray array = new JSONArray(response.body().string());

                for (int i = 0; i < array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    MiniItem item = new MiniItem(object.getInt("ItemID") ,object.getString("Image"), object.getString("Name"), (float) object.getDouble("Rating"),
                            (float) object.getDouble("Price"), object.getInt("Reviews"));

                    itemList.add(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                System.out.println("End of content");
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(ArrayList<MiniItem> arraylist) {
            delegate.processFinish(arraylist);
        }
    }
