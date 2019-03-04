package coryfudgenuts.missionarytracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import coryfudgenuts.missionarytracker.utils.GetAsyncTask;
import coryfudgenuts.missionarytracker.utils.GetImageFromUrl;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileHighlightsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileHighlightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileHighlightsFragment extends Fragment {
    private String TAG = "ProfileHighlights";
    private JSONObject myRandomMissionaryJson;

    //Screen elements for profile
    private TextView myMissionaryGroupName;
    private TextView myMissionField;
    private ImageView myDefaultPic;

    private OnFragmentInteractionListener mListener;

    public ProfileHighlightsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileHighlightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileHighlightsFragment newInstance(String param1, String param2) {
        ProfileHighlightsFragment fragment = new ProfileHighlightsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_highlights, container, false);
        myMissionaryGroupName = view.findViewById(R.id.SummaryName);
        myMissionField = view.findViewById(R.id.SummaryMissionField);
        myDefaultPic = view.findViewById(R.id.SummaryMissionaryImg);
        getRandomMissionary();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getRandomMissionary() {
        Uri uri = new Uri.Builder().scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_random_missionary))
                .build();
        Log.d(TAG, uri.toString());
        new GetAsyncTask.Builder(uri.toString())
                .onPostExecute(this::handleGetRandMissionary)
                .build().execute();
    }

    public void handleGetRandMissionary(String response) {
        Log.d(TAG, response);
        try {
            myRandomMissionaryJson = new JSONObject(response);
            Log.d(TAG, myRandomMissionaryJson.toString());

            if(myRandomMissionaryJson.getBoolean("success")) {
                JSONObject missionary = myRandomMissionaryJson.getJSONObject("missionary");
                String groupName = missionary.getString("groupname");
                String missionField = missionary.getString("missionfield");
                String defaultPicUrl = missionary.getString("defaultpiclink");

                myMissionaryGroupName.setText(groupName);
                myMissionField.setText(missionField);
                if(!defaultPicUrl.isEmpty()) {
                    new GetImageFromUrl(myDefaultPic).execute(defaultPicUrl);
                }
            }
            else {
                Log.e(TAG, "getRandomMissionary API call returned false.");
            }

        } catch (JSONException e) {
            Log.e(TAG, "getRandomMissionary API call HTTP Connection failed.");
            e.printStackTrace();
        }
    }
}
