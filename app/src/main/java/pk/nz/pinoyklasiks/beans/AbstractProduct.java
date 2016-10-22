package pk.nz.pinoyklasiks.beans;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * The product class represent the
 * item that used in the app
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */

public abstract class AbstractProduct implements Serializable{
    protected int id;              // id
    protected int catId;           // the id of category
    protected String productName;  // the name of the product
    protected String productDesc;  // the description of the product
    protected double productPrice; // the price
    protected String productPic;   // the name of the file withpicture
    protected int quantity=1;       // the quantity of product


    public abstract int getId();

    public abstract void setId(int id);

    public abstract int getCatId();

    public abstract void setCatId(int cat_id);

    public abstract String getProductName();

    public abstract void setProductName(String product_name);

    public abstract String getProductDesc();

    public abstract void setProductDesc(String product_desc);

    public abstract double getProductPrice();

    public abstract void setProductPrice(double product_price);

    public abstract String getProductPic();

    public abstract void setProductPic(String product_pic) ;

    public abstract void setQuantity(int quantity);

    public abstract int getQuantity();

}
