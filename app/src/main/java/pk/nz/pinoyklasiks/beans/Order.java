package pk.nz.pinoyklasiks.beans;

import android.text.method.DateTimeKeyListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * The  class described the order
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Order implements Serializable{

    private int _id;
    private int global_id;
    private Date order_datetime_now;
    private Date order_datetime_for;
    private Customer customer;
    private SubOrder suborder;
    private Status status;
    private TypeOrder typeOrder;
    private int num_persons;
    private Address address;
    private String comment;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(int global_id) {
        this.global_id = global_id;
    }

    public Date getOrder_datetime_now() {
        return order_datetime_now;
    }

    public void setOrder_datetime_now(Date order_datetime_now) {
        this.order_datetime_now = order_datetime_now;
    }

    public Date getOrder_datetime_for() {
        return order_datetime_for;
    }

    public void setOrder_datetime_for(Date order_datetime_for) {
        this.order_datetime_for = order_datetime_for;
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

    public int getNum_persons() {
        return num_persons;
    }

    public void setNum_persons(int num_persons) {
        this.num_persons = num_persons;
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
}
