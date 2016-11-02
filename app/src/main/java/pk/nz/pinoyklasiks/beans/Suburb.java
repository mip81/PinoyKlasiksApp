package pk.nz.pinoyklasiks.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**<pre>
 * Title       : Suburb class
 * Purpose     : Represent information related to Suburb
 *               in the DB
 * Date        : 15.10.2016
 * Input       : int id, String suburbName, District district
 * Proccessing : none
 * Output      : Suburb class
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class Suburb implements Serializable{
    private int id;                 // ID of the record
    private String suburbName;     // Name of suburb
    private District district;      // District of the suburb

    public Suburb(){}

    public Suburb(int id, String suburbName, District district){
        this.id = id;
        this.suburbName = suburbName;
        this.district = district;

    }
    // Getters and Setters to work with fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuburbName() {
        return suburbName;
    }

    public void setSuburbName(String suburbName) {
        this.suburbName = suburbName;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

}
