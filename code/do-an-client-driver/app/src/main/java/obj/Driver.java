package obj;

import com.google.gson.Gson;

public class Driver {
    String phoneNumber;
    String name;
    String inforXe;
    String imChanDung;
    String imCMT1;
    String imCMT2;
    String imBLX1;
    String imBLX2;
    String imGTX1;
    String imGTX2;
    String imBaoHiem1;
    String imBaoHiem2;
    String rateNumber1;
    String rateStar;

    public String getInforXe() {
        return inforXe;
    }

    public void setInforXe(String inforXe) {
        this.inforXe = inforXe;
    }

    public Driver(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImChanDung() {
        return imChanDung;
    }

    public void setImChanDung(String imChanDung) {
        this.imChanDung = imChanDung;
    }

    public Driver(String phoneNumber, String name, String inforXe, String imChanDung, String imCMT1, String imCMT2, String imBLX1, String imBLX2, String imGTX1, String imGTX2, String imBaoHiem1, String imBaoHiem2) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.inforXe = inforXe;
        this.imChanDung = imChanDung;
        this.imCMT1 = imCMT1;
        this.imCMT2 = imCMT2;
        this.imBLX1 = imBLX1;
        this.imBLX2 = imBLX2;
        this.imGTX1 = imGTX1;
        this.imGTX2 = imGTX2;
        this.imBaoHiem1 = imBaoHiem1;
        this.imBaoHiem2 = imBaoHiem2;
    }

    public String getImCMT1() {
        return imCMT1;
    }

    public void setImCMT1(String imCMT1) {
        this.imCMT1 = imCMT1;
    }

    public String getImCMT2() {
        return imCMT2;
    }

    public void setImCMT2(String imCMT2) {
        this.imCMT2 = imCMT2;
    }

    public String getImBLX1() {
        return imBLX1;
    }

    public void setImBLX1(String imBLX1) {
        this.imBLX1 = imBLX1;
    }

    public String getImBLX2() {
        return imBLX2;
    }

    public void setImBLX2(String imBLX2) {
        this.imBLX2 = imBLX2;
    }

    public String getImGTX1() {
        return imGTX1;
    }

    public void setImGTX1(String imGTX1) {
        this.imGTX1 = imGTX1;
    }

    public String getImGTX2() {
        return imGTX2;
    }

    public void setImGTX2(String imGTX2) {
        this.imGTX2 = imGTX2;
    }

    public String getImBaoHiem1() {
        return imBaoHiem1;
    }

    public void setImBaoHiem1(String imBaoHiem1) {
        this.imBaoHiem1 = imBaoHiem1;
    }

    public String getImBaoHiem2() {
        return imBaoHiem2;
    }

    public void setImBaoHiem2(String imBaoHiem2) {
        this.imBaoHiem2 = imBaoHiem2;
    }

    public String getRateNumber1() {
        return rateNumber1;
    }

    public void setRateNumber1(String rateNumber1) {
        this.rateNumber1 = rateNumber1;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public static Driver fromJSON(String json) {
        return new Gson().fromJson(json, Driver.class);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRateStar() {
        return rateStar;
    }

    public void setRateStar(String rateStar) {
        this.rateStar = rateStar;
    }
}
