package net.agusharyanto.objectcollector;

/**
 * Created by agus on 10/15/15.
 */
public class Hotel {
    private String id="";
    private String name="";
    private String total_room="";
    private String address="";
    private String foto="";
    private String latitude="";
    private String longitude="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_room() {
        return total_room;
    }

    public void setTotal_room(String total_room) {
        this.total_room = total_room;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
