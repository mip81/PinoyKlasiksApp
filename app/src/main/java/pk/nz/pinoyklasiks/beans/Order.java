package pk.nz.pinoyklasiks.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import pk.nz.pinoyklasiks.db.IDBInfo;
import utils.AppConst;


/**<pre>
 * Title       : Order class
 * Purpose     : Represent information related to order address
 *               in the DB
 * Date        : 15.10.2016
 * Input       : int id, Suburb, District, String location
 * Proccessing : Keep the all information about order
 *               have the method toJSON() that convert it to JSON object
 *               also toString() will be used for sending the in text format
 * Output      : Order class
 *
 *</pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class Order implements Serializable{


    protected int id;
    protected int global_id;
    protected Date orderDatetimeNow;
    protected Date orderDatetimeFor;
    protected Customer customer;
    protected SubOrder suborder;
    protected Status status;
    protected TypeOrder typeOrder;
    protected int numPersons = 1;
    protected Address address;
    protected String comment="";

    public Order(){}


    // Getters and setters for fields
    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public int getGlobalId() {
        return global_id;
    }

    public void setGlobalId(int global_id) {
        this.global_id = global_id;
    }

    public Date getOrderDatetimeNow() {
        return orderDatetimeNow;
    }

    public void setOrderDatetimeNow(Date orderDatetimeNow) {
        this.orderDatetimeNow = orderDatetimeNow;
    }

    public Date getOrderDatetimeFor() {
        return orderDatetimeFor;
    }

    public void setOrderDatetimeFor(Date order_datetime_for) {
        this.orderDatetimeFor = order_datetime_for;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SubOrder getSuborder() {
        return suborder;
    }

    public void setSuborder(SubOrder suborder) {
        this.suborder = suborder;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeOrder getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(TypeOrder typeOrder) {
        this.typeOrder = typeOrder;
    }

    public int getnumPersons() {
        return numPersons;
    }

    public void setnumPersons(int num_persons) {
        this.numPersons = num_persons;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    /**
     * Prepared text of order for sending it by SMS, Email
     * @return string
     */
    @Override
    public String toString() {


        return "Order: \n"+
                "For Datetime: "+new SimpleDateFormat("dd M yyyy hh:mm").format(getOrderDatetimeFor())+" \n"+
                "Customer name: "+getCustomer().getCustomerName()+
                "\nPhone number: "+getCustomer().getPhoneCustomer()+
                "\nEmail: "+getCustomer().getEmail()+
                "\nNumber of person: "+getnumPersons()+
                "\nType: "+getTypeOrder().getTypeOrder()+"\n"+getSuborder();



    }

    /**
     * Convert the order to json object
     * @return
     */
    public JSONObject toJSON(){
            // If there are no any products in the cart
            if(getSuborder() == null) return null;

            // convert DateTime to String with MYSQL format
            SimpleDateFormat sdf = new SimpleDateFormat(IDBInfo.MYSQL_DATETIME_PATTERN);

            JSONObject json = new JSONObject(); // Root JSON Object (Order)
            JSONObject jCustomer = new JSONObject(); // JSON Object Customer information
            JSONObject jAddress = new JSONObject();  // JSON Object for Address of Customer

            JSONArray jArrProducts = new JSONArray();


        try{

            // Fill info about Customer
            jCustomer.put("name", getCustomer().getCustomerName());
            jCustomer.put("phone", getCustomer().getPhoneCustomer());
            jCustomer.put("email", getCustomer().getEmail());

            // Fill info about address of customer
            jAddress.put("suburbId", getAddress().getSuburb().getId());
            jAddress.put("districtId", getAddress().getSuburb().getDistrict().getId());
            jAddress.put("location", getAddress().getLocation());


            // Get the id of product and it's quantity
            // create the object and put it into JSONArray
            for(AbstractProduct product : getSuborder().getMapProducts().keySet()){
                JSONObject jProduct = new JSONObject();
                jProduct.put("productId", product.getId());
                jProduct.put("quantity" , getSuborder().getMapProducts().get(product));
                jArrProducts.put(jProduct);
            }


            // main fields of the order
            json.put("numPerson", getnumPersons());
            json.put("typeOrderId", getTypeOrder().getId());
            json.put("orderDateTimeNow", sdf.format(getOrderDatetimeNow()));
            json.put("orderDateTimeFor", sdf.format(getOrderDatetimeFor()));
            json.put("comment", getComment());

            // Add to main JSON object
            json.put("customer", jCustomer);       // Info about customer
            json.put("address", jAddress);        // Address of order
            json.put("products" , jArrProducts);  // ID products and quantity




        }catch(JSONException e){
            Log.e(AppConst.LOGE, "Order ::: toJSON ::: "+e);
        }


        return json;
    }

}
