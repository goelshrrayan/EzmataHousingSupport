package ezmata.housing.project.visito.SuperAdmin.NoticeWork;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import ezmata.housing.project.visito.R;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private Context context;
    private List<Notice> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    NoticeAdapter(Context context, List<Notice> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notice_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String title = mData.get(position).getTitle();
        final String date = mData.get(position).getDate();
        holder.titleTextView.setText(title);
        holder.dateTextView.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context.getApplicationContext(),ShowNotice.class);
                intent.putExtra("Title",title);
                intent.putExtra("Date",mData.get(position).getDate());
                intent.putExtra("Content",mData.get(position).getContent());
                context.startActivity(intent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,dateTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_of_notice);
            dateTextView=itemView.findViewById(R.id.date_of_notice);
        }

    }


}
