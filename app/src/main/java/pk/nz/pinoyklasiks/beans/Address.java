package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;

/**
 *
 *  The class represent address
 *  delivery or client
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class Address implements Serializable {
    private int _id;
    private Suburb suburb;     // suburb
    private String location;   // exact location


}
