package app;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by D on 4/17/2018.
 */

public class Config {
    //    public static final String SERVER_URL = "https://driper.herokuapp.com/";
    public static final String SERVER_URL = "http://192.168.1.109:81/";
    public static final String NEW_OK_BOOK = "NEW_OK_BOOK_DRIVER";
    public static final String NEW_BOOK = "NEW_BOOK";
    public static final String ACCOUNT_LOGIN = "ACCOUNT_CUSTOMER_LOGIN";
    public static final String MY_CACHE = "MY_CACHE";
    public static final String ACCOUNT_PHONE_NUMBER = "ACCOUNT_CUSTOMER_PHONE_NUMBER";
    public static final String ACCOUNT_NAME = "ACCOUNT_CUSTOMER_NAME";
    public static final String EMIT_REQUEST_LOGIN = "EMIT_CUSTOMER_REQUEST_LOGIN";
    public static final String ON_REQUEST_LOGIN = "ON_CUSTOMER_REQUEST_LOGIN";


    public static final String CLIENT_CUSTEMER_CONNECT = "CLIENT_CUSTOMER_CONNECT_SUCCES";


    public static final String DRIPER_PHONE_NUMBER = "DRIPER_PHONE_NUMBER";
    public static final String NOTIFY = "notify";

    public static String Custemer_Check_Login = "Custemer_Check_Login";
    public static String Custemer_Check_Login_Res = "Custemer_Check_Login_Res";


    public static String checkDriverIsExists_Res = "checkDriverIsExists_Res";
    public static String checkDriverIsExists = "checkDriverIsExists";
    public static String Driver_Regitor_Res = "Driver_Regitor_Res";
    public static String Driver_Regitor = "Driver_Regitor";
    public static String Driper_Vefify_Code_Res = "Driper_Vefify_Code_Res";
    public static String Driper_Vefify_Phonenumber = "Driper_Vefify_Phonenumber";
    public static String getDriverProfile_Res = "getDriverProfile_Res";
    public static String getDriverProfile = "getDriverProfile";
    public static String Driver_Update_Location = "Driver_Update_Location";
    public static String Driver_Update_State = "Driver_Update_State";
    public static String Driver_Update_Degrees = "Driver_Update_Degrees";
    public static String getCustomerOnline_Res = "getCustomerOnline_Res";
    public static String getCustomerOnline = "getCustomerOnline";
    public static String huyCuoc = "huyCuoc";
    public static String tuChoiCuoc = "tuChoiCuoc";
    public static String chapNhanCuoc = "chapNhanCuoc";

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
