package cliches.com.cliche.screens.mission;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import cliches.com.cliche.R;
import cliches.com.cliche.databinding.ActivityMissionBinding;
import cliches.com.cliche.models.Mission;
import cliches.com.cliche.screens.mission.MissionPresenter;
import cliches.com.cliche.screens.mission.SpotsAdapter;
import cliches.com.cliche.models.Spot;
import cliches.com.cliche.screens.spot.SpotActivity;
import cliches.com.cliche.utils.GridSpacingDecorator;
import cliches.com.cliche.utils.ResourcesHelper;
import timber.log.Timber;

public class MissionActivity extends AppCompatActivity implements MissionPresenter.ViewActions {

    public static final String MISSION_KEY = "mission_key";

    ActivityMissionBinding mViewBinding;
    MissionPresenter mPresenter;
    SpotsAdapter mSpotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        Mission mission = (Mission) getIntent().getSerializableExtra(MISSION_KEY);
        if(mission == null) {
            Timber.w("Trying to open MissionActivity without a missions");
            return;
        }

        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_mission);
        mPresenter = new MissionPresenter(this, mission);
        mSpotAdapter = new SpotsAdapter(this, mPresenter);

        setupToolbar();
        setupRecyclerView();

        getSupportActionBar().setTitle(mission.name);
        mViewBinding.setPresenter(mPresenter);

        mPresenter.refreshDisplay();
        mPresenter.refreshSpots();
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(mSpotAdapter.getSpanLookup());
        mViewBinding.spotsList.setLayoutManager(layoutManager);
        mViewBinding.spotsList.setAdapter(mSpotAdapter);
        mViewBinding.spotsList.addItemDecoration(new GridSpacingDecorator(2, ResourcesHelper.get().dpToPx(4)));
    }

    private void setupToolbar() {
        setSupportActionBar(mViewBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void notifyNewData() {
        mSpotAdapter.notifyDataSetChanged();
    }

    @Override
    public void open(Spot spot, View sharedView) {

        String transitionName = getString(R.string.spot_image_transition);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, sharedView, transitionName);

        Intent intent = new Intent(this, SpotActivity.class);
        intent.putExtra(SpotActivity.SPOT_KEY, spot);

        startActivity(intent, transitionActivityOptions.toBundle());
    }
}
