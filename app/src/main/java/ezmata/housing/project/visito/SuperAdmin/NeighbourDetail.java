package ezmata.housing.project.visito.SuperAdmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ezmata.housing.project.visito.MainActivity;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ezmata.housing.project.visito.R;


public class NeighbourDetail extends AppCompatActivity {

        SharedPreferences sharedPreferences;
        String flatno;
        FirebaseDatabase Database;
        DatabaseReference ref;
        TextView name,name2,phone,add;
        CircleImageView profile;
        String Name,Phone,address,Uri;
        Button btn_close;
        Bundle bundle;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_neighbour_detail);

         flatno=getIntent().getExtras().get("Flat").toString();
    bundle=getIntent().getExtras();
        btn_close=findViewById(R.id.user_close);
          name=findViewById(R.id.user_name);
            name2=findViewById(R.id.user_name2);
            phone=findViewById(R.id.user_mobile);
            add=findViewById(R.id.user_location);
            profile=findViewById(R.id.user_image);
            Firebase.setAndroidContext(this);
            Database=FirebaseDatabase.getInstance();

            ref=Database.getInstance().getReference("Users");
            ref.child(flatno.trim()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Name = dataSnapshot.child("name").getValue(String.class);
                    Phone = dataSnapshot.child("phoneOrder").getValue(String.class);
                    address = dataSnapshot.child("address").getValue(String.class);
                    Uri = dataSnapshot.child("image").getValue(String.class);

                name.setText(Name);
                name2.setText(Name);
                    Picasso.get().load(Uri).into(profile);
                    add.setText(address);
                    phone.setText(Phone);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    if(bundle.getString("temp").equals("1"))
                    startActivity(new Intent(NeighbourDetail.this, MainActivity.class));
                    else if(bundle.getString("temp").equals("2"))
                        startActivity(new Intent(NeighbourDetail.this, SuperAdminMainPage.class));
                }
            });



        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            if(bundle.getString("temp").equals("1"))
                startActivity(new Intent(NeighbourDetail.this, MainActivity.class));
            else if(bundle.getString("temp").equals("2"))
                startActivity(new Intent(NeighbourDetail.this, SuperAdminMainPage.class));

        }
    }

