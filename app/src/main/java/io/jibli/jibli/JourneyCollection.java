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


public class JourneyCollection extends AppCompatActivity {

   
     List<Journey> journeys;
     private sendingJourney sendingJourney = null;
             

    public JourneyCollection() {
        this.journeys = new LinkedList<>();
    
    }

    public boolean addjourney(Journey ano) {
        new sendingJourney(ano.getDate(),ano.getDest(),ano.getDepar());
        return journeys.add(ano);
        
    }

    public boolean removejourney(Journey ano) {
        return journeys.remove(ano);
    }

    public void modifyJourney(Journey search, Journey newMsg) {
   
        int index = journeys.indexOf(search);
        if (index >= 0) {
            journeys.set(index, newMsg);
        }
    }


    public class sendingJourney extends AsyncTask<Void, Void, Boolean> {
    private int journeyID;
   private String date;
   private String dest;
   private String depar;
   private int bringerId;

   public sendingJourney( String date, String dest, String depar) {
      this.date = date;
      this.dest = dest;
      this.depar = depar;
       sendToServer();
   }

        private JSONObject constructJsonObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("J_DATE", date);
                jsonObject.accumulate("J_DESTINATION", dest);
                jsonObject.accumulate("J_DEPARTURE", depar);
                jsonObject.accumulate("BR_ID", userConnectedEmail);
     

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
                URL url = new URL("http://get2mail.fr/jibli/journey_get.php");
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
                        sendingJourney = null;
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
                        sendingJourney = null;
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
                        sendingJourney = null;
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
            sendingJourney = null;
            //showProgress(false);
        }
    }


    public void JsonJourneyReader(InputStream inputStream) throws IOException {
        try {
            readJsonStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            readJourneysArray(reader);
        }
        finally{
            reader.close();
        }
    }


    public void readJourneysArray(JsonReader reader) throws IOException {
        //read the array and go through all the objects inside
        reader.beginArray();
        while (reader.hasNext()) {
            try {
                readJourney(reader);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        reader.endArray();
    }


    public void readJourney(JsonReader reader) throws IOException, ParseException {
        //assume that if not specificed these proprieties are null
                     int journeyID = 0;
                  String date = null;
                  String dest = null;
                  String depar = null;
                  String bringerId = null;

        //start reading an object from the array
        reader.beginObject();
        //look for name/value pairs i.e. filename & Journey
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("J_ID")) {
                journeyID =  reader.nextInt();
            } else if (name.equals("J_DATE")) {
               date = reader.nextString();
            } else if (name.equals("J_DESTINATION")) {
                depar =  reader.nextString();
            } else if (name.equals("J_DEPARTURE")) {
                depar = reader.nextString();
            } else if (name.equals("BR_ID")) {
                bringerId = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        java.text.SimpleDateFormat sdf = 
         new java.text.SimpleDateFormat("yyyy-MM-dd");
         java.util.Date dt =  sdf.parse(date);
         //  Journey jour = new Journey( journeyID,  dt,  dest,  depar,bringerId);

        //journeys.add(jour);

    }


    public void UpdateJourneys() {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        URL url = null;
        try {
            url = new URL("http://www.scantosign.com/sheet?q=");
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
            JsonJourneyReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

     /*   inputStream

        // return JSON String


        jObj.
        try {
            return (new Journey((String)jObj.get("M_MESSAGE"),(Date) jObj.get("M_DATE"),(String)jObj.get("RECEIVER"),(String)jObj.get("SENDER")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;*/
    }

}
