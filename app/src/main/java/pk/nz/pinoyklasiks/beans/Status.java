package pk.nz.pinoyklasiks.beans;

/**
 * Class represent status of order
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Status {
    private int _id;
    private String status_name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }
}
