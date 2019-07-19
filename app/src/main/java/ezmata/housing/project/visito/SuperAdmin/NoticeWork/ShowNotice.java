package ezmata.housing.project.visito.SuperAdmin.NoticeWork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ezmata.housing.project.visito.R;


public class ShowNotice extends AppCompatActivity {
TextView title,date,content;
Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);

        title=findViewById(R.id.ShowNoticeTitle);
        date=findViewById(R.id.Show_Notice_Date);
        content=findViewById(R.id.ShowNoticeContent);
        bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            title.setText(bundle.getString("Title"));
            content.setText(bundle.getString("Content"));
            date.setText(bundle.getString("Date"));
        }

    }
}
