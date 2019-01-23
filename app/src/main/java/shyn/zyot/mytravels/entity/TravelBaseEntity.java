package shyn.zyot.mytravels.entity;

import java.io.Serializable;
import java.util.Objects;

import shyn.zyot.mytravels.utils.MyDate;

public class TravelBaseEntity implements Serializable {
    String dateTime;
    String title;
    String desc;
    String placeId;
    String placeName;
    String placeAddr;
    double placeLat;
    double placeLng;
    double southwestLat;
    double southwestLng;
    double northeastLat;
    double northeastLng;
    boolean deleteYn;

    public boolean isDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(boolean deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = MyDate.getString(dateTime);
    }

    public long getDateTimeLong() {
        return MyDate.getTime(dateTime);
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Gets the string expression of dateTime.
     *
     * @return string in yyyy-MM-dd format.
     */
    public String getDateTimeText() {
        return MyDate.getDateString(dateTime);
    }

    /**
     * Gets the string expression of dateTime.
     *
     * @return string in yyyy-MM-dd HH:mm format.
     */
    public String getDateTimeMinText() {
        return MyDate.getDateTimeMinString(dateTime);
    }

    public String getDateTimeHourMinText() {
        return MyDate.getTimeMinString(dateTime);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddr() {
        return placeAddr;
    }

    public void setPlaceAddr(String placeAddr) {
        this.placeAddr = placeAddr;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(double placeLat) {
        this.placeLat = placeLat;
    }

    public double getPlaceLng() {
        return placeLng;
    }

    public void setPlaceLng(double placeLng) {
        this.placeLng = placeLng;
    }

    public double getSouthwestLat() {
        return southwestLat;
    }

    public void setSouthwestLat(double southwestLat) {
        this.southwestLat = southwestLat;
    }

    public double getSouthwestLng() {
        return southwestLng;
    }

    public void setSouthwestLng(double southwestLng) {
        this.southwestLng = southwestLng;
    }

    public double getNortheastLat() {
        return northeastLat;
    }

    public void setNortheastLat(double northeastLat) {
        this.northeastLat = northeastLat;
    }

    public double getNortheastLng() {
        return northeastLng;
    }

    public void setNortheastLng(double northeastLng) {
        this.northeastLng = northeastLng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelBaseEntity)) return false;
        TravelBaseEntity that = (TravelBaseEntity) o;
        return Double.compare(that.placeLat, placeLat) == 0 &&
                Double.compare(that.placeLng, placeLng) == 0 &&
                Double.compare(that.southwestLat, southwestLat) == 0 &&
                Double.compare(that.southwestLng, southwestLng) == 0 &&
                Double.compare(that.northeastLat, northeastLat) == 0 &&
                Double.compare(that.northeastLng, northeastLng) == 0 &&
                deleteYn == that.deleteYn &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(title, that.title) &&
                Objects.equals(desc, that.desc) &&
                Objects.equals(placeId, that.placeId) &&
                Objects.equals(placeName, that.placeName) &&
                Objects.equals(placeAddr, that.placeAddr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dateTime, title, desc, placeId, placeName, placeAddr, placeLat, placeLng, southwestLat, southwestLng, northeastLat, northeastLng, deleteYn);
    }

    @Override
    public String toString() {
        return "TravelBaseEntity{" +
                "dateTime='" + dateTime + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", placeId='" + placeId + '\'' +
                ", placeName='" + placeName + '\'' +
                ", placeAddr='" + placeAddr + '\'' +
                ", placeLat=" + placeLat +
                ", placeLng=" + placeLng +
                ", southwestLat=" + southwestLat +
                ", southwestLng=" + southwestLng +
                ", northeastLat=" + northeastLat +
                ", northeastLng=" + northeastLng +
                ", deleteYn=" + deleteYn +
                '}';
    }
}
