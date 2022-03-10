package bd.dkltd.dscode;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.widget.Toast;
import android.content.ContentValues;
import android.database.Cursor;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Storage DB";
    private static final int VERSION_NUMBER = 1;
    private Context cntx;

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.cntx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDb) {

        Toast.makeText(cntx, "OnCreate of StorageDB is called ", Toast.LENGTH_SHORT).show();

        /**
         @table: Storage_Path
         @description: All pre-built and added storage path will be here
         **/
        String queryCreateTable1 = "CREATE TABLE [Storage_Path] ([path_name] TEXT(20) NOT NULL UNIQUE,[storage_path] VARCHAR(255) NOT NULL UNIQUE);";
        /**
         @table: Opened_directory
         @description: Here all opened directory will be stored. It may contain nested table
         */
        String queryCreateTable2 = "CREATE TABLE [Opened_directory] ([directory_name] TEXT(20) NOT NULL,[directory_path] VARCHAR(255) NOT NULL UNIQUE);";
        /**
         @table: Recent_dir_list
         @description: Recently used directory will be stored here
         */
        String queryCreateTable3 = "CREATE TABLE [Recent_dir_list] ([directory_path] VARCHAR(255) NOT NULL UNIQUE);";
        try {
            sqlDb.execSQL(queryCreateTable1);
            Toast.makeText(cntx, "Table1 created", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(cntx, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show(); 
        }
        try {
            sqlDb.execSQL(queryCreateTable2);
            Toast.makeText(cntx, "Table2 created", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(cntx, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
            sqlDb.execSQL(queryCreateTable3);
            Toast.makeText(cntx, "Table3 created", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(cntx, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase p1, int p2, int p3) {
    }

    public long insertRow(String tableName, String name, String value) { 
        ContentValues cValues = new ContentValues();  
        SQLiteDatabase sqlDb = this.getWritableDatabase();
        long rowSl;
        switch(tableName) {
            case "Storage_Path":
                cValues.put("path_name",name);
                cValues.put("storage_path",value);
                rowSl = sqlDb.insert("Storage_Path",null,cValues);
                break;
            case "Opened_directory":
                cValues.put("directory_name",name);
                cValues.put("directory_path",value);
                rowSl = sqlDb.insert("Opened_directory",null,cValues);
                break;
            case "Recent_dir_list":
                cValues.put("directory_path",value);
                rowSl = sqlDb.insert("Recent_dir_list",null,cValues);
                break;
            default:
                rowSl = -1;
        } 
        return rowSl;
    }
    public Cursor fetchRowFrom(String tableName) {
        String query = "SELECT * FROM "+tableName;
        SQLiteDatabase sqlDb = this.getWritableDatabase();
        Cursor cursor = sqlDb.rawQuery(query,null);
        return cursor;
    }
    
    public int deleteRowFrom(String tableName, String column1, String value) {
        String selection = column1 + " LIKE ?";
        String[] selectionArgs = { value };
        SQLiteDatabase sqlDb = this.getWritableDatabase();
        int deletedRows = sqlDb.delete(tableName,selection,selectionArgs);
        return deletedRows;
    }
}
