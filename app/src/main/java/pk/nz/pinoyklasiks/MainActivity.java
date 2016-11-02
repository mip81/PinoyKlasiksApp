package pk.nz.pinoyklasiks;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import pk.nz.pinoyklasiks.activities.AboutUs;
import pk.nz.pinoyklasiks.activities.ListOfProductsActivity;
import pk.nz.pinoyklasiks.activities.MapsActivity;
import pk.nz.pinoyklasiks.activities.OrderHistoryActivity;
import pk.nz.pinoyklasiks.activities.SubOrderActivity;
import pk.nz.pinoyklasiks.activities.fragments.LeftMenuFragment;
import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.db.IDAOManager;
import pk.nz.pinoyklasiks.db.IDBInfo;
import pk.nz.pinoyklasiks.db.IWebService;
import pk.nz.pinoyklasiks.db.WebService;
import pk.nz.pinoyklasiks.service.WeatherService;
import utils.AppConst;
import utils.CategoryAdapter;


/**<pre>
 *
 *
 * Title       : MainActivity class
 * Purpose     : To show the main screen of an app, that have the
 *               all features of the app
 * * Date        : 13.09.2016
 * Input       : List of Categories, Weather, Search query
 * Proccessing : Get the List og categories from DB and populate
 *               it in the ListView using special adapter.
 *               Create the Navigation menu using DrawerLayout
 *               and observe orientation of the screen (if it horizontal
 *               hide Navigation menu and show menu from LeftMenuFragment)
 *               Add the toolbar with menu, and search feature.
 *               Using WeatherService show the Weather in NavigationMenu
 * Output      : Intent with choosen actio
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class MainActivity extends AppCompatActivity {



    private IDAOManager dbManager;               // Manager to work with objects in DB

    private FrameLayout flLeftMenu;              // The layout for menu Fragment
    private FragmentTransaction ft;              // for operations with the fragment (ex leftMenu)
    private LeftMenuFragment leftMenuFragment;

    private ViewGroup header;                    // Header for ListView with categories
    private ListView lvCategories ;               // ListView of categories
    private CategoryAdapter adapter;             // Adapter for populating data
    private List<AbstractCategory> listAbstractCategory;   // List with categories

    private NavigationView navView;               // Left navigation menu
    private NavigationView navViewFrame;
    private DrawerLayout drawerLayout;             // The main layout in app (will be used to disable NAVIGATION VIEW)
    private ActionBarDrawerToggle drawerToggle;    // Need to create humburger menu, to link DrawerLayout and Toolbar

    private ImageView ivAdvert;                     // To show special deals
    private Handler handler;                       // To work with UI




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // define handler
        handler = new Handler();

        // TODO: TASK 1. 9) ActionBar
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

         // TODO: TASK 1 5) 2. setNavigationItemSelectedListener

            navView.setNavigationItemSelectedListener(new ClickNavMenuListener());
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);




        // dbManager manager for work with DB
          dbManager = new WebService(this);



        ///////////////////////////////////////////////////////////////////////////
        // Get configuration of application and define the ORIENTATION OF THE SCREEN
        // if the orientation is horisonal hide DrawerLayout and show the fragment
        // with menu.
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


        /***********************************
         *  Get the categories from DB
         *  Create special adapter
         *  and put it to the ListView
         * *********************************/
        lvCategories  = (ListView)findViewById(R.id.lvCategories);
           listAbstractCategory =  dbManager.getCategories();
                adapter = new CategoryAdapter(getApplicationContext(), listAbstractCategory);

        // Create View from XML layout and add it to ListView as header
        LayoutInflater inflater = getLayoutInflater();


        /****************************************
         *  Create the LAYOUT for HEADER LISTVIEW
         *  for the special deals
         ****************************************/
         header = (ViewGroup) inflater.inflate(R.layout.header_categories, lvCategories, false);


                    lvCategories.addHeaderView(header);
                    lvCategories.setAdapter(adapter);
                    // Add click listener
                        // TODO: TASK1 5) 1. ClickCategoryListener
                        lvCategories.setOnItemClickListener(new ClickCategoryListener());


        /* Start thread showing special deals */
        ShowSpecialDealsThread deals = new ShowSpecialDealsThread();
        deals.start();


        try { // Read the stub picture
            InputStream is = getAssets().open("stub_deals.jpg");
             ivAdvert.setImageDrawable(Drawable.createFromStream(is, null));

        }catch (Exception e){
            Log.e(" ::: ERR ::: ", " SET IMAGE  "+e);
        }


        /**************************
         *  Get the LayoutInflater
         *  to create view with NavigationView
         *  for filling information about the weather
         ********************************************/
        View headerLayout = navView.getHeaderView(0);

       new WeatherService(headerLayout, getApplicationContext());



    }// END ONCREATE



    /***************************
     * Assign menu for toolbar
     *
     * @param menu
     * @return boolean
     ****************************/
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

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConst.DEBUG) Log.d(AppConst.LOGD, "App onResume");
    }


    // TODO:TASK 1. 6)  3. onDestroy

    /**
     *  Will run when activity
     *  will be destriyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< MainActivity >>> :: WAS DESTROYED");

        /* Delete resourse*/
        dbManager = null;

    }

    /**
     * Need to synchronize the state (for hamburger menu)
     * this this method will start when start ic complete
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Checking if configuration was changed
     * tell to the DrawerLayout
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    /******************************************
     *  DEFINE ACTIONS FOR TOP MENU
     * @param item
     * @return
     *****************************************/
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


    /**
     * Title       : ClickNavMenuListener class
     * Purpose     : Listener for Navigation Menu
     * Date        : 04.10.2016
     * Input       : Cliked menu item
     * Proccessing : Choose the action for appropriate menu
     *             : (Create new intent or action)
     *             :
     * Output      : Start new Activity or Action
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    public class ClickNavMenuListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {

            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()){

                // Proceed click on the Navigation menu - Main Category
                case R.id.nav_menu_categories:
                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentMain);
                    break;

                // Proceed click on the Navigation menu - Order History
                case R.id.nav_menu_history:
                    Intent intentOrderHistory = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                    startActivity(intentOrderHistory);

                    break;

                // Proceed click in the Navigation menu - Cart
                case R.id.nav_menu_orders :
                    Intent intentSubOrder = new Intent(getApplicationContext(), SubOrderActivity.class);
                    startActivity(intentSubOrder);

                    break;

                // Proceed click on the Navigation menu - AboutUs
                case R.id.nav_menu_about_us:
                    Intent intentAction = new Intent(getApplicationContext(), AboutUs.class);
                    startActivity(intentAction);
                    break;

                case R.id.nav_menu_location:
                        Intent intentLocation = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intentLocation);

                    break;

                // Proceed click on the Navigation menu - Call to the Restaurant
                case R.id.nav_menu_call_us:
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+IDBInfo.PHONE_OF_RESTAURANT));
                        intentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       try{
                           startActivity(intentCall);
                       }catch (Exception e){
                           Toast.makeText(getApplication(), "Sorry this function doesn't work :(", Toast.LENGTH_SHORT).show();
                       }
                    break;

                //Proceed click on the Navigation menu - Share Feature
                case R.id.nav_menu_share:
                         Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                         sharingIntent.setType("text/plain");
                         sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://market.android.com/details?id=" + getPackageName());

                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    break;

            }
            return false;
        }
    }


    /**
     * Title       : ClickCategoryListener class
     * Purpose     : Listener for ListView
     * Date        : 04.10.2016
     * Input       : Cliked item
     * Proccessing : Get the position of clicked item and get the object
     *               and pass it to ProductActivity
     *             : (Create new intent or action)
     *             :
     * Output      : Start new ProductActivity
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
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
                // do nothing
            }
        }
    }

    // TODO: TASK 1. 8) Thread class

    /**
     * Title       : ShowSpecialDealsThread class
     * Purpose     : Class Thread
     * Date        : 04.10.2016
     * Input       : List of Deals
     * Proccessing : Through Webservices recieve the array of picture
     *               on the server side and show it on the main
     *               screen, changing them after defined time
     *             :
     * Output      : image
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class ShowSpecialDealsThread extends Thread{
        @Override
        public void run() {

            List<String> listDeals = new ArrayList<>();         // Name of files on the Server with deals
            final List<Bitmap> listBitmap = new ArrayList<>(); // Have the all images of deals

            IWebService dbManager = new WebService(getApplicationContext());
            if(dbManager.isOnline()){

                // Getting list of filenames
                listDeals = dbManager.getDeals();

                // check if there no problem with
                // getting DATA from SERVER
                if(listDeals != null && !listDeals.isEmpty()){
                    for(String imgName : listDeals){
                        try{
                            // Load image and put to List
                            String link = IDBInfo.DEALS_IMG_URL+imgName;
                            URL url = new URL(link);
                            InputStream is = (InputStream) url.getContent();
                            Bitmap img = BitmapFactory.decodeStream(is);

                            listBitmap.add(img);

                        }catch(Exception e){
                            e.printStackTrace();

                        }


                    }

                    // Get the VIEWFLIPPER and put ImageView in it
                    // TODO: TASK 1. 8 Runnable interface
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            ViewFlipper vf = (ViewFlipper) header.findViewById(R.id.view_flipper_header_deals);

                            // Go through the LIST of BITMAPS
                            for(Bitmap b: listBitmap){
                                // Dynamicaly add the ImageView to ViewFlipper
                                ImageView iv = new ImageView(getApplicationContext());
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);


                                iv.setLayoutParams(lp);
                                iv.setImageBitmap(b);
                                vf.addView(iv);

                            }

                            // start animation (showing the deals)
                            vf.setAutoStart(true);
                            vf.setFlipInterval(AppConst.TIME_CHANGE_DEALS);
                            vf.startFlipping();
                        }
                    });
                }

            }
        }
    }



}
