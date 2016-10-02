package pk.nz.pinoyklasiks;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Install toolbar and settings for it
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title);
        getSupportActionBar().setIcon(R.drawable.ic_toolbal);
        getSupportActionBar().setSubtitle(R.string.subTitle);

        // Check if the action search then searching
        Intent searchIntent = getIntent();
        if(Intent.ACTION_SEARCH.equals(searchIntent.getAction())){
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, " QUERY IS "+query, Toast.LENGTH_LONG ).show();
        }



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
}
