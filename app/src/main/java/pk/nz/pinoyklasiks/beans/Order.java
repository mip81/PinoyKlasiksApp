package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;
import java.util.Date;


/**
 * The  class described the order
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
    protected int numPersons =1;
    protected Address address;
    protected String comment="";

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

    public int getNum_persons() {
        return numPersons;
    }

    public void setNum_persons(int num_persons) {
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
}
