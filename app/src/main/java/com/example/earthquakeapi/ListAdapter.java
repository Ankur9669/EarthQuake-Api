package com.example.earthquakeapi;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.os.IResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Earthquake>
{


    public ListAdapter(@NonNull Context context, int resource, @NonNull List<Earthquake> objects)
    {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View ListItemView = convertView;

        if(ListItemView == null)
        {
            ListItemView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent,false);
        }

        Earthquake item = getItem(position);

        TextView magnitudeTextView = ListItemView.findViewById(R.id.magnitudeView);
        magnitudeTextView.setText("" + item.getMagnitude());

        //Getting the Background
        GradientDrawable magnitudeTextViewColor = (GradientDrawable) magnitudeTextView.getBackground();
        //Getting the Color of the Background
        int magnitudeTextViewColorToDisplay = getMagnitudeColor(item.getMagnitude());
        //Changing the Color of the Background
        magnitudeTextViewColor.setColor(magnitudeTextViewColorToDisplay);

        TextView locationTextView = ListItemView.findViewById(R.id.locationView);
        locationTextView.setText(item.getLocation());

        TextView cityTextView = ListItemView.findViewById(R.id.cityView);
        cityTextView.setText(item.getCity());

        TextView dateTextView = ListItemView.findViewById(R.id.dateView);
        dateTextView.setText(item.getDate());

        TextView timeTextView = ListItemView.findViewById(R.id.timeView);
        timeTextView.setText(item.getTime());

        return ListItemView;
    }

    public int getMagnitudeColor(double magnitude)
    {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
