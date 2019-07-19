package ezmata.housing.project.visito.SuperAdmin.NoticeWork;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ezmata.housing.project.visito.R;

public class AddNotice extends AppCompatActivity {
EditText title,content;
TextView date;
FirebaseDatabase database;
DatabaseReference reference;
Button add;
String Content,Title,Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        date=findViewById(R.id.Notice_Date);
        title=findViewById(R.id.NoticeTitle);
        content=findViewById(R.id.NoticeContent);
        add=findViewById(R.id.AddNoticeBtn);



        database=FirebaseDatabase.getInstance();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        final String dates = df.format(Calendar.getInstance().getTime());
        date.setText(dates);

        Date=dates;


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().equals("")||content.getText().toString().equals(""))
                {
                    Toast.makeText(AddNotice.this, "Complete the details.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Content=content.getText().toString();
                    Title=title.getText().toString();
                 reference=database.getReference("Notices");
                 Notice notice=new Notice(Content,Title,Date);
                 reference.child(Title).setValue(notice).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(AddNotice.this,"Notice Sent!",Toast.LENGTH_SHORT).show();

                     }
                 });;

                }

            }
        });
    }
}
