package org.succlz123.blueboard.controller.activity.acfun;

import org.succlz123.blueboard.R;
import org.succlz123.blueboard.base.BaseActivity;
import org.succlz123.blueboard.controller.fragment.other.AcPartitionFragment;
import org.succlz123.blueboard.model.api.acfun.AcString;
import org.succlz123.blueboard.model.utils.common.ViewUtils;
import org.succlz123.blueboard.view.adapter.fragment.AcPartitionFmAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by succlz123 on 2015/7/27.
 */
public class AcPartitionActivity extends BaseActivity {

    public static void startActivity(Context activity, String partitionType) {
        Intent intent = new Intent(activity, AcPartitionActivity.class);
        intent.putExtra(AcString.CHANNEL_IDS, partitionType);
        activity.startActivity(intent);
    }

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private String mPartitionType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_activity_partition);

        mTabLayout = f(R.id.fragment_partition_tab_layout);
        mViewPager = f(R.id.fragment_partition_viewpager);
        mToolbar = f(R.id.toolbar);

        //根据partitionType来判断什么分区
        mPartitionType = getIntent().getStringExtra(AcString.CHANNEL_IDS);

        ViewUtils.setToolbar(AcPartitionActivity.this, mToolbar, true, mPartitionType);

        AcPartitionFmAdapter adapter = new AcPartitionFmAdapter(getSupportFragmentManager(), mPartitionType);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        if (TextUtils.equals(mPartitionType, AcString.TITLE_HOT)) {
            mTabLayout.setVisibility(View.GONE);
        }
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setHttpOrder(String order, String title) {
        SharedPreferences settings = getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(AcString.ORDER_BY, order);
        editor.putString(AcString.TITLE, title);
        editor.commit();

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getUserVisibleHint() && fragment instanceof AcPartitionFragment) {
                ((AcPartitionFragment) fragment).setHttpOrder();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!TextUtils.equals(mPartitionType, AcString.TITLE_HOT)
                && !TextUtils.equals(mPartitionType, AcString.TITLE_RANKING)) {
            MenuItem timeOrderItem = menu.add(Menu.NONE, 1, 100, AcString.TITLE_TIME_ORDER);
            MenuItem lastPostItem = menu.add(Menu.NONE, 2, 100, AcString.TITLE_LAST_POST);
            MenuItem mostReplyItem = menu.add(Menu.NONE, 3, 100, AcString.TITLE_MOST_REPLY);
            MenuItem popularityItem = menu.add(Menu.NONE, 4, 100, AcString.TITLE_POPULARITY);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case 1:
                setHttpOrder(AcString.TIME_ORDER, AcString.TITLE_TIME_ORDER);
                break;
            case 2:
                setHttpOrder(AcString.LAST_POST, AcString.TITLE_LAST_POST);
                break;
            case 3:
                setHttpOrder(AcString.MOST_REPLY, AcString.TITLE_MOST_REPLY);
                break;
            case 4:
                setHttpOrder(AcString.POPULARITY, AcString.TITLE_POPULARITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
