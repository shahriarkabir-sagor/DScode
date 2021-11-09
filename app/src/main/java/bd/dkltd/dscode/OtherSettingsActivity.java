package bd.dkltd.dscode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import bd.dkltd.dscode.R;
import bd.dkltd.dscode.myfragments.MyDialogListViewFragment;

public class OtherSettingsActivity extends AppCompatActivity implements OnCheckedChangeListener
{
	
	private TextView tv1,tv2;
	private String[] strLangArray, strPrevArray;
	private int prevModeIndex,chanLangIndex;
	private CheckBox checkbox1,checkbox2;
	private boolean boolExit,boolConsole;
	private SharedPreferences sPrefs;
	private SharedPreferences.Editor sPrefsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		init();
        loadSprefs();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {
		//TextView
		tv1 = findViewById(R.id.osChanLangSetTvId);
		tv2 = findViewById(R.id.osPrevModeSetTvId);
		strLangArray = getResources().getStringArray(R.array.app_langs);
		strPrevArray = getResources().getStringArray(R.array.preview_mode);
		prevModeIndex = chanLangIndex = 0;
		//Checkboxes
		checkbox1 = findViewById(R.id.osExitId);
		checkbox2 = findViewById(R.id.osConsoleId);
		//Boolean value
		boolExit = checkbox1.isChecked();
		boolConsole = checkbox2.isChecked();
		//SharedPreferences
		sPrefs = getSharedPreferences("AppSettings",Context.MODE_PRIVATE);
		sPrefsEditor = sPrefs.edit();
		//Set listener to checkbox
		checkbox1.setOnCheckedChangeListener(this);
		checkbox2.setOnCheckedChangeListener(this);
	}

	private void loadSprefs() {
		// Get value from shared preferences
		boolExit = sPrefs.getBoolean("keyBoolExit",boolExit);
		boolConsole = sPrefs.getBoolean("keyBoolConsole",boolConsole);
		prevModeIndex = sPrefs.getInt("keyIntPrevModeIndex",prevModeIndex);
		chanLangIndex = sPrefs.getInt("keyIntChanLangIndex",chanLangIndex);
		// set value
		checkbox1.setChecked(boolExit);
		checkbox2.setChecked(boolConsole);
		tv1.setText(strLangArray[chanLangIndex]);
		tv2.setText(strPrevArray[prevModeIndex]);
	}
	
	public void onOtherSettingsItemClick(View v) {
		switch (v.getId()) {
			case R.id.osItem1:
				boolExit = !boolExit;
				checkbox1.setChecked(boolExit);
				break;
			case R.id.osItem2:
				Intent inI2 = new Intent(getApplication(),KeyBindingActivity.class);
				startActivity(inI2);
				break;
			case R.id.osItem3:
				selLang();
				break;
			case R.id.osItem4:
				selPrevMode();
				break;
			case R.id.osItem5:
				boolConsole = !boolConsole;
				checkbox2.setChecked(boolConsole);
				break;
			default: Toast.makeText(getApplication(), "No known Action Found", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2) {
		switch (p1.getId()) {
			case R.id.osExitId:
				boolExit = p2;
				sPrefsEditor.putBoolean("keyBoolExit",boolExit);
				sPrefsEditor.apply();
				break;
			case R.id.osConsoleId:
				boolConsole = p2;
				sPrefsEditor.putBoolean("keyBoolConsole",boolConsole);
				sPrefsEditor.apply();
				break;
			default: Toast.makeText(getApplication(), "unknown checkbox", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void selLang() {
		MyDialogListViewFragment langDlog = new MyDialogListViewFragment();
		langDlog.setDialogTitle("Select Language");
		langDlog.setDialogItemId(R.array.app_langs);
		langDlog.setDialogItemClickListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int indexPosition) {
					switch (indexPosition) {
						case 0:
							sPrefsEditor.putInt("keyIntChanLangIndex",indexPosition);
							sPrefsEditor.apply();
							String str0 = strLangArray[0];
							tv1.setText(str0);
							Toast.makeText(getApplication(), "Language set to English", Toast.LENGTH_SHORT).show();
							break;
						case 1:
							sPrefsEditor.putInt("keyIntChanLangIndex",indexPosition);
							sPrefsEditor.apply();
							String str1 = strLangArray[1];
							tv1.setText(str1);
							Toast.makeText(getApplication(), "Language set to Bengali", Toast.LENGTH_SHORT).show();
							break;
						default: Toast.makeText(getApplication(), "No known action found", Toast.LENGTH_SHORT).show();
					}
				}
				
			
		});
		langDlog.show(getFragmentManager(),"Select language");
	}

	private void selPrevMode() {
		MyDialogListViewFragment prevMode = new MyDialogListViewFragment();
		prevMode.setDialogTitle("Select Preview mode");
		prevMode.setDialogItemId(R.array.preview_mode);
		prevMode.setDialogItemClickListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int indexPosition) {
					switch (indexPosition) {
						case 0:
							sPrefsEditor.putInt("keyIntPrevModeIndex",indexPosition);
							sPrefsEditor.apply();
							String str0 = strPrevArray[0];
							tv2.setText(str0);
							break;
						case 1:
							sPrefsEditor.putInt("keyIntPrevModeIndex",indexPosition);
							sPrefsEditor.apply();
							String str1 = strPrevArray[1];
							tv2.setText(str1);
							break;
						case 2:
							sPrefsEditor.putInt("keyIntPrevModeIndex",indexPosition);
							sPrefsEditor.apply();
							String str2 = strPrevArray[2];
							tv2.setText(str2);
							break;
						default:Toast.makeText(getApplication(), "No known mode found", Toast.LENGTH_SHORT).show();
					}
				}
			});
		prevMode.show(getFragmentManager(),"Select Preview mode");
		
	}
}
