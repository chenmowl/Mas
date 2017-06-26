package com.eme.mas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author zulei
 * @date 2016-8-31
 */
public abstract class WBaseRecycleAdapter<T> extends
		RecyclerView.Adapter<WBaseRecycleViewHolder> {

	private List<T> list;
	private int resourceID;
	private LayoutInflater inflater;

	/**
	 */
	public WBaseRecycleAdapter(Context context, List<T> list, int resourceID) {
		super();
		this.list = list;
		this.resourceID = resourceID;
		this.inflater = LayoutInflater.from(context);
	}


	public List<T> getDatas() {
		return list;
	}
	// RecyclerView显示的子View
	// 该方法返回是ViewHolder，当有可复用View时，就不再调用
	@Override
	public WBaseRecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = inflater.inflate(resourceID, viewGroup,
				false);
		return WBaseRecycleViewHolder.get(v);
	}

	// 将数据绑定到子View
	@Override
	public void onBindViewHolder(WBaseRecycleViewHolder viewHolder, int i) {
		onBindViewHolder(viewHolder, i,list.get(i));

	}

	// RecyclerView显示数据条数
	@Override
	public int getItemCount() {
		return list.size();
	}

	public abstract void onBindViewHolder(WBaseRecycleViewHolder holder, int position, T t);


	public OnItemClickListener<T> mOnItemClickListener;


	// 点击事件接口
	public interface OnItemClickListener<T> {
		void onItemClick(View view, int position, T model);

		void onItemLongClick(View view, int position, T model);
	}

	public void setOnItemClickListener(OnItemClickListener<T> listener) {
		this.mOnItemClickListener = listener;
	}


}
