package pk.nz.pinoyklasiks.beans;

import android.app.Dialog;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Class represent tb_district
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class District implements Serializable{
    private int id = -1 ;
    private String districtName;

    public District(){}

    public District(int id, String districtName){
        this.id = id;
        this.districtName = districtName;
    }

    
    // Parcebale implementation

    protected District(Parcel in) {
        id = in.readInt();
        districtName = in.readString();
    }

    // Getters and setters for fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

}
