package bd.dkltd.dscode;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import android.content.Context;

public class SettingsActivity extends AppCompatActivity 
{

	private String telegramGroupId = "doctype_studio";
	private String instaId = "es_sagor2";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onSettingItemClick(View v) {
		switch (v.getId()) {
			case R.id.aboutItemId:
				Intent i1 = new Intent(SettingsActivity.this,AboutActivity.class);
				startActivity(i1);
				break;
			case R.id.backRestItemId:
				Intent i2 = new Intent(SettingsActivity.this,BackRestActivity.class);
				startActivity(i2);
				break;
			case R.id.esItemId:
				Intent i3 = new Intent(SettingsActivity.this,EditorSettings.class);
				startActivity(i3);
				break;
			case R.id.osItemId:
				Intent i4 = new Intent(SettingsActivity.this,OtherSettingsActivity.class);
				startActivity(i4);
				break;
			default:
			Toast.makeText(getApplicationContext(),"Under development", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onSocialItemClick(View v) {
		switch (v.getId()) {
			case R.id.instaId:
				startActivity(instaIntn(getApplicationContext()));
				break;
			case R.id.tgId:
				startActivity(telegramIntn(getApplicationContext()));
				break;
			case R.id.ghId:
				openGithub();
				break;
			default: Toast.makeText(getApplication(), "No familiar social handle found", Toast.LENGTH_SHORT).show();
		}
	}
	private Intent instaIntn(Context context) {
		Intent i1;
		
		String appResolver = "instagram://user?username=";
		String webResolver = "https://instagram.com/";
		String instaPackageName = "com.instagram.android";
		String instaLitePackName = "com.instagram.lite";
		
		try {
			context.getPackageManager().getPackageInfo(instaPackageName, 0);
			i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(appResolver+instaId));
		} catch (PackageManager.NameNotFoundException e1) {
			Toast.makeText(getApplication(), "Instagram not found", Toast.LENGTH_SHORT).show();
			try {
				context.getPackageManager().getPackageInfo(instaLitePackName, 0);
				i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(appResolver+instaId));
			} catch (PackageManager.NameNotFoundException e2) {
				Toast.makeText(getApplication(), "Instagram and instagram lite not found", Toast.LENGTH_SHORT).show();
				i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(webResolver+instaId));
			}
		}
		return i1;
	}
	private Intent telegramIntn(Context context) {
		Intent i1;
		try {
			context.getPackageManager().getPackageInfo("org.telegram.messenger", 0);
			i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain="+telegramGroupId));
		} catch (PackageManager.NameNotFoundException e1) {
			Toast.makeText(getApplication(), "Telegram Messenger not found", Toast.LENGTH_SHORT).show();
			try {
				context.getPackageManager().getPackageInfo("org.thunderdog.challegram", 0);
				i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain="+telegramGroupId));
			} catch (PackageManager.NameNotFoundException e2) {
				Toast.makeText(getApplication(), "Telegram X app not found", Toast.LENGTH_SHORT).show();
				i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/"+telegramGroupId));
			}
		}
		return i1;
	}
	private void openGithub() {
		String ghub = "https://github.com/";
		String ghId = "azclub-ltd/DScode";
		Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse(ghub+ghId));
		startActivity(i3);
	}
	
}
