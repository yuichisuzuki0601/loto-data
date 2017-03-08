package jp.co.sbro.util;

import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SortableList<T extends Comparable<T>> extends LinkedList<T> {

	private static final long serialVersionUID = 1L;

	public SortableList<T> sort() {
		return stream().sorted().collect(collector());
	}

	public SortableList<T> reverse() {
		return stream().sorted((o1, o2) -> -1).collect(collector());
	}

	private Collector<T, ?, SortableList<T>> collector() {
		return Collectors.toCollection(SortableList::new);
	}

}
