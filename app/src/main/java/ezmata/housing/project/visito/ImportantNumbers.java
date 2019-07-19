package ezmata.housing.project.visito;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImportantNumbers extends AppCompatActivity {
FirebaseDatabase database;
DatabaseReference myRef;
List<String> list;
ListView listView;
ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_numbers);
        Firebase.setAndroidContext(this);
        database=FirebaseDatabase.getInstance();
        list=new ArrayList<String>();
        listView=findViewById(R.id.lis_imp_numbers);

        Log.i("Important Numbers","LLLLLLLL");

        myRef=  database.getInstance().getReference("ImportantNumbers");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for(DataSnapshot ds:dataSnapshot.getChildren())
                     {
                         Log.i("asdfgh",ds.getKey()+" "+ds.getValue());
                         String detail=ds.getKey()+": "+ds.getValue();
                         list.add(detail);
                     }
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(ImportantNumbers.this,android.R.layout.simple_list_item_1
                                ,list );
                     listView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(ImportantNumbers.this,MainActivity.class));
    }
}
