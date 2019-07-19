package ezmata.housing.project.visito.SuperAdmin;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ezmata.housing.project.visito.R;

public class AllotFlats extends AppCompatActivity {

    FloatingActionButton addFlatFab;
    FirebaseFirestore db,db2;
    int flag;
    String flatnoadd,emailadd;
    RecyclerView recyclerView;
    ArrayList<AllotList> allotlists;
    AllotFlatAdapter allotFlatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allot_flats);
        recyclerView=findViewById(R.id.alloted_flat_rv);
        db = FirebaseFirestore.getInstance();
        db2=FirebaseFirestore.getInstance();

        addFlatFab = findViewById(R.id.fab_allot_flat);
        allotlists=new ArrayList<>();

        db2.collection("Residents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allotlists.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                emailadd="";
                                flatnoadd="";
                                emailadd= document.getString("gmail");
                                flatnoadd= document.getString("flat_no");
                                Log.i("email,flat:",emailadd+" "+flatnoadd);
                                if(emailadd==null||flatnoadd==null)
                                {continue;}
                                else {
                                    AllotList allotList = new AllotList(emailadd, flatnoadd);
                                    allotlists.add(allotList);
                                }

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(AllotFlats.this));
                            allotFlatAdapter = new AllotFlatAdapter(AllotFlats.this, allotlists);
                            recyclerView.setAdapter(allotFlatAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(AllotFlats.this,
                                    DividerItemDecoration.VERTICAL));
                        }
                    }
                });

        addFlatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AllotFlats.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.activity_allot_flat);
                // Set dialog title
                dialog.setTitle("Allot Flat");
                flag=0;

                Button add_allot = dialog.findViewById(R.id.addFlatAllot);

                add_allot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // set values for custom dialog components - text, image and button
                        final EditText user_gmail = dialog.findViewById(R.id.flat_gmail);
                        final EditText flat_number = dialog.findViewById(R.id.flat_no);

                        final String gmail = user_gmail.getText().toString().trim();
                        final String flat_num = flat_number.getText().toString().trim();



                        db.collection("Residents")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                      if(gmail.trim().equals(document.getString("gmail"))||
                                                              flat_num.trim().equals(document.getString("flat_no"))) {
                                                          Log.i("inside", document.getId() + " => " + document.get("gmail"));
                                                          Toast.makeText(AllotFlats.this, "Redundant Id or Flat", Toast.LENGTH_SHORT).show();
                                                         flag=1;
                                                         Log.i("Flag1",flag+"");
                                                          break;
                                                      }
                                            }

                                            if(flag!=1){
                                                AllotList list = new AllotList(gmail, flat_num);
                                                if(flag!=1) {
                                                    Log.i("Flag3",flag+"");
                                                    db.collection("Residents")
                                                            .add(list)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    if(flag!=1) {
                                                                        Log.i("Flag2", flag + "");
                                                                        Toast.makeText(AllotFlats.this, "Alloted Successfully", Toast.LENGTH_SHORT).show();
                                                                        Log.i("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                                                                    }
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("TAG", "Error adding document", e);
                                                                }
                                                            });

                                                    dialog.dismiss();
                                                }
                                            }

                                        } else {
                                            Log.i("TAG", "Error getting documents.", task.getException());
                                        }
                                    }
                                });


                            }


                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });




    }
}
