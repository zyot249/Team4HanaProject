package shyn.zyot.mytravels.entity;

import java.util.Objects;

import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "travel_diary"
        , foreignKeys = @ForeignKey(
        entity = Travel.class
        , parentColumns = "id"
        , childColumns = "travelId"
        , onDelete = ForeignKey.CASCADE
)
        , indices = {
        @Index("travelId")
}
)
public class TravelDiary extends TravelBaseEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long travelId;
    private String imgUri;
    private String thumbUri;


    public static DiffUtil.ItemCallback<TravelDiary> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TravelDiary>() {
                // Item details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(TravelDiary oldItem, TravelDiary newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(TravelDiary oldItem,
                                                  TravelDiary newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelDiary)) return false;
        if (!super.equals(o)) return false;
        TravelDiary that = (TravelDiary) o;
        return id == that.id &&
                travelId == that.travelId &&
                Objects.equals(imgUri, that.imgUri) &&
                Objects.equals(thumbUri, that.thumbUri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, travelId, imgUri, thumbUri);
    }

    @Override
    public String toString() {
        return "TravelDiary{" +
                "id=" + id +
                ", travelId=" + travelId +
                ", imgUri='" + imgUri + '\'' +
                ", thumbUri='" + thumbUri + '\'' +
                ", dateTime=" + dateTime +
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
                '}';
    }
}
