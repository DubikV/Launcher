package com.gmail.vanyadubik.Launcher;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gmail.vanyadubik.homework3.R;

import java.util.ArrayList;
import java.util.List;

public class Second_Activity extends AppCompatActivity  {

    private List applisttwo;
    public Intent answerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        final ArrayList<ApplicationInfo> applisttwo = new ArrayList<>();
        final GridView gridView = (GridView) findViewById(R.id.grid);

        Fragment_Table f = new Fragment_Table();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(R.id.fragment, f).commit();

        answerIntent = new Intent();
        gridView.setAdapter(MainActivity.adapter);
        gridView.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 3);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ApplicationInfo app = (ApplicationInfo) MainActivity.applist.get(position);
                applisttwo.add(app);
                Toast.makeText(Second_Activity.this, "Added app: " + app.loadLabel(MainActivity.pm), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        answerIntent.putExtra("com.gmail.vanyadubik.launcher.second", applisttwo);
        setResult(RESULT_OK, answerIntent);
    }


}


