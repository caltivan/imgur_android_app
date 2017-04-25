package imgur.mobilesolutions.com.imgur.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import imgur.mobilesolutions.com.imgur.R;
import imgur.mobilesolutions.com.imgur.model.GalleryImage;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by jgomez on 4/23/17.
 */
public class ImageAdapter extends BaseAdapter {
    private final ArrayList<GalleryImage> gallery;
    private final int slotResourceId;
    private final SharedPreferences prefs;
    private Context myContext;
    private static final String PREFERENCES_NAME = "preferences";
    private static final String LIST_VIEW_SPS = "list_view_sp";

    public ImageAdapter(Context context, int _slotResourceId, ArrayList<GalleryImage> _gallery) {
        myContext = context;
        gallery = _gallery;
        slotResourceId = _slotResourceId;
        prefs = myContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

    }

    public int getCount() {
        return gallery.size();
    }

    public Object getItem(int position) {
        return gallery.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(slotResourceId, null);
        }

        GalleryImage image = gallery.get(position);
        TextView title = (TextView) v.findViewById(R.id.imageTitleTextView);
        title.setText(image.title);

        ImageView imageView = (ImageView) v.findViewById(R.id.galleryImageView);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(myContext).setIndicatorsEnabled(true);
        RequestCreator build = Picasso.with(myContext).load(image.link);
        build.into(imageView);
        build.networkPolicy(NetworkPolicy.OFFLINE);
        build.error(R.mipmap.image_fail);

        int w = parent.getWidth();
        int h = parent.getHeight();
        boolean isListView = prefs.getBoolean(LIST_VIEW_SPS, false);
        if (isListView) {
            GridView.LayoutParams gridParams = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h/3);
            v.setLayoutParams(gridParams);
        } else {

            v.setLayoutParams(new GridView.LayoutParams(w / 3, h / 3));
        }
        v.setPadding(8, 8, 8, 8);
        return v;
    }


}