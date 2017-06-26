package com.eme.mas.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zulei
 * @date 2016-8-31
 */
public class WBaseRecycleViewHolder extends RecyclerView.ViewHolder {

	private SparseArray<View> mViews;

	private View mConvertView;

	public WBaseRecycleViewHolder(View itemView) {
		super(itemView);
		this.mConvertView = itemView;
		mViews = new SparseArray<View>();
		// TODO Auto-generated constructor stub
	}

	public static WBaseRecycleViewHolder get(View itemView) {
		return new WBaseRecycleViewHolder(itemView);
	}

	public View getConvertView() {
		return mConvertView;
	}

	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 设置textview的值
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public WBaseRecycleViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * 设置textview的值
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public WBaseRecycleViewHolder setText(int viewId, SpannableString text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * 设置图片
	 *
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public WBaseRecycleViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}

	/**
	 * 设置图片
	 *
	 * @param viewId
	 * @return
	 */
	public WBaseRecycleViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}



	/**
	 * 设置图片
	 *
	 * @param viewId
	 * @param resId
	 * @return
	 */
	// public ViewHolder setImageURI(int viewId, String url) {
	// ImageView view = getView(viewId);
	// // ImageLoader.getInstance.loadImg(view,url);
	// return this;
	// }

	/**
	 * 设置背景图片
	 *
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public WBaseRecycleViewHolder setBackgroundImage(int viewId, int resId) {
		View view = getView(viewId);
		view.setBackgroundResource(resId);
		return this;
	}

	/**
	 * 设置文字颜色
	 *
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public WBaseRecycleViewHolder setTextColor(int viewId, int resId) {
		TextView view = getView(viewId);
		view.setTextColor(resId);
		return this;
	}

	/**
	 * 设置点击事件
	 *
	 * @param viewId
	 * @return
	 */
	public WBaseRecycleViewHolder setOnClickListener(int viewId,
													 OnClickListener listener) {
		getView(viewId).setOnClickListener(listener);
		return this;
	}

}
