package com.wh.pjtr.ngs.batch.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {
	public static final char EXTENSION_SEPARATOR = '.';
	public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);

	public static final char UNIX_SEPARATOR = '/';
	public static final char WINDOWS_SEPARATOR = '\\';

	public static final char SYSTEM_SEPARATOR = File.separatorChar;
	public static final char OTHER_SEPARATOR;

	private static final String DESTINATION = "Destination '";
	private static final String SOURCE = "Source '";

	private FileUtils() {
		throw new IllegalStateException("Utility class");
	}

	static {
		if (isSystemWindows()) {
			OTHER_SEPARATOR = UNIX_SEPARATOR;
		} else {
			OTHER_SEPARATOR = WINDOWS_SEPARATOR;
		}
	}

	static boolean isSystemWindows() {
		return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
	}

	private static boolean isSeparator(char ch) {
		return ch == UNIX_SEPARATOR || ch == WINDOWS_SEPARATOR;
	}

	/**
	 * The number of bytes in a kilobyte.
	 */
	public static final long ONE_KB = 1024;

	/**
	 * The number of bytes in a megabyte.
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * The file copy buffer size (30 MB)
	 */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
		copyFileToDirectory(srcFile, destDir, true);
	}

	public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && !destDir.isDirectory()) {
			throw new IllegalArgumentException(DESTINATION + destDir + "' is not a directory");
		}
		File destFile = new File(destDir, srcFile.getName());
		copyFile(srcFile, destFile, preserveFileDate);
	}

	public static void copyFile(File srcFile, File destFile) throws IOException {
		copyFile(srcFile, destFile, true);
	}

	public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new FileNotFoundException(SOURCE + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException(SOURCE + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException(SOURCE + srcFile + "' and " + DESTINATION + destFile + "' are the same");
		}
		File parentFile = destFile.getParentFile();
		if (parentFile != null && (!parentFile.mkdirs() && !parentFile.isDirectory())) {
			throw new IOException(DESTINATION + parentFile + "' directory cannot be created");
		}
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException(DESTINATION + destFile + "' exists but is read-only");
		}
		doCopyFile(srcFile, destFile, preserveFileDate);
	}

	private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException(DESTINATION + destFile + "' exists but is a directory");
		}

		try(FileInputStream fis = new FileInputStream(srcFile);
			FileOutputStream fos = new FileOutputStream(destFile);
			FileChannel input = fis.getChannel();
			FileChannel output = fos.getChannel()) {
			long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
				pos += output.transferFrom(input, pos, count);
			}
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	public static String createTempFileName() {
		String fileNameFormat = "%s-%s-%s";
		String prefix = "temp";
		String uuid = GuidGenerator.generateUuid();
		return String.format(fileNameFormat, prefix, DateUtils.now(), uuid);
	}

	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}

	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	public static int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
		int lastSeparator = indexOfLastSeparator(filename);
		return lastSeparator > extensionPos ? -1 : extensionPos;
	}

	public static String getName(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfLastSeparator(filename);
		return filename.substring(index + 1);
	}

}
