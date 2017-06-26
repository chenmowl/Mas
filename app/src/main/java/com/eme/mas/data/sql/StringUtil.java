package com.eme.mas.data.sql;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class StringUtil {
	/**
	 * UTF-8的三个字节的BOM
	 */
	public static final byte[] BOM = new byte[] { (byte) 239, (byte) 187, (byte) 191 };

	/**
	 * 十六进制字符
	 */
	public static final char HexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取指定字符串的MD5摘要
	 */
	public static byte[] md5(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src.getBytes());
			return md;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取指定二进制数组的MD5摘要
	 */
	public static byte[] md5(byte[] src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src);
			return md;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将字符串进行md5摘要，然后输出成十六进制形式
	 */
	public static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src.getBytes());
			return hexEncode(md);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将字符串进行sh1摘要，然后输出成十六进制形式
	 */
	public static String sha1Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA1");
			byte[] md = md5.digest(src.getBytes());
			return hexEncode(md);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将二进制数组转换成十六进制表示
	 */
	public static String hexEncode(byte[] bs) {
		return new String(new Hex().encode(bs));
	}

	/**
	 * 将字符串转换成十六进制表示
	 */
	public static byte[] hexDecode(String str) {
		try {
			if (str.endsWith("\n")) {
				str = str.substring(0, str.length() - 1);
			}
			char[] cs = str.toCharArray();
			return Hex.decodeHex(cs);
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字节数组转换成二制形式字符串
	 */
	public static String byteToBin(byte[] bs) {
		char[] cs = new char[bs.length * 9];
		for (int i = 0; i < bs.length; i++) {
			byte b = bs[i];
			int j = i * 9;
			cs[j] = (b >>> 7 & 1) == 1 ? '1' : '0';
			cs[j + 1] = (b >>> 6 & 1) == 1 ? '1' : '0';
			cs[j + 2] = (b >>> 5 & 1) == 1 ? '1' : '0';
			cs[j + 3] = (b >>> 4 & 1) == 1 ? '1' : '0';
			cs[j + 4] = (b >>> 3 & 1) == 1 ? '1' : '0';
			cs[j + 5] = (b >>> 2 & 1) == 1 ? '1' : '0';
			cs[j + 6] = (b >>> 1 & 1) == 1 ? '1' : '0';
			cs[j + 7] = (b & 1) == 1 ? '1' : '0';
			cs[j + 8] = ',';
		}
		return new String(cs);
	}

	/**
	 * 转换字节数组为十六进制字串
	 */

	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
			resultSb.append(" ");
		}
		return resultSb.toString();
	}

	/**
	 * 字节转换为十六进制字符串
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HexDigits[d1] + "" + HexDigits[d2];
	}

	/**
	 * 判断指定的二进制数组是否是一个UTF-8字符串
	 */
	public static boolean isUTF8(byte[] bs) {
		if (StringUtil.hexEncode(ArrayUtils.subarray(bs, 0, 3)).equals("efbbbf")) {// BOM标志
			return true;
		}
		int encodingBytesCount = 0;
		for (int i = 0; i < bs.length; i++) {
			byte c = bs[i];
			if (encodingBytesCount == 0) {
				if ((c & 0x80) == 0) {// ASCII字符范围0x00-0x7F
					continue;
				}
				if ((c & 0xC0) == 0xC0) {
					encodingBytesCount = 1;
					c <<= 2;
					// 非ASCII第一字节用来存储长度
					while ((c & 0x80) == 0x80) {
						c <<= 1;
						encodingBytesCount++;
					}
				} else {
					return false;// 不符合 UTF8规则
				}
			} else {
				// 后续字集必须以10开头
				if ((c & 0xC0) == 0x80) {
					encodingBytesCount--;
				} else {
					return false;// 不符合 UTF8规则
				}
			}
		}
		if (encodingBytesCount != 0) {
			return false;// 后续字节数不符合UTF8规则
		}
		return true;
	}

	/**
	 * 将字符串转换成可以在JAVA表达式中直接使用的字符串，处理一些转义字符
	 */
	public static String javaEncode(String txt) {
		if (txt == null || txt.length() == 0) {
			return txt;
		}
		txt = replaceEx(txt, "\\", "\\\\");
		txt = replaceEx(txt, "\r\n", "\n");
		txt = replaceEx(txt, "\r", "\\r");
		txt = replaceEx(txt, "\t", "\\t");
		txt = replaceEx(txt, "\n", "\\n");
		txt = replaceEx(txt, "\"", "\\\"");
		txt = replaceEx(txt, "\'", "\\\'");
		return txt;
	}

	/**
	 * 将StringUtil.javaEncode()处理过的字符还原
	 */
	public static String javaDecode(String txt) {
		if (txt == null || txt.length() == 0) {
			return txt;
		}
		StringBuilder sb = new StringBuilder();
		int lastIndex = 0;
		while (true) {
			int index = txt.indexOf("\\", lastIndex);
			if (index < 0) {
				break;
			}
			sb.append(txt.substring(lastIndex, index));
			if (index < txt.length() - 1) {
				char c = txt.charAt(index + 1);
				if (c == 'n') {
					sb.append("\n");
				} else if (c == 'r') {
					sb.append("\r");
				} else if (c == 't') {
					sb.append("\t");
				} else if (c == '\'') {
					sb.append("\'");
				} else if (c == '\"') {
					sb.append("\"");
				} else if (c == '\\') {
					sb.append("\\");
				}
				lastIndex = index + 2;
				continue;
			} else {
				sb.append(txt.substring(index, index + 1));
			}
			lastIndex = index + 1;
		}
		sb.append(txt.substring(lastIndex));
		return sb.toString();
	}

	/**
	 * 将一个字符串按照指下的分割字符串分割成数组。分割字符串不作正则表达式处理，<br>
	 * String类的split方法要求以正则表达式分割字符串，有时较为不便，可以转为采用本方法。
	 */
	public static String[] splitEx(String str, String spliter) {
		char escapeChar = '\\';
		if (spliter.equals("\\")) {
			escapeChar = '&';
		}
		return splitEx(str, spliter, escapeChar);
	}

	public static String[] splitEx(String str, String spliter, char escapeChar) {
		if (str == null) {
			return null;
		}
		if (spliter == null || spliter.equals("") || str.length() < spliter.length()) {
			String[] t = { str };
			return t;
		}
		ArrayList<String> list = new ArrayList<String>();
		char[] cs = str.toCharArray();
		char[] ss = spliter.toCharArray();
		int length = spliter.length();
		int lastIndex = 0;
		for (int i = 0; i <= str.length() - length;) {
			boolean notSuit = false;
			for (int j = 0; j < length; j++) {
				if (cs[i + j] != ss[j]) {
					notSuit = true;
					break;
				}
			}
			if (!notSuit) {
				list.add(str.substring(lastIndex, i));
				i += length;
				lastIndex = i;
			} else {
				i++;
			}
		}
		if (lastIndex <= str.length()) {
			list.add(str.substring(lastIndex, str.length()));
		}
		ArrayList<String> r = new ArrayList<String>(list.size());
		for (int i = 0; i < list.size(); i++) {
			String a = list.get(i);
			if (a.length() > 0 && a.charAt(a.length() - 1) == escapeChar) {
				r.add(a.substring(0, a.length() - 1) + spliter + (i == list.size() - 1 ? "" : list.get(i + 1)));
				i++;
			} else {
				r.add(a);
			}
		}
		String[] t = new String[r.size()];
		return r.toArray(t);
	}

	/**
	 * 将一个字符串中的指定片段全部替换，替换过程中不进行正则处理。<br>
	 * 使用String类的replaceAll时要求片段以正则表达式形式给出，有时较为不便，可以转为采用本方法。
	 */
	public static String replaceEx(String str, String subStr, String reStr) {
		if (str == null) {
			return null;
		}
		if (subStr == null || subStr.equals("") || subStr.length() > str.length() || reStr == null) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		int lastIndex = 0;
		while (true) {
			int index = str.indexOf(subStr, lastIndex);
			if (index < 0) {
				break;
			} else {
				sb.append(str.substring(lastIndex, index));
				sb.append(reStr);
			}
			lastIndex = index + subStr.length();
		}
		sb.append(str.substring(lastIndex));
		return sb.toString();
	}

	/**
	 * 不区分大小写的全部替换，替换时使用了正则表达式。
	 */
	public static String replaceAllIgnoreCase(String source, String oldstring, String newstring) {
		Pattern p = Pattern.compile(oldstring, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		return m.replaceAll(newstring);
	}

	/**
	 * 以全局编码进行URL编码
	 */
	public static String urlEncode(String str) {
		return urlEncode(str,"utf-8");
	}

	/**
	 * 以全局编码进行URL解码
	 */
	public static String urlDecode(String str) {
		return urlDecode(str, "utf-8");
	}

	/**
	 * 以指定编码进行URL编码
	 */
	public static String urlEncode(String str, String charset) {
		try {
			return new URLCodec().encode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 以指定编码进行URL解码
	 */
	public static String urlDecode(String str, String charset) {
		try {
			return new URLCodec().decode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对字符串进行HTML编码
	 */
	public static String htmlEncode(String txt) {
		return StringEscapeUtils.escapeHtml(txt);
	}

	/**
	 * 对字符串进行HTML解码
	 */
	public static String htmlDecode(String txt) {
		txt = replaceEx(txt, "&#8226;", "·");
		return StringEscapeUtils.unescapeHtml(txt);
	}

	/**
	 * 替换字符串中的双引号
	 */
	public static String quotEncode(String txt) {
		if (txt == null || txt.length() == 0) {
			return txt;
		}
		txt = replaceEx(txt, "&", "&amp;");
		txt = replaceEx(txt, "\"", "&quot;");
		return txt;
	}

	/**
	 * 还原通过StringUtil.quotEncode()编码的字符串
	 */
	public static String quotDecode(String txt) {
		if (txt == null || txt.length() == 0) {
			return txt;
		}
		txt = replaceEx(txt, "&quot;", "\"");
		txt = replaceEx(txt, "&amp;", "&");
		return txt;
	}

	/**
	 * Javascript中escape的JAVA实现
	 */
	public static String escape(String src) {
		char j;
		StringBuilder sb = new StringBuilder();
		sb.ensureCapacity(src.length() * 6);
		for (int i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
				sb.append(j);
			} else if (j < 256) {
				sb.append("%");
				if (j < 16) {
					sb.append("0");
				}
				sb.append(Integer.toString(j, 16));
			} else {
				sb.append("%u");
				sb.append(Integer.toString(j, 16));
			}
		}
		return sb.toString();
	}

	/**
	 * Javascript中unescape的JAVA实现
	 */
	public static String unescape(String src) {
		StringBuilder sb = new StringBuilder();
		sb.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					sb.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					sb.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					sb.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					sb.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 在一字符串左边填充若干指定字符，使其长度达到指定长度
	 */
	public static String leftPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();
		int i, iMax;
		if (tLen >= length)
			return srcString;
		iMax = length - tLen;
		StringBuilder sb = new StringBuilder();
		for (i = 0; i < iMax; i++) {
			sb.append(c);
		}
		sb.append(srcString);
		return sb.toString();
	}

	/**
	 * 将长度超过length的字符串截取length长度，若不足，则返回原串
	 */
	public static String subString(String src, int length) {
		if (src == null) {
			return null;
		}
		int i = src.length();
		if (i > length) {
			return src.substring(0, length);
		} else {
			return src;
		}
	}

	/**
	 * 将长度超过length的字符串截取length长度，若不足，则返回原串。<br>
	 * 其中ASCII字符算1个长度单位，非ASCII字符算2个长度单位。
	 */
	public static String subStringEx(String src, int length) {
		// length = length * 2;
		if (src == null) {
			return null;
		}
		int m = 0;
		boolean unixFlag = false;
		String osname = System.getProperty("os.name").toLowerCase();
		if (osname.indexOf("sunos") > 0 || osname.indexOf("solaris") > 0 || osname.indexOf("aix") > 0
				|| (osname.indexOf("windows") >= 0 && osname.indexOf("7") > 0)) {
			unixFlag = true;
		}
		try {
			byte[] b = src.getBytes("Unicode");
			for (int i = 2; i < b.length; i += 2) {
				byte flag = b[i + 1];
				if (unixFlag) {
					flag = b[i];
				}
				if (flag == 0) {
					m++;
				} else {
					m += 2;
				}
				if (m > length) {
					return src.substring(0, (i - 2) / 2);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("String.getBytes(\"Unicode\") failed");
		}
		return src;
	}

	/**
	 * 获得字符串的长度，其中ASCII字符算1个长度单位,非ASCII字符算两个长度单位
	 */
	public static int lengthEx(String src) {
		return lengthEx(src, false);
	}

	/**
	 * 获得字符串的长度，其中ASCII字符算1个长度单位,如果UTF8Flag为ture,则非ASCII字符算3个字节，否则非ASCII字符算2个字节
	 */
	public static int lengthEx(String src, boolean UTF8Flag) {
		if (ObjectUtil.empty(src)) {
			return 0;
		}
		int length = 0;
		boolean bigFlag = true;
		try {
			byte[] b = src.getBytes("Unicode");
			if (b[0] == -2) {
				bigFlag = false;
			}
			for (int i = 2; i < b.length; i += 2) {
				byte flag = b[(i + 1)];
				if (!(bigFlag)) {
					flag = b[i];
				}
				if (flag == 0) {
					length++;
				} else {
					if (UTF8Flag) {
						length += 3;
					} else {
						length += 2;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("String.getBytes(\"Unicode\") failed");
		}
		return length;
	}

	/**
	 * 在一字符串右边填充若干指定字符，使其长度达到指定长度
	 */
	public static String rightPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();
		int i, iMax;
		if (tLen >= length)
			return srcString;
		iMax = length - tLen;
		StringBuilder sb = new StringBuilder();
		sb.append(srcString);
		for (i = 0; i < iMax; i++) {
			sb.append(c);
		}
		return sb.toString();
	}


	/**
	 * 清除字符右边的空格
	 */
	public static String rightTrim(String src) {
		if (src != null) {
			char[] chars = src.toCharArray();
			for (int i = chars.length - 1; i >= 0; i--) {
				if (chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\r') {
					continue;
				} else {
					return src.substring(0, i + 1);
				}
			}
			return "";// 说明全是空格
		}
		return src;
	}

	/**
	 * 历遍所有字符集，看哪种字符集下可以正确转化
	 */
	public static void printStringWithAnyCharset(String str) {
		Map<String, Charset> map = Charset.availableCharsets();
		for (String key1 : map.keySet()) {
			for (String key2 : map.keySet()) {
				System.out.print("\t");
				try {
					System.out.println("From " + key1 + " To " + key2 + ":" + new String(str.getBytes(key1.toString()), key2.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 半角转全角，转除英文字母之外的字符
	 */
	public static String toSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if ((c[i] > 64 && c[i] < 91) || (c[i] > 96 && c[i] < 123)) {
				continue;
			}

			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/**
	 * 半角转全角，转所有能转为全角的字符，包括英文字母
	 */
	public static String toNSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}

			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/**
	 * 全角转半角的函数 全角空格为12288，半角空格为32 <br>
	 * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 */
	public static String toDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static final Pattern PTitle = Pattern.compile("<title>(.+?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/**
	 * 从一段html文本中提取出<title>标签内容
	 */
//	public static String getHtmlTitle(File f) {
//		String html = FileUtil.readText(f);
//		String title = getHtmlTitle(html);
//		return title;
//	}

	/**
	 * 从一段html文本中提取出<title>标签内容
	 */
	public static String getHtmlTitle(String html) {
		Matcher m = PTitle.matcher(html);
		if (m.find()) {
			return m.group(1).trim();
		}
		return null;
	}

	public static Pattern patternHtmlTag = Pattern.compile("<[^<>]+>", Pattern.DOTALL);

	/**
	 * 清除HTML文本中所有标签
	 */
	public static String clearHtmlTag(String html) {
		String text = patternHtmlTag.matcher(html).replaceAll("");
		if (isEmpty(text)) {
			return "";
		}
		text = StringUtil.htmlDecode(text);
		return text.replaceAll("[\\s　]{2,}", " ");
	}

	/**
	 * 将内容中的<*> 进行html编码
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlTagEncode(String str) {
		if (isEmpty(str)) {
			return str;
		}
		while (true) {
			Matcher matcher = patternHtmlTag.matcher(str);
			if (matcher.find()) {
				String content = matcher.group(0);
				str = matcher.replaceFirst(htmlEncode(content));
			} else {
				break;
			}
		}
		return str.replaceAll("[\\s　]{2,}", " ");
	}

	/**
	 * 首字母大写
	 */
	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
	}

	/**
	 * 字符串是否为空，null或空字符串时返回true,其他情况返回false
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 字符串是否不为空，null或空字符串时返回false,其他情况返回true
	 */
	public static boolean isNotEmpty(String str) {
		return !StringUtil.isEmpty(str);
	}

	/**
	 * 字符串是否为空，null或空字符或者为"null"字符串时返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return isEmpty(str) || "null".equals(str);
	}

	/**
	 * 字符串是否不为空，null,空字符串,或者"null" 字符串时返回false,其他情况返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		return isNotEmpty(str) && !"null".equals(str);
	}

	/**
	 * 字符串为空时返回defaultString，否则返回原串
	 */
	public static final String noNull(String string, String defaultString) {
		return isEmpty(string) ? defaultString : string;
	}

	/**
	 * 字符串为空时返回defaultString，否则返回空字符串
	 */
	public static final String noNull(String string) {
		return noNull(string, "");
	}

	/**
	 * 将一个数组拼成一个字符串，数组项之间以逗号分隔
	 */
	public static String join(Object[] arr) {
		return join(arr, ",");
	}

	/**
	 * 将一个二维数组拼成一个字符串，第二维以逗号分隔，第一维以换行分隔
	 */
	public static String join(Object[][] arr) {
		return join(arr, "\n", ",");
	}

	/**
	 * 将一个数组以指定的分隔符拼成一个字符串
	 */
	public static String join(Object[] arr, String spliter) {
		if (arr == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * 将一个二维数组拼成一个字符串，第二维以指定的spliter2参数分隔，第一维以换行spliter1分隔
	 */
	public static String join(Object[][] arr, String spliter1, String spliter2) {
		if (arr == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(spliter2);
			}
			sb.append(join(arr[i], spliter2));
		}
		return sb.toString();
	}

	/**
	 * 将一个List拼成一个字符串，数据项之间以逗号分隔
	 */
	public static String join(List<?> list) {
		return join(list, ",");
	}

	/**
	 * 将一个List拼成一个字符串，数据项之间以指定的参数spliter分隔
	 */
	public static String join(List<?> list, String spliter) {
		if (list == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(list.get(i));
		}
		return sb.toString();
	}

	/**
	 * 计算一个字符串中某一子串出现的次数
	 */
	public static int count(String str, String findStr) {
		int lastIndex = 0;
		int length = findStr.length();
		int count = 0;
		int start = 0;
		while ((start = str.indexOf(findStr, lastIndex)) >= 0) {
			lastIndex = start + length;
			count++;
		}
		return count;
	}

	public static final Pattern PLetterOrDigit = Pattern.compile("^\\w*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	public static final Pattern PLetter = Pattern.compile("^[A-Za-z]*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	public static final Pattern PDigit = Pattern.compile("^\\d*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/**
	 * 判断字符串是否全部由数字或字母组成
	 */
	public static boolean isLetterOrDigit(String str) {
		return PLetterOrDigit.matcher(str).find();
	}

	/**
	 * 判断字符串是否全部字母组成
	 */
	public static boolean isLetter(String str) {
		return PLetter.matcher(str).find();
	}

	/**
	 * 判断字符串是否全部由数字组成
	 */
	public static boolean isDigit(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return PDigit.matcher(str).find();
	}

	private static Pattern chinesePattern = Pattern.compile("[^\u4e00-\u9fa5]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/**
	 * 判断字符串中是否含有中文字符
	 */
	public static boolean containsChinese(String str) {
		if (!chinesePattern.matcher(str).matches()) {
			return true;
		}
		return false;
	}

	private static Pattern idPattern = Pattern.compile("[\\w\\s\\_\\.\\,]*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/**
	 * 检查ID，防止SQL注入，主要是在删除时传入多个ID时使用
	 */
	public static boolean checkID(String str) {
		if (StringUtil.isEmpty(str)) {
			return true;
		}
		if (idPattern.matcher(str).matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 将一个类似于Name=John,Age=18,Gender=3的字符串拆成一个Mapx
	 */
	public static Mapx<String, String> splitToMapx(String str, String entrySpliter, String keySpliter) {
		return splitToMapx(str, entrySpliter, keySpliter, '\\');
	}

	public static Mapx<String, String> splitToMapx(String str, String entrySpliter, String keySpliter, char escapeChar) {
		Mapx<String, String> map = new Mapx<String, String>();
		String[] arr = StringUtil.splitEx(str, entrySpliter, escapeChar);
		for (int i = 0; i < arr.length; i++) {
			String[] arr2 = StringUtil.splitEx(arr[i], keySpliter, escapeChar);
			String key = arr2[0];
			if (StringUtil.isEmpty(key)) {
				continue;
			}
			key = key.trim();
			String value = null;
			if (arr2.length > 1) {
				value = arr2[1];
			}
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 得到URL的文件扩展名
	 */
	public static String getURLExtName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf('?');
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf('.', index1);
		if (index2 == -1) {
			return null;
		}
		int index3 = url.indexOf('/', 8);
		if (index3 == -1) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		if (ext.matches("[^\\/\\\\]*")) {
			return ext;
		}
		return null;
	}

	/**
	 * 得到URL的文件名
	 */
	public static String getURLFileName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf('?');
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf('/', index1);
		if (index2 == -1 || index2 < 8) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		return ext;
	}


	/**
	 * 去掉XML字符串中的非法字符, 在XML中0x00-0x20 都会引起一定的问题
	 */
	public static String clearForXML(String str) {
		char[] cs = str.toCharArray();
		char[] ncs = new char[cs.length];
		int j = 0;
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] > 0xFFFD) {
				continue;
			} else if (cs[i] < 0x20 && cs[i] != '\t' & cs[i] != '\n' & cs[i] != '\r') {
				continue;
			}
			ncs[j++] = cs[i];
		}
		ncs = ArrayUtils.subarray(ncs, 0, j);
		return new String(ncs);
	}
	
	/**
	 * 拼接字符串
	 */
	public static String concat(Object... args) {
		if(ObjectUtil.empty(args)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object arg : args) {
			if(ObjectUtil.notEmpty(arg)) {
				sb.append(arg);
			}
		}
		return sb.toString();
	}
}
