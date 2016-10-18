package pk.nz.pinoyklasiks.db;

import java.util.HashMap;
import java.util.List;

import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.SubOrder;

/**
 * Abstract class describe
 * DAO class for intecracting
 * with DB using objects
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public interface IDAOManager {

    /**
     * Method return the collection of categories from DB
     * of Categories
     * @return
     */
    public abstract List<AbstractCategory> getCategories();

    /**
     * Method return collection of product from DB
     * @return List of products
     */
    public abstract  List<AbstractProduct> getProductByIdCat(int idCat);

    /**
     *  Add the product to Order. Check the existance of open order if no create new
     *  and add the product to suborder table with appropriate quntity.
     * @param product   the product to add
     * @param quantity  quantity of product
     * @param isUpdate if true the quantity will be updated if false it will be increased
     */
    public abstract void addProductToOrder(AbstractProduct product, int quantity, boolean isUpdate);

    /**
     * Remove product from order
      */
    public abstract void removeProductFromOrder(int id);

    /**
     * Get id of open order
     * return 0 if there is no open order
     */
    public int getIdOpenOrder();

    /**
     * Get the order by id
     * @param id of order
     * @return  Order
     */
    public abstract Order getOrderById(int id);

    /**
     *  Metho return object Order
     *  that has status OPEN
     * @return Order
     */
    public abstract Order getOpenOrder();


    /**
     * The method return the sub order
     *  of the order using order_id
     * @param order_id order's id
     * @return  SubOrder object
     */
    public SubOrder getSubOrderByOrderId(int order_id);

    /**
     * Delete all records in the table tb_suborder
     * with given order id
     * @param orderId id of order
     */
    public void cleanSubOrder(int orderId);


    /**
     * Method delete the product from suborder(cart)
     * @param product need to be deleted (used id of it)
     * @param orderId of suborder
     */
    public void deleteProductfromSubOrder(AbstractProduct product, int orderId);

    /**
     * Method close DB
     */
    public abstract void close();

}
