package com.eme.mas.data.sql;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;


/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class ObjectUtil {
	/**
	 * 检查第一个参数是否等于后续参数中的一个。<br>
	 * 如第一个参数不是数组或者集合，但后续参数中有数组或者集合，则会取出其中的元素逐一比较
	 */
	public static boolean in(Object... args) {
		if (args == null || args.length < 2) {
			return false;
		}
		Object arg1 = args[0];
		for (int i = 1; i < args.length; i++) {
			if (arg1 == null) {
				if (args[i] == null) {
					return true;
				}
			} else {
				if (arg1.equals(args[i])) {
					return true;
				} else {
					// 如果后续参数是数组，则遍历数组并比较
					if (!arg1.getClass().isArray() && args[i].getClass().isArray()) {
						for (Object obj : (Object[]) args[i]) {
							if (arg1.equals(obj)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean notIn(Object... args) {
		return !in(args);
	}

	/**
	 * 符合如下条件返回true： <br>
	 * 1、等于null<br>
	 * 2、equals("") <br>
	 * 3、java.lang.Number的子类且其值等于0
	 */
	public static boolean empty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return obj.equals("");
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue() == 0;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).size() == 0;
		}
		return false;
	}

	/**
	 * 不等于null、0、空字符串时返回true
	 */
	public static boolean notEmpty(Object obj) {
		return !empty(obj);
	}

	/**
	 * 判断两个参数是否相等
	 */
	public static boolean equal(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 == null) {
			if (obj2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return obj1.equals(obj2);
		}
	}

	/**
	 * 判断两个参数是否不相等
	 */
	public static boolean notEqual(Object obj1, Object obj2) {
		return !equal(obj1, obj2);
	}

	/**
	 * 获得传入的所有参数中值最小的
	 */
	public static Number minNumber(double... args) {
		if (args == null || args.length == 0) {
			return null;
		}
		Number minus = null;
		for (double t : args) {
			if (minus == null) {
				minus = t;
				continue;
			}
			if (minus.doubleValue() > t) {
				minus = t;
			}
		}
		return minus;
	}

	/**
	 * 获得传入的所有参数中值最大的
	 */
	public static Number maxNumber(double... args) {
		if (args == null || args.length == 0) {
			return null;
		}
		Number max = null;
		for (double t : args) {
			if (max == null) {
				max = t;
				continue;
			}
			if (max.doubleValue() < t) {
				max = t;
			}
		}
		return max;
	}

	/**
	 * 获得传入的所有参数中值最小的
	 */
	public static <T extends Comparable<T>> T min(T... args) {
		if (args == null || args.length == 0) {
			return null;
		}
		T minus = null;
		for (T t : args) {
			if (minus == null && t != null) {
				minus = t;
				continue;
			}
			if (minus.compareTo(t) > 0) {
				minus = t;
			}
		}
		return minus;
	}

	/**
	 * 获得传入的所有参数中值最大的
	 */
	public static <T extends Comparable<T>> T max(T... args) {
		if (args == null || args.length == 0) {
			return null;
		}
		T max = null;
		for (T t : args) {
			if (max == null && t != null) {
				max = t;
				continue;
			}
			if (max.compareTo(t) < 1) {
				max = t;
			}
		}
		return max;
	}

	/**
	 * 如果第一个参数不为空，则返回第一个参数，否则返回第二个参数
	 */
	public static <T> T ifEmpty(T obj1, T obj2) {
		return empty(obj1) ? obj2 : obj1;
	}

	/**
	 * 如果是null则返回null，否则返回Object.toString()
	 */
	public static String toString(Object obj) {
		return obj == null ? null : obj.toString();
	}

	/**
	 * 将参数组织成一个List
	 */
	public static <T> List<T> toList(T... args) {
		ArrayList<T> list = new ArrayList<T>();
		for (T t : args) {
			list.add(t);
		}
		return list;
	}

	public static int[] toIntArray(Object[] arr) {
		int[] arr2 = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Integer.parseInt(String.valueOf(arr[i]));
		}
		return arr2;
	}

	public static long[] toLongArray(Object[] arr) {
		long[] arr2 = new long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Long.parseLong(String.valueOf(arr[i]));
		}
		return arr2;
	}

	public static float[] toFloatArray(Object[] arr) {
		float[] arr2 = new float[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Float.parseFloat(String.valueOf(arr[i]));
		}
		return arr2;
	}

	public static double[] toDoubleArray(Object[] arr) {
		double[] arr2 = new double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Double.parseDouble(String.valueOf(arr[i]));
		}
		return arr2;
	}

	public static boolean[] toBooleanArray(Object[] arr) {
		boolean[] arr2 = new boolean[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Boolean.valueOf(String.valueOf(arr[i]));
		}
		return arr2;
	}

	public static String[] toStringArray(Object[] arr) {
		String[] arr2 = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = String.valueOf(arr[i]);
		}
		return arr2;
	}

	public static <T> Object[] toObjectArray(T[] arr) {
		Object[] arr2 = new Object[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = arr[i];
		}
		return arr2;
	}

	public static Object[] toObjectArray(Object arrayObject) {
		if (arrayObject == null) {
			return null;
		}
		if (!arrayObject.getClass().isArray()) {
			throw new RuntimeException("Not an array!");
		}
		if (arrayObject instanceof byte[]) {
			return toObjectArray((byte[]) arrayObject);
		}
		if (arrayObject instanceof short[]) {
			return toObjectArray((short[]) arrayObject);
		}
		if (arrayObject instanceof int[]) {
			return toObjectArray((int[]) arrayObject);
		}
		if (arrayObject instanceof long[]) {
			return toObjectArray((long[]) arrayObject);
		}
		if (arrayObject instanceof float[]) {
			return toObjectArray((float[]) arrayObject);
		}
		if (arrayObject instanceof double[]) {
			return toObjectArray((double[]) arrayObject);
		}
		if (arrayObject instanceof boolean[]) {
			return toObjectArray((boolean[]) arrayObject);
		}
		return (Object[]) arrayObject;
	}

	/**
	 * 返回排序后的新数组，原数组保持不变。
	 */
	public static <T> T[] sort(T[] arr, Comparator<T> c) {
		if (arr == null || arr.length == 0) {
			return arr;
		}
		@SuppressWarnings("unchecked")
		T[] a = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length);
		int i = 0;
		for (T t : arr) {
			a[i++] = t;
		}
		Arrays.sort(a, c);
		return a;
	}

	/**
	 * 返回过排序后的新List，原List保持不变。
	 */
	public static <T> List<T> sort(List<T> arr, Comparator<T> c) {
		if (arr == null || arr.size() == 0) {
			return arr;
		}
		try {
			@SuppressWarnings("unchecked")
			T[] a = (T[]) Array.newInstance(arr.toArray().getClass().getComponentType(), arr.size());
			a = arr.toArray(a);
			Arrays.sort(a, c);
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) arr.getClass().newInstance();
			for (T t : a) {
				list.add(t);
			}
			return list;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回排序掉后的新Mapx，原Mapx保持不变。
	 */
	public static <K, V> Mapx<K, V> sort(Mapx<K, V> map, Comparator<Entry<K, V>> c) {
		if (map == null || map.size() == 0) {
			return map;
		}
		try {
			@SuppressWarnings("unchecked")
			Entry<K, V>[] a = (Entry<K, V>[]) Array.newInstance(Entry[].class.getComponentType(), map.size());
			@SuppressWarnings("unchecked")
			Mapx<K, V> map2 = (Mapx<K, V>) map.getClass().newInstance();
			a = map.entrySet().toArray(a);
			Arrays.sort(a, c);
			for (Entry<K, V> t : a) {
				map2.put(t.getKey(), t.getValue());
			}
			return map2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串顺序排序器
	 */
	public static Comparator<String> ASCStringComparator = new Comparator<String>() {
		public int compare(String s1, String s2) {
			if (s1 == null) {
				if (s2 == null) {
					return 0;
				} else {
					return -1;
				}
			} else {
				if (s2 == null) {
					return 1;
				}
				return s1.compareTo(s2);
			}
		}
	};

	/**
	 * 字符串逆序排序器
	 */
	public static Comparator<String> DESCStringComparator = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return -ASCStringComparator.compare(o1, o2);
		}
	};

	public static String getCurrentStack() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stack.length; i++) {
			StackTraceElement ste = stack[i];
			if (ste.getClassName().indexOf("ObjectUtil.getCurrentStack") == -1) {
				sb.append("\t");
				sb.append(ste.getClassName());
				sb.append(".");
				sb.append(ste.getMethodName());
				sb.append("(),行号:");
				sb.append(ste.getLineNumber());
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 主要是为了方便转换一部分JDK1.6源代码中的Arrays.copyOf()调用
	 */
	public static <T> T[] copyOf(T[] arr, int length) {
		@SuppressWarnings("unchecked")
		T[] copy = (T[]) Array.newInstance(arr.getClass().getComponentType(), length);
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	public static byte[] copyOf(byte[] arr, int length) {
		byte[] copy = new byte[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	public static boolean[] copyOf(boolean[] arr, int length) {
		boolean[] copy = new boolean[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	public static float[] copyOf(float[] arr, int length) {
		float[] copy = new float[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	public static int[] copyOf(int[] arr, int length) {
		int[] copy = new int[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	public static boolean isBoolean(String v) {
		return "true".equals(v) || "false".equals(v);
	}
}
