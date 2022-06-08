package animals.service;

import java.util.ResourceBundle;

public class ResourceBundleService {
    static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    public static String getLocalString(String str){
        return resourceBundle.getString(str);
    }
}

