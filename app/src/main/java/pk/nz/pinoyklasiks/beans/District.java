package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**
 * Class represent tb_district
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class District implements Serializable{
    private int id;
    private String districtName;

    public District(int id, String districtName){
        this.id = id;
        this.districtName = districtName;
    }

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
