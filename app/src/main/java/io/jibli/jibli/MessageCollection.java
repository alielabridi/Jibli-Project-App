/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.jibli.jibli;


/**
 *
 * @author Mohamed
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

public class MessageCollection extends AppCompatActivity {

   
    List<Message> messages;
    private sendingMessage sendingMessage = null;



    public MessageCollection() {
        this.messages = new LinkedList<>();
    }

    public boolean addmessage(Message msg) {
        new sendingMessage(msg.getContent(),msg.getDate(),msg.getSender_ID(),msg.getReceiver_ID());
        return messages.add(msg);
    }

    public boolean removemessage(Message msg) {
        return messages.remove(msg);
    }
    
    public ArrayList<Message>  retrieveconversation (int userid)
    {
        ListIterator<Message> iter = messages.listIterator();
        ArrayList<Message> results = new ArrayList<>();
        while (iter.hasNext()) {
            Message ms = iter.next();
             if (ms.getSender_ID()==userid) {
               results.add(ms);
             }
             else if (ms.getReceiver_ID()==userid)
                results.add(ms); 
        }
           
        return results;  
    }
 
    
    public void modifyMessage(Message search, Message newMsg) {
   
        int index = messages.indexOf(search);
        if (index >= 0) {
            messages.set(index, newMsg);
        }
    }
  
    @Override
    public String toString() {
        String str = "";
        ListIterator<Message> iter = messages.listIterator();
        while (iter.hasNext()) {
            Message st = iter.next();
            str += st.toString() + "\n";
        }
        return str;
    }

    public ListIterator<Message> getStudents() {
        return messages.listIterator();
    }



    public class sendingMessage extends AsyncTask<Void, Void, Boolean> {
        private String content;
        private Date date;
        private int sender_ID;
        private int receiver_ID;

        public sendingMessage(String content, Date date, int sender_ID, int receiver_ID) {
            this.content = content;
            this.date = date;
            this.sender_ID = sender_ID;
            this.receiver_ID = receiver_ID;
        }


        private JSONObject constructJsonObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("BU_ID", sender_ID);
                jsonObject.accumulate("BR_ID", receiver_ID);
                jsonObject.accumulate("M_MESSAGE", content);
                jsonObject.accumulate("M_DATE", date);
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
                URL url = new URL("http://www.scantosign.com/sheet?q=");
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
                        sendingMessage = null;
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
                        sendingMessage = null;
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
                        sendingMessage = null;
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
            sendingMessage = null;
            //showProgress(false);
        }
    }



    public void JsonMessageReader(InputStream inputStream) throws IOException {
        try {
            readJsonStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            readMessagesArray(reader);
        }
        finally{
            reader.close();
        }
    }


    public void readMessagesArray(JsonReader reader) throws IOException {
        //read the array and go through all the objects inside
        reader.beginArray();
        while (reader.hasNext()) {
            readMessage(reader);
        }
        reader.endArray();
    }


    public void readMessage(JsonReader reader) throws IOException {
        //assume that if not specificed these proprieties are null
        String M_MESSAGE = null;
        String M_DATE = null;
        int SENDER = 0;
        int RECEIVER = 0;

        //start reading an object from the array
        reader.beginObject();
        //look for name/value pairs i.e. filename & Message
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("SENDER")) {
                SENDER =  reader.nextInt();
            } else if (name.equals("RECEIVER")) {
                RECEIVER = reader.nextInt();
            } else if (name.equals("M_MESSAGE")) {
                M_MESSAGE =  reader.nextString();
            } else if (name.equals("M_DATE")) {
                M_DATE = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Message message = new Message(M_MESSAGE,M_DATE,RECEIVER,SENDER);
        messages.add(message);

    }


    public void UpdateMessages() {

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
        }

        try {
            JsonMessageReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return JSON String
/*

        try {
            return (new Message((String)jObj.get("M_MESSAGE"),(Date) jObj.get("M_DATE"),(String)jObj.get("RECEIVER"),(String)jObj.get("SENDER")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;*/
    }


   

}
