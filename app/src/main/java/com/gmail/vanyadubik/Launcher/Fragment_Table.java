package com.gmail.vanyadubik.Launcher;

/*
 * Created by Vanya on 13.01.2016.
*/

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.vanyadubik.homework3.R;

public class Fragment_Table extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public void  onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState){
        View rootView =
                inflater.inflate(R.layout.fragment_layout, container, false);
        return rootView;
    }


}
