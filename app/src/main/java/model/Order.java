package model;

import java.util.Date;

import utils.StringUtils;


public class Order implements Comparable<Order>{
	private int _ID;

    private int ID;

	private boolean IsLog;

    private String _EndDate;

    private String _Notes;

    private int _Status;

    private String ErrorMessage;

    private int _LadingID;

    private int _OrderID;

    private int OrderID;

    private String _DateCreated;

    private String OldValue;

    private String Noilay;
    private String Noitra;

    private double Prices;
    private double PriceShip;

    private String OrderName;
    private int Status;

    private String Phone;

    private String Address;

    private String EndDate;

    private String NguoiNhan;

    private String Phone_AM;
    private String Notes_AM;

    private String Notes;

    private long dealine = -1;

    public boolean isLog() {
        return IsLog;
    }

    public void setLog(boolean log) {
        IsLog = log;
    }

    public String getNoilay() {
        return Noilay;
    }

    public void setNoilay(String noilay) {
        Noilay = noilay;
    }

    public double getPrices() {
        return Prices;
    }

    public void setPrices(double prices) {
        Prices = prices;
    }

    public double getPriceShip() {
        return PriceShip;
    }

    public void setPriceShip(double priceShip) {
        PriceShip = priceShip;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getNguoiNhan() {
        return NguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        NguoiNhan = nguoiNhan;
    }

    public String getPhone_AM() {
        return Phone_AM;
    }

    public void setPhone_AM(String phone_AM) {
        Phone_AM = phone_AM;
    }

    public String getNotes() {
        if(Notes == null) return "";
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public boolean getIsLog ()
    {
        return IsLog;
    }

    public void setIsLog (boolean IsLog)
    {
        this.IsLog = IsLog;
    }

    public String get_EndDate ()
    {
        return _EndDate;
    }

    public void set_EndDate (String _EndDate)
    {
        this._EndDate = _EndDate;
    }

    public String get_Notes ()
    {
        if(_Notes == null) return "";
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

    public int get_LadingID ()
    {
        return _LadingID;
    }

    public void set_LadingID (int _LadingID)
    {
        this._LadingID = _LadingID;
    }

    public int get_OrderID ()
    {
        return _OrderID;
    }

    public void set_OrderID (int _OrderID)
    {
        this._OrderID = _OrderID;
    }

    public String get_DateCreated ()
    {
        return _DateCreated;
    }

    public void set_DateCreated (String _DateCreated)
    {
        this._DateCreated = _DateCreated;
    }

    public String getOldValue ()
    {
        return OldValue;
    }

    public void setOldValue (String OldValue)
    {
        this.OldValue = OldValue;
    }

    public int get_ID ()
    {
        return _ID;
    }

    public void set_ID (int _ID)
    {
        this._ID = _ID;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [IsLog = "+IsLog+", _EndDate = "+_EndDate+", _Notes = "+_Notes+", _Status = "+_Status+", ErrorMessage = "+ErrorMessage+", _LadingID = "+_LadingID+", _OrderID = "+_OrderID+", _DateCreated = "+_DateCreated+", OldValue = "+OldValue+", _ID = "+_ID+"]";
    }

    public String getNoitra() {
        return Noitra;
    }

    public void setNoitra(String noitra) {
        Noitra = noitra;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public int compareTo(Order order) {
        if(order.Status == 4 && Status == 4){
            if(order.getDealine() > getDealine()){
                return 1;
            }else
                return -1;
        }else if(Status == 4){
            return 1;
        }else if(order.Status == 4){
            return  -1;
        }else {
            if(order.getDealine() > getDealine()){
                return 1;
            }else
                return -1;
        }
    }

    public long getDealine() {
        if(dealine <= 0 && dealine > -2){
            dealine = StringUtils.dateTimeFromText(EndDate);
        }
        dealine = dealine - System.currentTimeMillis();
        return dealine;
    }

    public void setDealine(long dealine) {
        this.dealine = dealine;
    }

    public String getNotes_AM() {
        return Notes_AM;
    }

    public void setNotes_AM(String notes_AM) {
        Notes_AM = notes_AM;
    }
}
