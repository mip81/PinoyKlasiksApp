package pk.nz.pinoyklasiks.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Class represent row in the table tb_suburb
 *
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

    // Parceable implementation
    protected Suburb(Parcel in) {
        id = in.readInt();
        suburbName = in.readString();
        district = in.readParcelable(District.class.getClassLoader());
    }

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
