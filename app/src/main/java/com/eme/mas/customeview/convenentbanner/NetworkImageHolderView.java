package com.eme.mas.customeview.convenentbanner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by Administrator on 2016/3/10.
 */
public class NetworkImageHolderView implements Holder<String> {

    private SimpleDraweeView imageView;
//    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new SimpleDraweeView(context);
//        imageView = new ImageView(context);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {
//        SharedPreferences spLogin =context.getSharedPreferences("homePage", Context.MODE_PRIVATE);
//        float aspect=spLogin.getFloat("bannerAspectRatio",2.65f);
//        imageView.setAspectRatio(2.65f);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageURI(UriUtil.getImage(data));
//        imageView.setAdjustViewBounds(true);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setImageResource(R.mipmap.llb_banner);
    }
}
