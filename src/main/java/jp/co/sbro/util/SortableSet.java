package jp.co.sbro.util;

import java.util.LinkedHashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SortableSet<T extends Comparable<T>> extends LinkedHashSet<T> {

	private static final long serialVersionUID = 1L;

	public SortableSet<T> sort() {
		return stream().sorted().collect(collector());
	}

	public SortableSet<T> reverse() {
		return stream().sorted((o1, o2) -> -1).collect(collector());
	}

	private Collector<T, ?, SortableSet<T>> collector() {
		return Collectors.toCollection(SortableSet::new);
	}

}
