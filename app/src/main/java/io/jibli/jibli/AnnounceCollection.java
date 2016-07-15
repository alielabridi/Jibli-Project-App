/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.jibli.jibli;



import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Date;
import java.net.URL;
import java.text.ParseException;
import static io.jibli.jibli.LoginActivity.userConnectedEmail;


public class AnnounceCollection extends AppCompatActivity {

   
     List<Announce> announces;
 private sendingAnnounce sendingAnnounce = null;
    public AnnounceCollection() {
        this.announces = new LinkedList<>();
    
    }

    public boolean addannounce(Announce ano) {
       new sendingAnnounce(ano.getProduct(),ano.getPrice(),ano.getProfit(),ano.getLocation(),ano.getComment(),ano.getBuyerId());
        return announces.add(ano);
        
    }

    public boolean removeannounce(Announce ano) {
        return announces.remove(ano);
    }

    public void modifyAnnounce(Announce search, Announce newMsg) {
   
        int index = announces.indexOf(search);
        if (index >= 0) {
            announces.set(index, newMsg);
        }
    }

    public Announce searchAnnounce (String ID){

        for(Announce announce : announces)
            if (announce.getAnnounceId() == Integer.parseInt(ID))
                return announce;
        return null;

    }

    public class sendingAnnounce extends AsyncTask<Void, Void, Boolean> {
    private int announceId;
   private String product;
   private double price;
   private double profit;
   private String location;
   private Date   postdatetime;
   private String comment;
   private int buyerId;

   public sendingAnnounce( String product, double price, double profit, String location,  String comment, String bringerId) {
      this.announceId = announceId;
      this.product = product;
      this.price = price;
      this.profit = profit;
      this.location = location;
      this.postdatetime = postdatetime;
      this.comment = comment;
      this.buyerId = buyerId;
       sendToServer();
   }


        private JSONObject constructJsonObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("A_PRODUCT", product);
                jsonObject.accumulate("A_PRICE", price);
                jsonObject.accumulate("A_LOCATION", location);
                jsonObject.accumulate("A_COMMENT", comment);
                jsonObject.accumulate("A_PROFIT", profit);
                jsonObject.accumulate("BU_ID", userConnectedEmail);
            

                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        private String sendToServer() {
            String JsonResponse = null;
            String JsonDATA = constructJsonObject().toString();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            System.out.println(JsonDATA);
            try {
                URL url = new URL("http://get2mail.fr/jibli/announce_get.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                // json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                //response data
                Log.i("test", JsonResponse);
                //send to post execute
                return JsonResponse;
                //return null;



            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("test", "Error closing stream", e);
                    }
                }
            }
            return null;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(!isNetworkAvailable()){
                runOnUiThread(new Runnable() {
                    public void run() {
                        sendingAnnounce = null;
                        //showProgress(false);
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
            String result = sendToServer();
            for (int i = 0; i < 200000 && result != null && !result.contains("OK"); i++) {
                try {
                    wait(10);
                    result = sendToServer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (result != null && result.contains("OK")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        sendingAnnounce = null;
                        /*Toast.makeText(getApplicationContext(), "You have signed up successfully", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(signUpActivity.this,MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);*/
                    }
                });
                return true;
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        sendingAnnounce = null;
                        /*showProgress(false);
                        Toast.makeText(getApplicationContext(), "The connection to the server took too long! Please retry later", Toast.LENGTH_LONG).show();*/
                    }
                });
                return false;
            }
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }

        @Override
        protected void onCancelled() {
            sendingAnnounce = null;
            //showProgress(false);
        }
    }



    public void JsonAnnounceReader(InputStream inputStream) throws IOException {
        try {
            readJsonStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            readAnnouncesArray(reader);
        }
        finally{
            reader.close();
        }
    }


    public void readAnnouncesArray(JsonReader reader) throws IOException {
        //read the array and go through all the objects inside
        reader.beginArray();
        while (reader.hasNext()) {
            try {
                readAnnounce(reader);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        reader.endArray();
    }


    public void readAnnounce(JsonReader reader) throws IOException, ParseException {
        //assume that if not specificed these proprieties are null
           int announceId = 0;
    String product = null;
    double price = 0;
    double profit = 0;
    String location = null;
    String   postdatetime = null;
    String comment = null;
    String buyerId = null ;

        //start reading an object from the array
        reader.beginObject();
        //look for name/value pairs i.e. filename & Announce
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("A_ID")) {
                announceId =  reader.nextInt();
            } else if (name.equals("A_PRODUCT")) {
                product = reader.nextString();
            } else if (name.equals("A_POSTTIME")) {
                postdatetime =  reader.nextString();
            } else if (name.equals("A_PRICE")) {
                price = reader.nextDouble();
            } else if (name.equals("A_LOCATION")) {
                location = reader.nextString();
            } else if (name.equals("A_COMMENT")) {
                comment = reader.nextString();
             } else if (name.equals("A_PROFIT")) {
                profit = reader.nextDouble();
              } else if (name.equals("BU_ID")) {
                buyerId = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        java.text.SimpleDateFormat sdf = 
         new java.text.SimpleDateFormat("yyyy-MM-dd");
         java.util.Date dt = new java.util.Date();
         dt = sdf.parse(postdatetime);
          Announce announce = new Announce( announceId,  product,  price,  profit,  location,  postdatetime,  comment,  buyerId);

        announces.add(announce);

    }


    public void UpdateAnnounces() {


        while (!announces.isEmpty()) {
            announces.remove(0);
        }

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        URL url = null;
        try {
            url = new URL("http://get2mail.fr/jibli/announce_post.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true);
        // is output buffer writter
        InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //input stream
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
           //return  null;
        }
        try {
            JsonAnnounceReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

     /*   inputStream

        // return JSON String


        jObj.
        try {
            return (new Announce((String)jObj.get("M_MESSAGE"),(Date) jObj.get("M_DATE"),(String)jObj.get("RECEIVER"),(String)jObj.get("SENDER")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;*/
    }


   

}
