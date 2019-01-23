package shyn.zyot.mytravels.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import shyn.zyot.mytravels.entity.TravelExpense;

@Dao
public interface TravelExpenseDao {

    // The Integer type parameter tells Room to use a PositionalDataSource object,
    // with position-based loading under the hood.
    @Query("SELECT * from travel_expense WHERE travelId=:travelId ORDER BY dateTime DESC, id DESC")
    DataSource.Factory<Integer, TravelExpense> getAllExpensesOfTravel(long travelId);

    @Query("SELECT * from travel_expense WHERE deleteYn=0 and travelId=:travelId and substr(dateTime, 1, 8) = substr(:dateTime, 1, 8) ORDER BY dateTime DESC, id DESC")
    DataSource.Factory<Integer, TravelExpense> getExpensesOnDate(long travelId, String dateTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TravelExpense... items);

    @Update
    void update(TravelExpense... items);

    @Delete
    void delete(TravelExpense... items);

    @Query("DELETE FROM travel_expense WHERE deleteYn=1 and travelId=:travelId")
    void deleteAllMarkedYes(long travelId);

    @Query("UPDATE travel_expense SET deleteYn=0 WHERE deleteYn=1 and travelId=:travelId")
    void undeleteAllMarkedYes(long travelId);

    @Query("select count(*) as id" +
            ", travelId" +
            ", `dateTime`" +
            ", title" +
            ", `desc`" +
            ", placeId" +
            ", placeName" +
            ", placeAddr" +
            ", (select sum(amount) from travel_expense where travelId=te.travelId and currency=te.currency and type='BUD') as placeLat" +
            ", placeLng" +
            ", type" +
            ", (select sum(amount) from travel_expense where travelId=te.travelId and currency=te.currency and type='EXP') as amount" +
            ", currency " +
            ", southwestLat " +
            ", southwestLng " +
            ", northeastLat " +
            ", northeastLng " +
            ", deleteYn " +
            "from travel_expense  te " +
            "where travelId=:travelId group by currency order by id desc, amount desc")
    LiveData<List<TravelExpense>> getBudgetStatus(long travelId);

    @Query("SELECT * from travel_expense WHERE travelId=:travelId ORDER BY dateTime DESC, id DESC limit 1")
    LiveData<TravelExpense> getRecentItem(long travelId);

}
