package ezmata.housing.project.visito.Documents;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ezmata.housing.project.visito.R;

public class UploadedDocuments extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ImageView btn_add_attachment;
    EditText name;
    String flatno;
    String URL;
    Bundle bundle;
    Button btn_send_attachment,btn_remove_attachment;
    private static final int PICKFILE_RESULT_CODE = 1;
    Uri File;
    LinearLayout linearLayoutattach,linearLayoutsend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded);
            linearLayoutattach=findViewById(R.id.attach_linearlayout);
            linearLayoutsend=findViewById(R.id.send_linearlayout);
            bundle=getIntent().getExtras();
        name=findViewById(R.id.doc_name);
        btn_add_attachment=findViewById(R.id.btn_addAttachment);
    btn_send_attachment=findViewById(R.id.uploadattachment);
    btn_remove_attachment=findViewById(R.id.btn_remove_attachment);
        storage=FirebaseStorage.getInstance();

flatno=bundle.getString("Flat");
    btn_remove_attachment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File=null;
            linearLayoutsend.setVisibility(View.GONE);
            linearLayoutattach.setVisibility(View.VISIBLE);
        }
    });

        btn_add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadedDocuments.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");

                    startActivityForResult(intent, PICKFILE_RESULT_CODE);
                } else {
                    ActivityCompat.requestPermissions(UploadedDocuments.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });


        btn_send_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals(""))
                {
                    Toast.makeText(UploadedDocuments.this, "Add name first", Toast.LENGTH_SHORT).show();
                }
                else
                    uploadFile(File);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    File = data.getData();
                    linearLayoutsend.setVisibility(View.VISIBLE);
                    linearLayoutattach.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),  "file Attached" + "  " + FilePath, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void uploadFile(final Uri file) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setProgress(0);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Documents");




        final String fileName = name.getText().toString();

        StorageReference storageReference = storage.getReference("SuperAdmin");
        storageReference.child(fileName).putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        URL = uri.toString();
                                        //createNewPost(imageUrl);
                                        Log.i("URL IS", URL + "");
                                        Document document=new Document(URL,name.getText().toString().trim());
                                        databaseReference.child(fileName).setValue(document)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(UploadedDocuments.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent i=new Intent(UploadedDocuments.this,DocumentsUpload.class);
                                                        i.putExtra("parent","UploadedDocuments");
                                                        startActivity(i);
                                                    }
                                                });
                                        // sendNotificationToUser("updates", flatno+","+id , "Master.Java");




                                    }
                                });
                            }
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                {
                    Toast.makeText(getApplicationContext(), "File not uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }

}
