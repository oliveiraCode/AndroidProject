package model;

import java.io.Serializable;

public class Score implements Serializable {

    private int score;
    private Boolean isPassed;


    public Score() {
    }

    public Score(int score, Boolean isPassed) {
        this.score = score;
        this.isPassed = isPassed;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score='" + score + '\'' +
                ", isPassed=" + isPassed +
                '}';
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Boolean getPassed() {
        return isPassed;
    }

    public void setPassed(Boolean passed) {
        isPassed = passed;
    }
}
