package pk.nz.pinoyklasiks.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.db.IDBInfo;
import pk.nz.pinoyklasiks.db.IWebService;
import pk.nz.pinoyklasiks.db.WebService;


/**<pre>
 *
  * Title       : WeatherService class
  * Purpose     : Class service for getting Weather show it
  * Date        : 01.10.2016
  * Input       : NavigationView of the left menu
  * Proccessing : Get the JSON object with current weather from internet
 *                  get the the information about current temperature
 *                  and name of file for picture get it and put to the ImageView
  * Output      : Fill the widgets in the Navigation View header
  *
 * </pre>
  * @author Mikhail PASTUSHKOV
  * @author Melchor RELATADO
 */

public class WeatherService {

    // Class to access the method to check internet
    // connection and work with webservices;
    private IWebService dbManager;

    // Layout of HeaderView
    private View navHeaderLayout;

    // Image with temp
    private ImageView ivPicWeather;

    // Show the current temp
    private TextView tvTemp;

    // current date and day of the week
    private TextView tvAKLDate;

    JSONObject jsonWeather;

    // name picture represented the current weather
    private String picName;

    // current temp in Auckland
    private double temp;

    // Interact with UI
    Handler handler;

    // Wether description
    private String desc;

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, EEEE");


    /**
     * Title       : WeatherService class
     * Purpose     : Class service for getting Weather show it
     * Date        : 01.10.2016
     * Input       : NavigationView of the left menu
     * Proccessing : Get the JSON object with current weather from internet
     *                  get the the information about current temperature
     *                  and name of file for picture get it and put to the ImageView
     * Output      : Fill the widgets in the Navigation View header
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    public WeatherService(View view, Context ctx) {

        this.navHeaderLayout = view;
        // get the Views from
        ivPicWeather = (ImageView) navHeaderLayout.findViewById(R.id.ivNavMenuTempPic);
        // get the temp view
        tvTemp = (TextView) navHeaderLayout.findViewById(R.id.tvNavMenuTemp);
        // get the Date view
        tvAKLDate = (TextView) navHeaderLayout.findViewById(R.id.tvAKLDate);

        // Get handler
        handler = new Handler();

        // Manager to work with Webservice
        dbManager = new WebService(ctx);

        // Start task getting the weather info
        new GetWeatherTask().start();//execute();
    }


    /**
     * Title       : GetWeatherTask class
     * Purpose     : Class Thread get the current weather and show it to user
     * Date        : 02.10.2016
     * Input       : none
     * Proccessing : Connected to the server which offer JSON object with the last weather
     * Get the JSON object, parse it and fill the fields in the NavigationMenu
     * with current date and the weather
     * Output      : none
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class GetWeatherTask extends Thread {


        @Override
        public void run() {

            // Show the current day in the Navigation Menu
            LinearLayout llCityDate = (LinearLayout) navHeaderLayout.findViewById(R.id.llCityDate);
            llCityDate.setVisibility(View.VISIBLE);

            // Check if user have internet connection
            if (dbManager.isOnline()) {
                BufferedReader br = null; // for reading remote JSON
                InputStreamReader isr = null;

                try {
                    // Get the connection
                    URL url = new URL(IDBInfo.WEATHER_LINK);
                    URLConnection conn = url.openConnection();


                    // read ouptut from server
                    StringBuilder sb = new StringBuilder();

                    // every line from the stream
                    String line;

                    isr = new InputStreamReader(conn.getInputStream());
                    br = new BufferedReader(isr);

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // create JSON object and parse it
                    jsonWeather = new JSONObject(sb.toString());


                    // get name of the picture
                    picName = ((JSONObject) jsonWeather.getJSONArray("weather").get(0)).getString("icon");

                    temp = jsonWeather.getJSONObject("main").getDouble("temp") - 273.15;
                    desc = ((JSONObject) jsonWeather.getJSONArray("weather").get(0)).getString("main");

                    // Getting image for weather
                    url = new URL("http://openweathermap.org/img/w/" + picName + ".png");
                    conn = url.openConnection();
                    InputStream is = (InputStream) conn.getContent();
                    final Bitmap b = BitmapFactory.decodeStream(is);


                    // Refresh UI part with weather
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {// catch the exception for double format

                                // set image, temperature, and date
                                ivPicWeather.setImageBitmap(b);
                                tvTemp.setText(new DecimalFormat("##.0").format(temp) + "°C, " + desc);
                                tvAKLDate.setText(sdf.format(new Date()));

                                // Make weather layouts visible
                                LinearLayout llWeather = (LinearLayout) navHeaderLayout.findViewById(R.id.llWeather);

                                llWeather.setVisibility(View.VISIBLE);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (Exception e) {
                    // Show error messages
                    e.printStackTrace();
                } finally {
                    try {
                        // close resources
                        if (isr != null && br != null) {
                            isr.close();
                            br.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                }
            }
        }
    }
}