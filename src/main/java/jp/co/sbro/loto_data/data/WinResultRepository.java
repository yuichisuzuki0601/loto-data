package jp.co.sbro.loto_data.data;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.sbro.util.Counter;
import jp.co.sbro.util.DateFormattedMapper;
import jp.co.sbro.util.SortableMap;

public class WinResultRepository extends TreeMap<Integer, WinResult> {

	private static final long serialVersionUID = 1L;

	private final static ObjectMapper MAPPER = new DateFormattedMapper();

	public WinResultRepository merge(WinResultRepository repos) {
		this.putAll(repos);
		return this;
	}

	public WinResult getWinResult(int time) {
		return get(time) != null ? get(time) : new WinResult(time);
	}

	public WinResult getLatestWinResult() {
		return lastEntry().getValue();
	}

	public WinResultRepository filterByTimes(Integer... time) {
		return entrySet().stream().filter(e -> {
			return Arrays.asList(time).contains(e.getKey());
		}).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k, v) -> k, WinResultRepository::new));
	}

	public WinResultRepository filterByStartAndEnd(int startTime, int endTime) {
		return entrySet().stream().filter(e -> {
			return startTime <= e.getKey() && e.getKey() <= endTime;
		}).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k, v) -> k, WinResultRepository::new));
	}

	public WinResultRepository filterByStart(int startTime) {
		int endTime = getLatestWinResult().getTime();
		return filterByStartAndEnd(startTime, endTime);
	}

	public WinResultRepository filterByEnd(int endTime) {
		return filterByStartAndEnd(1, endTime);
	}

	public WinResultRepository filterByCnt(int cnt) {
		int endTime = getLatestWinResult().getTime();
		int startTime = endTime - cnt + 1;
		return filterByStartAndEnd(startTime, endTime);
	}

	public SortableMap<Integer, Integer> frequency() {
		if (this.isEmpty()) {
			return new SortableMap<>();
		}
		SortableMap<Integer, Integer> m = Counter.countCollection(
				entrySet().stream().map(e -> e.getValue().getHitsAndBonus()).collect(Collectors.toList()));
		IntStream.rangeClosed(1, 43).forEach(i -> {
			if (m.get(i) == null) {
				m.put(i, 0);
			}
		});
		return m;
	}

	public String toString() {
		try {
			return MAPPER.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
