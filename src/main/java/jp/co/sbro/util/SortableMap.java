package jp.co.sbro.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SortableMap<K extends Comparable<K>, V extends Comparable<V>> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;

	public SortableMap<K, V> sortByKey() {
		return entrySet().stream().sorted(Entry.comparingByKey()).collect(collector());
	}

	public SortableMap<K, V> sortByValue() {
		return entrySet().stream().sorted(Entry.comparingByValue()).collect(collector());
	}

	public SortableMap<K, V> reverse() {
		return entrySet().stream().sorted((o1, o2) -> -1).collect(collector());
	}

	private Collector<Entry<K, V>, ?, SortableMap<K, V>> collector() {
		return Collectors.toMap(Entry::getKey, Entry::getValue, (k, v) -> k, SortableMap::new);
	}

}
