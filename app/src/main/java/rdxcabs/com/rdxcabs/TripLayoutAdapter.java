package rdxcabs.com.rdxcabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arung on 22/4/16.
 */
public class TripLayoutAdapter extends ArrayAdapter{

    Context context;
    int size;
    List<TripsBean> list;
    LinearLayout layout;

    public TripLayoutAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context=context;
        this.size=resource;
        list=objects;
    }


    public View getView(int position, View view, ViewGroup viewGroup){
        View row = view;

        if(row == null){
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            row=layoutInflater.inflate(size,viewGroup,false);

            layout = (LinearLayout) row.findViewById(R.id.tripLayout);

            TripsBean tripsBean = list.get(position);

            TextView textView1= new TextView(context);
            textView1.setText("Trip: " + (position + 1));
            textView1.setTextSize(20);
            textView1.setTypeface(null, Typeface.BOLD_ITALIC);

            TextView textView2 = new TextView(context);
            String text = "From: " + tripsBean.getSourceLoc() +"\n"+
                    "To: " + tripsBean.getDestLoc() + "\n" +
                    "Date: " + tripsBean.getDate();

            textView2.setText(text);

            layout.addView(textView1);
            layout.addView(textView2);
        }

        return row;
    }

}
