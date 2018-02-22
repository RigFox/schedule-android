package xyz.rigfox.schedule_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import xyz.rigfox.schedule_android.R;
import xyz.rigfox.schedule_android.ScheduleSingleton;
import xyz.rigfox.schedule_android.adapters.GroupAdapter;
import xyz.rigfox.schedule_android.adapters.TeacherAdapter;

public class SelectGroupOrTeacherFragment extends Fragment {

    private ListView.OnItemClickListener groupListClickListener;
    private ListView.OnItemClickListener teacherListClickListener;
    private boolean showToolbar = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_group_or_teacher, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (!showToolbar) {
            toolbar.setVisibility(View.GONE);
        }

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(), groupListClickListener, teacherListClickListener);

        ViewPager mViewPager = view.findViewById(R.id.list_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public void setClickListeners(ListView.OnItemClickListener groupListClickListener, ListView.OnItemClickListener teacherListClickListener) {
        this.groupListClickListener = groupListClickListener;
        this.teacherListClickListener = teacherListClickListener;
    }

    public void hideToolbar() {
        showToolbar = false;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final AdapterView.OnItemClickListener groupListClickListener;
        private final AdapterView.OnItemClickListener teacherListClickListener;

        SectionsPagerAdapter(FragmentManager fm, ListView.OnItemClickListener groupListClickListener, ListView.OnItemClickListener teacherListClickListener) {
            super(fm);
            this.groupListClickListener = groupListClickListener;
            this.teacherListClickListener = teacherListClickListener;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    GroupListFragment groupListFragment = new GroupListFragment();
                    groupListFragment.setClickListener(groupListClickListener);
                    return groupListFragment;
                case 1:
                    TeacherListFragment teacherListFragment = new TeacherListFragment();
                    teacherListFragment.setClickListener(teacherListClickListener);
                    return teacherListFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Группы";
                case 1:
                    return "Преподаватели";
            }
            return null;
        }
    }

    public static class GroupListFragment extends Fragment {
        private ListView.OnItemClickListener clickListener;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.list_fragment, container, false);

            ListView listView = rootView.findViewById(R.id.list);
            EditText searchBox = rootView.findViewById(R.id.list_searchbox);


            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            final GroupAdapter groupAdapter = new GroupAdapter(getContext(), ScheduleSingleton.getInstance().getGroups());

            listView.setAdapter(groupAdapter);
            listView.setOnItemClickListener(clickListener);

            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    groupAdapter.getFilter().filter(s.toString());
                }
            });

            return rootView;
        }

        public void setClickListener(ListView.OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }

    public static class TeacherListFragment extends Fragment {
        private ListView.OnItemClickListener clickListener;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.list_fragment, container, false);

            ListView listView = rootView.findViewById(R.id.list);
            EditText searchBox = rootView.findViewById(R.id.list_searchbox);

            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            final TeacherAdapter teacherAdapter = new TeacherAdapter(getContext(), ScheduleSingleton.getInstance().getTeachers());

            listView.setAdapter(teacherAdapter);
            listView.setOnItemClickListener(clickListener);

            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    teacherAdapter.getFilter().filter(s.toString());
                }
            });

            return rootView;
        }

        public void setClickListener(ListView.OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }
}
