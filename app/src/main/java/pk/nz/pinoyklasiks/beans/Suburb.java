package pk.nz.pinoyklasiks.beans;

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

    public Suburb(int id, String suburbName, District district){
        this.id = id;
        this.suburbName = suburbName;
        this.district = district;

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
