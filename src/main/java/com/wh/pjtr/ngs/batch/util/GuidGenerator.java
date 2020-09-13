package com.wh.pjtr.ngs.batch.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GuidGenerator {
	private static final String UNIQUE_STUB;

	private static final String hostName = "";
	private static final AtomicLong sequence = new AtomicLong(0);

	private static final int LENGTH = 57;
	private static final long MAX_SEQ = 10000;
	public static final String DIV_STRING = "-";
	public static final char DIV_STRING_CHAR = DIV_STRING.charAt(0);

	@SuppressWarnings("unused")
	private static final int maxHostname = 15;
	static {
		UNIQUE_STUB = System.currentTimeMillis() + DIV_STRING + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 4);
	}

	private GuidGenerator() {
		throw new IllegalStateException("Utility class");
	}

	@SuppressWarnings("unused")
	private static String sanitizeHostName(String hostName) {
		boolean changed = false;
		StringBuilder sb = new StringBuilder();
		for (char ch : hostName.toCharArray()) {
			// only include ASCII chars
			if (ch < 127 && ch != DIV_STRING_CHAR) {
				sb.append(ch);
			} else {
				changed = true;
			}
		}

		if (changed) {
			return sb.toString();
		} else {
			return hostName;
		}
	}

	public static String generateUuid() {
		StringBuilder sb = new StringBuilder(LENGTH);
		sb.append(UNIQUE_STUB).append(DIV_STRING);
		sb.append(System.currentTimeMillis()%100000).append(DIV_STRING);
		sb.append(UUID.randomUUID().toString().replaceAll("-", ""), 0, 9).append(DIV_STRING);
		sb.append((sequence.getAndIncrement() % MAX_SEQ));
		return sb.toString();
	}

	public static String generateShortenId() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.currentTimeMillis()).append(DIV_STRING);
		sb.append((sequence.getAndIncrement() % MAX_SEQ));
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private static String getLocalHostName() throws UnknownHostException {
		try {
			return (InetAddress.getLocalHost()).getHostName();
		} catch (UnknownHostException uhe) {
			String host = uhe.getMessage();// host = "hostname: hostname"
			if (host != null) {
				int colon = host.indexOf(':');

				if (colon > 0) {
					return host.substring(0, colon);
				}
			}
			throw uhe;
		}
	}
}
