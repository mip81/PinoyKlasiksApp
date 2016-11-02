package pk.nz.pinoyklasiks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
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

import static android.R.attr.order;

/**<pre>
 * Title       : DBManager class
 * Purpose     : To work with all CRUD DB operation
 * Date        : 15.10.2016
 * Input       : Context
 * Proccessing : All CRUD operations with object and DB
 * Output      : Objects represented the tables in the DB
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class DBManager extends SQLiteOpenHelper implements IDBInfo, IDAOManager {

    private final String CLASSNAME = DBManager.class.getCanonicalName();

    private BufferedReader br = null;
    protected SQLiteDatabase db; // DB connection

    // Format for SQLite DATETIME output
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


    //
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
                      product.setProductName( cursor.getString( cursor.getColumnIndex(TB_PRODUCT_PRODUCT_NAME)) );
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
     * Method return collection of product from DB
     * using query
     * @return List of products
     */
    public List<AbstractProduct> getProductByQuery(String query) {
        db = getReadableDatabase();
        Cursor cursor;
        List<AbstractProduct> list = new ArrayList<>();

        String[] arrQuery = {"%"+query+"%", "%"+query+"%"};

                // prepdare query and recieve cursor with found product
            cursor = db.query(TB_PRODUCT, null, TB_PRODUCT_PRODUCT_NAME + " like ? or "
                    +TB_PRODUCT_PRODUCT_DESC+" like ?  ", arrQuery, null, null, null);


        if (cursor != null) {

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {
                    AbstractProduct product = new Product();

                    // Fill object with data
                    product.setId(cursor.getInt(cursor.getColumnIndex(TB_PRODUCT_ID)));
                    product.setCatId(cursor.getInt(cursor.getColumnIndex(TB_PRODUCT_CAT_ID)));
                    product.setProductName(cursor.getString(cursor.getColumnIndex(TB_PRODUCT_PRODUCT_NAME)));
                    product.setProductDesc(cursor.getString(cursor.getColumnIndex(TB_PRODUCT_PRODUCT_DESC)));
                    product.setProductPrice(cursor.getDouble(cursor.getColumnIndex(TB_PRODUCT_PRODUCT_PRICE)));
                    product.setProductPic(cursor.getString(cursor.getColumnIndex(TB_PRODUCT_PRODUCT_PIC)));

                    // add the product to the the list
                    // and go to the next row
                    list.add(product);
                    cursor.moveToNext();
                }
            }
        }
        //free resource
        if (cursor != null) cursor.close();

        return list;
    }



    /**
     * Create and load default data to DB
     * from assets (file db.sql)
     * @param db
     */
    private void loadDefaultData(SQLiteDatabase db){

        try{
            if(AppConst.DEBUG) Log.d(AppConst.LOGD, "Start reading file DB.SQL to create and fill the DB with data");


            //Access resource as a stream read the initial db queries
            // TODO TASK 5 . Class loader.
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
    public Cursor getProducts(int idCat) {
        db = getReadableDatabase();

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
     * Update order .
     * Take the order object and update info in DB
     *
     * @param order
     * @return int count of row was affected
     */
    public int saveOrder(Order order){
        int id = -1;
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(order != null){

            if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< DBmanager >>> ::: saveOrder ::: id : "+order.getId());

            // Read orders object
            Address address = order.getAddress();
            Customer customer = order.getCustomer();

            // save address and set id in the object
            address.setId(saveAddress(address));

            // save customer and save id in the object
            customer.setId( saveCustomer(customer));

            // prepare fields for update
            cv.put(TB_ORDER_NUM_PERSONS, order.getnumPersons());
            cv.put(TB_ORDER_TYPE_ORDER_ID, order.getTypeOrder().getId());
            cv.put(TB_ORDER_COMMENT, order.getComment());
            cv.put(TB_ORDER_ADDRESS_ID, address.getId());
            cv.put(TB_ORDER_CUSTOMER_ID, order.getCustomer().getId());
            cv.put(TB_ORDER_ORDER_DATETIME_FOR, dateFormat.format(order.getOrderDatetimeFor()) );
            cv.put(TB_ORDER_ORDER_DATETIME_NOW, dateFormat.format(order.getOrderDatetimeNow()) );
            cv.put(TB_ORDER_GLOBAL_ID, order.getGlobalId());
            cv.put(TB_ORDER_STATUS_ID, order.getStatus().getId());

            // Update ORDER object
            id = (int)db.update(TB_ORDER, cv, TB_ORDER_ID+"=?", new String[]{""+order.getId()});

        }

        return id;
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
            }

            return status;
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
            }
            cursor.close();
        }

        return typeOrder;
    }

    /**
     * Retrieve the last registered Customer
     * if there is no customer return null;
     * @return Customer
     */
    @Override
    @Nullable
    public Customer getLastCustomer() {
       // get DB connection
        db = getReadableDatabase();
        // Object to return
        Customer customer = null;

        // Query for getting last Customer
        String sql = "SELECT * FROM  "+TB_CUSTOMER+" ORDER BY "+TB_CUSTOMER_ID+
                    " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){

            if(cursor.moveToFirst()){

                customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndex( TB_CUSTOMER_ID )));
                customer.setAdrress(
                        getAddressById(cursor.getInt(cursor.getColumnIndex( TB_CUSTOMER_ADDRESS_ID ))));
                customer.setCustomerName(cursor.getString(cursor.getColumnIndex(TB_CUSTOMER_CUSTOMER_NAME)));
                customer.setEmail(cursor.getString(cursor.getColumnIndex(TB_CUSTOMER_EMAIL)));
                customer.setPhoneCustomer(cursor.getString(cursor.getColumnIndex(TB_CUSTOMER_PHONE_NUMBER)));
                cursor.close();
            }
        }

        return customer;
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
        }

        return customer;
    }


    @Override
    public int saveCustomer(Customer customer) {
        int id = 1;
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        if (customer!= null){

            cv.put(TB_CUSTOMER_CUSTOMER_NAME, customer.getCustomerName());
            cv.put(TB_CUSTOMER_EMAIL, customer.getEmail());
            cv.put(TB_CUSTOMER_ADDRESS_ID, customer.getAdrress().getId());
            cv.put(TB_CUSTOMER_PHONE_NUMBER, customer.getPhoneCustomer());


                // if this customer already in DB then update it
            if(getCustomerById(customer.getId()) != null){

                // Update Customer
                db.update(TB_CUSTOMER, cv, TB_CUSTOMER_ID+"=?", new String[]{""+customer.getId()});
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< DBManager >>> :: UpdateNewCustomer ID :"+id);

                return customer.getId();

            }else{ // There is no such customer  in DB

                // Insert new Customer
                if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< DBManager >>> :: InsertNewCustomer ID :"+id);

                id = (int)db.insert(TB_CUSTOMER, null, cv);
                return id;
            }

        }



        return id;
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
        }
        return category;
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
        Cursor cursor = db.query( TB_DISTRICT ,null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()){
                // fill Category object
                district = new District(cursor.getInt( cursor.getColumnIndex(TB_DISTRICT_ID)) ,
                        cursor.getString( cursor.getColumnIndex(TB_DISTRICT_DISTRICT_NAME)));
                cursor.close();
            }
        }
        return district;
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
        }
        return suburb;
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
     * Save Address object in DB
     * if exit then update it
     * @param address
     * @return id int
     */
    @Override
    public int saveAddress(Address address) {

        int id = -1;
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        if (address != null) {

            cv.put(TB_ADDRESS_SUBURB_ID, address.getSuburb().getId());
            cv.put(TB_ADDRESS_LOCATION, address.getLocation());
            cv.put(TB_ADDRESS_DISTRICT_ID, address.getDistrict().getId());

            // check if the object exist then update
            if (getAddressById(address.getId()) != null) {

                // We have this address UPDATE it
                id = db.update(TB_ADDRESS, cv, TB_SUBORDER_ID + "=?", new String[]{"" + address.getId()});

            } else {
                // There is no such address in DB INSERT it
                id = (int) db.insert(TB_ADDRESS, null, cv);
            }

        }
        return id;
    }






    /**
     * Get the order by id
     * @param id of order
     * @return  Order
     */
    @Override
    public Order getOrderById(int id) {
        if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: getOrderById ::: ID : "+id );

        Order order = new Order();
        db = getReadableDatabase();

            // getting cursor with data
        Cursor cursor = db.query(TB_ORDER ,null,"_id = ?", new String[]{""+id}, null, null, null);
        if(cursor != null){

            if(cursor.moveToFirst()) {

                // Fill the Object Order and return it back
                order.setId(cursor.getInt(cursor.getColumnIndex(TB_ORDER_ID)));
                order.setCustomer(
                        getCustomerById( cursor.getInt(cursor.getColumnIndex(TB_ORDER_CUSTOMER_ID)) ));

                    try { // Catch if there is DATETIME parse exceptions
                        order.setOrderDatetimeFor(
                            dateFormat.parse(cursor.getString(cursor.getColumnIndex(TB_ORDER_ORDER_DATETIME_FOR))));
                        order.setOrderDatetimeNow(
                                dateFormat.parse(cursor.getString(cursor.getColumnIndex(TB_ORDER_ORDER_DATETIME_FOR))));
                    }catch(ParseException e){
                        Log.e(AppConst.LOGE, " ::: getOrderById ::: ParseDateTime Err : "+e);

                    }

                order.setSuborder(
                        getSubOrderByOrderId( cursor.getInt( cursor.getColumnIndex(TB_ORDER_ID))));
                order.setTypeOrder(
                        getTypeOrderById( cursor.getInt(cursor.getColumnIndex(TB_ORDER_TYPE_ORDER_ID))));
                order.setStatus(
                        getStatusById(cursor.getInt(cursor.getColumnIndex(TB_ORDER_STATUS_ID))));
                order.setnumPersons(cursor.getInt( cursor.getColumnIndex(TB_ORDER_NUM_PERSONS)));
                order.setAddress(
                        getAddressById(cursor.getInt( cursor.getColumnIndex(TB_ORDER_ADDRESS_ID))) );
                order.setCustomer(
                        getCustomerById(cursor.getInt( cursor.getColumnIndex(TB_ORDER_CUSTOMER_ID))));
                order.setComment( cursor.getString(cursor.getColumnIndex(TB_ORDER_COMMENT)));

            }
                cursor.close(); // close resource
            }

        return order;
    }


    /**
     * Get List of all orders
     *
     * @return List
     */
    @Override
    public List<Order> getAllOrders(){


        if(AppConst.DEBUG) Log.d(AppConst.LOGD, " ::: getAllOrders :::");

        // List which will be returned
        List<Order> listOrders = new ArrayList<>();

         // DB connection
          db = getReadableDatabase();

        // getting cursor with data
        Cursor cursor = db.query(TB_ORDER ,null, TB_ORDER_STATUS_ID+"<>?", new String[]{"1"}, null, null, TB_ORDER_ID+ " DESC");
        if(cursor != null){

            if(cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {

                    // Order for gathering in the list
                    Order order = new Order();

                    // Fill the Object Order and return it back
                    order.setId(cursor.getInt(cursor.getColumnIndex(TB_ORDER_ID)));
                    order.setCustomer(
                            getCustomerById(cursor.getInt(cursor.getColumnIndex(TB_ORDER_CUSTOMER_ID))));

                    try { // Catch if there is DATETIME parse exceptions
                        order.setOrderDatetimeFor(
                                dateFormat.parse(cursor.getString(cursor.getColumnIndex(TB_ORDER_ORDER_DATETIME_FOR))));
                        order.setOrderDatetimeNow(
                                dateFormat.parse(cursor.getString(cursor.getColumnIndex(TB_ORDER_ORDER_DATETIME_FOR))));
                    } catch (ParseException e) {
                        Log.e(AppConst.LOGE, " ::: getOrderById ::: ParseDateTime Err : " + e);

                    }

                    order.setSuborder(
                            getSubOrderByOrderId(cursor.getInt(cursor.getColumnIndex(TB_ORDER_ID))));
                    order.setTypeOrder(
                            getTypeOrderById(cursor.getInt(cursor.getColumnIndex(TB_ORDER_TYPE_ORDER_ID))));
                    order.setStatus(
                            getStatusById(cursor.getInt(cursor.getColumnIndex(TB_ORDER_STATUS_ID))));
                    order.setnumPersons(cursor.getInt(cursor.getColumnIndex(TB_ORDER_NUM_PERSONS)));
                    order.setAddress(
                            getAddressById(cursor.getInt(cursor.getColumnIndex(TB_ORDER_ADDRESS_ID))));
                    order.setCustomer(
                            getCustomerById(cursor.getInt(cursor.getColumnIndex(TB_ORDER_CUSTOMER_ID))));
                    order.setComment(cursor.getString(cursor.getColumnIndex(TB_ORDER_COMMENT)));

                    listOrders.add(order);

                    cursor.moveToNext();
                }
            }
            cursor.close(); // close resource
        }

        return listOrders;




    }


    //////////////////////////////////////////////////////////
    // END FOR GETTING BEANS //////////////////////////////////
    ///////////////////////////////////////////////////////////



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
     * represent the last DateTime that the App was updated
     * @return date
     */
    public Date getVersionDate() {
        // get connection
        db = getReadableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat(IDBInfo.MYSQL_DATETIME_PATTERN);

        // The Date that will be returned
        Date date = null;

        Cursor cursor = db.query(TB_VERSION, null,null, null, null, null, null ,"1");
            if(cursor!= null){
                if(cursor.moveToFirst()){
                    try{
                        // get the date
                        date = sdf.parse(cursor.getString(cursor.getColumnIndex(TB_VERSION_LAST_CHANGES_DATETIME)));
                    }catch (Exception e){
                        Log.d(AppConst.LOGE, "<<< DBManager >>> ::: getVersionDate : "+ e.toString());
                    }

                }
            }

        return date;
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

    /**
     * Return id of typeOrder by Name
     * if there is no such type return -1
     * @param name
     * @return int id order or -1 (if there is no type)
     */
    public int getTypeOrderIdByName(String name){
        int id = -1;
        db = getReadableDatabase();
        Cursor cursor =  db.query(TB_TYPEORDER, new String[]{TB_TYPEORDER_ID}, TB_TYPEORDER_TYPE_ORDER+"=?", new String[]{name},null, null, null, null);

        if(cursor != null){
            if(cursor.moveToFirst()){

                // get the returned id
                id = cursor.getInt(cursor.getColumnIndex(TB_ORDER_ID));

                // close resources
                cursor.close();


                return id;
            }

        }




        return id;
    }



    @Override
    public synchronized void close() {
        db.close();
    }
}
