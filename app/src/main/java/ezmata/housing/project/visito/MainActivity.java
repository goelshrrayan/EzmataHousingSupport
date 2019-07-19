package ezmata.housing.project.visito;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import ezmata.housing.project.visito.AccountActivity.AccountMainActivity;
import ezmata.housing.project.visito.AccountActivity.VisitorList;
import ezmata.housing.project.visito.Admin.MySingleton;
import ezmata.housing.project.visito.Documents.DocumentsUpload;



import ezmata.housing.project.visito.SuperAdmin.NoticeWork.NoticeBoard;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    ImageView btn_visitors,btn_society,btn_impnos,btn_notices,btn_documents;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int Allowance=0;
    int decision=1;
    Bundle bundle;
    DatabaseReference myRef;
    Uri notification;
    ImageView btn_myprof;
    FirebaseDatabase database;
    MediaPlayer mp;
    Ringtone r;
    String Name,Phone,Reason,Uri;


    String id;
    //FCM notification
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AIzaSyCmBcPE7-pnEjH0A_nfzbWyR85MlBQcAUo";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private ProgressDialog progressDialog;
    String flatno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        id="";Allowance=0;
        setContentView(R.layout.activity_main);
btn_documents=findViewById(R.id.documents);
        btn_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DocumentsUpload.class);
                intent.putExtra("parent","MainActivity");
                intent.putExtra("Flat",flatno);
                startActivity(intent);
            }
        });

        btn_society=findViewById(R.id.btn_society);
        btn_society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,AdminFlatList.class);
                i.putExtra("parent","MainActivity");
                startActivity(i);
            }
        });
        btn_impnos=findViewById(R.id.important_numbers);
        btn_impnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImportantNumbers.class));
            }
        });
        btn_myprof=findViewById(R.id.btn_Myprofile);
        btn_myprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AccountMainActivity.class));
            }
        });

        btn_notices=findViewById(R.id.UsersNoticeBoard);
        btn_notices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, NoticeBoard.class);
                intent.putExtra("parent","MainActivity");
                startActivity(intent);
            }
        });

final SharedPreferences myPref=getSharedPreferences("Details",Context.MODE_PRIVATE);
flatno=myPref.getString("Flat","");
         Bundle bundle=getIntent().getExtras();
         if(bundle != null)
        {
            Allowance = bundle.getInt("Allowance", 0);
            id = bundle.getString("Id", "null");
            String parent=bundle.getString("Parent","");
            if(parent.equals("notification")) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Visitor has arrived");
                progressDialog.setMessage("Showing Visitor details...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            }
        Log.i("ID and allowance",id+" "+Allowance);

        database = FirebaseDatabase.getInstance();

if(id!=null&&Allowance==1) {

    try {
         notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(notification == null){
            // alert is null, using backup
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if(notification == null) {
                // alert backup is null, using 2nd backup
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
         Log.i("notification uri",notification+"");
         String[] uris=notification.toString().split(":");
         String requri="file:"+uris[1];
         Log.i("requri",requri);
         android.net.Uri uri= android.net.Uri.parse(requri);
//         r = RingtoneManager.getRingtone(getApplicationContext(), uri);
//        r.play();
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
    myRef=database.getReference();



    myRef.child("Visitors_admin").child(myPref.getString("Flat",""))
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    Name = dataSnapshot.child(id).child("visitorName").getValue(String.class);
                    Phone = dataSnapshot.child(id).child("visitorPhone").getValue(String.class);
                    Reason = dataSnapshot.child(id).child("visitorReason").getValue(String.class);
                    Uri = dataSnapshot.child(id).child("imageUri").getValue(String.class);


                    final Dialog dialog = new Dialog(MainActivity.this);
                    Rect displayRectangle = new Rect();
                    Window window = (MainActivity.this).getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                    LayoutInflater inflater2= (LayoutInflater) (MainActivity.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater2.inflate(R.layout.decision, null);
                    layout.setMinimumWidth((int)(displayRectangle.width() * 0.8f));
                    layout.setMinimumHeight((int)(displayRectangle.height() * 0.8f));
                    dialog.setContentView(layout);
                    ImageView imageView=dialog.findViewById(R.id.decision_image);
                    TextView name=dialog.findViewById(R.id.visitor_name_decision);
                    TextView reason=dialog.findViewById(R.id.visitor_reason_decision);
                    TextView phone=dialog.findViewById(R.id.visitor_phone_decision);

                    name.setText(Name);
                    phone.setText(Phone);
                    reason.setText(Reason);
                    Picasso.get().load(Uri).into(imageView);


// inflate and adjust layout

                    Button accept_btn=dialog.findViewById(R.id.Accept_request);
                     Button decline_btn=dialog.findViewById(R.id.Decline_request);

                    if(!(MainActivity.this).isFinishing())
                    {getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialog.dismiss();
                        dialog.show();
                        dialog.setCancelable(false);
                    }
                    accept_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decision=1;
                            dialog.dismiss();
                            Log.i("dialog val accept",dialog+"");
                            myRef.child("Visitors_admin").child(myPref.getString("Flat","")).child(id)
                                    .child("allowance").setValue("accepted");

                            if(Allowance==1&&id!=null) {
                                if (mp.isPlaying())
                                    mp.stop();
                                    mp.release();
                                }

                            Intent i=new Intent(MainActivity.this, VisitorList.class);
                            i.putExtra("Flat",flatno);
                            i.putExtra("parent","MainActivity");
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();


                        }
                    });
                    decline_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decision=0;
                            dialog.dismiss();
                            Log.i("dialog val",dialog+"");
                            Log.i("Dismissed","dismissed");
                            myRef.child("Visitors_admin").child(myPref.getString("Flat","")).child(id)
                            .child("allowance").setValue("rejected");
                            if(Allowance==1&&id!=null) {
                                if (mp.isPlaying())
                                    mp.stop();
                                    mp.release();
                                }


                            Intent i=new Intent(MainActivity.this, VisitorList.class);
                            i.putExtra("Flat",flatno);
                            i.putExtra("parent","MainActivity");
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

}





            btn_visitors=findViewById(R.id.btn_visitors);
            btn_visitors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(MainActivity.this, VisitorList.class);
                    i.putExtra("Flat",flatno);
                    i.putExtra("parent","MainActivity");
                    startActivity(i);
                }
            });

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Allowance==1&&id!=null) {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
        }
        Intent startMain = new Intent(Intent.ACTION_MAIN);
     startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
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
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("TAG", "onErrorResponse: Didn't work");
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

}
