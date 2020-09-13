package com.wh.pjtr.ngs.batch.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Locale;

public class StringUtils {
	private static final String OPEN_ANGLE_BRACKET = "<";
	private static final String CLOSE_ANGLE_BRACKET = ">";
	private static final String HTML_ESCAPE_CHAR_OPEN_ANGLE_BRACKET = "&lt;";
	private static final String HTML_ESCAPE_CHAR_CLOSE_ANGLE_BRACKET = "&gt;";

	private static final String[] HTML_FILTER_WORDS = {"javascript", "script", "iframe", "document", "vbscript",
			"applet", "embed", "object", "frame", "grameset", "layer", "bgsound", "alert", "enerror", "href",
			"fscommand", "onabort", "onactivate", "onafterprint", "onafterupdate", "onbeforeactivate", "onbeforecopy",
			"onbeforecut", "onbeforedeactivate", "onbeforeeditfocus", "onbeforepaste", "onbeforeprint",
			"onbeforeunload", "onbeforeupdate", "onbegin", "onblur", "onbounce", "oncellchange", "onchange", "onclick",
			"oncontextmenu", "oncontrolselect", "oncopy", "oncut", "ondataavailable", "ondatasetchanged",
			"ondatasetcomplete", "ondblclick", "ondeactivate", "ondrag", "ondragend", "ondragleave", "ondragenter",
			"ondragover", "ondragdrop", "ondragstart", "ondrop", "onend", "onerror", "onerrorupdate", "onfilterchange",
			"onfinish", "onfocus", "onfocusin", "onfocusout", "onhashchange", "onhelp", "oninput", "onkeydown",
			"onkeypress", "onkeyup", "onlayoutcomplete", "onload", "onlosecapture", "onmediacomplete", "onmediaerror",
			"onmessage", "onmousedown", "onmouseenter", "onmouseleave", "onmousemove", "onmouseout", "onmouseover",
			"onmouseup", "onmousewheel", "onmove", "onmoveend", "onmovestart", "onoffline", "ononline", "onoutofsync",
			"onpaste", "onpause", "onpopstate", "onprogress", "onpropertychange", "onreadystatechange", "onredo",
			"onrepeat", "onreset", "onresize", "onresizeend", "onresizestart", "onresume", "onreverse", "onrowsenter",
			"onrowexit", "onrowdelete", "onrowinserted", "onscroll", "onseek", "onselect", "onselectionchange",
			"onselectstart", "onstart", "onstop", "onstorage", "onsyncrestored", "onsubmit", "ontimeerror",
			"ontrackchange", "onundo", "onunload", "onurlflip", "seeksegmenttime"};

	private static final String[][] HTML_BASIC_ESCAPE_ENTITIES = {
			{"&", "&amp;"},
			{"\"", "&quot;"},
			{"<", "&lt;"},
			{">", "&gt;"}
	};

	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String concat(String... a) {
		StringBuilder sb = new StringBuilder();
		for (String tmp: a) {
			sb.append(tmp);
		}
		return sb.toString();
	}

	public static String htmlEscape(String source) {
		String filteredHtml = source;
		// 필터링할 특수문자를 치환
		for(String[] entity: HTML_BASIC_ESCAPE_ENTITIES) {
			filteredHtml = filteredHtml.replaceAll(entity[0], entity[1]);
		}

		//Filtering으로 허용하지 않을 script, document 등을 필터링
		filteredHtml = filteredHtml.toLowerCase();
		boolean filtered = false;
		for(String word: HTML_FILTER_WORDS) {
			if (!filtered && filteredHtml.contains(word)) {
				filtered = true;
			}
			filteredHtml = filteredHtml.replaceAll(word, "x-" + word);
		}

		if(!filtered){
			filteredHtml = filteredHtml.replaceAll(OPEN_ANGLE_BRACKET, HTML_ESCAPE_CHAR_OPEN_ANGLE_BRACKET);
			filteredHtml = filteredHtml.replaceAll(CLOSE_ANGLE_BRACKET, HTML_ESCAPE_CHAR_CLOSE_ANGLE_BRACKET);
		}

		if (!checkFilterededHtml(filteredHtml)) {
			throw new HtmlEscapeCharInvalidRuntimeException("입력값 검증이 필요합니다. 관리자에게 연락하시기 바랍니다.");
		}
		return filteredHtml;
	}

	private static boolean checkFilterededHtml(String filteredHtml) {
		String checkHtml = filteredHtml.toLowerCase(Locale.ENGLISH).trim().replace(" ", "");
		for (String word: HTML_FILTER_WORDS) {
			checkHtml = checkHtml.replaceAll("x-" + word, "");
			if (checkHtml.contains(word)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((!Character.isWhitespace(str.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	public static String encodeToBase64String(String str) {
		byte[] encodeByte = Base64.encodeBase64(str.getBytes());
		return new String(encodeByte);
	}

	public static String decodeFromBase64String(String str) {
		byte[] decodeByte = Base64.decodeBase64(str.getBytes());
		return new String(decodeByte);
	}

	public  static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static String convert2CamelCase(String underScore) {
		if (underScore.indexOf(95) < 0 && Character.isLowerCase(underScore.charAt(0))) {
			return underScore;
		} else {
			StringBuilder result = new StringBuilder();
			boolean nextUpper = false;
			int len = underScore.length();

			for (int i = 0; i < len; ++i) {
				char currentChar = underScore.charAt(i);
				if (currentChar == '_') {
					nextUpper = true;
				} else if (nextUpper) {
					result.append(Character.toUpperCase(currentChar));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(currentChar));
				}
			}

			return result.toString();
		}
	}

	public static String convert2UpperCaseMerge(String[] textList) {
		StringBuilder result = new StringBuilder();
		int len = textList.length;
		for (int i =0; i < len; i++) {
			result.append(convert2UpperCase(textList[i]));
			if (i + 1 != len) {
				result.append(", ");
			}
		}
		return result.toString();
	}

	public static String convert2UpperCase(String text) {
		StringBuilder result = new StringBuilder();
		int len = text.length();

		for (int i = 0; i < len; ++i) {
			char currentChar = text.charAt(i);
			if (currentChar == Character.toUpperCase(currentChar)) {
				result.append("_");
				result.append(Character.toUpperCase(currentChar));
			} else {
				result.append(Character.toUpperCase(currentChar));
			}
		}

		return result.toString();
	}

	/**
	 * camelCase to DB Column naming
	 * <pre>
	 * ex) abcDefGh ==> ABC_DEF_GH
	 * </pre>
	 */
	public static String camelCaseToUnderscoreName(String str) {
		String regex = "([a-z])([A-Z])";
		String replacement = "$1_$2";
		return str.replaceAll(regex, replacement).toUpperCase();
	}
}
