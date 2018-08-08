package com.midterm.lasalle.androidprojectquestion;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import data.DataCollection;
import data.ObjectSerializer;
import model.Answer;
import model.AnswerSelected;
import model.Question;
import model.ResultListAdapter;
import model.Score;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Result extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    Button btnFinish, btnHome;
    TextView textViewResults, textViewTitle, textViewScore;
    ResultListAdapter adapter;
    ListView listViewResults;
    ArrayList<Score> scoreArrayList = new ArrayList<>();

    public Fragment_Result() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        initialize(rootView);

        adapter = new ResultListAdapter(getContext(),R.layout.adapter_result_layout, funcCheckAnswers());
        listViewResults.setAdapter(adapter);

        saveSharePref();

        return rootView;
    }

    private void findViewById(View rootView){
        btnFinish = rootView.findViewById(R.id.btnFinish);
        btnHome = rootView.findViewById(R.id.btnHome);
        textViewResults = rootView.findViewById(R.id.textViewResults);
        textViewScore = rootView.findViewById(R.id.textViewScore);
        textViewTitle = rootView.findViewById(R.id.textViewTitle);
        listViewResults = rootView.findViewById(R.id.listViewResults);
    }

    private void initialize(View rootView) {

        findViewById(rootView);

        textViewResults.setText(DataCollection.getExam().getCodeExam()+" - "+DataCollection.getExam().getTitleExam());
        btnFinish.setOnClickListener(this);
        btnHome.setOnClickListener(this);

        listViewResults.setOnItemClickListener(this);

    }

    private void saveSharePref(){

        int score = funcCalculateScore();

        Score scoreObj = new Score();
        scoreObj.setScore(score);

        if (score >= 70){
            textViewTitle.setText("Congratulations you pass in the exam");
            textViewScore.setText("Total score: "+score+"%");
            scoreObj.setPassed(true);
        } else {
            textViewTitle.setText("Sorry, you didn't pass in the exam");
            textViewTitle.setTextColor(Color.RED);
            textViewScore.setText("Total score: "+score+"%");
            textViewScore.setTextColor(Color.RED);
            scoreObj.setPassed(false);
        }

        getScoreSharePref(); //save the old value before add new one
        scoreArrayList.add(scoreObj);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        try {
            sharedPref.edit().putString("scoreArrayList", ObjectSerializer.serialize(scoreArrayList)).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getScoreSharePref(){

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

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnFinish:
                DataCollection.clearSelectedAnswerList();
                getActivity().finishAffinity(); //to finish the SummaryActivity that stayed opened.
                break;

            case R.id.btnHome:

                Fragment_Main fragment_main = new Fragment_Main();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(android.R.id.content, fragment_main);

                fragmentTransaction.commit();

                DataCollection.clearSelectedAnswerList();

                break;
        }
    }

    private ArrayList<String> funcCheckAnswers(){

        ArrayList<String> arrayListCheckAnswers = new ArrayList<>();

        int contCorrectAnswersFromUser = 0;
        int contTrueAnswersFromFile = 0;

        for (Question itemQ : DataCollection.getExam().getQuestionList()){
            for (Answer itemA : itemQ.getAnswersList()){
                for (AnswerSelected itemFromUser : DataCollection.getSelectedAnswerList()) {
                    if (itemFromUser.getIdQuestion() == itemQ.getIdQuestion() && itemFromUser.getName() != null){
                        if (itemFromUser.getName().equals(itemA.getAnswer()) && itemA.getAnswerTrueFalse() == true){
                            contCorrectAnswersFromUser++;
                        }
                    }

                }
                if (itemA.getAnswerTrueFalse() == true){
                    contTrueAnswersFromFile++;
                }
            }

            if (contCorrectAnswersFromUser == contTrueAnswersFromFile){
                arrayListCheckAnswers.add("Correct");
            } else {
                arrayListCheckAnswers.add("Error");
            }

            contCorrectAnswersFromUser = 0;
            contTrueAnswersFromFile = 0;
        }


        return arrayListCheckAnswers;
    }

    private int funcCalculateScore(){

        int contCorrectAnswersFromUser = 0;
        int contTrueAnswersFromFile = 0;
        int compareCorrectAnswersFromUserWithTrueAnswersFromFile = 0;

        for (Question itemQ : DataCollection.getExam().getQuestionList()){
            for (Answer itemA : itemQ.getAnswersList()){
                for (AnswerSelected itemFromUser : DataCollection.getSelectedAnswerList()) {

                    if (itemFromUser.getIdQuestion() == itemQ.getIdQuestion() && itemFromUser.getName() != null){
                        if (itemFromUser.getName().equals(itemA.getAnswer()) && itemA.getAnswerTrueFalse() == true){
                            contCorrectAnswersFromUser++;
                        }
                    }

                }
                if (itemA.getAnswerTrueFalse() == true){
                    contTrueAnswersFromFile++;
                }
            }

            if (contCorrectAnswersFromUser == contTrueAnswersFromFile){
                compareCorrectAnswersFromUserWithTrueAnswersFromFile++;
            }

            contCorrectAnswersFromUser = 0;
            contTrueAnswersFromFile = 0;
        }

        return (compareCorrectAnswersFromUserWithTrueAnswersFromFile * 100) / DataCollection.getExam().getQuestionList().size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
