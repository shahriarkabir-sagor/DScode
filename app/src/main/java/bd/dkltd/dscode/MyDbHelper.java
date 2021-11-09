package bd.dkltd.dscode;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.database.Cursor;

public class MyDbHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "MainDB";
	private static final String TABLE1_NAME = "File_table";
	private static final String FT_COLLUMN1 = "Sl";
	private static final String FT_COLLUMN2 = "Path_Name";
	private static final String FT_COLLUMN3 = "Path_String";
	private static final int DB_VERSION = 1;
	private Context cntx;
	
	private String internalPath;
	
	public MyDbHelper(Context context) {
		super(context,DATABASE_NAME,null,DB_VERSION);
		this.cntx = context;
	}

	public void setInternalPath(String internalPath) {
		this.internalPath = internalPath;
	}

	public String getInternalPath() {
		return internalPath;
	}

	@Override
	public void onCreate(SQLiteDatabase sqlDb) {
		String q1 = "CREATE TABLE "+TABLE1_NAME+"("+FT_COLLUMN1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+FT_COLLUMN2+" VARCHAR(255),"+FT_COLLUMN3+" VARCHAR(255));";
		Toast.makeText(cntx,"onCreate called",Toast.LENGTH_SHORT).show();
		try {
			sqlDb.execSQL(q1);
			Toast.makeText(cntx, "Table created", Toast.LENGTH_SHORT).show();
		} catch (SQLException e) {

		}
	}

	private boolean checkPerms() {
		int res = ContextCompat.checkSelfPermission(cntx,Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (res == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase p1, int p2, int p3) {
		
	}
	
	public long insertRow(String pathName, String pathValue) {
		
		ContentValues cValus = new ContentValues();
		cValus.put(FT_COLLUMN2,pathName);
		cValus.put(FT_COLLUMN3,pathValue);
		
		//insert into table
		SQLiteDatabase sqlDb = this.getWritableDatabase();
		long rowSl = sqlDb.insert(TABLE1_NAME,null,cValus);
		
		return rowSl;
	}
	public Cursor fetchRow() {
		String q2 = "SELECT * FROM "+TABLE1_NAME;
		SQLiteDatabase sqlDb = this.getWritableDatabase();
		Cursor cursor = sqlDb.rawQuery(q2,null);
		return cursor;
	}
}
