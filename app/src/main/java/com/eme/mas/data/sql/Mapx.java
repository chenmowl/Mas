package com.eme.mas.data.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
/**
 * 扩展了的LinkedHashMap<br>
 */
public class Mapx<K, V> extends LinkedHashMap<K, V> {
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private static final int DEFAULT_INIT_CAPACITY = 16;

	private static final long serialVersionUID = 200904201752L;

	private final int maxCapacity;

	private final boolean maxFlag;

	private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

	private Lock rlock = rwlock.readLock();

	private Lock wlock = rwlock.writeLock();

	/**
	 * 有最大容量限制的HashMap,当LRUFlag为true时按LRU算法换入换出,为false时按FIFO算法换入换出
	 */
	public Mapx(int maxCapacity, boolean LRUFlag) {
		super(maxCapacity, DEFAULT_LOAD_FACTOR, LRUFlag);
		this.maxCapacity = maxCapacity;
		maxFlag = true;
	}

	/**
	 * 键值无顺序,有最大容量,使用LRU算法换入换出的HashMap
	 */
	public Mapx(int maxCapacity) {
		this(maxCapacity, true);
	}

	/**
	 * 键值按加入先后顺序排序的HashMap,没有容量限制,不支持换入换出
	 */
	public Mapx() {
		super(DEFAULT_INIT_CAPACITY, DEFAULT_LOAD_FACTOR, false);
		maxCapacity = 0;
		maxFlag = false;
	}

	/**
	 * 递归Clone,防止Mapx中有Mapx时出现同步问题
	 */
	@SuppressWarnings("unchecked")
	public Mapx<K, V> clone() {
		Mapx<K, V> map = (Mapx<K, V>) super.clone();
		for (K k : keyArray()) {
			Object v = get(k);
			if (v instanceof Mapx) {
				map.put(k, (V) ((Mapx<?, ?>) v).clone());
			}
		}
		return map;
	}

	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		boolean flag = maxFlag && size() > maxCapacity;
		return flag;
	}

	public String getString(K key) {
		Object o = get(key);
		if (o == null) {
			return null;
		} else {
			return o.toString();
		}
	}

	public int getInt(K key) {
		Object o = get(key);
		if (o instanceof Number) {
			return ((Number) o).intValue();
		} else if (o != null) {
			try {
				return Integer.parseInt(o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public long getLong(K key) {
		Object o = get(key);
		if (o instanceof Number) {
			return ((Number) o).longValue();
		} else if (o != null) {
			try {
				return Long.parseLong(o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public Date getDate(K key) {
		Object o = get(key);
		if (o instanceof Date) {
			return (Date) o;
		} else if (o != null) {
			try {
				return DateUtil.parse(o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 返回键值列表，此方法与keySet()的区别在于：<br>
	 * 1、keySet()在大数据量下性能较好<br>
	 * 2、keySet()遍历时修改Map会有ConcurrentModificationException，但keyArray()不会
	 */
	public ArrayList<K> keyArray() {
		ArrayList<K> list = new ArrayList<K>();
		for (K k : keySet()) {
			list.add(k);
		}
		return list;
	}

	/**
	 * 返回键值列表，此方法与values()的区别在于：<br>
	 * 1、values()在大数据量下性能较好<br>
	 * 2、values()遍历时修改Map会有ConcurrentModificationException，但valueArray()不会
	 */
	public ArrayList<V> valueArray() {
		ArrayList<V> list = new ArrayList<V>();
		for (V v : values()) {
			list.add(v);
		}
		return list;
	}

	public boolean containsKey(Object k) {
		rlock.lock();
		try {
			return super.containsKey(k);
		} finally {
			rlock.unlock();
		}
	}

	public void clear() {
		wlock.lock();
		try {
			super.clear();
		} finally {
			wlock.unlock();
		}
	}

	public V put(K k, V v) {
		wlock.lock();
		try {
			return super.put(k, v);
		} finally {
			wlock.unlock();
		}
	}

	public V remove(Object k) {
		wlock.lock();
		try {
			return super.remove(k);
		} finally {
			wlock.unlock();
		}
	}

	public boolean containsValue(Object v) {
		rlock.lock();
		try {
			return super.containsValue(v);
		} finally {
			rlock.unlock();
		}
	}

	/**
	 * 将一个Map转为Mapx
	 */
	public static <K, V> Mapx<K, V> convertToMapx(Map<? extends K, ? extends V> map) {
		Mapx<K, V> mapx = new Mapx<K, V>();
		mapx.putAll(map);
		return mapx;
	}

	/**
	 * 将一个Map转为含有Key和Value两个字段的DataTable
	 */
	public DataTable toDataTable() {
		DataColumn[] dcs = new DataColumn[] { new DataColumn("Key", DataColumn.STRING), new DataColumn("Value", DataColumn.STRING) };
		Object[] ks = keySet().toArray();
		Object[][] vs = new Object[ks.length][2];
		DataTable dt = new DataTable(dcs, vs);
		for (int i = 0; i < ks.length; i++) {
			dt.set(i, 0, ks[i]);
			dt.set(i, 1, get(ks[i]));
		}
		return dt;
	}
}