package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vinh on 7/6/16.
 */

public class StatusOrder {
    @SerializedName("Status")
    private int status;
    @SerializedName("oStatus")
    private int oStatus;
    @SerializedName("Notes")
    private String note;

    public StatusOrder(int status, String note) {
        this.status = status;
        this.oStatus = status;
        if(this.status <= 3)
            this.oStatus = 2;
//        if(this.status == 2)
//            oStatus = 2;
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getoStatus() {
        return oStatus;
    }

    public void setoStatus(int oStatus) {
        this.oStatus = oStatus;
    }
}
