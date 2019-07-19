package ezmata.housing.project.visito;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import ezmata.housing.project.visito.AccountActivity.LoginActivity;

public class Clause extends AppCompatActivity {

    CheckBox checkBox;
    Button Continue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clause);

        sharedPreferences=getSharedPreferences("Clause",MODE_PRIVATE);
        editor=sharedPreferences.edit();
checkBox=findViewById(R.id.clause_checkbox);
Continue=findViewById(R.id.clause_continue_btn);

String check=sharedPreferences.getString("clause","false");
if(check.equals("true"))
    callLogin();


Continue.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(checkBox.isChecked())
        {
            editor.putString("clause","true");
            editor.commit();
            editor.apply();
            callLogin();
        }
        else
        {
            Toast.makeText(Clause.this, "Please accept the terms and conditions to continue", Toast.LENGTH_SHORT).show();
        }
    }
});

    }

    private void callLogin() {
    Intent intent=new Intent(Clause.this, LoginActivity.class);
    startActivity(intent);

    }
}
