package obj;

import com.google.gson.Gson;

public class Customer {
    String name;

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

    String sdt;
}
