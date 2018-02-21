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
import xyz.rigfox.schedule_android.models.Group;

public class GroupAdapter extends ArrayAdapter<Group> implements Filterable {
    private LayoutInflater lInflater;
    private ModelFilter filter;

    private List<Group> objects;
    private List<Group> filteredObjects;

    public GroupAdapter(Context context, List<Group> groups) {
        super(context, R.layout.list_fragment_item, groups);

        objects = new ArrayList<>();
        objects.addAll(groups);

        filteredObjects = new ArrayList<>();
        filteredObjects.addAll(groups);

        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Group getItem(int i) {
        return filteredObjects.get(i);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        Group g = filteredObjects.get(position);

        if (convertView == null) {
            view = lInflater.inflate(R.layout.list_fragment_item, null);
        } else {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.name)).setText(g.getName());

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
            ArrayList<Group> filteredObjects = new ArrayList<>();

            for (int i = 0, l = objects.size(); i < l; i++) {
                Group g = objects.get(i);
                if (g.getName().toLowerCase().contains(constraint))
                    filteredObjects.add(g);
            }
            result.count = filteredObjects.size();
            result.values = filteredObjects;

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredObjects = (ArrayList<Group>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredObjects.size(); i < l; i++)
                add(filteredObjects.get(i));
            notifyDataSetInvalidated();
        }
    }
}
