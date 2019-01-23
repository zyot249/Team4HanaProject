package shyn.zyot.mytravels;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Team2setImageAdapter  extends  RecyclerView.Adapter<Team2setImageAdapter.ViewHolder>{

    ArrayList<DataImage> dataImages;
    Context context;

    public Team2setImageAdapter(ArrayList<DataImage> dataImages, Context context) {
        this.dataImages = dataImages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.team2_gridview,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivSetImage.setImageURI(Uri.parse(dataImages.get(position).getUri()));
    }

    @Override
    public int getItemCount() {
        return dataImages.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView ivSetImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSetImage = itemView.findViewById(R.id.ivSetImage);
            ivSetImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveItem(getAdapterPosition());
                }
            });

        }
    }
    public void RemoveItem(int position){
        dataImages.remove(position);
        notifyItemRemoved(position);
    }
}
