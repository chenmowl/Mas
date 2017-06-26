package com.eme.mas.customeview.convenentbanner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.home.HomePhoneVipBannerBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by Administrator on 2016/3/10.
 */
public class BrandBannerHolderView implements Holder<HomePhoneVipBannerBo> {

    private SimpleDraweeView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new SimpleDraweeView(context);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, HomePhoneVipBannerBo data) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(UriUtil.getImage(KConfig.HOST_URL + data.getImageurl()));
    }

}
