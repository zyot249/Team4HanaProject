package shyn.zyot.mytravels.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import shyn.zyot.mytravels.entity.HotPlace;

@Dao
public interface HotPlaceDao {

    // The Integer type parameter tells Room to use a PositionalDataSource object,
    // with position-based loading under the hood.
    @Query("SELECT * from hot_place")
    DataSource.Factory<Integer, HotPlace> getAllHotPlaces();

    @Query("SELECT * from hot_place where dateTime is null or dateTime !=:dateTime limit 1")
    LiveData<HotPlace> geHotPlaceToDownload(String dateTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HotPlace... items);

    @Update
    void update(HotPlace... items);

    @Delete
    void delete(HotPlace... items);
}
