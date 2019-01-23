package shyn.zyot.mytravels;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;


public class Team2GalleryActivity extends AppCompatActivity {
    Button btnInsert,btnCheckLog;
    ImageView ivSetImagea;

    public static int i = 0;
    String [] imgUri = new String[1005];
    RecyclerView RVSetImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team2_gallery);

        RecyclerView RVSetImage= findViewById(R.id.RVSetImage);
        Team2SQLHelper sqLiteOpenHelper = new Team2SQLHelper(getBaseContext()) ;
        sqLiteOpenHelper.getAll(imgUri);

        while(imgUri[i] != null ){
            i++;
        }

        RVSetImage.setHasFixedSize(true);
        RVSetImage.setLayoutManager(new GridLayoutManager(this, 3));
        ArrayList<DataImage> arrayList = new ArrayList<>();
        int k = 0;
        final String [] getImgUri = new String[i];
        while (k<i){
            getImgUri[k] = imgUri[k];
            arrayList.add(new DataImage(getImgUri[k]));
            k++;
        }
        Team2setImageAdapter team2setImageAdapter = new Team2setImageAdapter(arrayList,getApplicationContext());
        RVSetImage.setAdapter(team2setImageAdapter);
    }
}
