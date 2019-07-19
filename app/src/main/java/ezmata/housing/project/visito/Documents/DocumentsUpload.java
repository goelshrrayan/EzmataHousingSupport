package ezmata.housing.project.visito.Documents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import ezmata.housing.project.visito.R;
import ezmata.housing.project.visito.SuperAdmin.SuperAdminMainPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DocumentsUpload extends AppCompatActivity {
TextView tv1,tv2,tv3;
FirebaseDatabase database;
DatabaseReference reference;
    Intent i;
    RecyclerView recyclerView;
    Bundle bundle;
    FloatingActionButton floatingActionButton;
    List<Document> myListData;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_upload);
        recyclerView = findViewById(R.id.documents_titles_rv);
        myListData = new ArrayList<>();
            bundle=getIntent().getExtras();

    database=FirebaseDatabase.getInstance();
    reference=database.getReference("Documents");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           myListData.clear();
            for(DataSnapshot ds:dataSnapshot.getChildren())
            {
                Document document=ds.getValue(Document.class);
                myListData.add(0,document);
            }

            DocumentListAdapter adapter = new DocumentListAdapter(myListData);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(DocumentsUpload.this));
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(DocumentsUpload.this,
                    DividerItemDecoration.VERTICAL));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    });



    floatingActionButton=(FloatingActionButton) findViewById(R.id.fab_add_document);
        bundle=getIntent().getExtras();
         i=new Intent(DocumentsUpload.this,UploadedDocuments.class);
         i.putExtra("parent","DocumentsUpload");

         floatingActionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(i);
             }
         });


    if(bundle.getString("parent").equals("MainActivity"))
        floatingActionButton.hide();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(bundle.getString("parent").equals("UploadedDocuments"))
        {startActivity(new Intent(DocumentsUpload.this, SuperAdminMainPage.class));}

    }


}
