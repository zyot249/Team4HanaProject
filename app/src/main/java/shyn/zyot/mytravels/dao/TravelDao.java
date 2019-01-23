package shyn.zyot.mytravels.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import shyn.zyot.mytravels.entity.Travel;

@Dao
public interface TravelDao {

    @Query("SELECT * FROM travel WHERE placeName MATCH :placeName")
    LiveData<List<Travel>> getAllTravelsByTypingCity(String placeName);

    @Query("SELECT * from travel ORDER BY id DESC")
    LiveData<List<Travel>> getAllTravels();

    @Query("SELECT * from travel ORDER BY title")
    LiveData<List<Travel>> getAllTravelsByTitleAsc();

    @Query("SELECT * from travel ORDER BY title DESC")
    LiveData<List<Travel>> getAllTravelsByTitleDesc();

    @Query("SELECT * from travel ORDER BY dateTime")
    LiveData<List<Travel>> getAllTravelsByStartAsc();

    @Query("SELECT * from travel ORDER BY dateTime DESC")
    LiveData<List<Travel>> getAllTravelsByStartDesc();

    @Insert
    void insert(Travel travel);

    @Update
    void update(Travel travel);

    @Delete
    void delete(Travel... travels);

    @Query("SELECT * FROM travel where id=:id")
    LiveData<Travel> getTravelById(long id);

    ///// Phung
    @Query("SELECT * from travel ORDER BY dateTime")
    List<Travel> getAllTravelsByStartDateAsc();
    /////

}
