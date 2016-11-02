package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**<pre>
 *
 *
 * Title       : Address class
 * Purpose     : Represent information related to customer address
 *               in the DB
 * Date        : 15.10.2016
 * Input       : int id, Suburb, District, String location
 * Proccessing : to work with class has getters and setters and also
 *              constuctor to fill the fields
 * Output      : Address class
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class Address implements Serializable {
    private int id = -1;            // id number
    private Suburb suburb;     // Suburb in the city
    private District district; // District in the city
    private String location;   // exact location

    public Address(){}

    /**
     * Constructor fill all field of the class
     * @param id int
     * @param suburb {@link Suburb}
     * @param district {@link District}
     * @param location String
     */
    public Address(int id, Suburb suburb, District district, String location){
        this.id = id;
        this.suburb = suburb;
        this.district = district;
        this.location = location;


    }


    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "Address: id : "+getId()+" suburb id "+getSuburb().getId()+" suburb name: "+getSuburb().getSuburbName()+
                " suburb id "+ getSuburb().getId()+ " ditrict id : "+getDistrict().getId()+" district name : "+getDistrict().getDistrictName()+
                " location : "+getLocation();
    }
}
