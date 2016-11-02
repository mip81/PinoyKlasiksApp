package pk.nz.pinoyklasiks.db;

/**<pre>
 *
 * Title       : IDBInfo interface
 * Purpose     : Keeper all constants for the App
 *               related to DB
 *
 * Date        : 15.10.2016
 * Input       : none
 * Proccessing : none
 * Output      : IDBInfo class
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public interface IDBInfo {

    // The link to get JSON Weather object
    public static String WEATHER_LINK = "http://api.openweathermap.org/data/2.5/weather?q=Auckland,nz&appid=c752691507cd475666536fb7b2150b37";

    // The phone number of Restuarant
    public static String PHONE_OF_RESTAURANT = "0211693229";


    // The constant for WebService
    //public static String WEBSERVICE_SHOST = "http://192.168.56.1/ws/pinoy_webservice.php";
    //public static String DEALS_IMG_URL = "http://192.168.56.1/ws/img/deals/";

    public static String WEBSERVICE_SHOST = "http://jenny.kz/ws/pinoy_webservice.php";
    public static String DEALS_IMG_URL = "http://jenny.kz/ws/img/deals/";

    public static String USERNAME="PINOYADMIN";
    public static String PASSWORD="429599";

    //The MYSQL settings
    public static String MYSQL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // The constatnt for JSON operations
    public static String JSON_ACTION_NEW_ORDER = "new_order";
    public static String JSON_ACTION_CHECK_VERSION = "check_version";
    public static String JSON_ACTION_CHECK_STATUS_ORDER = "check_status_order";
    public static String JSON_ACTION_GET_DEALS = "get_deals";



    // The constant for SQLite DB
    public static String PATH_TO_DB_RES = "res/raw/db.sql";
    public static String DBName = "db_pinoy_klasiks";
    public static int VER = 10;

    // The constant desribe all the names of fields and tables in the Database
    public static String TB_CATEGORY = "tb_category";
        public  static String TB_CATEGORY_ID = "_id";
        public  static String TB_CATEGORY_DESCRIPTION= "description";
        public  static String TB_CATEGORY_CAT_NAME= "cat_name";
        public  static String TB_CATEGORY_PIC = "pic";
        public  static String TB_CATEGORY_ORDER_ID = "order_id";

    public static String TB_COMMENT = "tb_comment";
        public static String TB_COMMENT_ID = "_id";
        public static String TB_COMMENT_PRODUCT_ID = "product_id";
        public static String TB_COMMENT_COMMENTED_TEXT = "commented_txt";
        public static String TB_COMMENT_RATE = "rate";

    public static String TB_CUSTOMER = "tb_customer";
        public static String TB_CUSTOMER_ID = "_id";
        public static String TB_CUSTOMER_CUSTOMER_NAME = "customer_name";
        public static String TB_CUSTOMER_PHONE_NUMBER = "phone_number";
        public static String TB_CUSTOMER_EMAIL = "email";
        public static String TB_CUSTOMER_ADDRESS_ID = "address_id";

    public static String TB_PRODUCT = "tb_product";
        public static String TB_PRODUCT_ID = "_id";
        public static String TB_PRODUCT_CAT_ID = "cat_id";
        public static String TB_PRODUCT_PRODUCT_NAME = "product_name";
        public static String TB_PRODUCT_PRODUCT_DESC = "product_desc";
        public static String TB_PRODUCT_PRODUCT_PRICE = "product_price";
        public static String TB_PRODUCT_PRODUCT_PIC = "product_pic";

    public static String TB_ORDER = "tb_order";
        public static String TB_ORDER_ID = "_id";
        public static String TB_ORDER_ORDER_DATETIME_NOW = "order_datetime_now";
        public static String TB_ORDER_ORDER_DATETIME_FOR = "order_datetime_for";
        public static String TB_ORDER_CUSTOMER_ID = "customer_id";
        public static String TB_ORDER_STATUS_ID = "status_id";
        public static String TB_ORDER_TYPE_ORDER_ID = "type_order_id";
        public static String TB_ORDER_NUM_PERSONS = "num_persons";
        public static String TB_ORDER_ADDRESS_ID = "address_id";
        public static String TB_ORDER_LOCATION = "location";
        public static String TB_ORDER_COMMENT = "comment";

        // The ID on the server get after sending the order
        public static String TB_ORDER_GLOBAL_ID = "global_id";

    public static String TB_STATUS = "tb_status";
        public static String TB_STATUS_ID = "_id";
        public static String TB_STATUS_STATUS_NAME = "status_name";

    public static String TB_SUBORDER = "tb_suborder";
        public static String TB_SUBORDER_ID = "_id";
        public static String TB_SUBORDER_PRODUCT_ID = "product_id";
        public static String TB_SUBORDER_QUANTITY = "quantity";
        public static String TB_SUBORDER_PRICE = "price";
        public static String TB_SUBORDER_ORDER_ID = "order_id";


    public static String TB_TYPEORDER = "tb_typeorder";
        public static String TB_TYPEORDER_ID = "_id";
        public static String TB_TYPEORDER_TYPE_ORDER = "type_order";

    public static String TB_VERSION = "tb_version";
        public static String TB_VERSION_VERSION = "version";
        public static String TB_VERSION_LAST_CHANGES_DATETIME = "last_changes_datetime";

    public static String TB_ADDRESS = "tb_address";
        public static String TB_ADDRESS_ID = "_id";
        public static String TB_ADDRESS_DISTRICT_ID = "district_id";
        public static String TB_ADDRESS_SUBURB_ID = "suburb_id";
        public static String TB_ADDRESS_LOCATION = "location";

    public static String TB_DISTRICT = "tb_district";
        public static String TB_DISTRICT_ID = "_id";
        public static String TB_DISTRICT_DISTRICT_NAME = "district_name";

public static String TB_SUBURB = "tb_suburb";
        public static String TB_SUBURB_ID = "_id";
        public static String TB_SUBURB_SUBURB_NAME = "suburb_name";
        public static String TB_SUBURB_DISTRICT_ID = "district_id";




}
