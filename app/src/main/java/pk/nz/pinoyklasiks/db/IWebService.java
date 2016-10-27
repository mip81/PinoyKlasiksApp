package pk.nz.pinoyklasiks.db;

import java.util.Date;

import pk.nz.pinoyklasiks.beans.Order;

/**
 * Describes method for interacting with webservice
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public interface IWebService {


    /**
     * Check if user has connection to Internet
     * @return boolean
     */
    public boolean isOnline();

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
    public int sendJSONORder(Order order);

    /**
     * CHECKING the status of ORDERID
     *
     * @param globalId
     * @return int id of status -1 if there is no the order
     */
    public int checkStatusOrder(int globalId);

    /**
     * GET THE DATETIME LAST VERSION from the server
     * @return Date of last changes or null
     */
    public Date getTheLastVersionDateTime();

}
