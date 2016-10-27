package pk.nz.pinoyklasiks.db;

import android.database.Cursor;

import java.util.Date;

/**
 * Intetface describe the methods
 * for work with DB
 *
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */
public interface IDBManager {




    /**
     * return the date of current version DB
     */
    public Date getVersionDate();

    /**
     * Get Cursor from  tb_categories table
     * @return Cursor
     */
    @Deprecated
    public Cursor getCategoriesCursor();

    /**
     *  Return cursor from tb_product
     *  the rows which have passed id category
     *
     * @param idCat
     * @return
     */
    @Deprecated
    public Cursor getProducts(int idCat);


}
