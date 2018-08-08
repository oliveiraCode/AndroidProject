package model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.midterm.lasalle.androidprojectquestion.R;
import java.util.ArrayList;
import data.DataCollection;


public class ResultListAdapter extends ArrayAdapter<String> {

    private Context myContext;
    private int myResource;


    public ResultListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        myContext = context;
        myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        TextView textViewIdQ = convertView.findViewById(R.id.textViewIdQ);
        ImageView imageViewStatus = convertView.findViewById(R.id.imageViewStatus);
        TextView textViewCorrectAnswer = convertView.findViewById(R.id.textViewCorrectAnswer);
        TextView textViewWrongAnswer = convertView.findViewById(R.id.textViewWrongAnswer);

        textViewIdQ.setText(String.valueOf(position+1));

        String concatenateText = "";

        for (Question itemQ : DataCollection.getExam().getQuestionList()){
            if (itemQ.getIdQuestion() == (position+1)){
                for (Answer itemA : itemQ.getAnswersList()){
                    if (itemQ.getTypeOfQuestion() == enumTypeOfQuestion.Single_Answer){
                        if (itemA.getAnswerTrueFalse() == true){
                            concatenateText += "> "+itemA.getAnswer()+"<br>"; //Ask how to skip the line without to use <br>
                            break;
                        }
                    } else {
                        if (itemA.getAnswerTrueFalse() == true){
                            concatenateText += "> "+itemA.getAnswer()+"<br>";
                        }
                    }
                }
            }
        }

        textViewCorrectAnswer.setText(Html.fromHtml(concatenateText));
        textViewCorrectAnswer.setTextColor(ContextCompat.getColor(myContext, R.color.colorGreen));

        concatenateText = "";


        for (Question itemQ : DataCollection.getExam().getQuestionList()){
            if (itemQ.getIdQuestion() == (position+1)){
                for (AnswerSelected itemAnswerSelected : DataCollection.getSelectedAnswerList()){
                    if (itemAnswerSelected.getIdQuestion() == itemQ.getIdQuestion()){
                        if (itemQ.getTypeOfQuestion() == enumTypeOfQuestion.Single_Answer){
                            for (Answer itemA : itemQ.getAnswersList()){
                                if (itemA.getAnswer().toString() == itemAnswerSelected.getName().toString() && itemA.getAnswerTrueFalse()){
                                    concatenateText += "> "+itemAnswerSelected.getName()+"<br>";
                                    break;
                                } else if (itemA.getAnswer().toString() == itemAnswerSelected.getName().toString() && itemA.getAnswerTrueFalse() == false){
                                    concatenateText += "> "+itemAnswerSelected.getName()+"<br>";
                                    break;
                                }
                            }
                        } else if (itemQ.getTypeOfQuestion() == enumTypeOfQuestion.Multiple_Answer) {
                            for (Answer itemA : itemQ.getAnswersList()){
                                if (itemA.getAnswer().toString() == itemAnswerSelected.getName().toString() && itemA.getAnswerTrueFalse()){
                                    concatenateText += "> "+itemAnswerSelected.getName()+"<br>";
                                    break;
                                } else if (itemA.getAnswer().toString() == itemAnswerSelected.getName().toString() && itemA.getAnswerTrueFalse() == false){
                                    concatenateText += "> "+itemAnswerSelected.getName()+"<br>";
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }


        textViewWrongAnswer.setText(Html.fromHtml(concatenateText));


        //compare if both textView are equals.
        if (textViewWrongAnswer.getText().equals(textViewCorrectAnswer.getText())){
            textViewWrongAnswer.setTextColor(ContextCompat.getColor(myContext, R.color.colorGreen));
        } else {
            textViewWrongAnswer.setTextColor(ContextCompat.getColor(myContext, R.color.colorRed));
        }



        if (getItem(position).equals("Correct")){
            imageViewStatus.setImageResource(R.drawable.correct_icon);
        }else {
            imageViewStatus.setImageResource(R.drawable.error_icon);
        }


        //to change the color when the position is odd or even
        if (position % 2 == 1) {
            convertView.setBackgroundColor(ContextCompat.getColor(myContext, R.color.colorWhite));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(myContext, R.color.colorGray));
        }


        return convertView;
    }

}
