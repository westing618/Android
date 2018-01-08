/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ztd.yyb.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ztd.yyb.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements
		PageIndicator {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";

	/**
	 * Interface for a callback when the selePageIndicatorted tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView) view;
			final int oldSelected = mViewPager.getCurrentItem();
			final int newSelected = tabView.getIndex();
			selectIndext=newSelected;
			mViewPager.setCurrentItem(newSelected);
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
			}
			notifyDataSetChanged();
			setTitlebar_FootImage(mViewPager.getCurrentItem());
		}
	};

	public TabLinearLayout mTabLayout;

	private ViewPager mViewPager;
	private OnPageChangeListener mListener;

//	private int mMaxTabWidth;
	private int mSelectedTabIndex;

	private OnTabReselectedListener mTabReselectedListener;

	public TabPageIndicator(Context context) {
		this(context, null);
	}

	@SuppressLint("NewApi")
	public TabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);
		mTabLayout = new TabLinearLayout(context);
		// mTabLayout.setGravity(Gravity.BOTTOM);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	public boolean isFen = false;
	public void setTitlebar_FootImage(int position){


		for (int i=0;i<mTabLayout.getChildCount();i++){
			if(mTabLayout==null){return;}
			TabPageIndicator.TabView tabView= (TabPageIndicator.TabView)mTabLayout.getChildAt(i);
			if(tabView==null){return;}
		}
	}
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {

			if (childCount > 2) {
//				mMaxTabWidth=(int)(MeasureSpec.getSize(widthMeasureSpec));
//				if (isFen) {
//					mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) / childCount);
//				} else {
//					if (childCount >= 4) {
//						mMaxTabWidth = (int) (MeasureSpec
//								.getSize(widthMeasureSpec) / childCount);
//					} else {
//						mMaxTabWidth = (int) (MeasureSpec
//								.getSize(widthMeasureSpec) / childCount);
//					}
//				}

			} else {
//				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {

//			mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) / 1);
			// mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		final int newWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}
	public int selectIndext=0;
//	private int []mTitlebar_PhoneImage={R.drawable.main_menu1,R.drawable.main_menu2,R.drawable.main_menu3};
	private void addTab(int index, CharSequence text) {

		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.tv_title.setText(text);
		if(!isChange){
//			tabView.imgs.setBackgroundResource(mTitlebar_PhoneImage[index]);
		}
		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0,
				MATCH_PARENT, 1));

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		final PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}
	private String titles[]={"首页","职位","我的"};
	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		IconPagerAdapter iconAdapter = null;
		if (adapter instanceof IconPagerAdapter) {
			iconAdapter = (IconPagerAdapter) adapter;
		}
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			CharSequence title = titles[i];
			if (title == null) {
				title = EMPTY_TITLE;
			}
//			String imageUrl = "";
//			String imageNoUrl="";
//			if (iconAdapter != null) {
//				imageUrl = iconAdapter.getIconResId(i);
//				imageNoUrl=iconAdapter.getImageNoUrl(i);
//			}
				addTab(i, title);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			// throw new IllegalStateException("ViewPager has not been bound.");
			return;
		}
		mSelectedTabIndex = item;
		mViewPager.setCurrentItem(item);
		// MainActivity.mEdit.putInt("TABNUM", item);
		// MainActivity.mEdit.commit();
		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				// ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_DIP,
				// 20);
				animateToTab(item);
			}
			// else {
			// ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			// }
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}
	public boolean isChange=false;
	public class TabView extends LinearLayout {
		public TabView(Context context) {
			super(context);
			View layview =null;
			if(isChange){
//				layview= LayoutInflater.from(context).inflate(
//						R.layout.lay_tabitem1, null);
			}else{
//				layview= LayoutInflater.from(context).inflate(
//						R.layout.lay_tabitem, null);
//				imgs = (ImageView) layview.findViewById(R.id.imgs);
			}
//			tv_title = (TextView) layview.findViewById(R.id.tv_title);
			addView(layview, new LayoutParams(0, MATCH_PARENT, 1));
		}

		private int mIndex;
		public ImageView imgs;
		public TextView tv_title;

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
//			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
//				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
//						MeasureSpec.EXACTLY), heightMeasureSpec);
//			}
		}

		public int getIndex() {
			return mIndex;
		}
	}

	public class TabLinearLayout extends LinearLayout {

		public TabLinearLayout(Context context) {
			super(context);

		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
//			 if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
//			super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth
//							* mTabLayout.getChildCount(), MeasureSpec.EXACTLY),
//					heightMeasureSpec);
//			 }
		}

		@Override
		protected void measureChildWithMargins(View child,
				int parentWidthMeasureSpec, int widthUsed,
				int parentHeightMeasureSpec, int heightUsed) {
			// TODO Auto-generated method stub
			super.measureChildWithMargins(child, parentWidthMeasureSpec,
					widthUsed, parentHeightMeasureSpec, heightUsed);
		}
	}
}
