package pk.nz.pinoyklasiks.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**select
 * Class represent the table tb_status
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Status implements Serializable{
    private int id = -1;              // number id
    private String statusName;   // name for status

    /**
     * Constructor set all fields of class
     * @param id int
     * @param statusName String
     */
    public Status(int id, String statusName){
        this.id = id;
        this.statusName = statusName;
    }


    // Parceable implementation
    protected Status(Parcel in) {
        id = in.readInt();
        statusName = in.readString();
    }


    // Getters and setters for fields
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
