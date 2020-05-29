package com.vlasovs.fastshop.app.background;

import android.os.AsyncTask;

import com.vlasovs.fastshop.app.classes.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.RequestBody.create;

public class SearchItemsTask extends AsyncTask <String, Void, ArrayList<Item>> {

        private ArrayList<Item> itemList = new ArrayList<>();
        public ItemResponse delegate = null;

        @Override
        protected ArrayList<Item> doInBackground(String... params) {

            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            JSONObject idForQuery = new JSONObject();

            int type;
            if (params[0].equals("category")){
                type = 1;
            } else {
                type = 0;
            }

            try{
                idForQuery.put("iscategory", String.valueOf(type));
                idForQuery.put("searchitem", params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            RequestBody body = create(JSON, idForQuery.toString());
            Request request = new Request.Builder()
                    .url("http://192.168.1.43/search.php")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();

                JSONArray array = new JSONArray(response.body().string());

                for (int i = 0; i < array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    Item item = new Item(object.getInt("ItemID") ,object.getString("Image"), object.getString("Name"), (float) object.getDouble("Rating"),
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
        protected void onPostExecute(ArrayList<Item> arraylist) {
            delegate.processFinish(arraylist);
        }
    }
