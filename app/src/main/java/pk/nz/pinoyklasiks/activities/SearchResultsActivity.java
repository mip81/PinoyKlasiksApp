package pk.nz.pinoyklasiks.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Product;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import utils.AppConst;
import utils.ProductAdapter;

/**<pre>
 *
 * Title       : SearchResultsActivity class
 * Purpose     : Class to show search results
 *
 * Date        : 09.10.2016
 * Input       : Serach query
 * Proccessing : Search and return list of Product from DB
 * Output      : ListView with result
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class SearchResultsActivity extends AppCompatActivity{

    // DAO Manager to work with objects and DB
    private IDAOManager dbManager;

    // ListView shows found results
    private ListView lvSearchResults;

    // Adapter for ListView with results
    private ProductAdapter productAdapter;

    // Result of query
    private List<AbstractProduct> productList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);


        // Assign view from layout
        lvSearchResults = (ListView)findViewById(R.id.lvSearchResults);

        // Get DAOManager
        dbManager = new DBManager(this);





        // ADD TOOLBAR FOR THIS ACTIVITY
            Toolbar toolbar = (Toolbar) findViewById(R.id.search_result_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.title);
                // add support back button
            getSupportActionBar().setHomeButtonEnabled(true);
                // Take us up to the parent activity (which was declred in MANIFEST file for this ACTIVITY)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // customize back home button
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


        // GET THE SEARCH QUERY AND DO THE SEARCH
            Intent intentSearch = getIntent();

        // if action is search do the search
            if(Intent.ACTION_SEARCH.equals(intentSearch.getAction())){
                // get query from intent put into subtitle
                String searchQuery = intentSearch.getStringExtra(SearchManager.QUERY);

                String search_lbl = getResources().getString(R.string.search_result_subtitle);
                getSupportActionBar().setSubtitle(search_lbl +"  "+ searchQuery);


                //Get the product which fit to query
                productList = dbManager.getProductByQuery(searchQuery);

                //create adapter and assign it to list view to show result
                productAdapter = new ProductAdapter(this, productList);
                lvSearchResults.setAdapter(productAdapter);

                // Assign listener to results

                lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        // read the product and pass it to the ProductView
                        AbstractProduct product = productList.get(position);

                        if (AppConst.DEBUG) Log.d(AppConst.LOGD, product.toString());

                        // Create Intent for ProductActivity and pass the selected product there
                        // Start new activity
                        Intent productIntent = new Intent(SearchResultsActivity.this, ProductActivity.class);
                        productIntent.putExtra("product", product);

                        startActivity(productIntent);
                    }
                });

            }



    }


    /**
     * Create toolbar menu which was defined in the activity_mainmain.xml
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Addin xml with menu
            getMenuInflater().inflate(R.menu.activity_main, menu);

        // To do for searching
            SearchView searchView  = (SearchView)menu.findItem(R.id.tmenu_search).getActionView();
            SearchManager seacrhManager = (SearchManager)getSystemService(SEARCH_SERVICE);

        // / Bind those methods to pass data
            searchView.setSearchableInfo(seacrhManager.getSearchableInfo(getComponentName()));


            return super.onCreateOptionsMenu(menu);

    }

    // DEFINE ACTIONS WHEN CLICKED ON THE ACTIONBAR MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.tmenu_cart:
                    Intent intentCart = new Intent(this, SubOrderActivity.class);
                    startActivity(intentCart);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
