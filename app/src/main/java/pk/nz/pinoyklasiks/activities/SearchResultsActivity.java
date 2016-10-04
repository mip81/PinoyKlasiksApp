package pk.nz.pinoyklasiks.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import pk.nz.pinoyklasiks.R;

/**
 * The Activity shows search results
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */

public class SearchResultsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        // ADD TOOLBAR FOR THIS ACTIVITY
            Toolbar toolbar = (Toolbar) findViewById(R.id.search_result_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setIcon(R.drawable.ic_toolbal);
            getSupportActionBar().setTitle(R.string.title);
                // add support back button
            getSupportActionBar().setHomeButtonEnabled(true);
                // Take us up to the parent activity (which was declred in MANIFEST file for this ACTIVITY)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // GET THE SEARCH QUERY AND DO THE SEARCH
            Intent intentSearch = getIntent();

        // if action is search do the search
            if(Intent.ACTION_SEARCH.equals(intentSearch.getAction())){
                String searchQuery = intentSearch.getStringExtra(SearchManager.QUERY);
                String search_lbl = getResources().getString(R.string.search_result_subtitle);
                getSupportActionBar().setSubtitle(search_lbl +"  "+ searchQuery);

                Toast.makeText(this,"The query for search is :"+searchQuery, Toast.LENGTH_LONG ).show();
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
        // Bind those methods to pass data
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
