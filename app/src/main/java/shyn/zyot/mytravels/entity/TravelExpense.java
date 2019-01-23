package shyn.zyot.mytravels.entity;

import java.util.Objects;

import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import shyn.zyot.mytravels.utils.MyString;

@Entity(tableName = "travel_expense"
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
public class TravelExpense extends TravelBaseEntity {
    public static DiffUtil.ItemCallback<TravelExpense> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TravelExpense>() {
                // Item details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(TravelExpense oldItem, TravelExpense newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(TravelExpense oldItem,
                                                  TravelExpense newItem) {
                    return oldItem.equals(newItem);
                }
            };
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long travelId;
    private String type;
    private double amount;
    private String currency;

    public double getAmount() {
        return amount;
    }

    public String getAmountText() {
        return MyString.getMoneyText(amount);
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

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

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelExpense)) return false;
        if (!super.equals(o)) return false;
        TravelExpense that = (TravelExpense) o;
        return id == that.id &&
                travelId == that.travelId &&
                Double.compare(that.amount, amount) == 0 &&
                Objects.equals(type, that.type) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, travelId, type, amount, currency);
    }

    @Override
    public String toString() {
        return "TravelExpense{" +
                "id=" + id +
                ", travelId=" + travelId +
                ", type='" + type + '\'' +
                ", currency='" + currency + '\'' +
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
