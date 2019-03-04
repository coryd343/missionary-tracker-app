package coryfudgenuts.missionarytracker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
    ImageView myImage;

    public GetImageFromUrl(ImageView destination) {
        this.myImage = destination;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urldisplay = strings[0];
        Bitmap result = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            result = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap response) {
        super.onPostExecute(response);
        myImage.setImageBitmap(response);
    }
}
