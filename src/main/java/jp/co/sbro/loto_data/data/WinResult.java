package jp.co.sbro.loto_data.data;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.sbro.util.DateFormattedMapper;

public class WinResult implements Comparable<WinResult> {

	private final static ObjectMapper MAPPER = new DateFormattedMapper();

	private int time;
	private Date date;
	private Set<Integer> hits = new TreeSet<>();
	private int bonus;

	private Set<Prize> prizes = new TreeSet<>();
	private int carryOver;

	public class Prize implements Comparable<Prize> {
		private int rank;
		private int unit;
		private int price;

		public Prize(int rank, int unit) {
			this.rank = rank;
			this.unit = unit;
		}

		public int getRank() {
			return rank;
		}

		public int getUnit() {
			return unit;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		@Override
		public int compareTo(Prize o) {
			return this.rank - o.rank;
		}
	}

	public WinResult(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<Integer> getHits() {
		return hits;
	}

	public void addHits(int num) {
		hits.add(num);
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public Prize addPrize(int unit) {
		Prize prize = new Prize(prizes.size() + 1, unit);
		prizes.add(prize);
		return prize;
	}

	public Set<Prize> getPrizes() {
		return prizes;
	}

	public int getCarryOver() {
		return carryOver;
	}

	public void setCarryOver(int carryOver) {
		this.carryOver = carryOver;
	}

	@JsonIgnore
	public Set<Integer> getHitsAndBonus() {
		Set<Integer> result = new TreeSet<>(hits);
		result.add(bonus);
		return result;
	}

	@Override
	public int compareTo(WinResult o) {
		return this.time - o.time;
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
