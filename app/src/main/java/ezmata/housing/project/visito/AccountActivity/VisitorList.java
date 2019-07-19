package ezmata.housing.project.visito.AccountActivity;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.FrameLayout;

import ezmata.housing.project.visito.AdminFlatList;
import ezmata.housing.project.visito.Fragments.AcceptedFragment;
import ezmata.housing.project.visito.Fragments.RejectedFragment;
import ezmata.housing.project.visito.MainActivity;
import ezmata.housing.project.visito.R;


public class VisitorList extends FragmentActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    String flatno;

    private AcceptedFragment acceptedFragment;
    private RejectedFragment rejectedFragment;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_list);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        { if(bundle.getString("parent").equals("MainActivity")) {
            i = 0;
            flatno=bundle.getString("Flat");
        }
           else if(bundle.getString("parent").equals("AdminFlatList"))
            {i=1;
            flatno=bundle.getString("Flat");
            }
        }
//        bottomNavigationView = findViewById(R.id.main_navigation_bar);
        frameLayout = findViewById(R.id.main_frame);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

//        acceptedFragment = new AcceptedFragment();
//        rejectedFragment = new RejectedFragment();
//        setFragment(acceptedFragment);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//                                                                             @Override
//                                                                             public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                                                                                 switch (menuItem.getItemId()) {
//                                                                                     case R.id.accepted:
//                                                                                         setFragment(acceptedFragment);
//                                                                                         return true;
//                                                                                     case R.id.decline:
//                                                                                         setFragment(rejectedFragment);
//                                                                                         return true;
//                                                                                     default:
//                                                                                         return true;
//                                                                                 } }
//        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return AcceptedFragment.newInstance(flatno);
                case 1: return RejectedFragment.newInstance(flatno);
                default: return AcceptedFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(i==0) {
          Intent intent=new   Intent(VisitorList.this, MainActivity.class);
          intent.putExtra("parent","VisitorList");
          startActivity(intent);
        }
       else if(i==1)
            startActivity(new Intent(VisitorList.this, AdminFlatList.class));

    }
}
