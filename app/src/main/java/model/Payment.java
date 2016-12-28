package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vinh on 8/15/16.
 */

public class Payment {
    @SerializedName("uID")
    private String uID;
    @SerializedName("pMoney")
    private int pMoney;
    @SerializedName("pShip")
    private int pShip;
    @SerializedName("DateCreat")
    private String dateCreated;

    public Payment(String uID, int pMoney, int pShip, String dateCreated) {
        this.uID = uID;
        this.pMoney = pMoney;
        this.pShip = pShip;
        this.dateCreated = dateCreated;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public int getpMoney() {
        return pMoney;
    }

    public void setpMoney(int pMoney) {
        this.pMoney = pMoney;
    }

    public int getpShip() {
        return pShip;
    }

    public void setpShip(int pShip) {
        this.pShip = pShip;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
