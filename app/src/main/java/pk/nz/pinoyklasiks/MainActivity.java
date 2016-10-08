package pk.nz.pinoyklasiks;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import pk.nz.pinoyklasiks.activities.ProductsActivity;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDBManager;
import pk.nz.pinoyklasiks.db.IDBInfo;



/**
 * The Main Activity of APP shows the main categories
 * of APP and other information
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */

public class MainActivity extends AppCompatActivity {

    private TextView tvTest;

    private ListView lvCategories ;   // ListView of categories
    private SimpleCursorAdapter scAdapter;  // Adapter for populating data
    private Cursor cursor; // Cursor with categories


    // Data for binding the fields from cursor in the lvCategories
    private String[] from = {IDBInfo.TB_CATEGORY_CAT_NAME, IDBInfo.TB_CATEGORY_DESCRIPTION};
    private int[] to = {R.id.tvCatName, R.id.tvDesc};

    private TelephonyManager telephonyManager; // get info about the phone
    private String myPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTest = (TextView)findViewById(R.id.tvOrientation);

        // Get the phone number of user
        telephonyManager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        myPhoneNumber = telephonyManager.getLine1Number();

        // db manager that has all db functions
        IDBManager db = new DBManager(this);

        // Get configuration of application later to define the orientation os the screen
        Configuration config = getResources().getConfiguration();

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){

            tvTest.setText("LANDSCAPE "+myPhoneNumber);

        }else{

            tvTest.setText("PORTRATE "+myPhoneNumber);

        }


        // Get and fill the ListView categories
        lvCategories  = (ListView)findViewById(R.id.lvCategories);
            cursor = db.getCategories(); // get cursor with categories
                scAdapter = new SimpleCursorAdapter(this ,R.layout.activity_lv_categories, cursor, from, to, 0);
                    lvCategories.setAdapter(scAdapter);
                    // Add click listener
                        lvCategories.setOnItemClickListener(new ChooseCategoryListener());


        // Install toolbar and settings for it
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(R.string.title);
                    getSupportActionBar().setIcon(R.drawable.ic_toolbal);
                        getSupportActionBar().setSubtitle(R.string.subTitle);


    }



    /**
     * Create toolbar menu which was defined in the main_menu.xml
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Addin xml with menu
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // To do for searching
         SearchView searchView  = (SearchView)menu.findItem(R.id.tmenu_search).getActionView();
         SearchManager seacrhManager = (SearchManager)getSystemService(SEARCH_SERVICE);
        //bind those methods to pass data
         searchView.setSearchableInfo(seacrhManager.getSearchableInfo(getComponentName()));


        return super.onCreateOptionsMenu(menu);

    }

    // DEFINE ACTIONS WHEN CLICKED ON TOP MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.tmenu_about:
                Toast.makeText(getApplicationContext(), "The about menu was selected!", Toast.LENGTH_LONG).show();
                break;
            case R.id.tmenu_settings:
                Toast.makeText(getApplicationContext(), "The settings menu was selected!", Toast.LENGTH_LONG).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    // Class proceed click on the category
    class ChooseCategoryListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int idCategory = cursor.getInt(0);

            Intent intentProducts = new Intent(getApplicationContext(), ProductsActivity.class);
            intentProducts.putExtra("idCategory", idCategory); // pass the id category
            startActivity(intentProducts);
        }
    }
}
