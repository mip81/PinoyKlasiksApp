package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**
 * Class represent Suburb in the City
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class Suburb implements Serializable{
    private int _id;
    private String suburb;     // Name of suburb
    private District district; // District of the suburb

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
