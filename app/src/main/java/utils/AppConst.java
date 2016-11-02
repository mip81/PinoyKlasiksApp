package utils;

import android.util.DebugUtils;

/**
 * <pre>
 *
 * Title       : AppConstant interface
 * Purpose     : Keep constant which used in application
 * Date        : 01.10.2016
 * Input       : None
 * Proccessing : None
 * Output      : Constant
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public interface AppConst {

    //time for checking status in service
    public static int TIME_CHECK_STATUS = 20000;

    // Time to change the DEALS banner
    public static int TIME_CHANGE_DEALS = 7000;

    // Name of the Restuarant
    public static final String RESTAURANT_NAME = "Pinoy Klasiks";

    // Aplication name
    public  static String APP_NAME = "Pinoy Klasiks App";

    // Turn on / off debug mode in app
    public static boolean DEBUG = true;

    // exctension for image files
    public static String PIC_EXT = ".jpg";

    // Message for debuging
    public static String LOGD = " :<<< DEBUG PINOY >>>:";

    // Message for exception
    public static String LOGE = " ::: EXCEPTION :::";

    // SubOrder Activity
    // Messsage cart have no products
    public static String CART_IS_EMPTY = " Your cart is empty...";
}
