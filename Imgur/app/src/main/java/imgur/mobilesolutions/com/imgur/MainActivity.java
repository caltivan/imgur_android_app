package imgur.mobilesolutions.com.imgur;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import imgur.mobilesolutions.com.imgur.adapter.ImageAdapter;
import imgur.mobilesolutions.com.imgur.model.GalleryImage;
import imgur.mobilesolutions.com.imgur.util.AppSettings;
import imgur.mobilesolutions.com.imgur.util.ConnectionHelper;


/**
 * Created by jgomez on 4/23/17.
 */
public class MainActivity extends AppCompatActivity {


    Context myContext;
    GridView gridview;
    Toolbar myToolbar;
    private ImageAdapter galleryAdapter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private boolean isListView;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myContext = this;
        prefs = myContext.getSharedPreferences(AppSettings.PREFERENCES_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        myToolbar = (Toolbar) findViewById(R.id.gallery_toolbar);
        gridview = (GridView) findViewById(R.id.galleryView);

        toolbarSetup();
        gridViewSetup();
        gridSetup();
    }

    private void toolbarSetup() {
        setSupportActionBar(myToolbar);
        myToolbar.setTitle(myContext.getApplicationInfo().name);
        myToolbar.setTitleTextColor(Color.WHITE);
    }

    private void gridViewSetup() {
        isListView = prefs.getBoolean(AppSettings.LIST_VIEW_SP, false);
        if (isListView) {
            gridview.setNumColumns(1);
        } else {
            gridview.setNumColumns(3);
        }
        loadGalleryTask();
    }

    private void gridSetup() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                GalleryImage image = (GalleryImage) galleryAdapter.getItem(position);
                FragmentManager fm = getFragmentManager();
                PreviewDialog dialogFragment = new PreviewDialog();
                Bundle args = new Bundle();
                args.putString(AppSettings.PREVIEW_IMAGE_INTENT, gson.toJson(image));
                dialogFragment.setArguments(args);
                dialogFragment.show(fm, "");
            }
        });
    }

    private void loadGalleryTask() {
        new AsyncTask<Void, Void, ArrayList<GalleryImage>>() {
            ConnectionHelper connectionhelper = new ConnectionHelper();
            ArrayList<GalleryImage> gallery;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                gallery = new ArrayList<>();
            }

            @Override
            protected ArrayList<GalleryImage> doInBackground(Void... voids) {
                boolean hot = prefs.getBoolean(AppSettings.VIRAL_CHECK_SP, false);
                boolean viral = prefs.getBoolean(AppSettings.HOT_CHECK_SP, false);
                String result = connectionhelper.getImgurGallerySevice(hot, viral);
                try {
                    if (result != null) {
                        JSONObject data = new JSONObject(result);
                        if (data.getJSONArray("data") != null && data.getJSONArray("data").length() > 0) {
                            JSONArray items = data.getJSONArray("data");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject imageJson = items.getJSONObject(i);
                                if (!imageJson.getBoolean("is_album")) {
                                    GalleryImage image = gson.fromJson(imageJson.toString(),GalleryImage.class);
                                    gallery.add(image);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return gallery;
            }

            @Override
            protected void onPostExecute(ArrayList<GalleryImage> result) {
                super.onPostExecute(result);
                galleryAdapter = new ImageAdapter(myContext, R.layout.gallery_slot_view, result);
                gridview.setAdapter(galleryAdapter);
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        boolean hot = prefs.getBoolean(AppSettings.VIRAL_CHECK_SP, false);
        boolean viral = prefs.getBoolean(AppSettings.HOT_CHECK_SP, false);
        boolean list = prefs.getBoolean(AppSettings.LIST_VIEW_SP, false);

        menu.findItem(R.id.action_hot).setChecked(hot);
        menu.findItem(R.id.action_viral).setChecked(viral);
        menu.findItem(R.id.action_grid_view).setChecked(list);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hot:
                menuFilterAction(item, AppSettings.HOT_CHECK_SP);
                return true;
            case R.id.action_viral:
                menuFilterAction(item, AppSettings.VIRAL_CHECK_SP);
                return true;
            case R.id.action_grid_view:
                menuFilterAction(item, AppSettings.LIST_VIEW_SP);
                return true;
            case R.id.about_action:
                FragmentManager fm = getFragmentManager();
                AboutFragmentDialog dialogFragment = new AboutFragmentDialog();
                dialogFragment.show(fm, "");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void menuFilterAction(MenuItem item, String spValue) {
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        editor.putBoolean(spValue, item.isChecked());
        editor.commit();
        gridViewSetup();

    }
}
