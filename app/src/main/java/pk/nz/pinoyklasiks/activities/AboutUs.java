package pk.nz.pinoyklasiks.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import pk.nz.pinoyklasiks.R;

/**<pre>
 *
 * Title       : AboutUs class
 * Purpose     : To show information about restaurant
 * Date        : 01.11.2016
 * Input       : none
 * Proccessing : Dislpay picture of restaurant, mail, phone number
 *
 * Output      : none
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Get toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_about_us);

        // Set toolbar and it's settings
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About us");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

    }
}
