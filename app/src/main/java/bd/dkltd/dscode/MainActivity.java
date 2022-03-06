package bd.dkltd.dscode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import bd.dkltd.dscode.myfragments.MyDialogFragment;
import java.util.ArrayList;
import androidx.appcompat.widget.SearchView;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private DrawerLayout dl;
	private String[] runTimePerm = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.MANAGE_EXTERNAL_STORAGE"};
	private String path;
	private boolean exitBool;
    private MyDbHelper opened_storage_db;
    private Cursor rowList;
    private RecyclerView rcv2;
    private LinearLayout ll;
    private ArrayList<SrcPaths> listOfDir;
    private OpenedDirAdapter oda;
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
		ActionBarDrawerToggle toggleIcon = new ActionBarDrawerToggle(this, dl, toolbar, R.string.app_name, R.string.app_name);
		dl.setDrawerListener(toggleIcon);
		toggleIcon.syncState();

        //Init recyclerview and No open file layout
        rcv2 = findViewById(R.id.activitymainRecyclerView1);
        ll = findViewById(R.id.activitymainLinearLayout2);

        // Check any open file and treat according to it
        checkOpenFile();

        // -------------------------------
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
        // ----------------------------------

		//initiate shared preferences
		sPrefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
		sPrefsEditor = sPrefs.edit();

		//Get value from sPrefs
		path = sPrefs.getString("keyInternalPath", null);
		exitBool = sPrefs.getBoolean("keyBoolExit", false);

		//Permissions Manager
		if (!checkPerms()) {
			//Request for permission
			managePerm();
		} else if (checkPerms()) {
			//Permissions given save the path
			savePath();
		}
    }

    private void checkOpenFile() {
        if (anyFileOpened()) {
            // Turn off No open file Layout 
            visibleNoFileViews(false);
            // fetch list 
            fetchOpenedFile();
            //Display in recyclerview
            displayToRecyclerView();
        } else {
            //Turn on No open file Layout
            visibleNoFileViews(true);
        }
    }

    private void fetchOpenedFile() {
        //check again if rowlist is not empty
        if (rowList.getCount() > 0) {
            listOfDir = new ArrayList<SrcPaths>();
            //now run a loop to fetch them and save in a array
            while (rowList.moveToNext()) {
                String dirName = rowList.getString(0);
                String dirPath = rowList.getString(1);
                SrcPaths eachPath = new SrcPaths(dirName, dirPath);
                listOfDir.add(eachPath);
            }
        }
    }

    private void displayToRecyclerView() {

        rcv2.setHasFixedSize(true);
        rcv2.setLayoutManager(new LinearLayoutManager(this));
        oda = new OpenedDirAdapter(this, listOfDir); //pass activity(not context) and array of files
        rcv2.setAdapter(oda);
        oda.setOnOptionClickListener(new OpenedDirAdapter.OptionClickListener() {

                @Override
                public void onAddIconClick(int position, View v) {
                    //show popup menu here
                    PopupMenu popupMenuOptions = new PopupMenu(getApplicationContext(), v);
                    popupMenuOptions.inflate(R.menu.popup_menu_one);
                    popupMenuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.ppmRename:
                                        return true;
                                    case R.id.ppmPaste:
                                        return true;
                                    case R.id.ppmNewFile:
                                        return true;
                                    case R.id.ppmNewFolder:
                                        return true;
                                    case R.id.ppmInsFile:
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                    popupMenuOptions.show();
                }

                @Override
                public void onInfoIconClick(int position, View v) {
                }

                @Override
                public void onCrossIconClick(int position, View v) {
                    String tempPath = listOfDir.get(position).getPathSource();
                    int affectedRow = opened_storage_db.deleteRowFrom("Opened_directory", "directory_path", tempPath);
                    if (affectedRow > 0) {
                        Toast.makeText(getApplicationContext(), "Row successfully deleted", Toast.LENGTH_SHORT).show();
                        listOfDir.remove(position);
                        oda.notifyItemRemoved(position);
                        if (listOfDir.size() <= 0) {
                            visibleNoFileViews(true);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private boolean anyFileOpened() {
        // instance of MyDbHelper
        opened_storage_db = new MyDbHelper(this);
        rowList = opened_storage_db.fetchRowFrom("Opened_directory");
        if (rowList.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void visibleNoFileViews(boolean shouldShow) {
        if (shouldShow) {
            rcv2.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
        } else {
            ll.setVisibility(View.GONE);
            rcv2.setVisibility(View.VISIBLE);
        }
    }

	private void savePath() {
		if (path == null) {
			path = Environment.getExternalStorageDirectory().getPath();
			//Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
			sPrefsEditor.putString("keyInternalPath", path);
			sPrefsEditor.apply();
		}
	}

	private boolean checkPerms() {
		int res = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (res == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			return false;
		}
	}

	private void managePerm() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(runTimePerm, 75);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.overflow_menu, menu);
        SearchView sv = (SearchView) menu.findItem(R.id.searchId).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String p1) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String p1) {
                    return false;
                }
            });

        return super.onCreateOptionsMenu(menu);
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
		switch (item.getItemId()) {
                //primary edit menu
			case R.id.editMenuId:
				return true;
			case R.id.filePropId:
				Toast.makeText(getApplicationContext(), "Show File properties", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.renameId:
				fileCreate();
				return true;
			case R.id.cutId:
				Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.copyId:
				Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.pasteId:
				Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.selectAllId:
				Toast.makeText(getApplicationContext(), "6", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.insertColorId:
				Toast.makeText(getApplicationContext(), "7", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.formatId:
				Toast.makeText(getApplicationContext(), "8", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.syntaxHighLightId:
				Toast.makeText(getApplicationContext(), "9", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.textEncodeId:
				Toast.makeText(getApplicationContext(), "10", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.readOnlyId:
				Toast.makeText(getApplicationContext(), "11", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.gotoLineId:
				Toast.makeText(getApplicationContext(), "13", Toast.LENGTH_SHORT).show();
				return true;
                //secondary overflow menu
			case R.id.newfileId:
				fileCreate();
				return true;
			case R.id.saveId:
				Toast.makeText(getApplicationContext(), "15", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.saveAsId:
				Toast.makeText(getApplicationContext(), "16", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.filesId:
				Intent intnt17 = new Intent(getApplicationContext(), FilesActivity.class);
				startActivity(intnt17);
				return true;
			case R.id.recentFileId:
				Toast.makeText(getApplicationContext(), "18", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.consoleId:
				Toast.makeText(getApplicationContext(), "19", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.gitId:
				startActivity(new Intent(this, RemoteRepoActivity.class));
				return true;
			case R.id.settingsId:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			case R.id.helpId:
				startActivity(new Intent(this, HelpActivity.class));
				return true;
			case R.id.exitId:
				Toast.makeText(getApplicationContext(), "App Closed", Toast.LENGTH_SHORT).show();
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
		dlog.show(getFragmentManager(), "create new file");
	}

	public void onNavImageClick(View v) {
		switch (v.getId()) {
			case R.id.fileImgView:
                onBackPressed();
				Intent intnt17 = new Intent(getApplicationContext(), FilesActivity.class);
				startActivity(intnt17);
				break;
			default:
				onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		exitBool = sPrefs.getBoolean("keyBoolExit", false);
        checkOpenFile();
	}



	@Override
	public void onBackPressed() {
		getSupportFragmentManager().popBackStackImmediate();
		if (dl.isDrawerOpen(GravityCompat.START)) {
			dl.closeDrawer(GravityCompat.START);
		} else if (exitBool) {
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

		LocalContentWebViewClient(WebViewAssetLoader assetLoader) {
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
