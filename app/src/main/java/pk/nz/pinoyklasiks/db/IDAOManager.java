package pk.nz.pinoyklasiks.db;

import java.util.List;

import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.beans.AbstractProduct;

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



}
