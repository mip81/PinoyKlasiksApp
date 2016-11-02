package pk.nz.pinoyklasiks.db;

import java.util.Date;
import java.util.List;

import pk.nz.pinoyklasiks.beans.Order;

/**<pre>
 * Title       : IWebService interface
 * Purpose     : Describe the methods for application
 *               to work with WebService
 *               related to DB
 *
 * Date        : 15.10.2016
 * Input       : none
 * Proccessing : none
 * Output      : none
 *
 * </pre>
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
     * Method create and send the JSON object of order to the server
     * and the get the returned global id (later the user will check the status by this id)
     * @param order
     * @return int the global id
     */
    public int sendJSONORder(Order order);

    /**
     * CHECKING the status of ORDERID
     * if the message is null the progressdialog will not be shown
     * @param globalId int order id
     * @param message String can be null
     * @return int id of status -1 if there is no the order
     */
    public int checkStatusOrder(int globalId, String message);

    /**
     * GET THE DATETIME LAST VERSION from the server
     * @return Date of last changes or null
     */
    public Date getTheLastVersionDateTime();

    /**
     * method connect to the server and
     * return the list of names of pictures
     * located  on the server.
     * @return List
     */
    public List<String> getDeals();
}
