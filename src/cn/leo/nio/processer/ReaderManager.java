package cn.leo.nio.processer;

import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentHashMap;

public class ReaderManager {
	private static ConcurrentHashMap<SelectionKey, Reader> mKeys = new ConcurrentHashMap<>();

	public static void putReader(SelectionKey key, Reader run) {
		mKeys.put(key, run);
	}

	public static Reader getReader(SelectionKey key) {
		return mKeys.get(key);
	}

	public static void remove(SelectionKey key) {
		mKeys.remove(key);
	}
	
	public static int size(){
		return mKeys.size();
	}
}
