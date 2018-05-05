package obj;

import com.google.gson.Gson;

import java.io.Serializable;

public class Book implements Serializable {
    String phoneCutomer;
    String phoneDriver;

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public String getNameTo() {
        return nameTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }

    public Book(String phoneCutomer, double latFrom, double latTo, double lngFrom, double lngTo, String nameFrom, String nameTo, int cost, double km) {

        this.phoneCutomer = phoneCutomer;
        this.latFrom = latFrom;
        this.latTo = latTo;
        this.lngFrom = lngFrom;
        this.lngTo = lngTo;
        this.nameFrom = nameFrom;
        this.nameTo = nameTo;
        this.cost = cost;
        this.km = km;
    }

    double latFrom;
    double latTo;
    double lngFrom;
    double lngTo;
    String nameFrom;
    String nameTo;
    int cost;
    Driver driver;
    Customer customer;
    double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPhoneDriver() {
        return phoneDriver;
    }

    public void setPhoneDriver(String phoneDriver) {
        this.phoneDriver = phoneDriver;
    }

    public String getPhoneCutomer() {
        return phoneCutomer;
    }

    public void setPhoneCutomer(String phoneCutomer) {
        this.phoneCutomer = phoneCutomer;
    }

    public double getLatFrom() {
        return latFrom;
    }

    public void setLatFrom(double latFrom) {
        this.latFrom = latFrom;
    }

    public double getLatTo() {
        return latTo;
    }

    public void setLatTo(double latTo) {
        this.latTo = latTo;
    }

    public double getLngFrom() {
        return lngFrom;
    }

    public void setLngFrom(double lngFrom) {
        this.lngFrom = lngFrom;
    }

    public double getLngTo() {
        return lngTo;
    }

    public void setLngTo(double lngTo) {
        this.lngTo = lngTo;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }


    double km;


    public String toJSON() {
        return new Gson().toJson(this);
    }

    public static Book fromJSON(String json) {
        return new Gson().fromJson(json, Book.class);
    }

}
