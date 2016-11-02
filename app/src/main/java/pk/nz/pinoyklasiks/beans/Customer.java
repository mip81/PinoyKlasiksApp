package pk.nz.pinoyklasiks.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/*<pre>
 * Title       : Customer class
 * Purpose     : Represent information related to customer
 *               in the DB
 * Date        : 10.10.2016
 * Input       : int id, String customerName, String phoneCustomer, String email, Address address
 * Proccessing : to work with class has getters and setters and also
 *              constuctor to fill the fields
 * Output      : Category class
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class Customer implements Serializable{
    private int id =-1;              // id number
    private String customerName; // name of customer
    private String phoneCustomer; // phone of customer
    private String email;         // email of customer
    private Address adrress;      // Address of customer

    public Customer(){}

    public Customer(int id, String customerName, String phoneCustomer, String email, Address address){
        this.id = id;
        this.customerName = customerName;
        this.phoneCustomer = phoneCustomer;
        this.email = email;
        this.adrress = address;
    }


    // Getters and setters for fields
    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAdrress() {
        return adrress;
    }

    public void setAdrress(Address adrress) {
        this.adrress = adrress;
    }

    @Override
    public String toString() {
        return "Customer id : "+id+" name: "+getCustomerName()+" phone number: "+getPhoneCustomer()+ " email: "+getEmail()+
                " addr id: "+getAdrress().getId();
    }

}
