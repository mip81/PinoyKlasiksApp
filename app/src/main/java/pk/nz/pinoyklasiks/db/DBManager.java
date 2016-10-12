package pk.nz.pinoyklasiks.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Category;
import pk.nz.pinoyklasiks.beans.Product;

/**
 * Class help to work with DB SQLite
 * has all CRUD methods
 *
 * @author  Mikhail PASTUSHKOV
 * @author  Melchor RELATADO
 */
public class DBManager extends SQLiteOpenHelper implements IDBInfo, IDBManager, IDAOManager {

    private final boolean DEBUG = true;
    private final String DEBUG_TXT = "*** DEBUG  *** ::: ";
    private BufferedReader br = null;



    public DBManager(Context ctx) {
        super(ctx, DBName, null, VER);


    }

    /**
     * Create and load default data
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        loadDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS "+TB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TB_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS "+TB_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS "+TB_DISTRICT);
        db.execSQL("DROP TABLE IF EXISTS "+TB_ORDER);
        db.execSQL("DROP TABLE IF EXISTS "+TB_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS "+TB_STATUS);
        db.execSQL("DROP TABLE IF EXISTS "+TB_SUBORDER);
        db.execSQL("DROP TABLE IF EXISTS "+TB_SUBURB);
        db.execSQL("DROP TABLE IF EXISTS "+TB_TYPEORDER);
        db.execSQL("DROP TABLE IF EXISTS "+TB_VERSION);

        onCreate(db);

    }


    /**
     *  Get cursor to populate
     *  the data (will be used for ListView)
     * @return Cursor
     */
    public Cursor getCategoriesCursor(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT "+TB_CATEGORY_ID+","+TB_CATEGORY_CAT_NAME+","+TB_CATEGORY_DESCRIPTION+
                 " FROM "+TB_CATEGORY+" ORDER BY  "+TB_CATEGORY_ORDER_ID , null);
        return cursor;
    }

    /**
     *  Get cursor to populate
     *  the data (will be used for ListView)
     * @return Cursor
     */
    public  List<AbstractCategory> getCategories(){
        SQLiteDatabase db = getReadableDatabase();
         List listCategories = new ArrayList();
                    if (DEBUG) Log.d("DEBUG ::: ", "getCategories() called : ");


        Cursor cursor = db.rawQuery(
                "SELECT "+TB_CATEGORY_ID+","
                        +TB_CATEGORY_CAT_NAME+","
                        +TB_CATEGORY_DESCRIPTION+","
                        +TB_CATEGORY_PIC+","
                        +TB_CATEGORY_ORDER_ID+
                 " FROM "+TB_CATEGORY+" ORDER BY  "+TB_CATEGORY_ORDER_ID , null);

        if (cursor != null) {
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    AbstractCategory abstractCategory = new Category();
                        abstractCategory.set_id( cursor.getInt(cursor.getColumnIndexOrThrow(TB_CATEGORY_ID)) );
                        abstractCategory.setCat_name( cursor.getString(cursor.getColumnIndex(TB_CATEGORY_CAT_NAME)) );
                        abstractCategory.setDescription( cursor.getString(cursor.getColumnIndex(TB_CATEGORY_DESCRIPTION)) );
                        abstractCategory.setOrder_id( cursor.getInt(cursor.getColumnIndex(TB_CATEGORY_ORDER_ID)) );
                        abstractCategory.setPic( cursor.getString(cursor.getColumnIndex(TB_CATEGORY_PIC)) );


                    listCategories.add(abstractCategory);
                    if (DEBUG) Log.d("DEBUG ::: ", "Get abstractCategory : abstractCategory : "+ abstractCategory);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return listCategories;

    }


    /**
     * The methog return the List of Product objects
     * tha having the appropriate id of catgory
     *
     * @param idCat id Category , 0 return all project in DB
     * @return
     */
    @Override
    public List<AbstractProduct> getProductByIdCat(int idCat) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        List<AbstractProduct> list = new ArrayList<>();

        String[] id = {String.valueOf(idCat)};

        if(idCat == 0){  // if 0 return all products
            cursor = db.query(TB_PRODUCT,null,null, null ,null, null, null);
        }else{
            cursor = db.query(TB_PRODUCT,null,TB_PRODUCT_CAT_ID+"=?", id ,null, null, null);
        }
        if(cursor!=null){

            if(cursor.moveToFirst()){

                while(!cursor.isAfterLast()){
                    AbstractProduct product = new Product();

                    // Fill object with data
                      product.set_id(cursor.getInt( cursor.getColumnIndex(IDBInfo.TB_PRODUCT_ID)) );
                      product.setCat_id(cursor.getInt( cursor.getColumnIndex(IDBInfo.TB_PRODUCT_CAT_ID)) );
                      product.setProduct_name( cursor.getString(cursor.getColumnIndex(IDBInfo.TB_PRODUCT_PRODUCT_NAME)) );
                      product.setProduct_desc( cursor.getString( cursor.getColumnIndex(IDBInfo.TB_PRODUCT_PRODUCT_DESC)) );
                      product.setProduct_price( cursor.getDouble( cursor.getColumnIndex(IDBInfo.TB_PRODUCT_PRODUCT_PRICE)) );
                      product.setProduct_pic( cursor.getString( cursor.getColumnIndex(IDBInfo.TB_PRODUCT_PRODUCT_PIC)) );

                    list.add(product);
                    cursor.moveToNext();
                }
            }
        }
        return list;
    }



    /**
     * Create and load default data to DB
     * from assets file db.sql
     * @param db
     */
    private void loadDefaultData(SQLiteDatabase db){

        try{
            if(DEBUG) Log.d(DEBUG_TXT, "Start reading file DB.SQL to create and fill the DB with data");


            //Access resource as a stream read the initial db queries
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(PATH_TO_DB_RES);
            br = new BufferedReader( new InputStreamReader(is) );
            String lineSQL;

             // read the file and EXECSQL
            while( (lineSQL = br.readLine()) != null ){
                if(lineSQL.charAt(0) != '-'){ //read only commands miss the comments
                        if(DEBUG) Log.d(DEBUG_TXT, lineSQL);

                    db.execSQL(lineSQL);

                }//end checking '-' symbol
            }
        }catch (Exception e){
            Log.e(" *** ERROR  ***", "loadDefaultData() *** "+e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Return cursor of products with provided ID
     * @param idCat id category
     * @return
     */
    @Override
    public Cursor getProducts(int idCat) {
        SQLiteDatabase db = getReadableDatabase();
        String[] id = {String.valueOf(idCat)};
         Cursor cursor = db.query(TB_PRODUCT,null,TB_PRODUCT_CAT_ID+"=?", id ,null, null, null);
        return cursor;
    }



}
