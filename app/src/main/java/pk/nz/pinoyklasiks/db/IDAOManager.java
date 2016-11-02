package pk.nz.pinoyklasiks.db;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Address;
import pk.nz.pinoyklasiks.beans.Category;
import pk.nz.pinoyklasiks.beans.Customer;
import pk.nz.pinoyklasiks.beans.District;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.Status;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.beans.Suburb;
import pk.nz.pinoyklasiks.beans.TypeOrder;



/**<pre>
 *
 * Title       : IDAOManager interface
 * Purpose     : Have all necessary DAO operations needed to app
 *
 * Date        : 15.10.2016
 * Input       : none
 * Proccessing : none
 * Output      : SubOrder class
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public interface IDAOManager {


    /**
     * Return Status object by ID
     * @param id
     * @return Status
     */
    public Status getStatusById(int id);

    /**
     * Return TypeOrder object by ID
     * @param id
     * @return TypeOrder
     */
    public TypeOrder getTypeOrderById(int id);


    /**
     * Return id of typeOrder by Name
     * @param name
     * @return int
     */
    public int getTypeOrderIdByName(String name);


    /**
     * Return Customer object by ID
     * @param id
     * @return Customer
     */
    public Customer getCustomerById(int id);

    /**
     * Save Customer object in DB
     * if exit then update it
     * @param customer
     * @return id int
     */
    public int saveCustomer(Customer customer);


    /**
     * Retrieve the last registered Customer
     * @return Customer
     */
    public Customer getLastCustomer();

    /**
     * Return Category object by ID
     * @param id
     * @return Category
     */
    public Category getCategoryById(int id);

    /**
     * Return Suburb object by ID
     * @param id
     * @return Suburb
     */
    public Suburb getSuburbById(int id);

    /**
     * Return Address object by ID
     * @param id
     * @return Address
     */
    public Address getAddressById(int id);


    /**
     * Write the Address to database
     * first check if the address exist then udate it
     * @param address
     * @return id int
     */
    public int saveAddress(Address address);





    /**
     * Return Disctrict object by ID
     * @param id
     * @return District
     */
    public District getDistrictById(int id);


    /**
     * Method return the collection of categories from DB
     * of Categories
     * @return
     */
    public abstract List<AbstractCategory> getCategories();

    /**
     * Method return collection of product from DB
     * using id of category as a key
     * @return List of products
     */
    public abstract  List<AbstractProduct> getProductByIdCat(int idCat);



    /**
     * Method return collection of product from DB
     * using query
     * @return List of products
     */
    public abstract  List<AbstractProduct> getProductByQuery(String query);

    /**
     *  Add the product to Order. Check the existance of open order if no create new
     *  and add the product to suborder table with appropriate quntity.
     * @param product   the product to add
     * @param quantity  quantity of product
     * @param isUpdate if true the quantity will be updated if false it will be increased
     */
    public abstract void addProductToOrder(AbstractProduct product, int quantity, boolean isUpdate);

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
     * Get List of all orders
     * @return List
     */
    public abstract List<Order> getAllOrders();



    /**
     * Update order return id order
     *
     * @param order
    * @return int ID
    */
    public abstract int saveOrder(Order order);


    /**
     * The method return the sub order
     *  of the order using orderId
     * @param order_id order's id
     * @return  SubOrder object
     */
    public abstract SubOrder getSubOrderByOrderId(int order_id);

    /**
     * Delete all records in the table tb_suborder
     * with given order id
     * @param orderId id of order
     */
    public abstract void cleanSubOrder(int orderId);


    /**
     * Method delete the product from suborder(cart)
     * @param product need to be deleted (used id of it)
     * @param orderId of suborder
     */
    public abstract void deleteProductfromSubOrder(AbstractProduct product, int orderId);

    /**
     * Return List with type orders
     * @return Cursor
     */
    public List<TypeOrder> getTypeOrder();

    /**
     * Method close DB
     */
    public abstract void close();

}
