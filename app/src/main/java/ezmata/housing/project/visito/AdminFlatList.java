package ezmata.housing.project.visito;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ezmata.housing.project.visito.AccountActivity.VisitorList;
import ezmata.housing.project.visito.Admin.AdminActivity;



import ezmata.housing.project.visito.SuperAdmin.NeighbourDetail;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminFlatList extends AppCompatActivity {
    RecyclerView recyclerView;
    HorizontalAdapter horizontalAdapter;
    private List<Flat> data;
    int temp=0;
    String parent;
    Flat flat;
    FirebaseDatabase Database;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flat_list);
        Firebase.setAndroidContext(this);
        Database=FirebaseDatabase.getInstance();
        flat=new Flat();
        fill_with_data();
Bundle bundle=getIntent().getExtras();


if(bundle!=null)
{parent=bundle.getString("parent");

            if (parent.equals("AdminActivity"))
                temp = 0;
            else if (parent.equals("MainActivity"))
                temp = 1;
            else if(parent.equals("SuperAdminMainPage"))
                temp=2;
}



//        horizontal_recycler_view.setLayoutManager(layoutManager);

    }

    private void showSociety() {


    }

    public void fill_with_data() {



        recyclerView=  findViewById(R.id.Recycler_View);
        //LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AdminFlatList.this, LinearLayoutManager.HORIZONTAL, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(AdminFlatList.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref=Database.getInstance().getReference("Visitors_admin");
        final List<Flat> data = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    flat =new Flat(ds.getKey());
                    Log.i("mDta",flat.getTxt()+"");
                    if (flat.getTxt() != null)
                        data.add(0,flat);
                }

                horizontalAdapter=new HorizontalAdapter(data, getApplication());
recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(horizontalAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(AdminFlatList.this,
                        DividerItemDecoration.VERTICAL));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<Flat> horizontalList;
        Context context;


        public HorizontalAdapter(List<Flat> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {


            TextView txtview;
            public MyViewHolder(View view) {
                super(view);

                txtview=(TextView) view.findViewById(R.id.txtview);
            }
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            holder.txtview.setText(horizontalList.get(position).txt);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(temp==0) {
                        Intent i = new Intent(v.getContext(), VisitorList.class);

                        i.putExtra("parent", "AdminFlatList");
                        i.putExtra("Flat",horizontalList.get(position).txt);
                        Log.i("text", horizontalList.get(position).txt.toString());
                        v.getContext().startActivity(i);
                    }
                    else if(temp==1||temp==2)
                    {
                        Intent i = new Intent(v.getContext(), NeighbourDetail.class);
                        i.putExtra("parent", "AdminFlatList");
                        i.putExtra("temp",temp+"");
                       i.putExtra("Flat",horizontalList.get(position).txt);
                        Log.i("text", horizontalList.get(position).txt.toString());
                        v.getContext().startActivity(i);

                    }
                }
            });



        }


        @Override
        public int getItemCount()
        {
            return horizontalList.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (temp==0)
        startActivity(new Intent(AdminFlatList.this, AdminActivity.class));
        else if(temp==1)
            startActivity(new Intent(AdminFlatList.this, MainActivity.class));
    }
}

