package model;

import android.content.Intent;

import java.util.Collections;
import java.util.List;

import helper.ShipperApplication;
import http.ServerConnector;

import static helper.TrackerService.UPDATE_TASK;

public class Task {

    private int _ID;

	private boolean IsLog;

    private String _LadingName;

    private String _DateUpdate;

    private String _Notes;

    private int _Status;

    private String ErrorMessage;

    private String _DateCreated;

    private int _OrderID;

    private int _LinesID;

    private int _LadingID;

    private String _Active;

    private String OldValue;

    private List<Order> order;

    public boolean getIsLog ()
    {
        return IsLog;
    }

    public void setIsLog (boolean IsLog)
    {
        this.IsLog = IsLog;
    }

    public String get_LadingName ()
    {
        return _LadingName;
    }

    public void set_LadingName (String _LadingName)
    {
        this._LadingName = _LadingName;
    }

    public String get_DateUpdate ()
    {
        return _DateUpdate;
    }

    public void set_DateUpdate (String _DateUpdate)
    {
        this._DateUpdate = _DateUpdate;
    }

    public String get_Notes ()
    {
        return _Notes;
    }

    public void set_Notes (String _Notes)
    {
        this._Notes = _Notes;
    }

    public int get_Status ()
    {
        return _Status;
    }

    public void set_Status (int _Status)
    {
        this._Status = _Status;
    }

    public String getErrorMessage ()
    {
        return ErrorMessage;
    }

    public void setErrorMessage (String ErrorMessage)
    {
        this.ErrorMessage = ErrorMessage;
    }

    public int get_LinesID ()
    {
        return _LinesID;
    }

    public void set_LinesID (int _LinesID)
    {
        this._LinesID = _LinesID;
    }

    public int get_LadingID ()
    {
        return _LadingID;
    }

    public void set_LadingID (int _LadingID)
    {
        this._LadingID = _LadingID;
    }

    public String get_Active ()
    {
        return _Active;
    }

    public void set_Active (String _Active)
    {
        this._Active = _Active;
    }

    public String getOldValue ()
    {
        return OldValue;
    }

    public void setOldValue (String OldValue)
    {
        this.OldValue = OldValue;
    }

    @Override
    public String toString()
    {
        return "ClassTASK [IsLog = "+IsLog+", _LadingName = "+_LadingName+", _DateUpdate = "+_DateUpdate+", _Notes = "+_Notes+", _Status = "+_Status+", ErrorMessage = "+ErrorMessage+", _LinesID = "+_LinesID+", _LadingID = "+_LadingID+", _Active = "+_Active+", OldValue = "+OldValue+"]";
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String get_DateCreated() {
        return _DateCreated;
    }

    public void set_DateCreated(String _DateCreated) {
        this._DateCreated = _DateCreated;
    }

    public int get_OrderID() {
        return _OrderID;
    }

    public void set_OrderID(int _OrderID) {
        this._OrderID = _OrderID;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> mOrder) {
        this.order = mOrder;
    }

    public void getOrderOnline(){
        if(_LadingID >= 0){
            this.order = ServerConnector.getInstance().getOrderOfTask(_LadingID);
            if(this.order != null && this.order.size() >0){
                Collections.sort(this.order);
            }
            ShipperApplication.get().sendBroadcast(new Intent(UPDATE_TASK));
            if(ShipperApplication.mService != null && _LadingID == ShipperApplication.mService.getListTask().get(0).get_LadingID()){
                ShipperApplication.mService.checkNotifi();
            }
        }
    }
}
