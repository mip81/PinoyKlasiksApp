package pk.nz.pinoyklasiks.db;

import pk.nz.pinoyklasiks.beans.Order;

/**
 * Describes method for interacting with webservice
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public interface IWebService {


    /**
     * Check the version on the server
     * and return true if it last one
     *
     * @return boolean
     */
    public boolean isTheLastVersion();

    /**
     * Method create and send the JSON object of order to the server
     * and the get the returned global id (later the user will check the status by this id)
     * @param order
     * @return int the global id
     */
    public int sendOrder(Order order);

}
