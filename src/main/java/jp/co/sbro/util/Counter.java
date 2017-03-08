package jp.co.sbro.util;

import java.util.Collection;

public final class Counter {

	private Counter() {
	}

	public static <T extends Comparable<T>> SortableMap<T, Integer> countCollection(
			Collection<? extends Collection<T>> c) {
		try {
			return c.stream().map(Counter::count).reduce(Counter::reduce).get();
		} catch (ArrayIndexOutOfBoundsException e) {
			return new SortableMap<>();
		}
	}

	public static <T extends Comparable<T>> SortableMap<T, Integer> count(Collection<T> c) {
		try {
			return c.stream().map(Counter::map).reduce(Counter::reduce).get();
		} catch (ArrayIndexOutOfBoundsException e) {
			return new SortableMap<>();
		}
	}

	private static <T extends Comparable<T>> SortableMap<T, Integer> map(T object) {
		SortableMap<T, Integer> result = new SortableMap<>();
		result.put(object, 1);
		return result;
	}

	private static <T extends Comparable<T>> SortableMap<T, Integer> reduce(SortableMap<T, Integer> left,
			SortableMap<T, Integer> right) {
		right.entrySet().stream().forEach(e -> {
			T key = e.getKey();
			Integer value = e.getValue();
			Integer current = left.get(key);
			left.put(key, current == null ? value : current + value);
		});
		return left;
	}

}
