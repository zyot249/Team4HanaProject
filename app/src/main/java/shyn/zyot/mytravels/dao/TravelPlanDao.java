package shyn.zyot.mytravels.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import shyn.zyot.mytravels.entity.TravelPlan;

@Dao
public interface TravelPlanDao {

    // The Integer type parameter tells Room to use a PositionalDataSource object,
    // with position-based loading under the hood.
    @Query("SELECT * from travel_plan WHERE deleteYn=0 and travelId=:travelId ORDER BY dateTime DESC, id DESC")
    DataSource.Factory<Integer, TravelPlan> getAllPlansOfTravel(long travelId);

    @Query("SELECT * from travel_plan WHERE deleteYn=0 and travelId=:travelId and substr(dateTime, 1, 8) = substr(:dateTime, 1, 8) ORDER BY dateTime DESC, id DESC")
    DataSource.Factory<Integer, TravelPlan> getPlansOnDate(long travelId, String dateTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TravelPlan... items);

    @Update
    void update(TravelPlan... items);

    @Delete
    void delete(TravelPlan... items);

    @Query("DELETE FROM travel_plan WHERE deleteYn=1 and travelId=:travelId")
    void deleteAllMarkedYes(long travelId);

    @Query("UPDATE travel_plan SET deleteYn=0 WHERE deleteYn=1 and travelId=:travelId")
    void undeleteAllMarkedYes(long travelId);

}
