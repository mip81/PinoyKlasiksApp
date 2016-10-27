package pk.nz.pinoyklasiks.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private static final int SLEEP_TIME = 3000;  // Time to wait the answer from server
    public final String CHECK_UPDATE = "check_update";

    private JSONObject jsonGlobalId; // JSON Object with global id
    private JSONObject jsonStatus;   // JSON Obkect with status information
    private boolean isRetrieveTaskNotFinished = true;  // serve to show that task finished and the method can return result
    private JSONObject jsonLastDateTimeChange;

    public WebService(Context ctx) {
        super(ctx);
    }

    /**
     * Checking Internet Access
     * Return true if the app have access to internet
     * @return boolean (true if has connection)
     */
    @Override
    public boolean isOnline() {

        // Getting connectivity service
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //check if network is available
        if( networkInfo != null && networkInfo.isConnectedOrConnecting() ){
            if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< WebService >>> : isOnline : "+ true);

            return true;
        }
            if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< WebService >>> : isOnline : "+ false);

        return false;

    }


    /**
     * Check if server has newer version of DB
     * @return boolean true is new version on the server
     */
    @Override
    public boolean isTheLastVersion() {
        // // TODO: 10/27/16 check version
        return false;
    }


    /**
     * CHECKING the status of ORDERID
     *
     * @param globalId
     * @return int id of status -1 if there is no the order
     */
    public int checkStatusOrder(int globalId){
        int statusId = -1; // return the status ID number

        // the object which will be send to server
        JSONObject jsonGlobalId = new JSONObject();
           try {

               jsonGlobalId.put("orderId", globalId);

               // Start task sending the json object with orderId to the server
               // and get back via CallBack method jsonObject which has statusId
               RetrieveJSONTask jsonTask = new RetrieveJSONTask("Checking the status of order...", jsonGlobalId, JSON_ACTION_CHECK_STATUS_ORDER, new AsyncJSONResponse() {
                   @Override
                   public void getResult(JSONObject jsonResult) {
                       jsonStatus = jsonResult;
                   }
               });
               jsonTask.execute();

               // Try to stop this thread until result will be ready
               try{
                   Thread.sleep(SLEEP_TIME);
               }catch(Exception e){

               }

           }catch(JSONException e){

               Log.e(AppConst.LOGE, " <<< WebService >>> ::: checkStatusOrder ::: "+ e.getMessage());
           }
            // CHECK if returned JSON Object not nul
            // READ STATUS ID
            if(jsonStatus != null){
                try{
                    statusId = jsonStatus.getInt("status_id");
                }catch (JSONException e){
                    Log.e(AppConst.LOGE, " <<< WebService >>  ::: checkStatusOrder ::: "+e.getMessage());
                }
            }

        return statusId;
    }




    /**
     * GET THE DATETIME LAST VERSION from the server
     * @return Date of last changes or null
     */
    public Date getTheLastVersionDateTime(){

        Date lastChangeDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(IDBInfo.MYSQL_DATETIME_PATTERN);
        jsonLastDateTimeChange = new JSONObject();


            // Start task sending the json object with DateTime of LAST CHANGES to the server
            // and get back via CallBack method jsonObject which has statusId
            RetrieveJSONTask jsonTask = new RetrieveJSONTask("Checking the status of order...", jsonLastDateTimeChange, JSON_ACTION_CHECK_VERSION, new AsyncJSONResponse() {
                @Override
                public void getResult(JSONObject jsonResult) {
                    jsonLastDateTimeChange = jsonResult;
                }
            });
            jsonTask.execute();


        try{
            Thread.sleep(SLEEP_TIME);
        }catch (Exception e){
            Log.e(AppConst.LOGE, "<<< WebService >> ::: getTheLastVersionDateTime ::: "+e.getMessage());
        }

        // CHECK if returned JSON Object not nul
        // READ STATUS ID
        if(jsonLastDateTimeChange != null){
            try{
                lastChangeDate = sdf.parse(jsonLastDateTimeChange.getString("last_changes_datetime"));
            }catch (ParseException e){
                Log.e(AppConst.LOGE, " <<< WebService >>  ::: getTheLastVersionDateTime ::: "+e.getMessage());
            }catch (Exception e){
                Log.e(AppConst.LOGE, " <<< WebService >>  ::: getTheLastVersionDateTime ::: "+e.getMessage());
            }
        }

        return lastChangeDate;
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

        // Check if the order object not null
        // otherwise return 0
        if (jsonOrder == null){
            Log.d(AppConst.LOGD, " ::: sendJSONORder ::: jsonOrder ::: is null");
            return 0;
        }


        // Start task sending the json object with order to the server
        // and get back via CallBack method jsonObject which has globalId
        RetrieveJSONTask jsonTask = new RetrieveJSONTask("Sending the order...", jsonOrder, JSON_ACTION_NEW_ORDER, new AsyncJSONResponse() {
            @Override
            public void getResult(JSONObject jsonResult) {
              //  jsonGlobalId = jsonResult;
            }
        });

            // get Result from Server
            try{
                jsonGlobalId = jsonTask.execute().get();

            }catch(Exception e){

            }

        // Read returned JSON object with OrderID
        if(jsonGlobalId != null){
            try {

                return jsonGlobalId.getInt("orderId");

            }catch (JSONException e){

                Log.e(AppConst.LOGE, "Error when get the globalID "+e.toString());
                return 0;
            }

        }

        return 0;
    }




    /**
     * Proccess of request and recieve the answer from server
     *
     */
    class RetrieveJSONTask extends AsyncTask<String, Void, JSONObject>{
        private String message;     // Message which be shown to User
        private JSONObject json;    // JSON object which we send
        private ProgressDialog progressDialog; // Progress dialog will be shown to user
        private  JSONObject jsonResult; // Result from server
        private AsyncJSONResponse jsonResponse; // Call back method


        /**
         *  Define main settings for getting and receiving JSON objects
         *
         * @param message will be shown in ProgressDialog if null not be shown
         * @param json JSON Object will be sent
         * @param action the constant show what should be done on the server
         */
        RetrieveJSONTask(String message, JSONObject json, String action, AsyncJSONResponse jsonResponse){
            // Message wil be shown in the ProgressDialog
            this.message = message;

            this.jsonResponse = jsonResponse;

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
        protected JSONObject doInBackground(String... params) {
            try{
                URL url = new URL(WEBSERVICE_SHOST);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // Allow input and output
                con.setDoOutput(true);
                con.setDoInput(true);

                // Method of request
                con.setRequestMethod("POST");


               // Set credentials
                JSONObject cred = new JSONObject();
                cred.put("username" , IDBInfo.USERNAME);
                cred.put("password", IDBInfo.PASSWORD);

                // Add  credentials to main JSON object
                json.put("cred" ,cred);



                // add parameter to request
                Uri.Builder builder  = new Uri.Builder()
                        .appendQueryParameter("request", json.toString());
                String query = builder.build().getEncodedQuery();


                // Get stream and send request
                OutputStream os = con.getOutputStream();
                BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(os));
                writer.write(query); // add parameters to request
                writer.flush();

                // close resources
                writer.close();
                os.close();



                // Receive response from server
                String response = convertStreamToString(con.getInputStream());

                Log.d(AppConst.LOGD, " :::>> WEBSERVICE RESPONSE <<::: Converted Stream from Server |:"+response);

                // convert response from server to JSON object
                jsonResult = new JSONObject(response);





            }catch (JSONException e){
                // Catching errors
                Log.e(AppConst.LOGE, " ::: RetrieveJSONTask ::: "+e);
            }catch (Exception e){

                Log.e(AppConst.LOGE, " ::: RetrieveJSONTask ::: " + e);
            }

            // send the result object to CallBack method;
            jsonResponse.getResult(jsonResult);

            return jsonResult;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    /**
     *  Used as callBack function to retrieve
     *  answer from the server
     */
    interface AsyncJSONResponse{
        void getResult(JSONObject jsonResult);

    }

}
