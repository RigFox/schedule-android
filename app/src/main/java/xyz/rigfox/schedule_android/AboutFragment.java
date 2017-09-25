package xyz.rigfox.schedule_android;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import xyz.rigfox.schedule_android.models.Setting;

public class AboutFragment extends Fragment {

    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ctx = getContext();

        (view.findViewById(R.id.resetbutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleSingleton.getInstance().resetDB();
            }
        });

        updateFields(view);

        return view;
    }

    void updateFields(View view) {
        Activity activity = getActivity();
        Setting setting = ScheduleSingleton.getInstance().getSetting();

        int version_int = 0;
        int revision_int = 0;
        String updated_at_text = "Расписание не загружено";
        String description_text = "";

        if (setting != null) {
            version_int = setting.getVersion();
            revision_int = setting.getRevision();
            updated_at_text = setting.getUpdated_at();
            description_text = setting.getDescription();
        }

        String version_text;
        try {
            version_text = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version_text = "Не удалось определить";
            e.printStackTrace();
        }

        EditText app_version = view.findViewById(R.id.app_version);
        EditText version = view.findViewById(R.id.version);
        EditText revision = view.findViewById(R.id.revision);
        EditText updated_at = view.findViewById(R.id.updated_at);
        EditText description = view.findViewById(R.id.description);

        app_version.setText(version_text);
        version.setText(String.valueOf(version_int));
        revision.setText(String.valueOf(revision_int));
        updated_at.setText(updated_at_text);
        description.setText(description_text);
    }
}
