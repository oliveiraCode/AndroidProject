package model;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.midterm.lasalle.androidprojectquestion.R;

import java.util.ArrayList;

public class ScoreListAdapter extends ArrayAdapter<Score> {

    private Context myContext;
    private int myResource;
    private ArrayList<Score> scoreArrayList;

    public ScoreListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Score> objects) {
        super(context, resource, objects);
        myContext = context;
        myResource = resource;
        scoreArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        TextView textViewScore = convertView.findViewById(R.id.textViewScore);
        TextView textViewStatus = convertView.findViewById(R.id.textViewStatus);

        textViewScore.setText(String.valueOf(scoreArrayList.get(position).getScore())+" %");


        if (scoreArrayList.get(position).getPassed()){
            textViewStatus.setText("Passed!");
            textViewStatus.setTextColor(Color.GREEN);
        }else {
            textViewStatus.setText("Failed!");
            textViewStatus.setTextColor(Color.RED);
        }

        return convertView;
    }

}
