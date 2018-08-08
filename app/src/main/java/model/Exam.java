package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Exam implements Serializable {
    private String codeExam;
    private String titleExam;
    private ArrayList<Question> questionList;

    @Override
    public String toString() {
        return "Exam{" +
                "codeExam='" + codeExam + '\'' +
                ", titleExam='" + titleExam + '\'' +
                ", questionList=" + questionList +
                '}';
    }

    public Exam() {
        super();
    }

    public Exam(String codeExam, String titleExam, ArrayList<Question> questionList) {
        this.codeExam = codeExam;
        this.titleExam = titleExam;
        this.questionList = questionList;
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    public String getCodeExam() {
        return codeExam;
    }

    public void setCodeExam(String codeExam) {
        this.codeExam = codeExam;
    }

    public String getTitleExam() {
        return titleExam;
    }

    public void setTitleExam(String titleExam) {
        this.titleExam = titleExam;
    }

}

