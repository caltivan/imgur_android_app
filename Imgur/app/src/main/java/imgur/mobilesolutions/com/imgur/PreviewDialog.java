package imgur.mobilesolutions.com.imgur;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import imgur.mobilesolutions.com.imgur.model.GalleryImage;
import imgur.mobilesolutions.com.imgur.util.AppSettings;

/**
 * Created by jgomez on 4/24/17.
 */

public class PreviewDialog extends DialogFragment {

    GalleryImage image;
    Gson gson = new Gson();
    private ImageView imageView;
    private Context myContext;
    private TextView imageTitle;
    private TextView imageDescription;
    private TextView scoreTextView;
    private TextView upsTextView;
    private TextView downsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_preview_dialog, container, false);
        myContext = this.getActivity();
        getDialog().setTitle(myContext.getString(R.string.preview_dialog_title));
        imageView = (ImageView) rootView.findViewById(R.id.galleryImageView);
        imageTitle = (TextView) rootView.findViewById(R.id.titleTextView);
        imageDescription = (TextView) rootView.findViewById(R.id.descriptionTextView);
        scoreTextView = (TextView) rootView.findViewById(R.id.scoreTextView);
        upsTextView = (TextView) rootView.findViewById(R.id.upsTextView);
        downsTextView = (TextView) rootView.findViewById(R.id.downsTextView);

        String imageJSON = getArguments().getString(AppSettings.PREVIEW_IMAGE_INTENT);
        image = gson.fromJson(imageJSON, GalleryImage.class);

        imageTitle.setText(image.title);
        if (image.description != null) {
            imageDescription.setText(image.description);
        } else {
            imageDescription.setText(myContext.getString(R.string.image_without_description));
        }
        scoreTextView.setText(String.valueOf(image.score));
        upsTextView.setText(String.valueOf(image.ups));
        downsTextView.setText(String.valueOf(image.downs));


        Picasso.with(myContext).load(image.link).into(imageView);
        return rootView;
    }
}
