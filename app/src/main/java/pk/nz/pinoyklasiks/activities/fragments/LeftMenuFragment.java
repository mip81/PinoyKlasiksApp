package pk.nz.pinoyklasiks.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pk.nz.pinoyklasiks.MainActivity;
import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.activities.AboutUs;
import pk.nz.pinoyklasiks.activities.MapsActivity;
import pk.nz.pinoyklasiks.activities.OrderHistoryActivity;
import pk.nz.pinoyklasiks.activities.SubOrderActivity;
import pk.nz.pinoyklasiks.db.IDBInfo;
import utils.AppConst;

/**<pre>
 *
 * Title       : LeftMenuFragment class
 * Purpose     : Menu for MainActivity
 *               will be show when horizontal screen
 * Date        : 24.09.2016
 * Input       : none
 * Proccessing : none
 *
 * Output      : fragment
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class LeftMenuFragment extends Fragment {

    private NavigationView  navViewFrame;
    private ViewGroup container;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.container = container;

         view = inflater.inflate(R.layout.fragment_left_menu, null);

         //Get the LEFT MENU in the frame and set Listener to procced menu actions
        navViewFrame = (NavigationView)view.findViewById(R.id.nav_menu_left_frame);
        navViewFrame.setNavigationItemSelectedListener(new ClickNavMenuListener());
        navViewFrame.setNavigationItemSelectedListener(new ClickNavMenuListener());
        return view;

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


            switch (menuItem.getItemId()){

                // Proceed click on the Navigation menu - Main Category
                case R.id.nav_menu_categories:
                    Intent intentMain = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intentMain);
                    break;

                // Proceed click on the Navigation menu - Order History
                case R.id.nav_menu_history:
                    Intent intentOrderHistory = new Intent(view.getContext(), OrderHistoryActivity.class);
                    startActivity(intentOrderHistory);

                    break;

                // Proceed click in the Navigation menu - Cart
                case R.id.nav_menu_orders :
                    Intent intentSubOrder = new Intent(view.getContext(), SubOrderActivity.class);
                    startActivity(intentSubOrder);

                    break;

                // Proceed click on the Navigation menu - AboutUs
                case R.id.nav_menu_about_us:
                    Intent intentAction = new Intent(view.getContext(), AboutUs.class);
                    startActivity(intentAction);
                    break;

                case R.id.nav_menu_location:
                    Intent intentLocation = new Intent(view.getContext(), MapsActivity.class);
                    startActivity(intentLocation);

                    break;

                // Proceed click on the Navigation menu - Call to the Restaurant
                case R.id.nav_menu_call_us:
                    Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+IDBInfo.PHONE_OF_RESTAURANT));
                    intentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try{
                        startActivity(intentCall);
                    }catch (Exception e){
                        Toast.makeText(view.getContext(), "Sorry this function doesn't work :(", Toast.LENGTH_SHORT).show();
                    }
                    break;

                //Proceed click on the Navigation menu - Share Feature
                case R.id.nav_menu_share:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "PinoyKlasiks phone : "+IDBInfo.PHONE_OF_RESTAURANT);

                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "market://details?id=pk.nz.pinoyklasiks");

                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    break;

            }
            return false;
        }
    }
}
