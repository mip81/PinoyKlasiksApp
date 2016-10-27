package pk.nz.pinoyklasiks.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Class represent the ordinary
 * product
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Product extends AbstractProduct implements Serializable {



    // Default constructor
    public Product(){}

    // Parceable implementation
    protected Product(Parcel in) {
    }


    // Getters and setters foer fields
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCatId() {
        return catId;
    }

    @Override
    public void setCatId(int cat_id) {
        this.catId = catId;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String getProductDesc() {
        return productDesc;
    }

    @Override
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    @Override
    public double getProductPrice() {
        return productPrice;
    }

    @Override
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String getProductPic() {
        return productPic;
    }

    @Override
    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public  String toString(){
        return  " Id : "+getId()+
                " Cat_id : "+getCatId()+
                " Name : "+getProductName()+
                " Desc : "+getProductDesc()+
                " Price : "+getProductPrice()+
                " Pic : "+getProductPic();
    }
}
