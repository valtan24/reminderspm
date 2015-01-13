package com.reminders.valerie.reminders;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TabFragment extends Fragment {

    private int image;
    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        image = bundle.getInt("image");
        color = bundle.getInt("color");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_tab, null);
        ImageView tabImage = (ImageView) view.findViewById(R.id.imageView);
        tabImage.setImageResource(image);
        view.setBackgroundResource(color);
        return view;
    }

}
