package model;

import android.support.annotation.NonNull;

public class AnswerSelected implements Comparable<AnswerSelected> {

    private int idQuestion;
    private String name;
    private Boolean answerLater;

    public AnswerSelected(int idQuestion, String name, Boolean answerLater) {
        this.idQuestion = idQuestion;
        this.name = name;
        this.answerLater = answerLater;
    }

    public AnswerSelected() {
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAnswerLater() {
        return answerLater;
    }

    public void setAnswerLater(Boolean answerLater) {
        this.answerLater = answerLater;
    }

    @Override
    public String toString() {
        return "AnswerSelected{" +
                "idQuestion=" + idQuestion +
                ", name='" + name + '\'' +
                ", answerLater=" + answerLater +
                '}';
    }

    @Override
    public int compareTo(@NonNull AnswerSelected objAnswerSelected1) {
        if (this.getIdQuestion() > objAnswerSelected1.getIdQuestion())
            return 1;
        if (this.getIdQuestion() < objAnswerSelected1.getIdQuestion())
            return -1;
        return 0;
    }
}
