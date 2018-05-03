package obj;

import com.google.gson.Gson;

public class Customer {
    String sdt;


    String name;
    boolean isOnline;

    double lat;
    double lng;

    public Customer(String sdt, double lat, double lng) {
        this.sdt = sdt;
        this.lat = lat;
        this.lng = lng;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Customer(String sdt, boolean isOnline) {
        this.sdt = sdt;
        this.isOnline = isOnline;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Customer(String name, String sdt) {

        this.name = name;
        this.sdt = sdt;
    }

}
