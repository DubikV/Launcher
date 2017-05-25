package com.gmail.vanyadubik.Launcher;

import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.Toast;

import com.gmail.vanyadubik.homework3.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static PackageManager pm;
    public static List applist;
    private EditText editText;
    public GridView gvMain;
    public static ArrayAdapter<String> adapter;
    private Filter filter;
    private boolean deleteInGrid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment_Table f = new Fragment_Table();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(R.id.fragment, f).commit();

        editText = (EditText) findViewById(R.id.edit);
        gvMain = (GridView) findViewById(R.id.grid);
        editText.addTextChangedListener(textWatcher);

        pm = this.getPackageManager();

       // new LoadApplications().execute();

        applist = checkForLaunchIntent(pm.getInstalledApplications(pm.GET_META_DATA));

        adapter = new AppAdapter(MainActivity.this, R.layout.item, applist, false);
        filter = adapter.getFilter();
        gvMain.setAdapter(adapter);

        GridView gView = (GridView) findViewById(R.id.grid);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gView.setNumColumns(3);
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gView.setNumColumns(5);
        }



        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ApplicationInfo app = (ApplicationInfo) applist.get(position);

                if (deleteInGrid){
                    ArrayList<ApplicationInfo> originalAppList = new ArrayList<ApplicationInfo>(applist);
                    applist.clear();
                    for(ApplicationInfo info : originalAppList) {
                        try{
                            if(info != app) {
                                applist.add(info);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                    adapter = new AppAdapter(MainActivity.this, R.layout.item_delete_in_grid, applist, true);
                    filter = adapter.getFilter();
                    gvMain.setAdapter(adapter);

                }else {
                    try {
                        Intent intent = pm.getLaunchIntentForPackage(app.packageName);

                        if (intent != null) {
                            startActivity(intent);
                        }
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    TextWatcher textWatcher = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }

       @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

       @Override
        public void afterTextChanged(Editable s) {
            filter.filter(s);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings :

              Toast.makeText(MainActivity.this, getString(R.string.settings), Toast.LENGTH_LONG).show();

              Intent intent = new Intent(MainActivity.this, Setting_Activity.class);
              startActivity(intent);

               return true;

            case R.id.device_settings :
              Toast.makeText(MainActivity.this, getString(R.string.device_settings), Toast.LENGTH_LONG).show();

              startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

              return true;

            case R.id.delete_apps :

                Toast.makeText(MainActivity.this, getString(R.string.delete_apps), Toast.LENGTH_LONG).show();

                if(deleteInGrid) {
                    applist.clear();
                    applist = checkForLaunchIntent(pm.getInstalledApplications(pm.GET_META_DATA));
                    adapter = new AppAdapter(MainActivity.this, R.layout.item, applist, false);
                    deleteInGrid = false;
                }else {
                    adapter = new AppAdapter(MainActivity.this, R.layout.item_delete_in_grid, applist, true);
                    deleteInGrid = true;
                }
                filter = adapter.getFilter();
                gvMain.setAdapter(adapter);

                return true;

            case R.id.exit :
                Toast.makeText(MainActivity.this, getString(R.string.exit), Toast.LENGTH_LONG).show();

                finish();
                System.exit(0);

                return true;
          }

        return super.onOptionsItemSelected(item);
    }
    public void onClickCall(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL_BUTTON);
        startActivity(callIntent);
    }

    public void onClickSms(View view) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.putExtra("sms_body", "");
        smsIntent.setType("vnd.android-dir/mms-sms");
        startActivity(smsIntent);
    }
    public void clicAllApps(View view){

        Toast.makeText(MainActivity.this, getString(R.string.applications), Toast.LENGTH_LONG).show();

        applist = checkForLaunchIntent(pm.getInstalledApplications(pm.GET_META_DATA));
        adapter = new AppAdapter(MainActivity.this, R.layout.item, applist, false);
        filter = adapter.getFilter();

        Intent intentA = new Intent(this, Second_Activity.class);
        //startActivity(intentA);
        startActivityForResult(intentA, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        ArrayList<ApplicationInfo> appListtwo=(ArrayList<ApplicationInfo>) data.getExtras().get("com.gmail.vanyadubik.launcher.second");
        adapter = new AppAdapter(MainActivity.this, R.layout.item, appListtwo, false);
        filter = adapter.getFilter();
        gvMain.setAdapter(adapter);
    }

    private List checkForLaunchIntent(List<ApplicationInfo> list) {

        ArrayList<ApplicationInfo> appList = new ArrayList<>();

        for(ApplicationInfo info : list) {
            try{
                if(pm.getLaunchIntentForPackage(info.packageName) != null) {
                    appList.add(info);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }



   /* private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;
        private String Tag = "My logs";

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(Tag, params.toString());

            applist = checkForLaunchIntent(pm.getInstalledApplications(pm.GET_META_DATA));

            adapter = new AppAdapter(MainActivity.this, R.layout.item, applist);

            filter = adapter.getFilter();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            gvMain = (GridView) findViewById(R.id.grid);
            gvMain.setAdapter(adapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, null, "Loading apps info...");
            super.onPreExecute();
        }
    }*/

}
