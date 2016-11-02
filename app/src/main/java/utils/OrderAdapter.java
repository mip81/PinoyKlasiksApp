package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;


/**<pre>
 *
 * Title       : OrderAdapter class
 * Purpose     : Adapter for Orders objects
 * Date        : 15.10.2016
 * Input       : Context context, List<Order> order
 * Proccessing : Get the Orders and organized it content
 *              in the lv_order_history_products
 * Output      : Adapter
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class OrderAdapter extends ArrayAdapter<Order>{

    // Price format output
    private DecimalFormat df = new DecimalFormat("$##.00");
    // Date time format output
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");




    // Constructor of customer adapter
    public OrderAdapter(Context context, List<Order> order) {
        super(context, 0, order);
    }

    /**
     * Bind views in layout to fields of AbstractCategory object

     * @param convertView
     * @param parent
     * @return View
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the Order object for this position
        Order order = getItem(position);

        // Check if this view null inflate it create another one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_order_history_products, parent, false);
        }

        // Get the view to populate new data
        ////////////////////////////////////
        // Date order was made
        TextView tvOhDateOfOrder = (TextView)convertView.findViewById(R.id.tvOhDateOfOrder);
        // Date for
        TextView tvDateForOrder = (TextView)convertView.findViewById(R.id.tvDateForOrder);

        // Customer name
        TextView tvOhCustomerName = (TextView)convertView.findViewById(R.id.tvOhCustomerName);

        // Phone number of customer
        TextView tvOhPhone = (TextView)convertView.findViewById(R.id.tvOhPhone);

        // Current status of the order
        TextView tvOhStatus = (TextView)convertView.findViewById(R.id.tvOhStatus);

        // Total price for order
        TextView tvOhTotalPrice = (TextView)convertView.findViewById(R.id.tvOhTotalPrice);


        // Populate our data into the layout views
        if( (order.getOrderDatetimeFor() != null) && (order.getOrderDatetimeFor()!= null) ){


            tvOhDateOfOrder.setText( sdf.format(order.getOrderDatetimeNow()) );

            tvDateForOrder.setText( sdf.format(order.getOrderDatetimeFor()) );

        }

        tvOhCustomerName.setText( order.getCustomer().getCustomerName() ) ;

        tvOhPhone.setText( order.getCustomer().getPhoneCustomer() ) ;

        tvOhStatus.setText( order.getStatus().getStatusName()) ;

        tvOhTotalPrice.setText( df.format(order.getSuborder().getTotalPrice()) ) ;



        return convertView;
    }

}
