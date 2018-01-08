package com.ztd.yyb.activity.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ztd.yyb.R;
import com.ztd.yyb.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class LaborAdapter extends RecyclerView.Adapter<LaborAdapter.MainViewHolder> {


    ArrayList<String> pic;
    Context context;
    LayoutInflater inflater;

    public LaborAdapter(Context context, ArrayList<String> pic) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.pic = pic;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater
                .inflate(R.layout.item_labor, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {

        holder.bindData(pic.get(position));

//        String s = pic.get(position);

        holder.image_labor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                List<String> photoUrls = new ArrayList<String>();
                for (int i = 0; i < pic.size(); i++) {
                    photoUrls.add(pic.get(i));
                }
                ImagePagerActivity.startImagePagerActivity(context, photoUrls, position, imageSize);

            }
        });


    }

    @Override
    public int getItemCount() {
        return pic.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        ImageView image_labor;

        public MainViewHolder(View itemView) {
            super(itemView);

            image_labor = (ImageView) itemView.findViewById(R.id.iv_imglabor);


        }

        public void bindData(String item) {

            if(!item.equals("")){

                Glide.with(context).load(Constants.BASE_URL + item)
                        .error(R.mipmap.mo_cal)
                        .placeholder(R.mipmap.mo_cal)
                        .fitCenter()
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image_labor);

            }




        }

    }
}
