package com.ztd.yyb.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaeger.library.StatusBarUtil;
import com.ztd.yyb.R;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.view.HackyViewPager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;


public class ViewPagerActivity extends Activity {

    private HackyViewPager mViewPager;
    private ArrayList<String> pic = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        StatusBarUtil.setColor(this, R.color.color_reb);//设置状态栏颜色
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("查看图片");

        findViewById(R.id.lin_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        pic = intent.getStringArrayListExtra("imageUrl");
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        SamplePagerAdapter samplePagerAdapter = new SamplePagerAdapter();
        mViewPager.setAdapter(samplePagerAdapter);

    }

    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pic.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            PhotoView photoView = new PhotoView(container.getContext());

//			UILUtils.displayImage(sDrawables.get(position), photoView);



            Glide.with(ViewPagerActivity.this).load(Constants.BASE_URL + pic.get(position))
                    .error(R.mipmap.load_init)
                    .placeholder(R.mipmap.load_init)
                    .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photoView);


//            PicassoUtil.getInstance().getPicasso().with(ViewPagerActivity.this)
//                    .load(Constants.BASE_URL + pic.get(position))
//                    .error(R.mipmap.load_init)
//                    .placeholder(R.mipmap.load_init)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop()
//                    .fit()
//                    .into(photoView);


            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


}
