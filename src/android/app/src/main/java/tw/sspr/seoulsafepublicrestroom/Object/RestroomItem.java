package tw.sspr.seoulsafepublicrestroom.Object;

import java.io.Serializable;

public class RestroomItem implements Serializable{
    private String id;
    private String name;
    private double lat;
    private double lng;
    private int near;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getNear() {
        return near;
    }

    public void setNear(int near) {
        this.near = near;
    }
}
