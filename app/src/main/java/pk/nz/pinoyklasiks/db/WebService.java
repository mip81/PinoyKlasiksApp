package pk.nz.pinoyklasiks.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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
       new RetrieveJSONTask(null).execute();
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
                try {
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
     * Proccess of request and recieve the answer from server
     *
     */
    class RetrieveJSONTask extends AsyncTask<String, String, Void>{
        String message;
        ProgressDialog progressDialog;


        /**
         *  If message is null do not show the ProgressDialog
         * @param message will be shown in ProgressDialog
         */
        RetrieveJSONTask(String message){
            // Message wil be shown in the ProgressDialog
            this.message = message;

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
                URL url = new URL(WSHOST);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");

                InputStream is = new BufferedInputStream(con.getInputStream());
                Log.d(AppConst.LOGD, " ::: Converted Stream from Server : "+convertStreamToString(is));

            }catch (Exception e){
                if(AppConst.DEBUG) Log.e(AppConst.LOGE, " ::: isTheLastVersion ::: "+e);

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
