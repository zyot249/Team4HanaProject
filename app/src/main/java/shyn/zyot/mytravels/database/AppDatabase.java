package shyn.zyot.mytravels.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import shyn.zyot.mytravels.dao.HotPlaceDao;
import shyn.zyot.mytravels.dao.TravelDao;
import shyn.zyot.mytravels.dao.TravelDiaryDao;
import shyn.zyot.mytravels.dao.TravelExpenseDao;
import shyn.zyot.mytravels.dao.TravelPlanDao;
import shyn.zyot.mytravels.entity.HotPlace;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelDiary;
import shyn.zyot.mytravels.entity.TravelExpense;
import shyn.zyot.mytravels.entity.TravelPlan;

@Database(entities = {Travel.class, TravelPlan.class, TravelDiary.class, TravelExpense.class, HotPlace.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "travel_db")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            /*.addMigrations(MIGRATION_1_2)*/
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TravelDao travelDao();

    public abstract TravelPlanDao travelPlanDao();

    public abstract TravelDiaryDao travelDiaryDao();

    public abstract TravelExpenseDao travelExpenseDao();

    public abstract HotPlaceDao hotPlaceDao();

    /**
     * Add some data when the database is created
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final HotPlaceDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.hotPlaceDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            HotPlace item = new HotPlace();
            int id = 1;
            // Hanoi
            item.setId(id++);
            item.setPlaceName("Hanoi");
            item.setPlaceAddr("Hoàn Kiếm, Hanoi, Vietnam");
            item.setPlaceId("ChIJoRyG2ZurNTERqRfKcnt_iOc");
            item.setPlaceLat(21.0277644);
            item.setPlaceLng(105.8341598);
            mDao.insert(item);
            // Hoàn Kiếm Lake
            item.setId(id++);
            item.setPlaceName("Hoàn Kiếm Lake");
            item.setPlaceAddr("Hoàn Kiếm Lake, Hàng Trống, Hoàn Kiếm, Hanoi, Vietnam");
            item.setPlaceId("ChIJlclXM5WrNTERDqL5tGu_ugE");
            item.setPlaceLat(21.028666899999997);
            item.setPlaceLng(105.85214839999999);
            mDao.insert(item);
            // Ho Chi Minh Mausoleum
            item.setId(id++);
            item.setPlaceName("Ho Chi Minh Mausoleum");
            item.setPlaceAddr("2 Hùng Vương, Điện Bàn, Ba Đình, Hà Nội 100000, Vietnam");
            item.setPlaceId("ChIJF13BXqGrNTERTE3hz8KFDmI");
            item.setPlaceLat(21.036778899999998);
            item.setPlaceLng(105.8346447);
            mDao.insert(item);
            // West Lake
            item.setId(id++);
            item.setPlaceName("West Lake");
            item.setPlaceAddr("West Lake, Tây Hồ, Hanoi, Vietnam");
            item.setPlaceId("ChIJawZgcv6qNTER26OqCYOYLEw");
            item.setPlaceLat(21.053238);
            item.setPlaceLng(105.82609430000001);
            mDao.insert(item);
            // Thăng Long Imperial Citadel
            item.setId(id++);
            item.setPlaceName("Thăng Long Imperial Citadel");
            item.setPlaceAddr("W19C Hoàng Diệu, Quán Thánh, Ba Đình, Hà Nội 100000, Vietnam");
            item.setPlaceId("ChIJSXwdOKOrNTERNylYj9mnIbU");
            item.setPlaceLat(21.0335423);
            item.setPlaceLng(105.83944869999999);
            mDao.insert(item);
            // One Pillar Pagoda
            item.setId(id++);
            item.setPlaceName("One Pillar Pagoda");
            item.setPlaceAddr("Chùa Một Cột, Đội Cấn, Ba Đình, Hà Nội 100000, Vietnam");
            item.setPlaceId("ChIJ7XWEcqGrNTERrsLf6W8259s");
            item.setPlaceLat(21.035856499999998);
            item.setPlaceLng(105.83361839999999);
            mDao.insert(item);
            // Hoa Lo Prison Memorial
            item.setId(id++);
            item.setPlaceName("Hoa Lo Prison Memorial");
            item.setPlaceAddr("1 phố Hoả Lò, Trần Hưng Đạo, Hoàn Kiếm, Hà Nội 100000, Vietnam");
            item.setPlaceId("ChIJld5RqparNTERVK8x7gvhAZc");
            item.setPlaceLat(21.0254177);
            item.setPlaceLng(105.8465933);
            mDao.insert(item);
            // Bảo Tàng Hồ Chí Minh
            item.setId(id++);
            item.setPlaceName("Ho Chi Minh Museum");
            item.setPlaceAddr("19 Ngách 158/19 Ngọc Hà, Đội Cấn, Ba Đình, Hà Nội 100000, Vietnam");
            item.setPlaceId("CChIJb-sdLqCrNTER0UkLBD-JnZQ");
            item.setPlaceLat(21.0370118);
            item.setPlaceLng(105.8297788);
            mDao.insert(item);
            return null;
        }
    }
}
