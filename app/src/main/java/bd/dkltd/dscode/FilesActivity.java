package bd.dkltd.dscode;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import bd.dkltd.dscode.myfragments.MyDialogInputPath;
import java.io.File;
import java.util.ArrayList;

public class FilesActivity extends AppCompatActivity implements ListFileFragment.OnPathReceivedCallback {

    private HorizontalScrollView hsv;
    private LinearLayout ll;
    private MyDbHelper storage_db;
    private Cursor rowList;
    private ArrayList<String> folderNavigationEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_window_close);
		init();
    }

	private void init() {
        hsv = findViewById(R.id.NavWrapHSV);
        ll = findViewById(R.id.pathNavigationHolderLL);
        storage_db = new MyDbHelper(this);
        folderNavigationEntries = new ArrayList<String>();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ListPathFragment fragment = new ListPathFragment();
        ft.add(R.id.frLayId, fragment.newInstance(), "List storage");
        ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fileactivity_menu, menu);
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
                            long rowId = storage_db.insertRow("Storage_Path", pathName, pathValue);
                            if (rowId == -1) {
                                Toast.makeText(getApplicationContext(), "Row insertion failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Row successfully inserted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
				iPathChooser.show(getFragmentManager(), "Choose path");
				return true;
            case R.id.fItem7:
                startActivity(new Intent(this, FileSettingsActivity.class));
                return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public void onPathReceived(String currentPath) {
        File file = new File(currentPath);
        String storageName = getStorageName(currentPath);
        if (storageName == null) {
            storageName = file.getName();
        }
        View childView = getLayoutInflater().inflate(R.layout.layout_nav_path, ll, false);
        TextView pathtv = childView.findViewById(R.id.pathNavTvId);
        folderNavigationEntries.add(currentPath);
        pathtv.setText(storageName);
        ll.addView(childView);
        hsv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                @Override
                public void onLayoutChange(View p1, int left, int top, int right, int bottom, int p6, int p7, int p8, int p9) {
                    hsv.fullScroll(View.FOCUS_RIGHT);
                }
            });
    }

    private String getStorageName(String path) {
        String result = null;
        String pathValue = null;
        rowList = storage_db.fetchRowFrom("Storage_Path");
        if (rowList.getCount() > 0) {
            while (rowList.moveToNext()) {
                pathValue = rowList.getString(1);
                if (path.equals(pathValue)) {
                    result = rowList.getString(0);
                }
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        if (folderNavigationEntries.size() > 0) {
            int lastIndex = folderNavigationEntries.size() -1;
            folderNavigationEntries.remove(lastIndex);
            ll.removeViewAt(lastIndex);
        }
        super.onBackPressed();
    }
}
