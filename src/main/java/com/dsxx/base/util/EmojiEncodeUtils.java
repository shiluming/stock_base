package com.dsxx.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiEncodeUtils {

	private static Pattern p = Pattern.compile("<u>(.+?)<\\/u>");

	/**
	 * 转成可以在db中存储的格式<u>unicode</u>
	 * @param content
	 * @return
	 */
	public static String filter2CharUnicode(String content)
	{
		if (content == null || content.trim().length() == 0)
		{
			return content;
		}
		StringBuffer sb = new StringBuffer();
		char[] ss = content.toCharArray();
		for (int i = 0; i < ss.length; i++)
		{
			if (Character.isHighSurrogate(content.charAt(i)))
			{
				String hex1 = Integer.toHexString(Character.codePointAt(ss, i));
				String hex2 = Integer.toHexString(Character.codePointAt(ss, i + 1));
				sb.append("<u>" + hex1 + "+" + hex2 + "</u>");
				i++;
			}
			else
			{
				sb.append(content.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * unicode转回utf(转成正常的表情)
	 * 
	 * @author Kin
	 * @create 2013-3-8 下午4:46:57
	 * @param content
	 * @return String
	 */
	public static String filter2CharUTF(String content)
	{
		if (content == null || content.trim().length() == 0)
		{
			return content;
		}
		String t = content;
		Matcher m = p.matcher(content);
		while (m.find())
		{
			String tmp = m.group(1);
			String[] hex = tmp.split("\\+");
			char c1 = Character.toChars(Integer.parseInt(hex[0], 16))[0];
			char c2 = Character.toChars(Integer.parseInt(hex[1], 16))[0];
			String tt = new String(new char[] { c1, c2 });
			t = t.replaceFirst("<u>(.+?)<\\/u>", tt);
		}
		return t;
	}
	
	/**
	 * @author klwb
	 * @date 2013-7-18
	 * @param emo
	 * @return
	 */
	private static String convertToUnicode(String emo)
	{
		int start = emo.indexOf("]");
		int end = emo.lastIndexOf("[");
		String _unicode = emo.substring(start + 1, end);
		String str = new String(Character.toChars(Integer.parseInt(_unicode, 16)));
		return str;
	}
	
	private static String parseEmoji(String input)
	{
		if (input == null || input.length() <= 0)
		{
			return "";
		}
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		for (int i = 0; i < codePoints.length; i++)
		{
			result.append(Character.toChars(codePoints[i]));
		}
		String string = result.toString();
		return string;
	}

	private static  int[] toCodePointArray(String str)
	{
		char[] ach = str.toCharArray();
		int len = ach.length;
		int[] acp = new int[Character.codePointCount(ach, 0, len)];
		int j = 0;
		for (int i = 0, cp; i < len; i += Character.charCount(cp))
		{
			cp = Character.codePointAt(ach, i);
			acp[j++] = cp;
		}
		return acp;
	}
}
