package com.midterm.lasalle.androidprojectquestion;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import data.DataCollection;
import model.AnswerSelected;
import model.SummaryListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Summary extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,DialogInterface.OnClickListener {


    Button btnSubmitQuiz;
    TextView textViewCodeExam, textViewTitleExam, textViewTimer;
    ListView listViewQuestions;
    SummaryListAdapter adapter;
    CountDownTimer countDownTimer;
    Long remainingTime;
    AlertDialog.Builder alertDialog;

    public Fragment_Summary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);

        initialize(rootView);
        countTimer(getArguments().getLong("remainingTime"));

        return rootView;
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

    @Override
    public void onResume() {
        super.onResume();
        adapter = new SummaryListAdapter(getContext(),R.layout.adapter_summary_layout, removeDuplicateObjectsFromSelectedAnswerList(DataCollection.getSelectedAnswerList()));
        listViewQuestions.setAdapter(adapter);

}

    private void initialize(View rootView) {
        textViewCodeExam = rootView.findViewById(R.id.textViewCodeExam);
        textViewTitleExam = rootView.findViewById(R.id.textViewTitleExam);
        textViewTimer = rootView.findViewById(R.id.textViewTimer);
        btnSubmitQuiz = rootView.findViewById(R.id.btnSubmitQuiz);
        listViewQuestions = rootView.findViewById(R.id.listViewSummary);


        listViewQuestions.setOnItemClickListener(this);
        btnSubmitQuiz.setOnClickListener(this);


        textViewCodeExam.setText(DataCollection.getExam().getCodeExam());
        textViewTitleExam.setText(DataCollection.getExam().getTitleExam());

        alertDialog = new AlertDialog.Builder(getContext()); //to initialize the element.
        alertDialog.setTitle("Time is over");
        alertDialog.setPositiveButton("OK", this); //How does make center this button?

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSubmitQuiz:

                boolean isPossibleToGo = true;
                for (AnswerSelected item : removeDuplicateObjectsFromSelectedAnswerList(DataCollection.getSelectedAnswerList())){
                    if (item.getAnswerLater()){
                        isPossibleToGo = false;

                        //super Toast
                        Toast toast = new Toast(getContext());
                        TextView textView = new TextView(getContext());
                        textView.setText("You need to answer all the questions");
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.signal_attention_icon, 0, 0);
                        toast.setView(textView);
                        toast.show();

                        break;
                    }
                }

                if (isPossibleToGo){
                    funcFinishFragmentAfterTime();
                }


                break;
        }
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

        countDownTimer.cancel();

        fragmentTransaction.commit();

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int idQuestion = 1;
        idQuestion += position;

        ImageView imageView = view.findViewById(R.id.imageViewStatus); //get the status on ListView (Answer not salved or Answer salved

        //The new Activity will be called only if the cell on ListView is Answer not salved

        if (Integer.parseInt(imageView.getTag().toString()) == R.drawable.attention_icon){

            Fragment_AnswerNotSalved fragment_answerNotSalved = new Fragment_AnswerNotSalved();

            Bundle args = new Bundle();
            args.putLong("remainingTime",remainingTime);
            args.putInt("idQuestion",idQuestion);
            fragment_answerNotSalved.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTransaction.replace(android.R.id.content, fragment_answerNotSalved);

            countDownTimer.cancel();

            fragmentTransaction.commit();

        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case Dialog.BUTTON_POSITIVE:

                funcFinishFragmentAfterTime();

                break;
        }
    }


}
