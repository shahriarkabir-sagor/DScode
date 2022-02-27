package bd.dkltd.dscode;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import bd.dkltd.dscode.myfragments.MyDialogInputPath;
import android.widget.LinearLayout.LayoutParams;

public class FilesActivity extends AppCompatActivity implements ListFileFragment.OnPathReceivedCallback {

    private LinearLayout ll;
    private TextView slashView;
    private TextView pathView;
    private LinearLayout.LayoutParams param1;
    private LinearLayout.LayoutParams param2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_window_close);
		init();
    }

	private void init() {
        //Create path navigation
        ll = findViewById(R.id.pathNavigationHolderLL);
        slashView = new TextView(this);
        param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param1.setMarginEnd(4);
        slashView.setLayoutParams(param1);
        slashView.setBackgroundResource(R.color.grey);
        slashView.setPadding(4, 0, 4, 0);
        slashView.setText("/");
        if (ll != null) {
            ll.addView(slashView);
        }
        pathView = new TextView(this);
        param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param2.setMarginStart(4);
        param2.setMarginEnd(4);
        pathView.setLayoutParams(param2);
        pathView.setBackgroundResource(R.color.lightPink);
        pathView.setPadding(4, 0, 4, 0);

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
                startActivity(new Intent(this,FileSettingsActivity.class));
                return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
    
    @Override
    public void onPathReceived(String currentPath) {
        
    }
}
