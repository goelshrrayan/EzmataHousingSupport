package ezmata.housing.project.visito.Admin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import ezmata.housing.project.visito.AccountActivity.LoginActivity;
import ezmata.housing.project.visito.AdminFlatList;


import com.firebase.client.Firebase;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ezmata.housing.project.visito.R;
import id.zelory.compressor.Compressor;

public class AdminActivity extends AppCompatActivity {

    //FCM notification
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AIzaSyCmBcPE7-pnEjH0A_nfzbWyR85MlBQcAUo";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private EditText name,phone,flat,reason;
    DatabaseReference databaseReference;
    int c=0;
    String id;
    TextView dati;
    private Button send;
    FirebaseStorage storage;
    String flatno;
    FirebaseDatabase database;
    String URL;
    Uri File;
   Bitmap compressedImageFile;
    ContentValues values;
     JSONObject notification;
    JSONObject notifcationBody;
    private RequestQueue requestQueue;
    String MyFlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

requestQueue= Volley.newRequestQueue(this);
        TOPIC = "/topics/updates";

        sharedPreferences=getSharedPreferences("Details",Context.MODE_PRIVATE);
        MyFlat=sharedPreferences.getString("Flat","");
        editor=sharedPreferences.edit();
        editor.putString("Flat","");
        editor.commit();
        editor.apply();

        imageView = (ImageView) findViewById(R.id.Camera);
        name=findViewById(R.id.et_username);
        flat=findViewById(R.id.et_flat_no);
        phone=findViewById(R.id.et_visitor_phone);
        reason=findViewById(R.id.et_reason);
        send=findViewById(R.id.btn_send);
        dati=findViewById(R.id.tv_date_time);
        Firebase.setAndroidContext(AdminActivity.this);
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        final String date = df.format(Calendar.getInstance().getTime());
        dati.setText(date);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0x4);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, 1);
//                }

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);


            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Visitors_admin");

                id = databaseReference.push().getKey();

               if(name.getText().toString().trim().equals("")||flat.getText().toString().trim().equals("")||
                        phone.getText().toString().trim().equals("")||reason.getText().toString().trim().equals(""))
               {
                   Toast.makeText(AdminActivity.this, "Enter all details!!", Toast.LENGTH_SHORT).show();
               }
               else if(imageUri==null)
               {
                   Toast.makeText(AdminActivity.this, "Please select Image!!", Toast.LENGTH_SHORT).show();
               }
               else
               {

                   Toast.makeText(AdminActivity.this, "Sending Request..", Toast.LENGTH_SHORT).show();

                   DateFormat df = new SimpleDateFormat("dd MMM yyyy, HH:mm");
                   final String dates = df.format(Calendar.getInstance().getTime());
                   uploadFile(imageUri,dates);





               }

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK&&data!=null) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//          imageUri=  getImageUri(this,imageBitmap);
//
//          Log.i("ImageUri",imageUri+"");
//          imageView.setBackgroundColor(Color.parseColor("#ffffff"));
//          imageView.setImageBitmap(imageBitmap);
//
//
//        }

        switch (requestCode) {

            case 1:
                if (requestCode == 1)
                    if (resultCode == Activity.RESULT_OK) {
                        try {

                            Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            imageView.setBackgroundColor(Color.parseColor("#ffffff"));
//                            imageView.setImageBitmap(thumbnail);

                       String imageurl = getPath(imageUri);
                            File file = new File(imageurl);
                            compressedImageFile =new Compressor(this).compressToBitmap(file);
                            imageView.setImageBitmap(compressedImageFile);
                            imageUri=getImageUri(this,compressedImageFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

        public String getRealPathFromURI (Uri contentUri){
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.i("Uri Path",path);
        return Uri.parse(path);
    }

        private void uploadFile (Uri file,final String date){

            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Sending Request...");
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            flatno=flat.getText().toString().trim();


            final String fileName = System.currentTimeMillis() + "";






            StorageReference storageReference = storage.getReference(flat.getText().toString().trim());
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
                                            Visitors_admin visitors_admin = new Visitors_admin(name.getText().toString().trim(), flat.getText().toString().trim(),
                                                    phone.getText().toString().trim(), reason.getText().toString().trim(), date, URL + "","",id);
                                            databaseReference.child(flat.getText().toString().trim()).child(id).setValue(visitors_admin)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    details();
                                                    Toast.makeText(AdminActivity.this, "Check the user response in the flat list.", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();

                                                }
                                            });
                                           // sendNotificationToUser("updates", flatno+","+id , "Master.Java");




                                        }
                                    });
                                }
                            }


//                        DatabaseReference reference = database.getReference(flat.getText().toString().trim());
//                        databaseReference.child(flat.getText().toString().trim()).child(id).child(fileName).setValue(URL).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
//
//
//
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "File not uploaded successfully", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });


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

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }



    public  void sendNotificationToUser(String topic_name, final String message, String TAG) {


        Firebase ref = new Firebase("https://visito-2b3c7.firebaseio.com/");
        final Firebase notifications = ref.child("notificationsRequest");

        Map notification = new HashMap<>();
        notification.put("topic_name", topic_name);
        notification.put("message", message);
        notification.put("click_action", TAG);

        notifications.push().setValue(notification);
    }

    public void details()
    {     NOTIFICATION_TITLE=flatno+","+id;


        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = flatno;
        NOTIFICATION_MESSAGE ="You have a New Visitor!!";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", flat.getText().toString().trim()+","+id);
            notifcationBody.put("message", NOTIFICATION_MESSAGE.trim());

            notification.put("to", TOPIC.trim());
            notification.put("data", notifcationBody);
            Log.i("Json object",notifcationBody+"");

        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }

        Log.i("title and message",NOTIFICATION_TITLE+" "+NOTIFICATION_MESSAGE);
    sendNotification(notification);}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.action_task)
        {
            Intent i=new Intent(AdminActivity.this, AdminFlatList.class);
            i.putExtra("parent","AdminActivity");
            startActivity(i);

        }else if(id == R.id.admin_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
            builder.setTitle("Are you sure you want to Logout?");
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent= new Intent(AdminActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;

            }

        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
                Intent startMain = new Intent(Intent.ACTION_MAIN);
     startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(AdminActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AdminActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(AdminActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);

            } else {

                ActivityCompat.requestPermissions(AdminActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            }
        }
    }
}
