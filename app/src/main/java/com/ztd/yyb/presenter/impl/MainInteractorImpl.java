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

package com.ztd.yyb.presenter.impl;

import android.support.v4.app.Fragment;

import com.ztd.yyb.fragment.DemandFragment;
import com.ztd.yyb.fragment.MessageFragment;
import com.ztd.yyb.fragment.MianFragment;
import com.ztd.yyb.fragment.MyFragment;
import com.ztd.yyb.presenter.MainInteractor;

import java.util.ArrayList;
import java.util.List;

/*
    MainInteractorImpl 左菜单栏导航选项设置，和导航点击后对应的大片段容器初始
 */
public class MainInteractorImpl implements MainInteractor {

    @Override
    public List<Fragment> getPagerFragments() {
        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new IndextFragment());
//        fragments.add(new JobFragment());
        fragments.add(new MianFragment());
        fragments.add(new DemandFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MyFragment());

        return fragments;
    }

}
