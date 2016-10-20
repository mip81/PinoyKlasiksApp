package pk.nz.pinoyklasiks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pk.nz.pinoyklasiks.beans.AbstractCategory;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Address;
import pk.nz.pinoyklasiks.beans.Category;
import pk.nz.pinoyklasiks.beans.Customer;
import pk.nz.pinoyklasiks.beans.District;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.Product;
import pk.nz.pinoyklasiks.beans.Status;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.beans.Suburb;
import pk.nz.pinoyklasiks.beans.TypeOrder;
import utils.AppConst;

/**
 * Class help to work with DB SQLite
 * has all CRUD methods
 *
 * @author  Mikhail PASTUSHKOV
 * @author  Melchor RELATADO
 */
public class DBManager extends SQLiteOpenHelper implements IDBInfo, IDBManager, IDAOManager {

    private final String CLASSNAME = DBManager.class.getCanonicalName();

    private BufferedReader br = null;
    protected SQLiteDatabase db; // DB connection

    // Format for price output
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    protected Context context; // Application context

    public DBManager(Context ctx) {
        super(ctx, DBName, null, VER);
        context = ctx;

    }

    /**
     * Create and load default data
     * @param db Sqlite db
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
                        abstractCategory.setId( cursor.getInt(cursor.getColumnIndexOrThrow(TB_CATEGORY_ID)) );
                        abstractCategory.setCatName( cursor.getString(cursor.getColumnIndex(TB_CATEGORY_CAT_NAME)) );
                        abstractCategory.setDescription( cursor.getString(cursor.getColumnIndex(TB_CATEGORY_DESCRIPTION)) );
                        abstractCategory.setOrderId( cursor.getInt(cursor.getColumnIndex(TB_CATEGORY_ORDER_ID)) );
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
                      product.setId(cursor.getInt( cursor.getColumnIndex(TB_PRODUCT_ID)) );
                      product.setCatId(cursor.getInt( cursor.getColumnIndex(TB_PRODUCT_CAT_ID)) );
                      product.setProductName( cursor.getString(cursor.getColumnIndex(TB_PRODUCT_PRODUCT_NAME)) );
                      product.setProductDesc( cursor.getString( cursor.getColumnIndex(TB_PRODUCT_PRODUCT_DESC)) );
                      product.setProductPrice( cursor.getDouble( cursor.getColumnIndex(TB_PRODUCT_PRODUCT_PRICE)) );
                      product.setProductPic( cursor.getString( cursor.getColumnIndex(TB_PRODUCT_PRODUCT_PIC)) );

                    list.add(product);
                    cursor.moveToNext();
                }
            }
        }
            //free resource
            if( cursor!= null ) cursor.close();

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

            if(AppConst.DEBUG) Log.d(AppConst.LOGD, " cleanSubOrder ::: orderId : "+orderId);

        int rows = db.delete(TB_SUBORDER, TB_SUBORDER_ORDER_ID+"=?", new String[]{ String.valueOf(orderId) });

            if(AppConst.DEBUG) Log.d(AppConst.LOGD, " cleanSubOrder ::: amount of rows was affected : "+rows);

    }

    /**
     *
     * @param product   the product to add
     * @param quantity  quantity of product
     * @param isUpdateQuantity
     */
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
                cv.put(TB_SUBORDER_PRODUCT_ID, product.getId());
                cv.put(TB_SUBORDER_PRICE, product.getProductPrice());
                cv.put(TB_SUBORDER_QUANTITY, quantity);

            db.insert(TB_SUBORDER, null , cv); // insert the product

