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
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.view.ViewGroup;

public class FilesActivity extends AppCompatActivity {
    
    private LinearLayout ll;

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
        //Create path navigation
        ll = findViewById(R.id.pathNavigationHolderLL);
        pathNavigator();
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ListPathFragment fragment = new ListPathFragment();
        ft.add(R.id.frLayId, fragment.newInstance(),"List storage");
        ft.commit();
	}

    private void pathNavigator() {
        TextView slashView = new TextView(this);
        slashView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        slashView.setBackgroundResource(R.color.grey);
        slashView.setPadding(4,0,4,0);
        slashView.setText("/");
        if (ll != null) {
            ll.addView(slashView);
        }
        TextView pathView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(4);
        params.setMarginEnd(4);
        pathView.setLayoutParams(params);
        pathView.setBackgroundResource(R.color.lightPink);
        pathView.setPadding(4,0,4,0);
        pathView.setText("paths");
        if (ll != null) {
            ll.addView(pathView);
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
				final MyDialogInputPath iPathChooser = new MyDialogInputPath();
                iPathChooser.setDialogTitle("Choose path");
                iPathChooser.setCancelable(false);
                iPathChooser.setOnPositiveListener(new MyDialogInputPath.OnPositiveListener() {

                        @Override
                        public void onClick(String pathName, String pathValue) {
                            MyDbHelper storage_db = new MyDbHelper(getApplicationContext());
                            long rowId = storage_db.insertRow("Storage_Path",pathName,pathValue);
                            if (rowId == -1) {
                                Toast.makeText(getApplicationContext(), "Row insertion failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Row successfully inserted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
				iPathChooser.show(getFragmentManager(),"Choose path");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
}
