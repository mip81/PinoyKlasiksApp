package pk.nz.pinoyklasiks.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import pk.nz.pinoyklasiks.MainActivity;

/**
 * Class help to work with DB SQLite
 * has all CRUD methods
 *
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */
public class DBManager extends SQLiteOpenHelper implements IDBInfo, IDBManager{

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
    public Cursor getCategories(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT "+TB_CATEGORY_ID+","+TB_CATEGORY_CAT_NAME+","+TB_CATEGORY_DESCRIPTION+
                 " FROM "+TB_CATEGORY+" ORDER BY  "+TB_CATEGORY_ORDER_ID , null);
        return cursor;
    }


    /**
     * Create and load default data to DB
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
