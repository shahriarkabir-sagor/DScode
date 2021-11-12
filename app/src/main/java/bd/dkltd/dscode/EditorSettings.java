package bd.dkltd.dscode;

import android.content.Context;
import android.content.DialogInterface;
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
import bd.dkltd.dscode.myfragments.MyDialogDecimalFragment;
import bd.dkltd.dscode.myfragments.MyDialogFragment;
import bd.dkltd.dscode.myfragments.MyDialogListViewFragment;
import bd.dkltd.dscode.myfragments.MyDialogNumberFragment;

public class EditorSettings extends AppCompatActivity implements OnCheckedChangeListener {
	

	private CheckBox animCbox,floatingBtnCbox,fullScreenCbox,lineNmbrCbox,syntaxErrorCbox,autoCmpltCbox,qToolCbox,printMrgnCbox,showSpaceCbox,softTabCbox,textWrapCbox,vTouchCbox;
	private boolean boolAnim,boolFbtn,boolFscreen,boolLnmbr,boolSErr,boolAcmplt,boolqTool,boolPrintMrgn,boolShowSpace,boolSoftTab,boolTxtWrap,boolVtouch,boolAutoSave;
	private TextView ccsTv,efTv,afpTv,autoSaveTv,autoSaveTv2,fontSizeTv,scrollBTv,tabSizeTv,lineHeightTv;
	private String[] ccsStrArray,efStrArray,afpStrArray;
	private int ccsIntPos,efIntPos,afpIntPos;
    private int autoSaveTime,fontSize,scrollBarSize,tabSize;
    private float lineHeight;
	private SharedPreferences sPrefs;
	private SharedPreferences.Editor sPrefsEditor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
		loadSprefs();
    }
	
	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2) {
		//Animation checkbox
		switch (p1.getId()) {
			case R.id.animCheckboxId:
				boolAnim = p2;
				sPrefsEditor.putBoolean("keyBoolAnimation",boolAnim);
				sPrefsEditor.apply();
				break;
			case R.id.floatingBtnCboxId:
				boolFbtn = p2;
				sPrefsEditor.putBoolean("keyBoolFbtn",boolFbtn);
				sPrefsEditor.apply();
				break;
			case R.id.fullScreenCboxId:
				boolFscreen = p2;
				sPrefsEditor.putBoolean("keyBoolFscreen",boolFscreen);
				sPrefsEditor.apply();
				break;
			case R.id.lineNmbrCboxId:
				boolLnmbr = p2;
				sPrefsEditor.putBoolean("keyBoolLnmbr",boolLnmbr);
				sPrefsEditor.apply();
				break;
			case R.id.syntaxErrorCboxId:
				boolSErr = p2;
				sPrefsEditor.putBoolean("keyBoolSErr",boolSErr);
				sPrefsEditor.apply();
				break;
			case R.id.autoCmpltCboxId:
				boolAcmplt = p2;
				sPrefsEditor.putBoolean("keyBoolAcmplt",boolAcmplt);
				sPrefsEditor.apply();
				break;
			case R.id.qToolCboxId:
				boolqTool = p2;
				sPrefsEditor.putBoolean("keyBoolqTool",boolqTool);
				sPrefsEditor.apply();
				break;
			case R.id.printMrgnCboxId:
				boolPrintMrgn = p2;
				sPrefsEditor.putBoolean("keyBoolPrintMrgn",boolPrintMrgn);
				sPrefsEditor.apply();
				break;
			case R.id.showSpaceCboxId:
				boolShowSpace = p2;
				sPrefsEditor.putBoolean("keyBoolShowSpace",boolShowSpace);
				sPrefsEditor.apply();
				break;
			case R.id.softTabCboxId:
				boolSoftTab = p2;
				sPrefsEditor.putBoolean("keyBoolSoftTab",boolSoftTab);
				sPrefsEditor.apply();
				break;
			case R.id.textWrapCboxId:
				boolTxtWrap = p2;
				sPrefsEditor.putBoolean("keyBoolTxtWrap",boolTxtWrap);
				sPrefsEditor.apply();
				break;
			case R.id.vTouchCboxId:
				boolVtouch = p2;
				sPrefsEditor.putBoolean("keyBoolVtouch",boolVtouch);
				sPrefsEditor.apply();
				break;
			default: Toast.makeText(getApplication(), "No known Checkbox found", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void init() {
		//initiate
		animCbox = findViewById(R.id.animCheckboxId);
		floatingBtnCbox = findViewById(R.id.floatingBtnCboxId);
		fullScreenCbox = findViewById(R.id.fullScreenCboxId);
		lineNmbrCbox = findViewById(R.id.lineNmbrCboxId);
		syntaxErrorCbox = findViewById(R.id.syntaxErrorCboxId);
		autoCmpltCbox = findViewById(R.id.autoCmpltCboxId);
		qToolCbox = findViewById(R.id.qToolCboxId);
		printMrgnCbox = findViewById(R.id.printMrgnCboxId);
		showSpaceCbox = findViewById(R.id.showSpaceCboxId);
		softTabCbox = findViewById(R.id.softTabCboxId);
		textWrapCbox = findViewById(R.id.textWrapCboxId);
		vTouchCbox = findViewById(R.id.vTouchCboxId);
		//TextView
		ccsTv = findViewById(R.id.esCCStvId);
		efTv = findViewById(R.id.esEdtrFontTvId);
		afpTv = findViewById(R.id.esAFtvId);
        autoSaveTv = findViewById(R.id.esAStv);
        autoSaveTv2 = findViewById(R.id.esAutoSave2ndary);
        fontSizeTv = findViewById(R.id.esFStv);
        scrollBTv = findViewById(R.id.esSBStv);
        tabSizeTv = findViewById(R.id.esTStv);
        lineHeightTv = findViewById(R.id.esLHtv);
        
		//Get array
		ccsStrArray = getResources().getStringArray(R.array.cursor_controller_size);
		efStrArray = getResources().getStringArray(R.array.app_font_name_array);
		afpStrArray = getResources().getStringArray(R.array.active_file);
		//initiate index position of item
		ccsIntPos = efIntPos = afpIntPos = 0;
        //initiate number,decimal variable
        autoSaveTime = fontSize = scrollBarSize = tabSize = 0;
        lineHeight = (float) 0.0;
		
		
		//value
		boolAnim = animCbox.isChecked();
		boolFbtn = floatingBtnCbox.isChecked();
		boolFscreen = fullScreenCbox.isChecked();
		boolLnmbr = lineNmbrCbox.isChecked();
		boolSErr = syntaxErrorCbox.isChecked();
		boolAcmplt = autoCmpltCbox.isChecked();
		boolqTool = qToolCbox.isChecked();
		boolPrintMrgn = printMrgnCbox.isChecked();
		boolShowSpace = showSpaceCbox.isChecked();
		boolSoftTab = softTabCbox.isChecked();
		boolTxtWrap = textWrapCbox.isChecked();
		boolVtouch = vTouchCbox.isChecked();
        boolAutoSave = false;
		
		//Shared Preferences
		sPrefs = getSharedPreferences("AppSettings",Context.MODE_PRIVATE);
		sPrefsEditor = sPrefs.edit();
		
		//Set OnCheckedChangeListener to every checkbox
		animCbox.setOnCheckedChangeListener(this);
		floatingBtnCbox.setOnCheckedChangeListener(this);
		fullScreenCbox.setOnCheckedChangeListener(this);
		lineNmbrCbox.setOnCheckedChangeListener(this);
		syntaxErrorCbox.setOnCheckedChangeListener(this);
		autoCmpltCbox.setOnCheckedChangeListener(this);
		qToolCbox.setOnCheckedChangeListener(this);
		printMrgnCbox.setOnCheckedChangeListener(this);
		showSpaceCbox.setOnCheckedChangeListener(this);
		softTabCbox.setOnCheckedChangeListener(this);
		textWrapCbox.setOnCheckedChangeListener(this);
		vTouchCbox.setOnCheckedChangeListener(this);
		
	}
	
	private void loadSprefs() {
		// Retrieve all the value from SharedPreferences
		boolAnim = sPrefs.getBoolean("keyBoolAnimation",boolAnim);
		boolFbtn = sPrefs.getBoolean("keyBoolFbtn",boolFbtn);
		boolFscreen = sPrefs.getBoolean("keyBoolFscreen",boolFscreen);
		boolLnmbr = sPrefs.getBoolean("keyBoolLnmbr",boolLnmbr);
		boolSErr = sPrefs.getBoolean("keyBoolSErr",boolSErr);
		boolAcmplt = sPrefs.getBoolean("keyBoolAcmplt",boolAcmplt);
		boolqTool = sPrefs.getBoolean("keyBoolqTool",boolqTool);
		boolPrintMrgn = sPrefs.getBoolean("keyBoolPrintMrgn",boolPrintMrgn);
		boolShowSpace = sPrefs.getBoolean("keyBoolShowSpace",boolShowSpace);
		boolSoftTab = sPrefs.getBoolean("keyBoolSoftTab",boolSoftTab);
		boolTxtWrap = sPrefs.getBoolean("keyBoolTxtWrap",boolTxtWrap);
		boolVtouch = sPrefs.getBoolean("keyBoolVtouch",boolVtouch);
        boolAutoSave = sPrefs.getBoolean("keyBoolAutoSave",boolAutoSave);
		// retrieve item position
		ccsIntPos = sPrefs.getInt("keyIntCCSindex",ccsIntPos);
		efIntPos = sPrefs.getInt("keyIntEFindex",efIntPos);
		afpIntPos = sPrefs.getInt("keyIntAFPindex",afpIntPos);
        // Retrieve integer, decimal value
        if (boolAutoSave) {
            autoSaveTime = sPrefs.getInt("keyIntAutoSave",0);
            autoSaveTv.setText(String.valueOf(autoSaveTime));
            autoSaveTv2.setVisibility(View.VISIBLE);
        } else {
            autoSaveTv.setText("Disabled");
            autoSaveTv2.setVisibility(View.INVISIBLE);
        }
        fontSize = sPrefs.getInt("keyIntFontSize",fontSize);
        if (fontSize >= 4 && fontSize <= 48){
            fontSizeTv.setText(String.valueOf(fontSize));
        }
        scrollBarSize = sPrefs.getInt("keyIntScrollBarSize",scrollBarSize);
        if (scrollBarSize >= 5 && scrollBarSize <= 20){
            scrollBTv.setText(String.valueOf(scrollBarSize));
        }
        tabSize = sPrefs.getInt("keyIntTabSize",tabSize);
        if (tabSize >= 2 && tabSize <= 20){
            tabSizeTv.setText(String.valueOf(tabSize));
        }
        lineHeight = sPrefs.getFloat("keyFloatLineHeight",lineHeight);
        if (lineHeight >= 1 && lineHeight <= 100) {
            lineHeightTv.setText(String.valueOf(lineHeight));
        }

		// Set checkbox value
		animCbox.setChecked(boolAnim);
		floatingBtnCbox.setChecked(boolFbtn);
		fullScreenCbox.setChecked(boolFscreen);
		lineNmbrCbox.setChecked(boolLnmbr);
		syntaxErrorCbox.setChecked(boolSErr);
		autoCmpltCbox.setChecked(boolAcmplt);
		qToolCbox.setChecked(boolqTool);
		printMrgnCbox.setChecked(boolPrintMrgn);
		showSpaceCbox.setChecked(boolShowSpace);
		softTabCbox.setChecked(boolSoftTab);
		textWrapCbox.setChecked(boolTxtWrap);
		vTouchCbox.setChecked(boolVtouch);
		
		//set Item value
		ccsTv.setText(ccsStrArray[ccsIntPos]);
		efTv.setText(efStrArray[efIntPos]);
		afpTv.setText(afpStrArray[afpIntPos]);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onEditorSettingItemClick(View v) {
		switch (v.getId()) {
			case R.id.esAnimItemId: 
				boolAnim = !boolAnim;
				animCbox.setChecked(boolAnim);
				break;
			case R.id.esAutoSaveItemId:
				//call autosave method
				autoSave();
				break;
			case R.id.esBeautifySaveItemId:
				//call beatutifyOnSave method
				beautifyOnSave();
				break;
			case R.id.esCCsizeItemId:
				//call method
				cursCntrlrSize();
				break;
			case R.id.esDefaultFontItemId:
				//call method
				fontSelector();
				break;
			case R.id.esFbtnItemId:
				boolFbtn = !boolFbtn;
				floatingBtnCbox.setChecked(boolFbtn);
				break;
			case R.id.esFontSizeItemId:
				//call method
				esFontSize();
				break;
			case R.id.esFScrnItemId:
				boolFscreen = !boolFscreen;
				fullScreenCbox.setChecked(boolFscreen);
				break;
			case R.id.esLineHeightItemId:
				//call method
				esLineHeight();
				break;
			case R.id.esLnmbrItemId:
				boolLnmbr = !boolLnmbr;
				lineNmbrCbox.setChecked(boolLnmbr);
				break;
			case R.id.esSerrItemId:
				boolSErr = !boolSErr;
				syntaxErrorCbox.setChecked(boolSErr);
				break;
			case R.id.esAcmpltItemId:
				boolAcmplt = !boolAcmplt;
				autoCmpltCbox.setChecked(boolAcmplt);
				break;
			case R.id.esActiveFileItemId:
				//call method
				activeFilePositionChooser();
				break;
			case R.id.esQtoolItemId:
				boolqTool = !boolqTool;
				qToolCbox.setChecked(boolqTool);
				break;
			case R.id.esScrollbarSizeItemId:
				//call method
				esScrollbarSize();
				break;
			case R.id.esPmrgnItemId:
				boolPrintMrgn = !boolPrintMrgn;
				printMrgnCbox.setChecked(boolPrintMrgn);
				break;
			case R.id.esSspaceItemId:
				boolShowSpace = !boolShowSpace;
				showSpaceCbox.setChecked(boolShowSpace);
				break;
			case R.id.esStabItemId:
				boolSoftTab = !boolSoftTab;
				softTabCbox.setChecked(boolSoftTab);
				break;
			case R.id.esTabSizeItemId:
				//call method
				esTabSize();
				break;
			case R.id.esTwrapItemId:
				boolTxtWrap = !boolTxtWrap;
				textWrapCbox.setChecked(boolTxtWrap);
				break;
			case R.id.esVtouchItemId:
				boolVtouch = !boolVtouch;
				vTouchCbox.setChecked(boolVtouch);
				break;
			default:
				Toast.makeText(getApplication(), "No known settings found", Toast.LENGTH_SHORT).show();
		}
	}

	private void autoSave() {
        int minValue = 1;
        int maxValue = 3600;
		MyDialogNumberFragment autoSaveDialog = new MyDialogNumberFragment(getApplicationContext(),1);
		autoSaveDialog.setDialogTitle("Enter time in second(1-3600) or 0");
        autoSaveDialog.setMinDialogValue(minValue);
        autoSaveDialog.setMaxDialogValue(maxValue);
		autoSaveDialog.setDialogZero(true);
        autoSaveDialog.setPlaceHolderValue(autoSaveTime);
		autoSaveDialog.setDialogPositiveListener(new MyDialogNumberFragment.DialogPositiveListener(){

				@Override
				public void onPositiveListen(int requestCode, int value) {
					// Do Operation with the value
                    if (value == 0) {
                        sPrefsEditor.putBoolean("keyBoolAutoSave",false);
                        sPrefsEditor.apply();
                        autoSaveTime = value;
                        //Now update textview
                        autoSaveTv.setText("Disabled");
                        autoSaveTv2.setVisibility(View.INVISIBLE);
                    } else{
                        sPrefsEditor.putInt("keyIntAutoSave",value);
                        sPrefsEditor.putBoolean("keyBoolAutoSave",true);
                        sPrefsEditor.apply();
                        autoSaveTime = value;
                        //Now update textview
                        autoSaveTv.setText(String.valueOf(value));
                        autoSaveTv2.setVisibility(View.VISIBLE);
                    }
				}
			});
		autoSaveDialog.show(getFragmentManager(),"Autosave Dialog");
	}

	private void esFontSize() {
        int minValue = 4;
        int maxValue = 48;
		MyDialogNumberFragment fSizeDlog = new MyDialogNumberFragment(getApplicationContext(),2);
		fSizeDlog.setDialogTitle("Set font size (4-48)");
		fSizeDlog.setMinDialogValue(minValue);
		fSizeDlog.setMaxDialogValue(maxValue);
        fSizeDlog.setPlaceHolderValue(fontSize);
        fSizeDlog.setDialogPositiveListener(new MyDialogNumberFragment.DialogPositiveListener(){

                @Override
                public void onPositiveListen(int requestCode, int value) {
                    sPrefsEditor.putInt("keyIntFontSize",value);
                    sPrefsEditor.apply();
                    fontSize = value;
                    //Now update textview
                    fontSizeTv.setText(String.valueOf(value));
                }
            });
		fSizeDlog.show(getFragmentManager(),"Font Size");
	}

	private void esScrollbarSize() {
        int minValue = 5;
        int maxValue = 20;
		MyDialogNumberFragment fSizeDlog = new MyDialogNumberFragment(getApplicationContext(),3);
		fSizeDlog.setDialogTitle("Set scrollbar size (5-20)");
		fSizeDlog.setMinDialogValue(minValue);
		fSizeDlog.setMaxDialogValue(maxValue);
        fSizeDlog.setPlaceHolderValue(scrollBarSize);
        fSizeDlog.setDialogPositiveListener(new MyDialogNumberFragment.DialogPositiveListener(){

                @Override
                public void onPositiveListen(int requestCode, int value) {
                    sPrefsEditor.putInt("keyIntScrollBarSize",value);
                    sPrefsEditor.apply();
                    scrollBarSize = value;
                    //Now update textView
                    scrollBTv.setText(String.valueOf(value));
                }
            });
		fSizeDlog.show(getFragmentManager(),"Scrollbar size");
	}

	private void esTabSize() {
        int minValue = 2;
        int maxValue = 20;
		MyDialogNumberFragment tSizeDlog = new MyDialogNumberFragment(getApplicationContext(),4);
		tSizeDlog.setDialogTitle("Set tab size (2-20)");
		tSizeDlog.setMinDialogValue(minValue);
		tSizeDlog.setMaxDialogValue(maxValue);
        tSizeDlog.setPlaceHolderValue(tabSize);
        tSizeDlog.setDialogPositiveListener(new MyDialogNumberFragment.DialogPositiveListener(){

                @Override
                public void onPositiveListen(int requestCode, int value) {
                    sPrefsEditor.putInt("keyIntTabSize",value);
                    sPrefsEditor.apply();
                    tabSize = value;
                    //Now update TextView
                    tabSizeTv.setText(String.valueOf(value));
                }
            });
		tSizeDlog.show(getFragmentManager(),"Tab Size");
	}

	private void activeFilePositionChooser() {
		MyDialogListViewFragment actFilePos = new MyDialogListViewFragment();
		actFilePos.setDialogTitle("Active File");
		actFilePos.setDialogItemId(R.array.active_file);
		actFilePos.setDialogItemClickListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int indexPos) {
					String str1;
					switch (indexPos) {
						case 0:
							sPrefsEditor.putInt("keyIntAFPindex",indexPos);
							sPrefsEditor.apply();
							str1 = afpStrArray[indexPos];
							afpTv.setText(str1);
							break;
						case 1:
							sPrefsEditor.putInt("keyIntAFPindex",indexPos);
							sPrefsEditor.apply();
							str1 = afpStrArray[indexPos];
							afpTv.setText(str1);
							break;
						default:
							Toast.makeText(getApplication(), "Unknown position", Toast.LENGTH_SHORT).show();
					}
				}
			});
		actFilePos.show(getFragmentManager(),"Active files");
	}

	private void esLineHeight() {
        int minValue = 1;
        int maxValue= 100;
		MyDialogDecimalFragment lHeightDlog = new MyDialogDecimalFragment();
		lHeightDlog.setDialogTitle("Set Line Height (0.0 - 100.0)");
		lHeightDlog.setMinDialogValue(minValue);
		lHeightDlog.setMaxDialogValue(maxValue);
        lHeightDlog.setPlaceHolderValue(lineHeight);
        lHeightDlog.setDialogPositiveListener(new MyDialogDecimalFragment.DialogPositiveListener(){

                @Override
                public void onPositiveListen(int requestCode, float value) {
                    sPrefsEditor.putFloat("keyFloatLineHeight",value);
                    sPrefsEditor.apply();
                    lineHeight = value;
                    //Now upate the TextView
                    lineHeightTv.setText(String.valueOf(value));
                }
            });
		lHeightDlog.show(getFragmentManager(),"Line Height");
	}

	private void fontSelector() {
		MyDialogListViewFragment fsDialog = new MyDialogListViewFragment();
		fsDialog.setDialogTitle("Choose Font: ");
		fsDialog.setDialogItemId(R.array.app_font_name_array);
		fsDialog.setDialogItemClickListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int indexPos) {
					String str1;
					switch (indexPos) {
						case 0:
							sPrefsEditor.putInt("keyIntEFindex",indexPos);
							sPrefsEditor.apply();
							str1 = efStrArray[indexPos];
							efTv.setText(str1);
							break;
						case 1:
							sPrefsEditor.putInt("keyIntEFindex",indexPos);
							sPrefsEditor.apply();
							str1 = efStrArray[indexPos];
							efTv.setText(str1);
							break;
						default:
							Toast.makeText(getApplication(), "Unknown Font", Toast.LENGTH_SHORT).show();
					}
				}
			});
		fsDialog.show(getFragmentManager(),"Choose font");
	}

	private void cursCntrlrSize() {
		MyDialogListViewFragment ccSize = new MyDialogListViewFragment();
		ccSize.setDialogTitle("Select cursor size: ");
		ccSize.setDialogItemId(R.array.cursor_controller_size);
		ccSize.setDialogItemClickListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int indexPos) {
					String str1;
					switch (indexPos) {
						case 0:
							sPrefsEditor.putInt("keyIntCCSindex",indexPos);
							sPrefsEditor.apply();
							str1 = ccsStrArray[indexPos];
							ccsTv.setText(str1);
							break;
						case 1:
							sPrefsEditor.putInt("keyIntCCSindex",indexPos);
							sPrefsEditor.apply();
							str1 = ccsStrArray[indexPos];
							ccsTv.setText(str1);
							break;
						case 2:
							sPrefsEditor.putInt("keyIntCCSindex",indexPos);
							sPrefsEditor.apply();
							str1 = ccsStrArray[indexPos];
							ccsTv.setText(str1);
							break;
						default:
							Toast.makeText(getApplication(), "Unknown cursor size", Toast.LENGTH_SHORT).show();
					}
				}
			});
		ccSize.show(getFragmentManager(),"cursor controller size");
	}

	private void beautifyOnSave() {
		MyDialogFragment dlog = new MyDialogFragment();
		dlog.setDialogTitle("Exception Lang (eg. php,py)");
		dlog.show(getFragmentManager(),"Beautify on save dialog");
	}
}
