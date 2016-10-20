package pk.nz.pinoyklasiks.beans;

/**
 * Class represent the ordinary
 * product
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Product extends AbstractProduct {

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int _id) {
        this.id = _id;
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
    public void setProductName(String product_name) {
        this.productName = productName;
    }

    @Override
    public String getProductDesc() {
        return productDesc;
    }

    @Override
    public void setProductDesc(String product_desc) {
        this.productDesc = product_desc;
    }

    @Override
    public double getProductPrice() {
        return productPrice;
    }

    @Override
    public void setProductPrice(double product_price) {
        this.productPrice = product_price;
    }

    @Override
    public String getProductPic() {
        return productPic;
    }

    @Override
    public void setProductPic(String product_pic) {
        this.productPic = product_pic;
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
