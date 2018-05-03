package app;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by D on 4/17/2018.
 */

public class Config {
    //    public static final String SERVER_URL = "https://driper.herokuapp.com/";
    public static final String SERVER_URL = "http://192.168.1.109:81/";
    public static final String ACCOUNT_LOGIN = "ACCOUNT_CUSTOMER_LOGIN";
    public static final String MY_CACHE = "MY_CACHE";
    public static final String ACCOUNT_PHONE_NUMBER = "ACCOUNT_CUSTOMER_PHONE_NUMBER";
    public static final String ACCOUNT_NAME = "ACCOUNT_CUSTOMER_NAME";
    public static final String EMIT_REQUEST_LOGIN = "EMIT_CUSTOMER_REQUEST_LOGIN";
    public static final String ON_REQUEST_LOGIN = "ON_CUSTOMER_REQUEST_LOGIN";

    public static final String CLIENT_CUSTEMER_CONNECT = "CLIENT_CUSTOMER_CONNECT_SUCCES";

    public static final String Vefify_Phonenumber = "Vefify_Phonenumber";
    public static final int RESULT_CODE_TAI_XE = 1070;
    public static final String BOOK_KEY = "BOOK_KEY";
    public static final String NEW_OK_BOOK = "NEW_OK_BOOK";
    public static String Vefify_Code_Res = "Vefify_Code_Res";
    public static String Custemer_Login = "Custemer_Login";
    public static String Custemer_Login_Res = "Custemer_Login_Res";
    public static String Custemer_Check_Login = "Custemer_Check_Login";
    public static String Custemer_Check_Login_Res = "Custemer_Check_Login_Res";
    public static String Custemer_View_Profile = "Custemer_View_Profile";
    public static String Custemer_View_Profile_Res = "Custemer_View_Profile_Res";
    public static String Custemer_Update_Profile_Res = "Custemer_Update_Profile_Res";
    public static String Custemer_Update_Profile = "Custemer_Update_Profile";
    public static String updateStateOnlineProfile = "updateStateOnlineProfile";
    public static String updateCustomLocation = "updateCustomLocation";
    public static String findDriperInLocation_res = "findDriperInLocation_res";
    public static String findDriperInLocation = "findDriperInLocation";
    public static String bookFindDriver_res = "bookFindDriver_res";
    public static String bookFindDriver = "bookFindDriver";
    public static final String NOTIFY = "notify";

    public static String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            String resp = formatter.format(number);
            resp = resp.replaceAll(",", ".");
            return resp;
        } catch (Exception e) {
            return "";
        }
    }
}
