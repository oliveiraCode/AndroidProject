package com.midterm.lasalle.androidprojectquestion;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import data.DataCollection;
import model.Answer;
import model.AnswerSelected;
import model.Exam;
import model.Question;
import model.enumTypeOfQuestion;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Question extends Fragment implements View.OnClickListener, Serializable, DialogInterface.OnClickListener {

    TextView textViewCodeExam,textViewTitleExam,textViewTimer,textViewQuestion,textViewTypeOfQuestion;
    Button btnHomePrevious,btnNext,btnQuestionNumber;
    CheckBox checkBoxMark,checkBox1,checkBox2,checkBox3,checkBox4;
    int indexTime,valueQuestion = 1,index;
    CountDownTimer countDownTimer;
    Exam examObj;
    enumTypeOfQuestion typeOfQuestion;
    Boolean isFirstTimeCheck = true;
    AlertDialog.Builder alertDialog;
    Long remainingTime;

    public Fragment_Question() {
        // Required empty public constructor
        examObj = DataCollection.getExam();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        initialize(rootView);

        return rootView;
    }

    private void startFindViewById (View rootView){
        textViewCodeExam = rootView.findViewById(R.id.textViewCodeExam);
        textViewTitleExam = rootView.findViewById(R.id.textViewTitleExam);
        textViewTimer = rootView.findViewById(R.id.textViewTimer);
        textViewQuestion = rootView.findViewById(R.id.textViewQuestion);
        textViewTypeOfQuestion = rootView.findViewById(R.id.textViewTypeOfQuestion);

        checkBox1 = rootView.findViewById(R.id.checkBox1);
        checkBox2 = rootView.findViewById(R.id.checkBox2);
        checkBox3 = rootView.findViewById(R.id.checkBox3);
        checkBox4 = rootView.findViewById(R.id.checkBox4);

        btnQuestionNumber = rootView.findViewById(R.id.btnQuestionNumber);
        btnHomePrevious = rootView.findViewById(R.id.btnReturn);
        checkBoxMark = rootView.findViewById(R.id.checkBoxMark);
        btnNext = rootView.findViewById(R.id.btnSave);



        checkBox1.setOnClickListener(this);
        checkBox2.setOnClickListener(this);
        checkBox3.setOnClickListener(this);
        checkBox4.setOnClickListener(this);

        checkBoxMark.setOnClickListener(this);

        btnHomePrevious.setOnClickListener(this);
        checkBoxMark.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    public void countTimer(){

        indexTime = DataCollection.getExam().getQuestionList().size()*30000; //30 seconds to answer one question

        countDownTimer = new CountDownTimer(indexTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                remainingTime = millisUntilFinished;

                long seconds = millisUntilFinished / 1000;


                textViewTimer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));

                if ((seconds / 60) < 1){
                    textViewTimer.setTextColor(Color.RED);
                }

            }

            @Override
            public void onFinish() {

                AlertDialog dialog = alertDialog.create();
                dialog.show();

            }
        };

        countDownTimer.start();
    }


    private void initialize(View rootView) {

        startFindViewById(rootView);

        textViewCodeExam.setText(examObj.getCodeExam());
        textViewTitleExam.setText(examObj.getTitleExam());

        loadQuestionToActivity(valueQuestion);
        countTimer();

        alertDialog = new AlertDialog.Builder(getContext()); //to initialize the element.
        alertDialog.setTitle("Time is over");
        alertDialog.setPositiveButton("OK", this);

    }

    private void loadQuestionToActivity(int valueQuestion){

        btnQuestionNumber.setText("Question "+Integer.toString(valueQuestion)+"/"+examObj.getQuestionList().size());
        CheckBox [] checkBox = {checkBox1,checkBox2,checkBox3,checkBox4};
        for (Question itemQuestion : examObj.getQuestionList()){
            index = 0;

            if (valueQuestion == itemQuestion.getIdQuestion()){
                textViewQuestion.setText(Html.fromHtml(itemQuestion.getNameQuestion())); //to use Html.fromHtml is not the best way, but \n doesn't work.
                typeOfQuestion = itemQuestion.getTypeOfQuestion();

                if (itemQuestion.getTypeOfQuestion() == enumTypeOfQuestion.Single_Answer){
                    textViewTypeOfQuestion.setText("Choose one single answer");
                } else {
                    textViewTypeOfQuestion.setText("Choose many answers");
                }

            }

            for (Answer itemAnswers : itemQuestion.getAnswersList()){

                if (valueQuestion == itemQuestion.getIdQuestion()){
                    checkBox[index++].setText(itemAnswers.getAnswer());
                }

            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnReturn:
                funcHomePrevious();

                break;

            case R.id.btnSave:

                if (!checkBoxMark.isChecked()){
                    if (!checkBox1.isChecked()){
                        if(!checkBox2.isChecked()){
                            if(!checkBox3.isChecked()){
                                if(!checkBox4.isChecked()){
                                    Toast.makeText(getContext(),"Please, select at least one option.",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }
                }

                btnHomePrevious.setText("Previous");
                funcNextQuestion();

                break;

            case R.id.checkBoxMark:

                if (checkBoxMark.isChecked()){
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                } else {
                    for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                        if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox1.getText().toString())){
                            checkBox1.setChecked(true);
                        }
                        if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox2.getText().toString())){
                            checkBox2.setChecked(true);
                        }
                        if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox3.getText().toString())){
                            checkBox3.setChecked(true);
                        }
                        if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox4.getText().toString())){
                            checkBox4.setChecked(true);
                        }
                    }
                }

                break;

            case R.id.checkBox1:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                }

                checkBoxMark.setChecked(false);

                break;
            case R.id.checkBox2:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                }

                checkBoxMark.setChecked(false);

                break;
            case R.id.checkBox3:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox4.setChecked(false);
                }

                checkBoxMark.setChecked(false);

                break;
            case R.id.checkBox4:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                }

                checkBoxMark.setChecked(false);

                break;
        }

    }

    private void funcHomePrevious (){

        CheckBox [] checkBox = {checkBox1,checkBox2,checkBox3,checkBox4};

        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        checkBoxMark.setChecked(false);

        valueQuestion--;

        if (valueQuestion == 1){
            btnHomePrevious.setText("Home");
            loadQuestionToActivity(valueQuestion);
        } else if (valueQuestion == 0){
            countDownTimer.cancel();
            DataCollection.clearSelectedAnswerList();

            Fragment_Main fragment_main = new Fragment_Main();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTransaction.replace(android.R.id.content, fragment_main);
            fragmentTransaction.commit();

        } else {
            loadQuestionToActivity(valueQuestion);
        }

        btnNext.setText("Next");


        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
            if (item.getAnswerLater() == true && item.getIdQuestion() == valueQuestion){
                checkBoxMark.setChecked(true);
                break;
            }
        }

        if (checkBoxMark.isChecked() == false){
            for (AnswerSelected itemAnswerSelected : DataCollection.getSelectedAnswerList()){
                if (valueQuestion == itemAnswerSelected.getIdQuestion()){

                    for (index = 0; index <= 3; index++){
                        if (itemAnswerSelected.getName().equals(checkBox[index].getText().toString())){
                            checkBox[index].setChecked(true);
                        }
                    }
                }
            }
        }

    }

    private void funcNextQuestion(){

        if (isFirstTimeCheck && checkBoxMark.isChecked()){
            AnswerSelected objAnswerSelected = new AnswerSelected();
            objAnswerSelected.setIdQuestion(valueQuestion);
            objAnswerSelected.setName("Not answered");
            objAnswerSelected.setAnswerLater(true);
            checkBoxMark.setChecked(false);
            DataCollection.addObjectToList(objAnswerSelected);
            isFirstTimeCheck = false;
        } else {

            if (checkBoxMark.isChecked()){
                AnswerSelected objAnswerSelected = new AnswerSelected();
                objAnswerSelected.setName("Not answered");
                objAnswerSelected.setIdQuestion(valueQuestion);
                objAnswerSelected.setAnswerLater(true);
                checkBoxMark.setChecked(false);

                for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                    if (item.getIdQuestion() != valueQuestion){
                        DataCollection.addObjectToList(objAnswerSelected);
                        break;
                    } else {
                        item.setAnswerLater(true);
                    }
                }



            } else {


                if (checkBox1.isChecked()){
                    AnswerSelected objAnswerSelected = new AnswerSelected();
                    objAnswerSelected.setName(checkBox1.getText().toString());
                    objAnswerSelected.setIdQuestion(valueQuestion);
                    objAnswerSelected.setAnswerLater(false);

                    if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                            if (item.getIdQuestion() == valueQuestion){
                                DataCollection.removeObjectToList(item);
                                break;
                            }
                        }
                    } else {
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                            if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                                DataCollection.removeObjectToList(item);
                                break;
                            } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox1.getText().toString()))){
                                DataCollection.removeObjectToList(item);
                                break;
                            }
                        }
                    }

                    DataCollection.addObjectToList(objAnswerSelected);
                } else {

                    for (AnswerSelected item : DataCollection.getSelectedAnswerList()){

                        if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                            DataCollection.removeObjectToList(item);
                            break;
                        } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox1.getText().toString()))){
                            DataCollection.removeObjectToList(item);
                            break;
                        }
                    }
                }

                if (checkBox2.isChecked()){
                    AnswerSelected objAnswerSelected = new AnswerSelected();
                    objAnswerSelected.setName(checkBox2.getText().toString());
                    objAnswerSelected.setIdQuestion(valueQuestion);
                    objAnswerSelected.setAnswerLater(false);

                    if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                            if (item.getIdQuestion() == valueQuestion){
                                DataCollection.removeObjectToList(item);
                                break;
                            }
                        }
                    } else {
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){

                            if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                                DataCollection.removeObjectToList(item);
                                break;
                            } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox2.getText().toString()))){
                                DataCollection.removeObjectToList(item);
                                break;
                            }

                        }
                    }

                    DataCollection.addObjectToList(objAnswerSelected);
                } else {

                    for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                        if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                            DataCollection.removeObjectToList(item);
                            break;
                        } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox2.getText().toString()))){
                            DataCollection.removeObjectToList(item);
                            break;
                        }
                    }
                }

                if (checkBox3.isChecked()){
                    AnswerSelected objAnswerSelected = new AnswerSelected();
                    objAnswerSelected.setName(checkBox3.getText().toString());
                    objAnswerSelected.setIdQuestion(valueQuestion);
                    objAnswerSelected.setAnswerLater(false);

                    if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                            if (item.getIdQuestion() == valueQuestion){
                                DataCollection.removeObjectToList(item);
                                break;
                            }
                        }
                    } else {
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){

                            if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                                DataCollection.removeObjectToList(item);
                                break;
                            } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox3.getText().toString()))){
                                DataCollection.removeObjectToList(item);
                                break;
                            }

                        }
                    }

                    DataCollection.addObjectToList(objAnswerSelected);
                } else {

                    for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                        if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                            DataCollection.removeObjectToList(item);
                            break;
                        } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox3.getText().toString()))){
                            DataCollection.removeObjectToList(item);
                            break;
                        }
                    }
                }
                if (checkBox4.isChecked()){
                    AnswerSelected objAnswerSelected = new AnswerSelected();
                    objAnswerSelected.setName(checkBox4.getText().toString());
                    objAnswerSelected.setIdQuestion(valueQuestion);
                    objAnswerSelected.setAnswerLater(false);

                    if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                            if (item.getIdQuestion() == valueQuestion){
                                DataCollection.removeObjectToList(item);
                                break;
                            }
                        }
                    } else {
                        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){

                            if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                                DataCollection.removeObjectToList(item);
                                break;
                            } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox4.getText().toString()))){
                                DataCollection.removeObjectToList(item);
                                break;
                            }

                        }
                    }
                    DataCollection.addObjectToList(objAnswerSelected);
                } else {
                    for (AnswerSelected item : DataCollection.getSelectedAnswerList()){
                        if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                            DataCollection.removeObjectToList(item);
                            break;
                        } else if ((item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox4.getText().toString()))){
                            DataCollection.removeObjectToList(item);
                            break;
                        }
                    }
                }


            }
        }

        if (valueQuestion == examObj.getQuestionList().size()){

            Fragment_Summary fragment_summary = new Fragment_Summary();

            Bundle args = new Bundle();
            args.putLong("remainingTime", remainingTime);
            fragment_summary.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTransaction.replace(android.R.id.content, fragment_summary);

            countDownTimer.cancel();

            fragmentTransaction.commit();

        } else {
            valueQuestion++;
            loadQuestionToActivity(valueQuestion);
        }


        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        isFirstTimeCheck = false;

        for (AnswerSelected item : DataCollection.getSelectedAnswerList()){

            if (item.getIdQuestion() == valueQuestion && item.getName() == null){
                checkBoxMark.setChecked(true);
            } else if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox1.getText().toString())){
                checkBox1.setChecked(true);
            }


            else if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox2.getText().toString())){
                checkBox2.setChecked(true);
            }
            else if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox3.getText().toString())){
                checkBox3.setChecked(true);
            }
            else  if (item.getIdQuestion() == valueQuestion && item.getName().equals(checkBox4.getText().toString())){
                checkBox4.setChecked(true);
            }
        }


        if (valueQuestion == examObj.getQuestionList().size()){
            btnNext.setText("Done");
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which){
            case Dialog.BUTTON_POSITIVE:

                saveAllObjectsAndFinishQuiz();

                break;
        }
    }

    private void saveAllObjectsAndFinishQuiz (){

        for (;valueQuestion<=examObj.getQuestionList().size();valueQuestion++){
            AnswerSelected objAnswerSelected = new AnswerSelected();
            objAnswerSelected.setIdQuestion(valueQuestion);
            objAnswerSelected.setName("Not answered");
            DataCollection.addObjectToList(objAnswerSelected);
        }

        Fragment_Result fragment_result = new Fragment_Result();

        Bundle args = new Bundle();
        args.putLong("remainingTime", remainingTime);
        fragment_result.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment_result);

        countDownTimer.cancel();

        fragmentTransaction.commit();

    }

}
