package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**
 *
 * Class represent existed categories
 * of the product
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public abstract class AbstractCategory implements Serializable{

    protected int _id;
    protected String cat_name;
    protected String description;
    protected String pic;
    protected int order_id;

    public abstract String getPic();
    public abstract void setPic(String pic);
    public abstract int get_id();
    public abstract void set_id(int _id);
    public abstract String getCat_name();
    public abstract void setCat_name(String cat_name);
    public abstract String getDescription();
    public abstract void setDescription(String description);
    public abstract int getOrder_id();
    public abstract void setOrder_id(int order_id);

    @Override
    public String toString(){
        return "id : "+get_id()+"; name category: "+getCat_name()+"; desc: "+getDescription()+ " picname : "+getPic()+"; order : "+getOrder_id();
    }
}