            if(AppConst.DEBUG) Log.d(AppConst.LOGD,"  ::: DBManager ::: AddProductToOrder ::: INSERT NEW PRODUCT ::: NAME : "+product.getProductName()+
                                    " ::: PRODUCT ID : "+product.getId()+" ::: ORDER_ID "+id);

        }else{ // the open order exist check if the product in the cart update quantity or insert new

           cursor = db.query(TB_SUBORDER, new String[]{TB_SUBORDER_ID, TB_SUBORDER_QUANTITY},
                   TB_SUBORDER_PRODUCT_ID+"=? AND "+TB_SUBORDER_ORDER_ID + "=?",
                   new String[]{""+product.getId(), ""+order_id}, null, null,null);

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
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: DBManager ::: AddProductToOrder ::: UPDATED PRODUCT : "+product.getProductName()+
                                        " ::: PRODUCT_ID : "+product.getId()+" QUANTITY : "+productQuantity);

            }else{ // I there is no product then just insert new

                cv.clear();
                    cv.put(TB_SUBORDER_ORDER_ID, order_id);
                    cv.put(TB_SUBORDER_PRODUCT_ID, product.getId());
                    cv.put(TB_SUBORDER_QUANTITY, quantity);
                    cv.put(TB_SUBORDER_PRICE, product.getProductPrice());

                db.insert(TB_SUBORDER, null, cv);
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: DBManager ::: AddProductToOrder ::: INSERTED NEW PRODUCT : "+product.getProductName()+" quantity : "+quantity);
            }
        }
    } // END addProductToOrder()

    @Override
    public Order getOpenOrder() {
        // TODO: 10/13/16 write code to return Order with status open

        return null;
    }


    /**
     *  Method return SubOrder with given orderId
     *  (ex getting the cart if pass getIdOpenOrder())
     * @param order_id order's id
     * @return
     */
    @Override
    public SubOrder getSubOrderByOrderId(int order_id) {
        db = getReadableDatabase();
        SubOrder suborder = new SubOrder();
        Map<AbstractProduct, Integer> hmPoducts = suborder.getMapProducts();
      Cursor cursor;
            if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: getSubOrderByOrderId ::: orderId : "+order_id);

        // Fields need to be return
        String[] fields = { TB_PRODUCT+"."+TB_PRODUCT_ID,
                            TB_PRODUCT+"."+TB_PRODUCT_CAT_ID,
                            TB_PRODUCT+"."+TB_PRODUCT_PRODUCT_NAME,
                            TB_PRODUCT+"."+TB_PRODUCT_PRODUCT_PIC,
                            TB_PRODUCT+"."+TB_PRODUCT_PRODUCT_DESC,
                            TB_PRODUCT+"."+TB_PRODUCT_PRODUCT_PRICE,
                            TB_SUBORDER+"."+TB_SUBORDER_ID,
                            TB_SUBORDER+"."+TB_SUBORDER_QUANTITY};


         // get cursor join 2 tables tb_sub_category and tb_product
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TB_SUBORDER+","+TB_PRODUCT); // Set tables which will be join

        queryBuilder.appendWhere(TB_SUBORDER+"."+TB_SUBORDER_PRODUCT_ID+"="+TB_PRODUCT+"."+TB_PRODUCT_ID+
                                " AND "+TB_SUBORDER+"."+TB_SUBORDER_ORDER_ID+"="+order_id);

        cursor = queryBuilder.query(db, null, null, null, null, null, null);

        // get data about the suborder and build SubOrder object
        if (cursor != null ){
             if(cursor.moveToFirst()){
                 while (!cursor.isAfterLast()){

                     // fill the product object and put it inside suborder
                     AbstractProduct product = new Product();
                        product.setId( cursor.getInt( cursor.getColumnIndex(TB_PRODUCT_ID)));
                        product.setCatId( cursor.getInt( cursor.getColumnIndex(TB_CATEGORY_ID) ));
                        product.setProductName(cursor.getString( cursor.getColumnIndex( TB_PRODUCT_PRODUCT_NAME) ));
                        product.setProductDesc(cursor.getString( cursor.getColumnIndex( TB_PRODUCT_PRODUCT_DESC) ));
                        product.setProductPic(cursor.getString( cursor.getColumnIndex( TB_PRODUCT_PRODUCT_PIC) ));
                        product.setProductPrice(cursor.getDouble( cursor.getColumnIndex( TB_PRODUCT_PRODUCT_PRICE) ));

                     int quantity = cursor.getInt( cursor.getColumnIndex( TB_SUBORDER_QUANTITY));

                     if(AppConst.DEBUG) Log.d(AppConst.LOGD, CLASSNAME+" ::: getSubOrderByOrderId ::: put to suborder product : "+product);

                     // put in map  products and quantity and set it to SubOrder object later will be used in adapter for ListView
                     hmPoducts.put(product, quantity);

                     cursor.moveToNext();

                 }

                    suborder.setOrderId( order_id );
                    cursor.close();

                     return suborder;
             }
        }
        return null;
    }

    /**
     * Return Status object by ID
     *
     * @param id
     * @return Status , null if no such ID
     */
    @Override
    public Status getStatusById(int id) {
        Status status = null;
        db = getReadableDatabase();
        Cursor cursor = db.query(TB_STATUS,null,"_id = ?", new String[]{""+id}, null, null, null);
            if(cursor != null){

                if(cursor.moveToFirst()){
                        // fill Status object
                    status = new Status(cursor.getInt( cursor.getColumnIndex(TB_STATUS_ID)) ,
                                        cursor.getString( cursor.getColumnIndex(TB_STATUS_STATUS_NAME)) );
                    cursor.close();
                }
                return status;
            }

            return null;
    }


    /**
     * Return TypeOrder object by ID
     * @param id
     * @return TypeOrder
     */
    @Override
    public TypeOrder getTypeOrderById(int id) {
        TypeOrder typeOrder = null;
        db = getReadableDatabase();
        Cursor cursor = db.query( TB_TYPEORDER, null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()){
                // fill Status object
                typeOrder = new TypeOrder(cursor.getInt( cursor.getColumnIndex(TB_TYPEORDER_ID)) ,
                        cursor.getString( cursor.getColumnIndex(TB_TYPEORDER_TYPE_ORDER)) );
                cursor.close();
            }
            return typeOrder;
        }

        return null;
    }


    /**
     * Return Customer object by ID
     * @param id
     * @return Customer if there is no id return null
     */
    @Override
    public Customer getCustomerById(int id) {
        Customer customer = null;
        Address address = null;

        db = getReadableDatabase();

        Cursor cursor = db.query( TB_CUSTOMER, null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()){
                // fill Address and Customer object
                address = getAddressById(cursor.getInt( cursor.getColumnIndex(TB_CUSTOMER_ADDRESS_ID)) );

                customer = new Customer(cursor.getInt( cursor.getColumnIndex(TB_CUSTOMER_ID)),
                                        cursor.getString( cursor.getColumnIndex(TB_CUSTOMER_CUSTOMER_NAME)),
                                        cursor.getString( cursor.getColumnIndex(TB_CUSTOMER_PHONE_NUMBER)),
                                        cursor.getString( cursor.getColumnIndex(TB_CUSTOMER_EMAIL)),
                                        address);

                cursor.close();
            }
            return customer;
        }

        return null;
    }

    /**
     * Return Category object by ID
     * @param id
     * @return Category
     */
    @Override
    public Category getCategoryById(int id) {
        Category category = null;
        db = getReadableDatabase();
        Cursor cursor = db.query(TB_CATEGORY,null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()){
                // fill Category object
                category = new Category(cursor.getInt( cursor.getColumnIndex(TB_CATEGORY_ID)) ,
                            cursor.getString( cursor.getColumnIndex(TB_CATEGORY_CAT_NAME)),
                            cursor.getString( cursor.getColumnIndex(TB_CATEGORY_DESCRIPTION)),
                            cursor.getString( cursor.getColumnIndex(TB_CATEGORY_PIC)) );
                cursor.close();
            }
            return category;
        }
        return null;
    }

    /**
     * Return Disctrict object by ID
     * @param id
     * @return District
     */

    @Override
    public District getDistrictById(int id) {
        District district = null;
        db = getReadableDatabase();
        Cursor cursor = db.query(TB_CATEGORY,null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()){
                // fill Category object
                district = new District(cursor.getInt( cursor.getColumnIndex(TB_DISTRICT_ID)) ,
                        cursor.getString( cursor.getColumnIndex(TB_DISTRICT_DISTRICT_NAME)));
                cursor.close();
            }
            return district;
        }
        return null;
    }

    /**
     * Return Suburb object by ID
     * @param id
     * @return Suburb
     */
    @Override
    public Suburb getSuburbById(int id) {
        Suburb suburb = null;
        District district = null;

        db = getReadableDatabase();
        Cursor cursor = db.query(TB_SUBURB,null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()){
                // fill District object
                district = getDistrictById(cursor.getInt(cursor.getColumnIndex(TB_SUBURB_DISTRICT_ID)));
                suburb = new Suburb(cursor.getInt( cursor.getColumnIndex(TB_SUBURB_ID)) ,
                        cursor.getString( cursor.getColumnIndex(TB_SUBURB_SUBURB_NAME)),
                        district);

                cursor.close();
            }
            return suburb;
        }
        return null;
    }



    /**
     * Return Address object by ID
     * @param id
     * @return Address if there is no ID returm null
     */
    @Override
    public Address getAddressById(int id) {
        Address address = null;
        District district = null;
        Suburb suburb = null;

        db = getReadableDatabase();
        Cursor cursor = db.query(TB_ADDRESS,null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()){
                // fill Address object
                district = getDistrictById( cursor.getInt(cursor.getColumnIndex(TB_ADDRESS_DISTRICT_ID)) );
                suburb = getSuburbById( cursor.getInt(cursor.getColumnIndex(TB_ADDRESS_SUBURB_ID)) );


                address = new Address(cursor.getInt( cursor.getColumnIndex(TB_ADDRESS_ID)) ,
                                        suburb,
                                        district,
                                        cursor.getString( cursor.getColumnIndex(TB_ADDRESS_LOCATION))
                        );

                cursor.close();
            }
            return address;
        }
        return null;
    }


    /**
     * Get the order by id
     * @param id of order
     * @return  Order
     */
    @Override
    public Order getOrderById(int id) {
        Order order = new Order();
        Customer customer = new Customer();
        Address address = new Address();
        //TODO FINISH IT
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //   queryBuilder.setTables(TB_ORDER+","+TB_CUSTOMER+","+TB_ADDRESS+","+TB_TYPEORDER+","+TB_STATUS,TB);

        return null;
    }

    //////////////////////////////////////////////////////////
    // END GET BEANS //////////////////////////////////////////
    ///////////////////////////////////////////////////////////


    /**
     * Return true if the app have access to internet
     * @return boolean (true if has connection)
     */
    @Override
    public boolean isOnline() {

        // Getting connectivity service
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //check if network is available
        if( networkInfo != null && networkInfo.isConnectedOrConnecting() ){
            return true;
        }
        return false;

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
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, " Open order ID : "+id);

                cursor.close();
                return id;
            }
        }
        return 0;
    }


    /**
     * Method delete the product from suborder(cart)
    */
    @Override
    public void deleteProductfromSubOrder( AbstractProduct product, int orderId ) {
        int productId = product.getId();

        db = getWritableDatabase();
        db.delete(TB_SUBORDER, TB_SUBORDER_PRODUCT_ID+"=? AND "+TB_SUBORDER_ORDER_ID+"=?", new String[]{""+productId, ""+orderId});

                if(AppConst.DEBUG) Log.d(AppConst.LOGD, CLASSNAME+" ::: DELETED FROM SUBORDER ::: PRODUCT : "+product.getProductName());

    }

    /**
     * Retrieve the date from tb_version table
     * represent the last update of
     * @return date
     */
    @Override
    public Date getVersionDate() {
        // TODO: 10/17/16 get the version date of the DB
        return null;
    }



    /**
     * Return TypeOrder objects from DB
     * @return List of TypeOrder
     */
    @Override
    public List<TypeOrder> getTypeOrder() {

        List<TypeOrder> list = new ArrayList<TypeOrder>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TB_STATUS , null, null ,null, null, null, null);

            // Read cursor, fill TypeOrder and put ir in the List then return
            if(cursor != null){
                if(cursor.moveToFirst()){
                    while(!cursor.isAfterLast()){
                        int id = cursor.getInt(cursor.getColumnIndex(TB_STATUS_ID));
                        String typeName = cursor.getString(cursor.getColumnIndex(TB_STATUS_STATUS_NAME));

                        TypeOrder typeOrder = new TypeOrder(id, typeName);
                        list.add(typeOrder);

                        cursor.moveToNext();
                    }

                }
                cursor.close();
            }

        return list;
    }

    @Override
    public synchronized void close() {
        db.close();
    }
}
