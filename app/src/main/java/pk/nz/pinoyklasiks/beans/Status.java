package pk.nz.pinoyklasiks.beans;

/**select
 * Class represent the table tb_status
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Status {
    private int id;              // number id
    private String statusName;   // name for status

    /**
     * Constructor set all fields of class
     * @param _id int
     * @param statusName String
     */
    public Status(int _id, String statusName){
        this.id = id;
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String status_name) {
        this.statusName = status_name;
    }
}
