package oneBill.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.presentation.DrawerData;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

public class DrawerDataAdapter extends ArrayAdapter<DrawerData> {

    private int resourceId;

    public DrawerDataAdapter(Context context, int textViewResourceId, List<DrawerData> drawerDataList) {
        super(context, textViewResourceId, drawerDataList);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerData data = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView drawerImage = (ImageView) view.findViewById(R.id.drawerImage);
        TextView drawerText = (TextView) view.findViewById(R.id.drawerText);
        drawerImage.setImageResource(data.getIcon());
        drawerText.setText(data.getName());
        return view;
    }
}
