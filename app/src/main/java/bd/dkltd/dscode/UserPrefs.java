package bd.dkltd.dscode;

import android.app.Application;

public class UserPrefs extends Application {
    
    private static final String PREFERENCES = "preferences";
	// Do something
    private static final String CUSTOM_THEME = "customTheme";
	private static final String LIGHT_THEME = "lightTheme";
	private static final String DARK_THEME = "darkTheme";
	
	private String customTheme;


	public void setCustomTheme(String customTheme) {
		this.customTheme = customTheme;
	}

	public String getCustomTheme() {
		return customTheme;
	}
}
