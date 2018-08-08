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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import data.DataCollection;
import model.Answer;
import model.AnswerSelected;
import model.Question;
import model.enumTypeOfQuestion;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AnswerNotSalved extends Fragment implements View.OnClickListener,DialogInterface.OnClickListener{


    Button btnReturn, btnSave, btnQuestionNumber;
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4;
    TextView textViewQuestion, textViewCodeExam, textViewTitleExam,textViewTimer,textViewTypeOfQuestion;
    enumTypeOfQuestion typeOfQuestion;
    int valueQuestion, index;
    Long remainingTime;
    CountDownTimer countDownTimer;
    AlertDialog.Builder alertDialog;

    public Fragment_AnswerNotSalved() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_answer_not_salved, container, false);

        initialize(rootView);

        return rootView;
    }

    private void initialize(View rootView) {

        startFindViewById(rootView);

        textViewCodeExam.setText(DataCollection.getExam().getCodeExam());
        textViewTitleExam.setText(DataCollection.getExam().getTitleExam());

        valueQuestion = getArguments().getInt("idQuestion");
        loadQuestionToActivity(valueQuestion);

        countTimer(getArguments().getLong("remainingTime"));

        alertDialog = new AlertDialog.Builder(getContext()); //to initialize the element.
        alertDialog.setTitle("Time is over");
        alertDialog.setPositiveButton("OK", this);

    }

    private void startFindViewById (View rootView){
        btnReturn = rootView.findViewById(R.id.btnReturn);
        btnSave = rootView.findViewById(R.id.btnSave);
        btnQuestionNumber = rootView.findViewById(R.id.btnQuestionNumber);
        textViewQuestion = rootView.findViewById(R.id.textViewQuestion);
        textViewCodeExam = rootView.findViewById(R.id.textViewCodeExam);
        textViewTitleExam = rootView.findViewById(R.id.textViewTitleExam);
        textViewTimer = rootView.findViewById(R.id.textViewTimer);
        textViewTypeOfQuestion = rootView.findViewById(R.id.textViewTypeOfQuestion);

        checkBox1 = rootView.findViewById(R.id.checkBox1);
        checkBox2 = rootView.findViewById(R.id.checkBox2);
        checkBox3 = rootView.findViewById(R.id.checkBox3);
        checkBox4 = rootView.findViewById(R.id.checkBox4);

        btnReturn.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        checkBox1.setOnClickListener(this);
        checkBox2.setOnClickListener(this);
        checkBox3.setOnClickListener(this);
        checkBox4.setOnClickListener(this);


    }

    public void countTimer(long value){

        countDownTimer = new CountDownTimer(value,1000) {
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

    private void funcFinishFragmentAfterTime(){

        for (AnswerSelected itemAnswerSelected : removeDuplicateObjectsFromSelectedAnswerList(DataCollection.getSelectedAnswerList())){
            if (itemAnswerSelected.getAnswerLater()){
                itemAnswerSelected.setAnswerLater(false);
                itemAnswerSelected.setName(itemAnswerSelected.getName());
                itemAnswerSelected.setIdQuestion(itemAnswerSelected.getIdQuestion());
                DataCollection.removeObjectToList(itemAnswerSelected);
                DataCollection.addObjectToList(itemAnswerSelected);
                break;
            }
        }

        Fragment_Result fragment_result = new Fragment_Result();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment_result);

        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View v) {
        Fragment_Summary fragment_summary = new Fragment_Summary();

        Bundle args = new Bundle();
        args.putLong("remainingTime",remainingTime);
        fragment_summary.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment_summary);


        switch (v.getId()){
            case R.id.btnReturn:
                countDownTimer.cancel();
                fragmentTransaction.commit();

                break;
            case R.id.btnSave:
                funcSave();
                countDownTimer.cancel();
                fragmentTransaction.commit();

                break;

            case R.id.checkBox1:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                }

                break;
            case R.id.checkBox2:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                }

                break;
            case R.id.checkBox3:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox4.setChecked(false);
                }

                break;
            case R.id.checkBox4:

                if (typeOfQuestion == enumTypeOfQuestion.Single_Answer){
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                }

                break;
        }
    }

    private void loadQuestionToActivity(int valueQuestion){

        btnQuestionNumber.setText("Question "+Integer.toString(valueQuestion));
        CheckBox[] checkBox = {checkBox1,checkBox2,checkBox3,checkBox4};
        for (Question itemQuestion : DataCollection.getExam().getQuestionList()){

            index = 0;

            if (valueQuestion == itemQuestion.getIdQuestion()){
                textViewQuestion.setText(itemQuestion.getNameQuestion());
                typeOfQuestion = itemQuestion.getTypeOfQuestion();

                if (itemQuestion.getTypeOfQuestion() == enumTypeOfQuestion.Single_Answer){
                    textViewTypeOfQuestion.setText("Choose one single answer");
                } else {
                    textViewTypeOfQuestion.setText("Choose at least two answers");
                }
            }

            for (Answer itemAnswers : itemQuestion.getAnswersList()){
                if (valueQuestion == itemQuestion.getIdQuestion()){
                    checkBox[index++].setText(itemAnswers.getAnswer());
                }
            }

        }
    }

    private void funcSave(){

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

        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case Dialog.BUTTON_POSITIVE:

                funcFinishFragmentAfterTime();

                break;
        }
    }

    private ArrayList<AnswerSelected> removeDuplicateObjectsFromSelectedAnswerList(ArrayList<AnswerSelected> selectedAnswerList){
        ArrayList<AnswerSelected> selectedArrayListWithoutDuplicateObjects = new ArrayList<>();

        int idQuestion = 0;

        Collections.sort(selectedAnswerList); //to sort the list, the compareTo method is implemented in AnswerSelected class

        for (AnswerSelected item : selectedAnswerList){
            if (idQuestion != item.getIdQuestion()){
                selectedArrayListWithoutDuplicateObjects.add(item);
                idQuestion++;
            }
        }

        Collections.sort(selectedArrayListWithoutDuplicateObjects); //to sort the list, the compareTo method is implemented in AnswerSelected class

        return selectedArrayListWithoutDuplicateObjects;
    }

}
