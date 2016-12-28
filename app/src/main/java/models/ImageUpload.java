package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vinh on 7/21/16.
 */
public class ImageUpload {
    @SerializedName("ImagesURL")
    private String imageUrl;
    @SerializedName("UserID")
    private String userId;
    @SerializedName("Custommers")
    private String customers;
    @SerializedName("DateCreats")
    private String dateCreated;

    public ImageUpload(String imageUrl, String userId, String customers, String dateCreated) {
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.customers = customers;
        this.dateCreated = dateCreated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomers() {
        return customers;
    }

    public void setCustomers(String customers) {
        this.customers = customers;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
