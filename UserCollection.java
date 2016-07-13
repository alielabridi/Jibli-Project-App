/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User.newpackage;

import User.User;
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


/**
 *
 * @author Mohamed
 */
public class UserCollection {
   
   static List<User> users;

   
   public UserCollection(){
      users=new LinkedList<>();
   }
   public void addMember(User m){
      
      users.add(m);
  
      
   }
   public void removeUser(User m){
      
      users.remove(m);
   }
   public void editUser(User user,User newObject)
   {
      
      users.set(users.indexOf(user), newObject);
   }
 
   public boolean authenticateUser (User user)
   {
        ListIterator<User> iter = users.listIterator();
        while (iter.hasNext()) {
            User us = iter.next();
            if (us.getPassWord().equals(user.getPassWord()) && user.getEmail().equalsIgnoreCase(user.getEmail())) {
               return true;
            } 
            }
            return false;
 
   }
   public class sendingUser extends AsyncTask<Void, Void, Boolean> {
 private String passWord;
   private String name;
   private String email;
   private String phoneNumber;

   public sendingUser(String passWord, String name, String email, String phoneNumber, int rating, double transact) {
      this.passWord = passWord;
      this.name = name;
      this.email = email;
      this.phoneNumber = phoneNumber;

   }


        private JSONObject constructJsonObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("U_EMAIL", email);
                jsonObject.accumulate("U_PASSWORD", passWord);
                jsonObject.accumulate("U_PHONE", phoneNumber);
                jsonObject.accumulate("U_LNAME", name);

     

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
                        sendingUser = null;
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
                        sendingUser = null;
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
                        sendingUser = null;
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
            sendingUser = null;
            //showProgress(false);
        }
    }


    public void JsonUserReader(InputStream inputStream) throws IOException {
        try {
            readJsonStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            readUsersArray(reader);
        }
        finally{
            reader.close();
        }
    }


    public void readUsersArray(JsonReader reader) throws IOException {
        //read the array and go through all the objects inside
        reader.beginArray();
        while (reader.hasNext()) {
            readUser(reader);
        }
        reader.endArray();
    }


    public void readUser(JsonReader reader) throws IOException, ParseException {
        //assume that if not specificed these proprieties are null
              String passWord;
               String name1;
               String email;
               String phoneNumber;


        //start reading an object from the array
        reader.beginObject();
        //look for name/value pairs i.e. filename & User
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("U_EMAIL")) {
                email =  reader.nextInt();
            } else if (name.equals("U_PASSWORD")) {
               passWord = reader.nextString();
            } else if (name.equals("U_PHONE")) {
                 phoneNumber=  reader.nextString();
            } else if (name.equals("U_LNAME")) {
                name1 = reader.nextString();
       
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

         User use = new User(  passWord,  name1,  email,  phoneNumber);

        users.add(use);

    }


    public void UpdateUsers() {

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

     /*   inputStream

        // return JSON String


        jObj.
        try {
            return (new User((String)jObj.get("M_MESSAGE"),(Date) jObj.get("M_DATE"),(String)jObj.get("RECEIVER"),(String)jObj.get("SENDER")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;*/
    }
 