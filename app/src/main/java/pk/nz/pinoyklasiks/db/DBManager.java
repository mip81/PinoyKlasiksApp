package pk.nz.pinoyklasiks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Category;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.Product;
import pk.nz.pinoyklasiks.beans.SubOrder;
import utils.AppConst;

/**
 * Class help to work with DB SQLite
 * has all CRUD methods
 *
 * @author  Mikhail PASTUSHKOV
 * @author  Melchor RELATADO
 */
public class DBManager extends SQLiteOpenHelper implements IDBInfo, IDBManager, IDAOManager {


    private BufferedReader br = null;
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

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
        db = getReadableDatabase();

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
         db = getReadableDatabase();
         List listCategories = new ArrayList();
                    if (AppConst.DEBUG) Log.d("DEBUG ::: ", "getCategories() called : ");


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
                    if (AppConst.DEBUG) Log.d("DEBUG ::: ", "Get abstractCategory : abstractCategory : "+ abstractCategory);
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
        db = getReadableDatabase();
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
            if(AppConst.DEBUG) Log.d(AppConst.LOGD, "Start reading file DB.SQL to create and fill the DB with data");


            //Access resource as a stream read the initial db queries
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(PATH_TO_DB_RES);
            br = new BufferedReader( new InputStreamReader(is) );
            String lineSQL;

