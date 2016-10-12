package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**
 * The product class represent the
 * item that used in the app
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */

public abstract class AbstractProduct implements Serializable{
    protected int _id;              // id
    protected int cat_id;           // the id of category
    protected String product_name;  // the name of the product
    protected String product_desc;  // the description of the product
    protected double product_price; // the price
    protected String product_pic;   // the name of the file withpicture


    public abstract int get_id();

    public abstract void set_id(int _id);

    public abstract int getCat_id();

    public abstract void setCat_id(int cat_id);

    public abstract String getProduct_name();

    public abstract void setProduct_name(String product_name);

    public abstract String getProduct_desc();

    public abstract void setProduct_desc(String product_desc);

    public abstract double getProduct_price();

    public abstract void setProduct_price(double product_price);

    public abstract String getProduct_pic();

    public abstract void setProduct_pic(String product_pic) ;


}
