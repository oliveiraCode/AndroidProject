package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private int idQuestion;
    private String nameQuestion;
    private enumTypeOfQuestion typeOfQuestion;
    private ArrayList<Answer> answersList;

    public Question() {
        super();
    }

    public Question(int idQuestion, String nameQuestion, enumTypeOfQuestion typeOfQuestion, ArrayList<Answer> answersList) {
        this.idQuestion = idQuestion;
        this.nameQuestion = nameQuestion;
        this.typeOfQuestion = typeOfQuestion;
        this.answersList = answersList;
    }

    public enumTypeOfQuestion getTypeOfQuestion() {
        return typeOfQuestion;
    }

    public void setTypeOfQuestion(enumTypeOfQuestion typeOfQuestion) {
        this.typeOfQuestion = typeOfQuestion;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getNameQuestion() {
        return nameQuestion;
    }

    public void setNameQuestion(String nameQuestion) {
        this.nameQuestion = nameQuestion;
    }

    public ArrayList<Answer> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(ArrayList<Answer> answersList) {
        this.answersList = answersList;
    }

    @Override
    public String toString() {
        return "Question{" +
                "idQuestion=" + idQuestion +
                ", nameQuestion='" + nameQuestion + '\'' +
                ", typeOfQuestion=" + typeOfQuestion +
                ", answersList=" + answersList +
                '}';
    }
}
