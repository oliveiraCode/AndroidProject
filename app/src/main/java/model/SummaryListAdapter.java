package model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.midterm.lasalle.androidprojectquestion.R;
import java.util.ArrayList;


public class SummaryListAdapter extends ArrayAdapter<AnswerSelected> {

    private Context myContext;
    private int myResource;


    public SummaryListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AnswerSelected> objects) {
        super(context, resource, objects);
        myContext = context;
        myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        convertView = inflater.inflate(myResource, parent, false);

        TextView textViewIdQ =  convertView.findViewById(R.id.textViewIdQ);
        ImageView imageViewStatus =  convertView.findViewById(R.id.imageViewStatus);


            textViewIdQ.setText(String.valueOf(getItem(position).getIdQuestion()));


            if (getItem(position).getAnswerLater()){
                imageViewStatus.setImageResource(R.drawable.attention_icon);
                imageViewStatus.setTag(R.drawable.attention_icon);
            }else {
                imageViewStatus.setImageResource(R.drawable.save_icon);
                imageViewStatus.setTag(R.drawable.save_icon);
            }

        return convertView;
    }
}
