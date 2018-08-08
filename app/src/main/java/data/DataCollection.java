package data;

import java.util.ArrayList;
import model.AnswerSelected;
import model.Exam;

public class DataCollection {

    private static ArrayList<AnswerSelected> selectedAnswerList = new ArrayList<>();
    private static Exam examObj;

    public static ArrayList<AnswerSelected> getSelectedAnswerList (){
        return selectedAnswerList;
    }
    public static void clearSelectedAnswerList(){
        selectedAnswerList.clear();
    }

    public static void addObjectToList(AnswerSelected obj){
        selectedAnswerList.add(obj);
    }

    public static void removeObjectToList(AnswerSelected obj){
        selectedAnswerList.remove(obj);
    }

    public static Exam getExam (){
        return examObj;
    }

    public static void setExam (Exam obj){
        examObj = obj;
    }

}
