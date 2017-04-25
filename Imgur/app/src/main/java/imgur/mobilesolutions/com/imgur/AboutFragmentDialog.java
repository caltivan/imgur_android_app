package imgur.mobilesolutions.com.imgur;

import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by jgomez on 4/24/17.
 */

public class AboutFragmentDialog extends DialogFragment {

    private Context myContext;
    private TextView appTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_dialog, container, false);
        myContext = this.getActivity().getApplicationContext();
        getDialog().setTitle(myContext.getString(R.string.about_action));
        appTextView = (TextView) rootView.findViewById(R.id.appTextView);

        try {
            PackageInfo pckgInfo = myContext.getPackageManager().getPackageInfo(myContext.getPackageName(), 0);

            String version = myContext.getString(R.string.about_app_version) + pckgInfo.versionName;
            String appName = myContext.getString(R.string.about_app_name) +  myContext.getString(R.string.app_name);
            appTextView.setText(String.format("%s - %s", appName, version));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return rootView;
    }
}
