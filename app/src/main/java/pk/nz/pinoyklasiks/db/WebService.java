package pk.nz.pinoyklasiks.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import pk.nz.pinoyklasiks.beans.Address;
import pk.nz.pinoyklasiks.beans.Customer;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.SubOrder;
import utils.AppConst;

/**
 * Class has methods to works with
 * webservices
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class WebService extends DBManager implements IWebService{

    public final String CHECK_UPDATE = "check_update";


    public WebService(Context ctx) {
        super(ctx);
    }







    /**
     * Check if server has newer version of DB
     * @return boolean true is new version on the server
     */
    @Override
    public boolean isTheLastVersion() {
       //new RetrieveJSONTask(null, ).execute();
        return false;
    }



    /**
     * Convert InputStream to String
     * after the method close passed InputStream
     * @param is InputStream
     * @return String
     */
    public String convertStreamToString(InputStream is){
        BufferedReader br = null;
        InputStreamReader inputStreamReader = null;
        StringBuilder sb = new StringBuilder();
        String str;
            try{

                inputStreamReader = new InputStreamReader(is);

                br = new BufferedReader(new InputStreamReader(is));

                //read bufferedreader and return the string
                    while((str = br.readLine()) != null){
                        sb.append(str);
                    }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try { // close resouces
                    inputStreamReader.close();
                    is.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        return sb.toString();

    }





    /**
     * Method convert orderto JSON and  send to the server
     * and the get the returned global id
     * (later the user will check the status by this id)
     *
     * @param order
     * @return int the global id
     */
    public int sendJSONORder(Order order){
        JSONObject jsonOrder = order.toJSON();

        if (jsonOrder == null){
            Log.d(AppConst.LOGD, " ::: sendJSONORder ::: jsonOrder ::: is null");
            return 0;
        }
        new RetrieveJSONTask("Sending the order...", jsonOrder, JSON_ACTION_NEW_ORDER ).execute();


        return 0;
    }


    /**
     * Proccess of request and recieve the answer from server
     *
     */
    class RetrieveJSONTask extends AsyncTask<String, String, Void>{
        String message;
        JSONObject json;
        ProgressDialog progressDialog;


        /**
         *  Define main settings for getting and receiving JSON objects
         *
         * @param message will be shown in ProgressDialog if null not be shown
         * @param json JSON Object will be sent
         * @param action the constant show what should be done on the server
         */
        RetrieveJSONTask(String message, JSONObject json, String action){
            // Message wil be shown in the ProgressDialog
            this.message = message;
            try{

                json.put("action", action);
                this.json = json;

            }catch (JSONException e){
                Log.e(AppConst.LOGE, " ::: RetrieveJSONTask ::: "+e);
            }

        }

        @Override
        protected void onPreExecute() {

            if(message != null){
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL(WEBSERVICE_SHOST);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // Allow input and output
                con.setDoOutput(true);
                con.setDoInput(true);

                // Fill header fields of request
                //con.setRequestProperty("Content-Type", "aplication/json");
                //con.setRequestProperty("Accept" , "application/json");

                con.setRequestMethod("POST");


               // Set credentials
                JSONObject cred = new JSONObject();
                cred.put("username" , IDBInfo.USERNAME);
                cred.put("password", IDBInfo.PASSWORD);

                // Add  credentials to main JSON object
                json.put("cred" ,cred);



                // add parameter to request
                Uri.Builder builder  = new Uri.Builder()
                        .appendQueryParameter("order", json.toString());
                String query = builder.build().getEncodedQuery();


                // Get stream and send request
                OutputStream os = con.getOutputStream();
                BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(os));
                writer.write(query); // add parameters to request
                writer.flush();
                writer.close();
                os.close();



                // Receive response from server
                InputStream is = new BufferedInputStream(con.getInputStream());
                Log.d(AppConst.LOGD, " :::>> WEBSERVICE RESPONSE <<::: Converted Stream from Server : "+convertStreamToString(is));


            }catch (JSONException e){
                // Catching errors
                Log.e(AppConst.LOGE, " ::: isTheLastVersion ::: "+e);
            }catch (Exception e){

                Log.e(AppConst.LOGE, " ::: isTheLastVersion ::: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

        }
    }

}
