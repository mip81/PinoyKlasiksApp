package pk.nz.pinoyklasiks.beans;

/**
 * Created 10/10/16.
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Category extends AbstractCategory {

    public Category(){}

    /**
     * Constructor fill all fields the Object
     * @param id numberb
     * @param catName String
     * @param desc  String
     * @param pic   String
     */
    public Category(int id, String catName, String desc, String pic){
        this.id = id;
        this.catName = catName;
        this.description = desc;
        this.pic = pic;

    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String cat_name) {
        this.catName = cat_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int order_id) {
        this.orderId = order_id;
    }
}
