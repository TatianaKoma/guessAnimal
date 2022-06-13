package animals.service;

import java.util.ResourceBundle;

public class ResourceBundleService {
    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
    public static final String PATTERN_YES = getLocalString("pattern.yes");
    public static final String PATTERN_NO = getLocalString("pattern.no");

    public static String getLocalString(String str) {
        return resourceBundle.getString(str);
    }
}

