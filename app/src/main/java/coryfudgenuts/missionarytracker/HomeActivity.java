package coryfudgenuts.missionarytracker;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements ProfileHighlightsFragment.OnFragmentInteractionListener{
    private static String TAG = "HomeActivity";
    private ProfileHighlightsFragment myPHFrag;
    private Button myRefreshButton;
    private Fragment myFragContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myRefreshButton = findViewById(R.id.homeRefreshButton);
        myPHFrag = new ProfileHighlightsFragment();
        if(savedInstanceState == null) {
            if(findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, myPHFrag)
                        .commit();
            }
        }
        //myPHFrag = (ProfileHighlightsFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.SummaryLayout);
        myRefreshButton.setOnClickListener(this::onRefreshButtonClicked);
    }

    public void onRefreshButtonClicked(View view) {
        Log.d(TAG, "onRefreshButtonClicked.");
        if(myPHFrag != null) {
            Log.d(TAG, "Refresh button success branch.");
            myPHFrag.getRandomMissionary();
        }
        Log.d(TAG, "onRefreshButtonClicked end of block.");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
