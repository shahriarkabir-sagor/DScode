package bd.dkltd.dscode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bd.dkltd.dscode.myfragments.MyDialogInputPath;
import java.util.ArrayList;

public class FilesActivity extends AppCompatActivity {
	
	private String iPath;
	private MyDbHelper db_helper;
	private SharedPreferences sPrefs;
	private ArrayList<Paths> pathRcrd;
	private Cursor rowlist;
	private RecyclerView rcv;
	private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_window_close);
		init();
    }

	private void init() {
		//initiate spref
		sPrefs = getSharedPreferences("AppSettings",Context.MODE_PRIVATE);
		iPath = sPrefs.getString("keyInternalPath",null);
		//TextView
		tv1 = findViewById(R.id.eventCheck1);
		tv1.setText("Test");
		
		//db
		db_helper = new MyDbHelper(this);
		db_helper.setInternalPath(iPath);
		rowlist = db_helper.fetchRow();
		
		//path record
		pathRcrd = new ArrayList<Paths>();
		
		//Check Permission
		if (checkPerms()) {
			pathInit();
		} else{
			updateRcrd();
		}
		//img
		int[] img = {R.drawable.ic_folder_home,R.drawable.ic_folder};
		
		//Adapter
		FilePathAdapter fpa = new FilePathAdapter(this,img,pathRcrd);
		fpa.setOnItemClickListener(new FilePathAdapter.ClickListener(){

				@Override
				public void onItemClick(int position, View v) {
					Intent i1 = new Intent(getApplicationContext(),ListFile.class);
					i1.putExtra("rootPath","/storage/emulated/0/");
					startActivity(i1);
				}

				@Override
				public void onItemLongClick(int position, View v) {
					String pos = String.valueOf(position) + " long clicked";
					tv1.setText(pos);
				}
			});
		
		//RecyclerView
		rcv = findViewById(R.id.activity_RecyclerView);
		rcv.setAdapter(fpa);
		rcv.setLayoutManager(new LinearLayoutManager(this));
	}
	
	private boolean checkPerms() {
		int res = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (res == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			return false;
		}
	}

	private void pathInit() {
		if (rowlist.getCount() == 0) {
			long rowId = db_helper.insertRow("Internal",iPath);
			if (rowId == -1) {
				Toast.makeText(getApplication(), "Row insertion failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplication(), "Row successfully inserted", Toast.LENGTH_SHORT).show();
				updateRcrd();
			}
		} else if(rowlist.getCount() == 1) {
			Toast.makeText(getApplication(), "Internal storage already in db", Toast.LENGTH_SHORT).show();
			updateRcrd();
		} else {
			//db has data
			updateRcrd();
		}
		
	}

	private void updateRcrd() {
		rowlist = db_helper.fetchRow();
		if (rowlist.getCount() > 0) {
			while(rowlist.moveToNext()) {
				String pathName = rowlist.getString(1);
				String pathValue = rowlist.getString(2);
				Paths eachPath = new Paths(pathName,pathValue);
				pathRcrd.add(eachPath);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fmanager_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
			case R.id.fItem3:
				MyDialogInputPath iPathChooser = new MyDialogInputPath();
                iPathChooser.setDialogTitle("Choose path");
                iPathChooser.setCancelable(false);
				iPathChooser.show(getFragmentManager(),"Choose path");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
}
