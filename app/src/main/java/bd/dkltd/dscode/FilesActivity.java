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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FilesActivity extends AppCompatActivity {
	
	private String iPath;
	private SharedPreferences sPrefs;

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
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle  = new Bundle();
        bundle.putString("internalPath",iPath);
        
        Fragment fragment = new ListPathFragment();
        fragment.setArguments(bundle);
        ft.replace(R.id.frLayId, fragment);
        ft.commit();
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
