package ezmata.housing.project.visito.SuperAdmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import ezmata.housing.project.visito.AccountActivity.LoginActivity;
import ezmata.housing.project.visito.AdminFlatList;
import ezmata.housing.project.visito.Documents.DocumentsUpload;

import ezmata.housing.project.visito.R;
import ezmata.housing.project.visito.SuperAdmin.NoticeWork.NoticeBoard;

public class SuperAdminMainPage extends AppCompatActivity {

    ImageView add_imp_num, add_notices, allot_flats, my_society, btn_documents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_main_page);
        add_imp_num = findViewById(R.id.add_important_numbers);
        allot_flats = findViewById(R.id.btn_flat_allot);
        add_notices = findViewById(R.id.btn_add_notices);
        my_society = findViewById(R.id.btn_admin_society);

        btn_documents = findViewById(R.id.admin_add_documents);
        btn_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SuperAdminMainPage.this, DocumentsUpload.class);
                i.putExtra("parent", "SuperAdminMainPage");
                startActivity(i);
            }
        });
        my_society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperAdminMainPage.this, AdminFlatList.class);
                intent.putExtra("parent", "SuperAdminMainPage");
                startActivity(intent);
            }
        });

        allot_flats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flatsAllotIntent = new Intent(SuperAdminMainPage.this, AllotFlats.class);
                startActivity(flatsAllotIntent);
            }
        });

        add_notices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flatallotIntent = new Intent(SuperAdminMainPage.this, NoticeBoard.class);
                flatallotIntent.putExtra("parent", "SuperAdminMainPage");
                startActivity(flatallotIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.super_admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.super_admin_logout) {


            AlertDialog.Builder builder = new AlertDialog.Builder(SuperAdminMainPage.this);
            builder.setTitle("Are you sure you want to Logout?");
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent= new Intent(SuperAdminMainPage.this, LoginActivity.class);
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

        return super.onOptionsItemSelected(item);
    }
}
