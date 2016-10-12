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
    public int get_id() {
        return _id;
    }

    @Override
    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public int getCat_id() {
        return cat_id;
    }

    @Override
    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    @Override
    public String getProduct_name() {
        return product_name;
    }

    @Override
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    @Override
    public String getProduct_desc() {
        return product_desc;
    }

    @Override
    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    @Override
    public double getProduct_price() {
        return product_price;
    }

    @Override
    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    @Override
    public String getProduct_pic() {
        return product_pic;
    }

    @Override
    public void setProduct_pic(String product_pic) {
        this.product_pic = product_pic;
    }

    @Override
    public  String toString(){
        return  " Id : "+get_id()+
                " Cat_id : "+getCat_id()+
                " Name : "+getProduct_name()+
                " Desc : "+getProduct_desc()+
                " Price : "+getProduct_price()+
                " Pic : "+getProduct_pic();
    }
}
