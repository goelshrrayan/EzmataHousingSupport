package ezmata.housing.project.visito.SuperAdmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import ezmata.housing.project.visito.R;

public class AllotFlatAdapter extends RecyclerView.Adapter<AllotFlatAdapter.ViewHolder> {
private Context context;
private List<AllotList> mData;
private LayoutInflater mInflater;

        // data is passed into the constructor
       public AllotFlatAdapter(Context context, List<AllotList> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        }

        public AllotFlatAdapter()
        {}

// inflates the row layout from xml when needed
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.allot_list_item_view, parent, false);
        return new ViewHolder(view);
        }

// binds the data to the TextView in each row
@Override
public void onBindViewHolder(ViewHolder holder, final int position) {
final String mail = mData.get(position).getGmail();
final String houseno = mData.get(position).getFlat_no();
        holder.emailTextView.setText(mail);
        holder.flatTextView.setText(houseno);


        }

// total number of rows
@Override
public int getItemCount() {
        return mData.size();
        }


// stores and recycles views as they are scrolled off screen
public class ViewHolder extends RecyclerView.ViewHolder {
    TextView emailTextView,flatTextView;

    ViewHolder(View itemView) {
        super(itemView);
        emailTextView = itemView.findViewById(R.id.email_alloted_flat);
        flatTextView=itemView.findViewById(R.id.flatno_alloted_flat);
    }

}


}