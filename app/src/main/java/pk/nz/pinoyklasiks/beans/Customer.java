package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**
 *
 * Class represent Customer
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Customer implements Serializable{
    private int _id;
    private String customerName;
    private String phoneCustomer;
    private String email;
    private Address adrress;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
}
