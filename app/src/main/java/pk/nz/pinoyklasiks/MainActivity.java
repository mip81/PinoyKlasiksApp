package pk.nz.pinoyklasiks;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;


import pk.nz.pinoyklasiks.activities.ListOfProductsActivity;
import pk.nz.pinoyklasiks.activities.fragments.LeftMenuFragment;
import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.beans.Address;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import pk.nz.pinoyklasiks.db.WebService;
import utils.AppConst;
import utils.CategoryAdapter;


/**
 * The Main Activity of APP shows the main categories
 * of APP and other information
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */

public class MainActivity extends AppCompatActivity {

    private TextView tvTest;

    private IDAOManager db;

    private FrameLayout flLeftMenu;     // The layout for menu Fragment
    private FragmentTransaction ft;    // for operations with the fragment (ex leftMenu)
    private LeftMenuFragment leftMenuFragment;

    private ListView lvCategories ;          // ListView of categories
    private CategoryAdapter adapter;        // Adapter for populating data
    private List<AbstractCategory> listAbstractCategory;   // List with categories

    private NavigationView navView;               // Left navigation menu
    private DrawerLayout drawerLayout;           // The main layout in app (will be used to disable NAVIGATION VIEW)
    private ActionBarDrawerToggle drawerToggle; // Need to create humburger menu, to link DrawerLayout and Toolbar

    private ImageView ivAdvert;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //<<<< TEST ZONE >>>>>


            Order order = new DBManager(this).getOrderById(1);
            new WebService(this).sendJSONORder(order);


        //<<<< TEST ZONE >>>>>

        // Install toolbar and settings for it
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(R.string.title);


        // WORKING WITH DRAWER LAYOUT AND NAVIGATION VIEW
        flLeftMenu = (FrameLayout)findViewById(R.id.flLeftMenu); // Get the layout for leftMenu
        leftMenuFragment = new LeftMenuFragment();              // Fragment with left menu;
        navView = (NavigationView)findViewById(R.id.navViewMenu);
        navView.setItemIconTintList(null);

        // Add listener that proceed clicks om menu
            navView.setNavigationItemSelectedListener(new ClickNavMenuListener());
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);




        // db manager for work with DB
          db = new WebService(this);



        ///////////////////////////////////////////////////////////////////////////
        // Get configuration of application and define the ORIENTATION OF THE SCREEN
        Configuration config = getResources().getConfiguration();

            if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){

                drawerLayout.closeDrawer(GravityCompat.START);

                flLeftMenu.setVisibility(View.VISIBLE);
                    ft = getFragmentManager().beginTransaction();       // get the fragment transation
                    ft.add(R.id.flLeftMenu, leftMenuFragment);
                    ft.commit();

                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        drawerToggle.setDrawerIndicatorEnabled(false);

                    }
                }, 1000);

            }else{
                // find and remove loaded fragment
                if( ((LeftMenuFragment) getFragmentManager().findFragmentById(R.id.flLeftMenu) ) != null){
                    ft = getFragmentManager().beginTransaction();
                    ft.remove(getFragmentManager().findFragmentById(R.id.flLeftMenu));
                    ft.commit();
                }

                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }


        ////////////////////////////////////////
        // Get and fill the ListView categories
        lvCategories  = (ListView)findViewById(R.id.lvCategories);
           listAbstractCategory =  db.getCategories();
                adapter = new CategoryAdapter(getApplicationContext(), listAbstractCategory);

        // Create View from XML layout and add it to ListView as header
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_categories, lvCategories, false);
                    lvCategories.addHeaderView(header);
                    lvCategories.setAdapter(adapter);
                    // Add click listener
                        lvCategories.setOnItemClickListener(new ClickCategoryListener());



        // get the ImageView from header where we gonna show specials
        ivAdvert = (ImageView) header.findViewById(R.id.ivAdvert);



        try {
            InputStream is = getAssets().open("adv1.jpg");
             ivAdvert.setImageDrawable(Drawable.createFromStream(is, null));

        }catch (Exception e){
            Log.e(" ::: ERR ::: ", " SET IMAGE  "+e);
        }

    }


    /**
     * Create toolbar menu which was defined in the activity_mainmain.xml
     * @param menu
     * @return boolean
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adding menu
        getMenuInflater().inflate(R.menu.activity_main, menu);

        // To do for searching
         SearchView searchView  = (SearchView)menu.findItem(R.id.tmenu_search).getActionView();
         SearchManager seacrhManager = (SearchManager)getSystemService(SEARCH_SERVICE);

        //bind those methods to pass data
         searchView.setSearchableInfo(seacrhManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }




    // Need to synchronize the state (for hamburger menu)
    // this this method will start when start ic complete
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }



///////////////////////////////////////////////////////
// LISTENERS /////////////////////////////////////////
//////////////////////////////////////////////////////

    // DEFINE ACTIONS WHEN CLICKED ON TOP MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // For hamburger menu
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()){
            case R.id.tmenu_cart:
                // open suborder activity (with status OPEN)
                startActivity(new Intent("pk.nz.pinoyklasiks.open_suborder"));
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, " OPEN SUBORDER ACTIVITY WITH STATUS OPEN ::: ");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // LISTENER FOR NAVIGATION VIEW
    class ClickNavMenuListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_menu_categories:
                    Toast.makeText(getApplicationContext(),"MENU", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.nav_menu_about_us:
                    // TODO: 10/9/16 Add code for loading fragments
                    Toast.makeText(getApplicationContext(),"ABOUT US", Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }
    }


    //LISTENER FOR CATEGORIES
    class ClickCategoryListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position > 0){ // if clicked not on header of listview
                
                // get the id of categories and pass it to the ProductActivity
                int idCategory = listAbstractCategory.get(position-1).getId();
                String nameCategory = listAbstractCategory.get(position-1).getCatName();
                // position - 1 because of header was attached before
    
                Intent intentProducts = new Intent(getApplicationContext(), ListOfProductsActivity.class);
                intentProducts.putExtra("idCategory", idCategory);      // pass the id category
                intentProducts.putExtra("nameCategory", nameCategory ); // pass the name of category
                startActivity(intentProducts);
            
            }else{

                // TODO: 10/12/16  Do smth when we clicked on header of  categoryList
                
            }
        }
    }
}
