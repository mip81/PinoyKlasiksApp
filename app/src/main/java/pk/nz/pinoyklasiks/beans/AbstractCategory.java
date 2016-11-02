package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;


/**<pre>
 *
 * Title       : AbstractCategory class
 * Purpose     : Abstract class for categories
 *
 * Date        : 01.09.2016
 * Input       : none
 * Proccessing : none
 * Output      : none
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public abstract class AbstractCategory implements Serializable{

    protected int id;
    protected String catName;
    protected String description;
    protected String pic;
    protected int orderId;

    public abstract String getPic();
    public abstract void setPic(String pic);
    public abstract int getId();
    public abstract void setId(int _id);
    public abstract String getCatName();
    public abstract void setCatName(String cat_name);
    public abstract String getDescription();
    public abstract void setDescription(String description);
    public abstract int getOrderId();
    public abstract void setOrderId(int order_id);


    @Override
    public String toString(){
        return "id : "+ getId()+"; name category: "+ getCatName()+"; desc: "+getDescription()+ " picname : "+getPic()+"; order : "+ getOrderId();
    }
}
