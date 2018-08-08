package model;

import java.io.Serializable;

public class Answer implements Serializable {
    private String answer;
    private Boolean answerTrueFalse;

    public Answer() {
    }

    public Answer(String answer, Boolean answerTrueFalse) {
        this.answer = answer;
        this.answerTrueFalse = answerTrueFalse;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getAnswerTrueFalse() {
        return answerTrueFalse;
    }

    public void setAnswerTrueFalse(Boolean answerTrueFalse) {
        this.answerTrueFalse = answerTrueFalse;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + answer + '\'' +
                ", answerTrueFalse=" + answerTrueFalse +
                '}';
    }
}
