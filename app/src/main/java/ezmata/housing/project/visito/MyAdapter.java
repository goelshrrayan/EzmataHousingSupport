package ezmata.housing.project.visito;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import ezmata.housing.project.visito.Admin.Visitors_admin;


import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public List<Visitors_admin> list;
    public Context mContext;


    public MyAdapter(List<Visitors_admin> list)
    {
        this.list = list;

    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visitor_list_item,viewGroup,false);
        mContext=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.allow=list.get(i).getAllowance();


            viewHolder.name.setText(list.get(i).getVisitorName());
            viewHolder.reason.setText(list.get(i).getVisitorReason());
            viewHolder.phone.setText(list.get(i).getVisitorPhone());
            viewHolder.time.setText(list.get(i).getdati());
            Picasso.get().load(list.get(i).getImageUri()).into(viewHolder.visitorImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("kkk:",i+"");
                Activity activity=(Activity) mContext;
                final Dialog dialog = new Dialog(activity);
                // Include dialog.xml file

                // Set dialog title
                Rect displayRectangle = new Rect();
                Window window = activity.getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

// inflate and adjust layout
                LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.details, null);
                layout.setMinimumWidth((int)(displayRectangle.width() * 0.8f));
                layout.setMinimumHeight((int)(displayRectangle.height() * 0.8f));
                dialog.setContentView(layout);

                // set values for custom dialog components - text, image and button
                ImageView imageView=dialog.findViewById(R.id.details_image);
                TextView name=dialog.findViewById(R.id.visitor_name_details);
                TextView reason=dialog.findViewById(R.id.visitor_reason_details);
                TextView phone=dialog.findViewById(R.id.visitor_phone_details);
                TextView date=dialog.findViewById(R.id.visitor_date_details);


                Picasso.get().load(list.get(i).getImageUri()).into(imageView);
                name.setText(list.get(i).getVisitorName());
                reason.setText(list.get(i).getVisitorReason());
                phone.setText(list.get(i).getVisitorPhone());
                date.setText(list.get(i).getdati());






                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name,reason,phone,time;
        private ImageView visitorImage;
        private View mView;
        private String allow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name=mView.findViewById(R.id.visitor_name);
            reason = mView.findViewById(R.id.visitor_reason);
            phone = mView.findViewById(R.id.visitor_phone);
            time = mView.findViewById(R.id.visitor_time);
            visitorImage = mView.findViewById(R.id.visitor_image);
        }



    }
}
