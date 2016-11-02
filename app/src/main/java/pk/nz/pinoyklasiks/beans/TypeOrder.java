package pk.nz.pinoyklasiks.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**<pre>
 * Title       : TypeOrder class
 * Purpose     : Represent information related to TypeOrder
 *               in the DB
 * Date        : 15.10.2016
 * Input       : int id, String typeOrder
 * Proccessing : none
 * Output      : TypeOrder class
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class TypeOrder implements Serializable {
    private int id;          // numer id
    private String typeOrder; // name of type

    public TypeOrder(){

    }


    /**
     * Constructor that set all fields of the class
     * @param id int  order
     * @param typeOrder String name of type
     */
    public TypeOrder(int id, String typeOrder){
        this.id = id;
        this.typeOrder = typeOrder;
    }

    // Getters and Setter to work with fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(String typeOrder) {
        this.typeOrder = typeOrder;
    }

    /**
     * Return type name
     * @return String
     */
    @Override
    public String toString() {
        return typeOrder;
    }

}
