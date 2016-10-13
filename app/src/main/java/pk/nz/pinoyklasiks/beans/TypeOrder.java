package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**
 * Class represent type of order
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class TypeOrder implements Serializable {
    private int _id;
    private String type_order;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getType_order() {
        return type_order;
    }

    public void setType_order(String type_order) {
        this.type_order = type_order;
    }
}
