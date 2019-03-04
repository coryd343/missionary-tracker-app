package coryfudgenuts.missionarytracker.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

public class GetAsyncTask extends AsyncTask<Void, String, String> {
    //The http endpoint to hit.
    private final String mUrl;
    private Runnable mOnPre;
    private Consumer<String[]> mOnProg;
    private Consumer<String> mOnPost;
    private Consumer<String> mOnCancel;

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection urlConnection = null;

        try {
            URL urlObject = new URL(mUrl.toString());
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            InputStream content = urlConnection.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response.append(s);
            }
        }
        catch(Exception e) {
            response.append("HTTP connection failure: " + e.getMessage());
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return response.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mOnPre.run();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        mOnProg.accept(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mOnPost.accept(result);
    }

    @Override
    protected void onCancelled(String result) {
        super.onCancelled(result);
        mOnCancel.accept(result);
    }

    public GetAsyncTask(final Builder builder) {
        mUrl = builder.mUrl;
        mOnPre = builder.onPre;
        mOnProg = builder.onProg;
        mOnPost = builder.onPost;
        mOnCancel = builder.onCancel;
    }

    public static class Builder {
        private final String mUrl;
        private Runnable onPre = () -> {};
        private Consumer<String[]> onProg = x -> {};
        private Consumer<String> onPost = x -> {};
        private Consumer<String> onCancel = x -> {};

        public Builder(final String url) {
            mUrl = url;
        }

        public Builder onPreExecute(final Runnable method) {
            onPre = method;
            return this;
        }

        public Builder onProgressUpdate(final Consumer <String[]> method) {
            onProg = method;
            return this;
        }

        public Builder onPostExecute(final Consumer<String> method) {
            onPost = method;
            return this;
        }

        public Builder onCancelled(final Consumer<String> method) {
            onCancel = method;
            return this;
        }

        public GetAsyncTask build() {
            return new GetAsyncTask(this);
        }
    }
}
