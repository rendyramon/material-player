package com.edavtyan.materialplayer.utils;

import java.io.File;

public final class FileUtils {
	private FileUtils() {}


	public static void delete(String filePath) {
		new File(filePath).delete();
	}
}
