package com.imobile.mt8382hotkeydefine;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Arnab Chakraborty
 */
public class AppsGridFragment extends GridFragment implements LoaderManager.LoaderCallbacks<ArrayList<AppModel>> {

    AppListAdapter mAdapter;
    EditText mKey1, mKey2;
    Button mSave;
    Context mContext;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No Applications");

        mAdapter = new AppListAdapter(getActivity());
        setGridAdapter(mAdapter);

        // till the data is loaded display a spinner
        setGridShown(false);

        // create the loader to load the apps list in background
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<AppModel>> onCreateLoader(int id, Bundle bundle) {
        return new AppsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AppModel>> loader, ArrayList<AppModel> apps) {
        mAdapter.setData(apps);

        if (isResumed()) {
            setGridShown(true);
        } else {
            setGridShownNoAnimation(true);
        }

        mSave = (Button) getView().getRootView().findViewById(R.id.save_ini);
        mSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mKey1.getText().length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(),"Please select application for keys", Toast.LENGTH_SHORT).show();
                    return;
                }
                String root = Environment.getExternalStorageDirectory().toString();
                File hotkey_ini = new File(root, "Hotkey.ini");
                String msg;
                if (mKey2.getText().length() > 0) msg = "Key1: "+mKey1.getText()+"\nKey2: "+mKey2.getText()+"\n";
                else msg = "Key1: "+mKey1.getText()+"\n";

                if (hotkey_ini.exists()) hotkey_ini.delete();
                try {
                    FileOutputStream out = new FileOutputStream(hotkey_ini);
                    out.write(msg.getBytes());
                    Toast.makeText(getActivity().getApplicationContext(), "FILE: "+root+"/Hotkey.ini\n"+msg, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Cannot write file: "+root+"/Hotkey.ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mKey1 = (EditText) getView().getRootView().findViewById(R.id.key1);
        mKey2 = (EditText) getView().getRootView().findViewById(R.id.key2);
        DisplayMetrics screenMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(screenMetrics);
        int width = (screenMetrics.widthPixels - 150) >> 1;

        mKey2.setEnabled(false);
        mKey2.setText("");
        mKey2.setFocusable(false);
        mKey2.setWidth(width);
        mKey1.setEnabled(true);
        mKey1.setText("");
        mKey1.setFocusable(false);
        mKey1.setWidth(width);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AppModel>> loader) {
        mAdapter.setData(null);
    }

    @Override
    public void onGridItemClick(GridView g, View v, int position, long id) {
        AppModel app = (AppModel) getGridAdapter().getItem(position);
        // Launch App by:
        // Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(app.getApplicationPackageName());
        if (mContext == null && app != null) mContext = getActivity().getApplicationContext();
        if (mContext != null) {
            String myActivityName = getActivity()
                    .getPackageManager()
                    .getLaunchIntentForPackage(app.getApplicationPackageName())
                    .resolveActivity(mContext.getPackageManager())
                    .getClassName();
            String PoPackageName = getActivity()
                    .getPackageManager()
                    .getLaunchIntentForPackage(app.getApplicationPackageName())
                    .resolveActivity(mContext.getPackageManager())
                    .getPackageName();
            String PoResult = PoPackageName + "/"+ myActivityName;
            if (mKey1.isEnabled()) {
                mKey1.setText(PoResult);
                mKey1.setEnabled(false);
                mKey2.setEnabled(true);
            }
            else {
                mKey2.setText(PoResult);
                mKey2.setEnabled(false);
                mKey1.setEnabled(true);
            }

/*
            Toast.makeText(myContext, myActivityName, Toast.LENGTH_SHORT).show();
*/
        }
    }
}
