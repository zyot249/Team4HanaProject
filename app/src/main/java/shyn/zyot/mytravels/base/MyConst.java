package shyn.zyot.mytravels.base;

import java.util.HashMap;
import java.util.Map;

public class MyConst {

    public static final int REQCD_SEARCH_TRAVELS_BY_CITY = 3066 ;
    public static final int REQCD_PLACE_AUTOCOMPLETE = 1000;
    public static final int REQCD_PLACE_PICKER = 1001;
    public static final int REQCD_TRAVEL_ADD = 2000;
    public static final int REQCD_TRAVEL_EDIT = 2001;
    public static final int REQCD_IMAGE_CAMERA = 3000;
    public static final int REQCD_IMAGE_GALLERY = 3001;
    public static final int REQCD_IMAGE_CROP = 3002;
    public static final int REQCD_ACCESS_GALLERY = 9000;
    public static final int REQCD_ACCESS_CAMERA = 9001;
    public static final int REQCD_ACCESS_FINE_LOCATION = 9002;
    public static final String REQKEY_TRAVEL = "REQKEY_TRAVEL";
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_DATE = "KEY_DATE";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_SUBTITLE = "KEY_SUBTITLE";
    public static final String KEY_DESC = "KEY_DESC";
    public static final String REQKEY_TRAVEL_ID = "REQKEY_TRAVEL_ID";
    public static final String REQACTION_EDIT_TRAVEL = "REQACTION_EDIT_TRAVEL";
    public static final String REQACTION_DEL_TRAVEL = "REQACTION_DEL_TRAVEL";
    public static final String THUMBNAIL_PREFIX = "thumb";
    public static final int THUMBNAIL_SIZE = 135;
    // Request for lock
    public static final int REQCD_SETPASS = 6000;
    public static final int REQCD_CONFIRMPASS = 6001;

    public static final String TRAVEL_LIST_SIZE = "TRAVEL_LIST_SIZE";

    public static final Map<String, MyKeyValue> CURRENCY_CODE = new HashMap<>();
    public static final Map<String, MyKeyValue> BUDGET_CODE = new HashMap<>();

    public static MyKeyValue getCurrencyCode(String key) {
        if (CURRENCY_CODE.containsKey(key)) return CURRENCY_CODE.get(key);
        return new MyKeyValue(0, "NA", "NA");
    }

    public static MyKeyValue getBudgetCode(String key) {
        if (BUDGET_CODE.containsKey(key)) return BUDGET_CODE.get(key);
        return new MyKeyValue(0, "NA", "NA");
    }
}
