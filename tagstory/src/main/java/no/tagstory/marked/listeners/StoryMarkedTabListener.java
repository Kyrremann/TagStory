package no.tagstory.marked.listeners;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v4.view.ViewPager;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StoryMarkedTabListener implements ActionBar.TabListener {

	private ViewPager mViewPager;

	public StoryMarkedTabListener(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
}
