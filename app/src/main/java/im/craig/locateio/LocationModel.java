package im.craig.locateio;


import com.google.gson.annotations.SerializedName;

public class LocationModel {
    @SerializedName("locationID")
    public String mlocationID;
    @SerializedName("username")
    public String musername;
    @SerializedName("title")
    public String mtitle;
    @SerializedName("description")
    public String mdescription;
    @SerializedName("extraInfo")
    public String mextraInfo;
    @SerializedName("lat")
    public String mlat;
    @SerializedName("lng")
    public String mlng;
    @SerializedName("rating")
    public String mrating;
    @SerializedName("posted")
    public String mposted;

}