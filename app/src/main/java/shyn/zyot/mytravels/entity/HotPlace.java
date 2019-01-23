package shyn.zyot.mytravels.entity;

import java.util.Objects;

import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hot_place")
public class HotPlace extends TravelBaseEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String imgUri;


    public static DiffUtil.ItemCallback<HotPlace> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<HotPlace>() {
                // Item details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(HotPlace oldItem, HotPlace newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(HotPlace oldItem,
                                                  HotPlace newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotPlace)) return false;
        if (!super.equals(o)) return false;
        HotPlace hotPlace = (HotPlace) o;
        return id == hotPlace.id &&
                Objects.equals(imgUri, hotPlace.imgUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, imgUri);
    }

    @Override
    public String toString() {
        return "HotPlace{" +
                "id=" + id +
                ", imgUri='" + imgUri + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", desc='" + desc + '\'' +
                ", placeId='" + placeId + '\'' +
                ", placeLat=" + placeLat +
                ", placeLng=" + placeLng +
                '}';
    }
}
