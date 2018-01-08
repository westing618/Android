package com.ztd.yyb.view;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    String getIconResId(int index);
    String getImageNoUrl(int index);
    // From PagerAdapter
    int getCount();
}
