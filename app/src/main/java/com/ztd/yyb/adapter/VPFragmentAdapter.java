/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ztd.yyb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/*
    * viewpager适配器
 */
public class VPFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mListFragments = null;

    public VPFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mListFragments = fragments;
    }

    @Override
    public int getCount() {
        return null != mListFragments ? mListFragments.size() : 0;
    }

    @Override
    public Fragment getItem(int index) {
        if (mListFragments != null && index > -1 && index < mListFragments.size()) {
            return mListFragments.get(index);
        } else {
            return null;
        }
    }

}
