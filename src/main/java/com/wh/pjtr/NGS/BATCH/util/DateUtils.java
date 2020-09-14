package com.wh.pjtr.ngs.batch.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final String YYYYMM = "yyyyMM";
	public static final String SHORT = "yyyyMMdd";
	public static final String FULL = "yyyyMMddHHmmss";
	public static final String FORMATTED_YMD = "yyyy-MM-dd";
	public static final String FORMATTED_FULL = "yyyy-MM-dd HH:mm:ss";

	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String now() {
		return now(false);
	}

	public static String getDateAsText(String format) {
		return getDateAsText(new Date(), format);
	}

	public static String getDateAsText(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String now(boolean isFormatted) {
		if (isFormatted) {
			return getDateAsText(FORMATTED_FULL);
		}
		return getDateAsText(FULL);
	}
}
