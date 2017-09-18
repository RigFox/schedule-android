package xyz.rigfox.schedule_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import xyz.rigfox.schedule_android.models.Group;

public class GroupAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Group> objects;

    GroupAdapter(Context context, List<Group> groups) {
        ctx = context;
        objects = groups;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.schedule_widget_configure_item, viewGroup, false);
        }

        Group g = (Group) getItem(i);

        ((TextView) view.findViewById(R.id.name)).setText(g.getName());

        return view;
    }
}
