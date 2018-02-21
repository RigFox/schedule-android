package xyz.rigfox.schedule_android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.rigfox.schedule_android.R;
import xyz.rigfox.schedule_android.models.Teacher;

public class TeacherAdapter extends ArrayAdapter<Teacher> implements Filterable {
    private LayoutInflater lInflater;
    private ModelFilter filter;

    private List<Teacher> objects;
    private List<Teacher> filteredObjects;

    public TeacherAdapter(Context context, List<Teacher> teachers) {
        super(context, R.layout.list_fragment_item, teachers);

        objects = new ArrayList<>();
        objects.addAll(teachers);

        filteredObjects = new ArrayList<>();
        filteredObjects.addAll(teachers);

        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Teacher getItem(int i) {
        return filteredObjects.get(i);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        Teacher t = filteredObjects.get(position);

        if (convertView == null) {
            view = lInflater.inflate(R.layout.list_fragment_item, null);
        } else {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.name)).setText(t.getName());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ModelFilter();
        }
        return filter;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            ArrayList<Teacher> filteredObjects = new ArrayList<>();

            for (int i = 0, l = objects.size(); i < l; i++) {
                Teacher t = objects.get(i);
                if (t.getName().toLowerCase().contains(constraint))
                    filteredObjects.add(t);
            }
            result.count = filteredObjects.size();
            result.values = filteredObjects;

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredObjects = (ArrayList<Teacher>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredObjects.size(); i < l; i++)
                add(filteredObjects.get(i));
            notifyDataSetInvalidated();
        }
    }
}
