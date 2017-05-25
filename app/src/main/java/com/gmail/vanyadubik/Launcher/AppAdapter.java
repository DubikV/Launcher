package com.gmail.vanyadubik.Launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.vanyadubik.homework3.R;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends ArrayAdapter implements Filterable{

    private ListFilter filter = new ListFilter();
    private List<ApplicationInfo> originalAppList;
    private List<ApplicationInfo> appList;
    private Context context;
    private PackageManager packageManager;
    private List<ApplicationInfo> filteredList;
    private boolean close_item;

    public AppAdapter(Context context, int resource, List objects, Boolean close_item) {
        super(context, resource, objects);
        this.close_item = close_item;

        this.context = context;
        this.appList = objects;

        originalAppList = new ArrayList<ApplicationInfo>(objects);
        filteredList = new ArrayList<>();
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appList) ? appList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appList) ? (ApplicationInfo)appList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(this.close_item){
                view = layoutInflater.inflate(R.layout.item_delete_in_grid, null);
            }else{
                view = layoutInflater.inflate(R.layout.item, null);
            }
        }

        ApplicationInfo data = (ApplicationInfo)appList.get(position);

        if(null != data) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView packageName = (TextView) view.findViewById(R.id.app_package);
            ImageView iconView = (ImageView) view.findViewById(R.id.app_icon);

            appName.setText(data.loadLabel(packageManager));
            packageName.setText(data.packageName);
            iconView.setImageDrawable(data.loadIcon(packageManager));
            if(this.close_item){
                ImageView iconViewDel = (ImageView) view.findViewById(R.id.app_icon_del);
                int id = view.getResources().getIdentifier("android:drawable/ic_delete", null, null);
                iconViewDel.setImageResource(id);
            }
        }
        return view;
    }



    @Override
    public Filter getFilter() {
        return filter;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            appList.clear();
            filteredList.clear();
            FilterResults filterResults = new FilterResults();

            if (constraint != null || constraint.length() != 0) {

                for(ApplicationInfo info : originalAppList) {
                    try{
                        if (info.loadLabel(packageManager).toString().contains(constraint.toString())) {
                            filteredList.add(info);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            filterResults.values = filteredList;
            filterResults.count = filteredList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            appList.addAll((List<ApplicationInfo>) results.values);
            notifyDataSetChanged();
        }

    }
}
