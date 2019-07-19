package ezmata.housing.project.visito.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ezmata.housing.project.visito.Admin.Visitors_admin;
import ezmata.housing.project.visito.MyAdapter;
import ezmata.housing.project.visito.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcceptedFragment extends Fragment  {


    public AcceptedFragment() {
        // Required empty public constructor
    }
    private List<Visitors_admin> list;
    Visitors_admin temperoray;
    RecyclerView recyclerView;
    DatabaseReference myRef;
    FirebaseDatabase database;
    MyAdapter mAdapter;
    int allow=0;
    String flat;
    int decision=1;
    SharedPreferences sharedPreferences;
    FirebaseStorage storage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView= inflater.inflate(R.layout.fragment_accepted, container, false);



        sharedPreferences=container.getContext().getSharedPreferences("Details", Context.MODE_PRIVATE);
        flat=sharedPreferences.getString("Flat","w102");
        list=new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.visitor_recyclerView);
        storage = FirebaseStorage.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        // 4. set adapter

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        database = FirebaseDatabase.getInstance();
            flat=getArguments().getString("msg");

        myRef = database.getReference().child("Visitors_admin");
        myRef.child(flat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                String allowance=ds.child("allowance").getValue(String.class);
                    Log.i("dataSnapshot",ds+"");
                    Visitors_admin visitors = ds.getValue(Visitors_admin.class);
                    if(allowance.equals("accepted"))
                    list.add(0,visitors);

//                    String name = visitors.getName();
//                    String reason = visitors.getReason();
//                    String phone = visitors.getPhone();
//                    String dati = visitors.getDati();
//                    String imageUrl = visitors.getImageUri();
//                    Visitors newvisitor = new Visitors(name,reason,phone,dati,imageUrl);
//                    AllotList.add(newvisitor);
//                    mAdapter.notifyDataSetChanged();
                }


                mAdapter = new MyAdapter(list);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 5. set item animator to DefaultAnimator




        return rootView;
    }

    public void fetchDatabaseImage(String flat)
  {
//        StorageReference storageReference = storage.getReference(flat);
//        storageReference.child(fileName).get
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        String URL = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//
////                        DatabaseReference reference = database.getReference(flat.getText().toString().trim());
//
//
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                {
//                    Toast.makeText(getApplicationContext(), "File not uploaded successfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                progressDialog.setProgress(currentProgress);
//            }
//        });


    }



    public static AcceptedFragment newInstance(String text) {

        AcceptedFragment f = new AcceptedFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);


        f.setArguments(b);

        return f;
    }

}