             // read the file and EXECSQL
            while( (lineSQL = br.readLine()) != null ){
                if(lineSQL.charAt(0) != '-'){ //read only commands miss the comments
                        if(AppConst.DEBUG) Log.d(AppConst.LOGD, lineSQL);

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


    /**
     * Method delete all records in the tb_suborder
     * wit given order id
     * @param orderId id of order
     */
    @Override
    public void cleanSubOrder(int orderId) {
        db = getWritableDatabase();

        int rows = db.delete(TB_SUBORDER, TB_SUBORDER_ORDER_ID+"=,", new String[]{""+orderId});
        if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: cleanSubOrder ::: amount of rows was deleted : "+rows);
    }

    @Override
    public void addProductToOrder(AbstractProduct product, int quantity, boolean isUpdateQuantity) {
        String sql;
        Cursor cursor;
        int subOrderId;                         // id of found existed suborder
        int productQuantity;                    // quantity of product
        db = getWritableDatabase();             // get DB
        ContentValues cv = new ContentValues(); // content for inserting
        int order_id = getIdOpenOrder();        // get the open id of existing order


        if(order_id == 0){

            String dateTime =  dateFormat.format(new Date());



                cv.put(TB_ORDER_ORDER_DATETIME_NOW, dateTime );
                cv.put(TB_ORDER_ORDER_DATETIME_FOR, dateTime );
                cv.put(TB_ORDER_CUSTOMER_ID, 0);
                cv.put(TB_ORDER_STATUS_ID , 1);
                cv.put(TB_ORDER_TYPE_ORDER_ID , 0);
                cv.put(TB_ORDER_NUM_PERSONS, 1);
                cv.put(TB_ORDER_ADDRESS_ID , 0);
                cv.put(TB_ORDER_TYPE_ORDER_ID , 0);

            long id = db.insert(TB_ORDER, null, cv);

            if(AppConst.DEBUG) Log.d(AppConst.LOGD," ::: DBManager ::: AddProductToOrder :::  CREATED NEW ORDER ::: GIVEN ID "+id);

            cv.clear();
                cv.put(TB_SUBORDER_ORDER_ID, id);
                cv.put(TB_SUBORDER_PRODUCT_ID, product.get_id());
                cv.put(TB_SUBORDER_PRICE, product.getProduct_price());
                cv.put(TB_SUBORDER_QUANTITY, quantity);

            db.insert(TB_SUBORDER, null , cv); // insert the product

            if(AppConst.DEBUG) Log.d(AppConst.LOGD,"  ::: DBManager ::: AddProductToOrder ::: INSERT NEW PRODUCT ::: Name : "+product.getProduct_name()+"ORDER_ID "+id);

        }else{ // the open order exist check if the product in the cart update quantity or insert new

           cursor = db.query(TB_SUBORDER, new String[]{TB_SUBORDER_ID, TB_SUBORDER_QUANTITY},
                   TB_SUBORDER_PRODUCT_ID+"=? AND "+TB_SUBORDER_ORDER_ID + "=?",
                   new String[]{""+product.get_id(), ""+order_id}, null, null,null);

            // if product exists then update only the quantity
            if(cursor.getCount() > 0){

                    // read the id of suborder and quantity existed product
                cursor.moveToFirst();
                subOrderId = cursor.getInt( cursor.getColumnIndex(TB_SUBORDER_ID) );
                productQuantity = cursor.getInt( cursor.getColumnIndex(TB_SUBORDER_QUANTITY) );
                cursor.close();

                cv.clear();                                  // clear ContentValue

                // Check update or increase quant.
                     if(isUpdateQuantity){ productQuantity = quantity;}
                        else
                     { productQuantity += quantity; }

                cv.put(TB_SUBORDER_QUANTITY, productQuantity);
                db.update(TB_SUBORDER, cv, TB_SUBORDER_ID+"=?", new String[]{""+subOrderId} );
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: DBManager ::: AddProductToOrder ::: UPDATED PRODUCT : "+product.getProduct_name()+" quantity : "+productQuantity);

            }else{ // I there is no product then just insert new

                cv.clear();
                    cv.put(TB_SUBORDER_ORDER_ID, order_id);
                    cv.put(TB_SUBORDER_PRODUCT_ID, product.get_id());
                    cv.put(TB_SUBORDER_QUANTITY, quantity);
                    cv.put(TB_SUBORDER_PRICE, product.getProduct_price());

                db.insert(TB_SUBORDER, null, cv);
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: DBManager ::: AddProductToOrder ::: INSERTED NEW PRODUCT : "+product.getProduct_name()+" quantity : "+quantity);
            }
        }
    } // END addProductToOrder()

    @Override
    public Order getOpenOrder() {
        // TODO: 10/13/16 write code to return Order with status open

        return null;
    }



    @Override
    public SubOrder getSubOrderByOrderId(int order_id) {
        db = getReadableDatabase();
        SubOrder suborder = new SubOrder();
        Map<AbstractProduct, Integer> hmPoducts = suborder.getMapProducts();
      Cursor cursor;
            if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: getSubOrderByOrderId ::: ");

         // get cursor join 2 tables tb_sub_category and tb_product
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TB_SUBORDER+","+TB_PRODUCT); // Set tables which will be join
        queryBuilder.appendWhere(TB_SUBORDER+"."+TB_PRODUCT_ID+"="+TB_PRODUCT+"."+TB_PRODUCT_ID+
                                " AND "+TB_SUBORDER+"."+TB_SUBORDER_ORDER_ID+"="+order_id);

        cursor = queryBuilder.query(db, null, null, null, null, null, null);
        if (cursor != null ){
             if(cursor.moveToFirst()){
                 while (!cursor.isAfterLast()){
                     AbstractProduct product = new Product();
                        product.setProduct_name(cursor.getString(cursor.getColumnIndex( TB_PRODUCT_PRODUCT_NAME) ));
                        product.setProduct_desc(cursor.getString(cursor.getColumnIndex( TB_PRODUCT_PRODUCT_DESC) ));
                        product.setProduct_pic(cursor.getString(cursor.getColumnIndex( TB_PRODUCT_PRODUCT_PIC) ));
                        product.setProduct_price(cursor.getDouble(cursor.getColumnIndex( TB_PRODUCT_PRODUCT_PRICE) ));
                     int quantity = cursor.getInt( cursor.getColumnIndex( TB_SUBORDER_QUANTITY));
                     hmPoducts.put(product, quantity);

                     cursor.moveToNext();

                 }
                    suborder.setOrder_id( order_id );
                    cursor.close();

                     return suborder;
             }
        }
        return null;
    }

    // TODO: 10/13/16 finish the metod that delete product from order rename it

    @Override
    public void removeProductFromOrder(int id) {

    }


    /**
     * Return id of order with status open
     * otherwise return 0
     * @return id of Order
     */
    @Override
    public int getIdOpenOrder() {
        SQLiteDatabase db = getReadableDatabase();
        // get the the order with status "Open"
        Cursor cursor = db.rawQuery("SELECT "+IDBInfo.TB_ORDER_ID+" FROM "+TB_ORDER+" WHERE "+IDBInfo.TB_ORDER_STATUS_ID+" = 1" , null);
         if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: getIdOpenOrder() ::: Cursor "+cursor.toString());
        if(cursor != null){
            if(cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(IDBInfo.TB_ORDER_ID));
                return id;
            }
        }
        return 0;
    }

    @Override
    public Order getOrderById(int id) {
        return null;
    }

    @Override
    public synchronized void close() {
        db.close();
    }
}
