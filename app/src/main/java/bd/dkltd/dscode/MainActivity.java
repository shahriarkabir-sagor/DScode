package bd.dkltd.dscode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import bd.dkltd.dscode.myfragments.MyDialogFragment;

public class MainActivity extends AppCompatActivity {
	
	private Toolbar toolbar;
	private DrawerLayout dl;
	private String[] runTimePerm = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.MANAGE_EXTERNAL_STORAGE"};
	private String path;
	private boolean exitBool;
	private SharedPreferences sPrefs;
	private SharedPreferences.Editor sPrefsEditor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
        //Initiate
		toolbar = findViewById(R.id.toolbar);
		dl = findViewById(R.id.mainDrawerLayout1);

		//Toolbar
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Toggle icon
		ActionBarDrawerToggle toggleIcon = new ActionBarDrawerToggle(this,dl,toolbar,R.string.app_name,R.string.app_name);
		dl.setDrawerListener(toggleIcon);
		toggleIcon.syncState();

		//code engine
		WebView mWebView = findViewById(R.id.wvId);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//New method
		final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
			.addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
			.addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(this))
			.build();
		mWebView.setWebViewClient(new LocalContentWebViewClient(assetLoader)); //calling private class LocalContentWebViewClient
		mWebView.loadUrl("https://appassets.androidplatform.net/assets/index.html");
		/*
		//Old method, new method works with https means better security
		mWebView.loadUrl("file:///android_asset/index.html");
		*/
		mWebView.requestFocusFromTouch();
        
		//initiate spref
		sPrefs = getSharedPreferences("AppSettings",Context.MODE_PRIVATE);
		sPrefsEditor = sPrefs.edit();
		
		//Get value from sPrefs
		path = sPrefs.getString("keyInternalPath",null);
		exitBool = sPrefs.getBoolean("keyBoolExit",false);
		
		//Permissions Manager
		if (!checkPerms()) {
			//Request for permission
			managePerm();
		} else if (checkPerms()) {
			//Permissions given
			savePath();
		}
    }

	private void savePath() {
		if (path == null){
			path = Environment.getExternalStorageDirectory().getPath();
			//Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
			sPrefsEditor.putString("keyInternalPath",path);
			sPrefsEditor.apply();
		}
	}

	private boolean checkPerms() {
		int res = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (res == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			return false;
		}
	}

	private void managePerm() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(runTimePerm,75);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.overflow_menu, menu);
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 75) {
			if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
				Toast.makeText(getApplication(), "write external permission denied", Toast.LENGTH_SHORT).show();
			} else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				savePath();
			}
			
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.editMenuId:
				return true;
			case R.id.filePropId:
				Toast.makeText(getApplicationContext(),"Show File properties",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.renameId:
				fileCreate();
				return true;
			case R.id.cutId:
				Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.copyId:
				Toast.makeText(getApplicationContext(),"4",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.pasteId:
				Toast.makeText(getApplicationContext(),"5",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.selectAllId:
				Toast.makeText(getApplicationContext(),"6",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.insertColorId:
				Toast.makeText(getApplicationContext(),"7",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.formatId:
				Toast.makeText(getApplicationContext(),"8",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.syntaxHighLightId:
				Toast.makeText(getApplicationContext(),"9",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.textEncodeId:
				Toast.makeText(getApplicationContext(),"10",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.readOnlyId:
				Toast.makeText(getApplicationContext(),"11",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.searchId:
				Toast.makeText(getApplicationContext(),"12",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.gotoLineId:
				Toast.makeText(getApplicationContext(),"13",Toast.LENGTH_SHORT).show();
				return true;
			//secondary overflow menu
			case R.id.newfileId:
				fileCreate();
				return true;
			case R.id.saveId:
				Toast.makeText(getApplicationContext(),"15",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.saveAsId:
				Toast.makeText(getApplicationContext(),"16",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.filesId:
				Intent intnt17 = new Intent(getApplicationContext(),FilesActivity.class);
				startActivity(intnt17);
				return true;
			case R.id.recentFileId:
				Toast.makeText(getApplicationContext(),"18",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.consoleId:
				Toast.makeText(getApplicationContext(),"19",Toast.LENGTH_SHORT).show();
				return true;
			case R.id.gitId:
				Intent i20 = new Intent(MainActivity.this,RemoteRepoActivity.class);
				startActivity(i20);
				return true;
			case R.id.settingsId:
				Intent i21 = new Intent(MainActivity.this,SettingsActivity.class);
				startActivity(i21);
				return true;
			case R.id.helpId:
				Intent i22 = new Intent(MainActivity.this,HelpActivity.class);
				startActivity(i22);
				return true;
			case R.id.exitId:
				Toast.makeText(getApplicationContext(),"App Closed",Toast.LENGTH_SHORT).show();
				//super.onBackPressed();
				finishAffinity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void fileCreate() {
		MyDialogFragment dlog = new MyDialogFragment();
		dlog.setDialogTitle("Create New File: ");
		dlog.setDialogPositiveText("Create");
		dlog.show(getFragmentManager(),"create new file");
	}
	
	public void onNavImageClick(View v) {
		switch (v.getId()) {
			case R.id.fileImgView:
				Intent intnt17 = new Intent(getApplicationContext(),FilesActivity.class);
				startActivity(intnt17);
				break;
			default:
				
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		exitBool = sPrefs.getBoolean("keyBoolExit",false);
	}
	
	

	@Override
	public void onBackPressed() {
		getSupportFragmentManager().popBackStackImmediate();
		if (dl.isDrawerOpen(GravityCompat.START)) {
			dl.closeDrawer(GravityCompat.START);
		} else if(exitBool){
			AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Exit?")
				.setMessage("Do you want to exit? Unsaved file may be dismissed.")
				.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dia, int which) {
						finishAffinity();
                    }
				})
				.setNegativeButton("Cancel", null)
				.create();
			dialog.show();
		} else {
			super.onBackPressed();
		}
	}
	private static class LocalContentWebViewClient extends WebViewClientCompat {
		private final WebViewAssetLoader mAssetLoader; //this should be final
		
		LocalContentWebViewClient(WebViewAssetLoader assetLoader){
			mAssetLoader = assetLoader;
		}

		@Override
		@RequiresApi(21)
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
			return mAssetLoader.shouldInterceptRequest(request.getUrl());
		}

		@Override
		@SuppressWarnings("deprecation") //to support api < 21
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			return mAssetLoader.shouldInterceptRequest(Uri.parse(url));
		}
		
	}
}
