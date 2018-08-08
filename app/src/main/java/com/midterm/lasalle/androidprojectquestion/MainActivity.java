package com.midterm.lasalle.androidprojectquestion;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import data.DataCollection;
import data.QuizTask;
import model.Answer;
import model.Exam;
import model.Question;
import model.enumTypeOfQuestion;

public class MainActivity extends AppCompatActivity {


    private String request = "https://api.myjson.com/bins/15izb2";
    int typeOfQuestionCount;
    Exam examObj = new Exam();
    Question objQuestion = new Question();

    ArrayList<Answer> listAnswer = new ArrayList<>();
    ArrayList<Question> listQuestion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getQuestionFromJson();

        setCustomActionBarTitleCenter();

        Fragment_Main fragment_main = new Fragment_Main();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment_main);
        fragmentTransaction.commit();

    }

    private void getQuestionFromJson() {
        new QuizTask(this).execute(request);
    }

    public void parseJson(String json) {

            try {
                JSONObject jsonObj = new JSONObject(json);

                examObj.setCodeExam((String) jsonObj.get("codeExam"));
                examObj.setTitleExam(jsonObj.getString("titleExam"));

                // Getting JSON Array node
                JSONArray questionList = jsonObj.getJSONArray("questionList");

                for (int i = 0; i < questionList.length(); i++) {
                    JSONObject question = questionList.getJSONObject(i);

                    objQuestion = new Question();
                    objQuestion.setIdQuestion(i+1);
                    objQuestion.setNameQuestion(question.getString("Q"+(i+1)));

                    // Getting JSON Array node
                    JSONArray answerList = question.getJSONArray("Answers");
                    for (int index = 0; index < answerList.length(); index++) {
                        Answer objAnswer = new Answer();

                        JSONObject answer = answerList.getJSONObject(index);

                        objAnswer.setAnswer(answer.getString("answer"));
                        objAnswer.setAnswerTrueFalse(answer.getBoolean("correct"));
                        listAnswer.add(objAnswer);
                    }

                    objQuestion.setAnswersList(listAnswer);
                    listQuestion.add(objQuestion);
                    listAnswer = new ArrayList();

                }

                examObj.setQuestionList(listQuestion);

                for (Question itemQ :examObj.getQuestionList() ){

                    for (Answer itemA : itemQ.getAnswersList()){
                        if (itemA.getAnswerTrueFalse()){
                            typeOfQuestionCount++;
                        }
                    }
                    if (typeOfQuestionCount <= 1){
                        itemQ.setTypeOfQuestion(enumTypeOfQuestion.Single_Answer);
                    } else {
                        itemQ.setTypeOfQuestion(enumTypeOfQuestion.Multiple_Answer);
                    }
                    typeOfQuestionCount = 0;
                }

                DataCollection.setExam(examObj);
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }
    }


    private void setCustomActionBarTitleCenter(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_bar_layout);
        getSupportActionBar().setElevation(0); //Remove shadow below ActionBar

        TextView title= findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("QUIZ");
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
    }



}
