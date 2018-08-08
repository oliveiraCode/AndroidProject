package com.midterm.lasalle.androidprojectquestion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Main extends Fragment {

    public Fragment_Main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnStart = rootView.findViewById(R.id.btnStart);
        Button btnScore = rootView.findViewById(R.id.btnScore);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentQuestionCalled();
            }
        });

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentScoreCalled();
            }
        });

        return rootView;
    }


    private void fragmentQuestionCalled(){
        Fragment_Question fragment_question = new Fragment_Question();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment_question);
        fragmentTransaction.commit();
    }

    private void fragmentScoreCalled(){
        Fragment_Score fragment_score = new Fragment_Score();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment_score);
        fragmentTransaction.commit();
    }

}
