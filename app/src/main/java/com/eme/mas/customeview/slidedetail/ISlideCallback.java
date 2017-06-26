package com.eme.mas.customeview.slidedetail;

/**
 *
 * 上拉加载商品详情的回调
 * <b>Project:</b> SlideDetailsLayout<br>
 * <b>Create Date:</b> 16/1/25<br>
 * <b>Author:</b> Gordon<br>
 * <b>Description:</b> <br>
 */
public interface ISlideCallback {

    void openDetails(boolean smooth);

    void closeDetails(boolean smooth);
}
