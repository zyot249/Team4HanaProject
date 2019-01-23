package shyn.zyot.mytravels;

import android.app.Application;

import org.junit.Test;

import java.util.List;

import androidx.lifecycle.LiveData;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.repository.TravelRepository;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private TravelRepository travelDao = TravelRepository.getInstance(new Application());

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getTRavel() {
         travelDao= TravelRepository.getInstance(new Application());
        LiveData<List<Travel>> travels = travelDao.getAllTravels();
        Travel travel = travels.getValue().get(0);
        System.out.println("travel name="+travel.getPlaceName()+"placeId="+travel.getPlaceId());
    }

    @Test
    public void insertTravel() {
        travelDao= TravelRepository.getInstance(new Application());

//        travelDao.insert(new Travel());
    }
}