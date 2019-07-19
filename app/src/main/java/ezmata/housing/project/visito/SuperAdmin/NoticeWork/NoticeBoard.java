package ezmata.housing.project.visito.SuperAdmin.NoticeWork;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ezmata.housing.project.visito.R;

public class NoticeBoard extends AppCompatActivity {
FloatingActionButton fab;
NoticeAdapter noticeAdapter;
RecyclerView recyclerView;
FirebaseDatabase database;
DatabaseReference databaseReference;
ArrayList<Notice> noticesList;
String title,content,date;

Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        recyclerView = findViewById(R.id.list_of_notices_rv);
        noticesList = new ArrayList<>();
        bundle=getIntent().getExtras();
        fab=findViewById(R.id.fab_add_notice);
        if(bundle.getString("parent").equals("MainActivity"))
        {fab.hide();}
        if(bundle.getString("parent").equals("SuperAdminMainPage"))
        {   fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NoticeBoard.this, AddNotice.class);
                startActivity(i);
            }
        });}

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Notices");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticesList.clear();
               for(DataSnapshot ds:dataSnapshot.getChildren())
               {
                   Notice notice = ds.getValue(Notice.class);
                   title = notice.getTitle();
                    content = notice.getContent();
                    date = notice.getDate();
                    noticesList.add(notice);

               }

                recyclerView.setLayoutManager(new LinearLayoutManager(NoticeBoard.this));
                noticeAdapter = new NoticeAdapter(NoticeBoard.this, noticesList);
                recyclerView.setAdapter(noticeAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(NoticeBoard.this,
                        DividerItemDecoration.VERTICAL));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });














    }
}
