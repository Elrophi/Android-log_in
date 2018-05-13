package com.example.maigoje.myapplication;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class Main2Activity extends AppCompatActivity {
    Button btn;

    private boolean open = false;
    private ConstraintSet l1, l2;
    private ConstraintLayout cs;
    private ImageView im;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element element = new Element();
        element.setTitle("We are hiring");

        View aboutpage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Prototype")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(element)
                .addGroup("connect with us")
                .addEmail("marcus@gmail.com")
                .addFacebook("Resource hub kenya")
                .addGitHub("mcben267")
                .addInstagram("resourceeshub")
                .addWebsite("www.mcben267@wordpress.com")
                .addItem(createCopyright())
                .create();

        setContentView(aboutpage);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Element createCopyright() {
        Element copyright = new Element();
        final String crs = String.format("Copyright %d by Us", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(crs);
        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Main2Activity.this, crs ,Toast.LENGTH_LONG).show();
            }
        });
        return copyright;
    }
}
