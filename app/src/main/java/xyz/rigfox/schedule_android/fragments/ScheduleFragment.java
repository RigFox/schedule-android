package xyz.rigfox.schedule_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import xyz.rigfox.schedule_android.R;
import xyz.rigfox.schedule_android.ScheduleActivity;
import xyz.rigfox.schedule_android.models.Group;
import xyz.rigfox.schedule_android.models.Teacher;

public class ScheduleFragment extends Fragment {

    SelectGroupOrTeacherFragment selectGroupOrTeacherFragment = new SelectGroupOrTeacherFragment();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        selectGroupOrTeacherFragment.setClickListeners(groupListClickListener, teacherListClickListener);
        selectGroupOrTeacherFragment.hideToolbar();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, selectGroupOrTeacherFragment).commit();

        return view;
    }

    private ListView.OnItemClickListener groupListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Group group = (Group) adapterView.getAdapter().getItem(i);

            Intent intent = new Intent(getContext(), ScheduleActivity.class);
            intent.putExtra(ScheduleActivity.GROUP_ID, Integer.valueOf(Long.toString(group.getId())));
            intent.putExtra(ScheduleActivity.TEACHER_ID, -1);
            startActivity(intent);
        }
    };

    private ListView.OnItemClickListener teacherListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Teacher teacher = (Teacher) adapterView.getAdapter().getItem(i);

            Intent intent = new Intent(getContext(), ScheduleActivity.class);
            intent.putExtra(ScheduleActivity.GROUP_ID, -1);
            intent.putExtra(ScheduleActivity.TEACHER_ID, Integer.valueOf(Long.toString(teacher.getId())));
            startActivity(intent);
        }
    };
}
