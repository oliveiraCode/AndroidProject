package com.midterm.lasalle.androidprojectquestion;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import data.ObjectSerializer;
import model.ResultListAdapter;
import model.Score;
import model.ScoreListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Score extends Fragment {

    ScoreListAdapter adapter;
    ListView listViewScore;
    ArrayList<Score> scoreArrayList;

    public Fragment_Score() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_score, container, false);

        getSharePref(rootView);

        Button btnHome = rootView.findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentMainCalled();
            }
        });

        return rootView;
    }

    private void getSharePref(View rootView){

        listViewScore = rootView.findViewById(R.id.listViewScore);


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        try {
            scoreArrayList = (ArrayList<Score>) ObjectSerializer.deserialize(
                    sharedPref.getString("scoreArrayList",
                            ObjectSerializer.serialize(new ArrayList<Score>())
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        adapter = new ScoreListAdapter(getContext(),R.layout.adapter_score_layout,scoreArrayList);
        listViewScore.setAdapter(adapter);

    }


    private void fragmentMainCalled(){

        Fragment_Main fragment_main = new Fragment_Main();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment_main);

        fragmentTransaction.commit();
    }


}
