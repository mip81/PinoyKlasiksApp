package pk.nz.pinoyklasiks.db;

import android.database.Cursor;

/**
 * Intetface describe the methods
 * for work with DB
 *
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */
public interface IDBManager {

    public Cursor getCategoriesCursor();

    public Cursor getProducts(int idCat);





}
