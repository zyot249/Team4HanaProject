package shyn.zyot.mytravels.entity;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
import shyn.zyot.mytravels.utils.MyDate;

@Entity(tableName = "travel")
//@Fts4
public class Travel extends TravelBaseEntity {

    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "rowid")
    private long id;
    private String endDt;
    private String imgUri;

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getImgUri() {
        return imgUri;
    }

    public Travel(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }



    public void setId(long id) {
        this.id = id;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(long endDt) {
        this.endDt = MyDate.getString(endDt);
    }

    public long getEndDtLong() {
        return MyDate.getTime(endDt);
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    /**
     * Gets the string expression of the end date.
     *
     * @return string in yyyy-MM-dd format
     */
    public String getEndDtText() {
        return MyDate.getDateString(endDt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Travel)) return false;
        if (!super.equals(o)) return false;
        Travel travel = (Travel) o;
        return id == travel.id &&
                Objects.equals(endDt, travel.endDt) &&
                Objects.equals(imgUri, travel.imgUri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, endDt, imgUri);
    }

    @Override
    public String toString() {
        return "Travel{" +
                "id=" + id +
                ", endDt='" + endDt + '\'' +
                ", imgUri='" + imgUri + '\'' +
                ", dateTime='" + dateTime + '\'' +
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
