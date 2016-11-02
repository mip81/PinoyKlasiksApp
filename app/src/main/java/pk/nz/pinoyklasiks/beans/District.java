package pk.nz.pinoyklasiks.beans;

import android.app.Dialog;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/*<pre>
 * Title       : District class
 * Purpose     : Represent information related to district
 *               in the DB
 * Date        : 10.10.2016
 * Input       : int id, String districtName
 * Proccessing : to work with class has getters and setters and also
 *              constuctor to fill the fields
 * Output      : Category class
 *
 *</pre>
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
