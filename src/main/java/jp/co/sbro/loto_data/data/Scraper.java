package jp.co.sbro.loto_data.data;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jp.co.sbro.loto_data.data.WinResult.Prize;

public class Scraper {

	private final static String MAIN_URL = "https://www.mizuhobank.co.jp/takarakuji/loto/";
	private final static String RECENT_URL = MAIN_URL + "loto6/index.html";
	private final static String LAST_YEAR_URL = MAIN_URL + "backnumber/lt6-#yearMonth#.html";
	private final static String PAST_URL = MAIN_URL + "backnumber/loto6#time#.html";

	private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy年MM月dd日");

	private final WinResultRepository repos;

	public Scraper() throws Exception {
		this.repos = scrape();
	}

	public WinResultRepository getWinResultRepository() {
		return repos;
	}

	private WinResultRepository scrape() throws Exception {
		WinResultRepository repos = new WinResultRepository();

		// 過去の結果
		for (int i = 1; i <= 99999; i += 20) {
			try {
				repos.merge(scrapePastResults(i));
			} catch (IOException e) {
				break;
			}
		}

		// 過去一年間の結果
		repos.merge(scrapeLastYearResults());

		// 最新の結果
		repos.merge(scrapeResults(RECENT_URL));

		return repos;
	}

	private WinResultRepository scrapePastResults(int timeUrl) throws IOException, ParseException {
		WinResultRepository repos = new WinResultRepository();
		String url = PAST_URL.replace("#time#", String.format("%04d", timeUrl));
		Document document = Jsoup.connect(url).get();
		Elements elms = document.select("tbody > tr");
		for (Element elm : elms) {
			String[] data = elm.text().split(" ");
			int time = Integer.parseInt(removeStr(data[0], "第", "回"));
			WinResult result = new WinResult(time);
			result.setDate(SDF.parse(data[1]));
			for (int i = 1; i <= 6; ++i) {
				result.addHits(Integer.parseInt(data[1 + i]));
			}
			result.setBonus(Integer.parseInt(data[8]));
			repos.put(time, result);
		}
		return repos;
	}

	private WinResultRepository scrapeLastYearResults() throws Exception {
		WinResultRepository repos = new WinResultRepository();
		Calendar c = Calendar.getInstance();
		// 毎月1日の午前中はまだページができてないので対応
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR);
		if (day == 1 && (0 <= hour && hour <= 12)) {
			c.add(Calendar.MONTH, -1);
		}
		c.add(Calendar.YEAR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		for (int i = 1; i <= 12; ++i) {
			String url = LAST_YEAR_URL.replaceAll("#yearMonth#", sdf.format(c.getTime()));
			repos.merge(scrapeResults(url));
			c.add(Calendar.MONTH, 1);
		}
		return repos;
	}

	private WinResultRepository scrapeResults(String url) throws Exception {
		WinResultRepository repos = new WinResultRepository();
		Document document = Jsoup.connect(url).get();
		Elements elms = document.select("th, td");
		WinResult result = null;
		boolean nohitFlg = false;
		boolean performanceFlg = false;
		boolean carryOverFlg = false;
		Prize prize = null;
		for (Element elm : elms) {
			String text = elm.text();
			if (contains(text, "第", "回")) {
				int time = Integer.parseInt(removeStr(text, "第", "回"));
				result = new WinResult(time);
				repos.put(time, result);
			} else if (contains(text, "年", "月", "日")) {
				result.setDate(SDF.parse(text));
			} else if (isNumber(text)) {
				result.addHits(Integer.parseInt(text));
			} else if (contains(text, "(", ")")) {
				result.setBonus(Integer.parseInt(removeStr(text, "\\(", "\\)")));
			} else if (contains(text, "該当なし")) {
				if (!nohitFlg) {
					prize = result.addPrize(0);
				}
				nohitFlg = !nohitFlg;
			} else if (contains(text, "口")) {
				prize = result.addPrize(Integer.parseInt(removeStr(text, ",", "口")));
			} else if (contains(text, "円")) {
				if (performanceFlg) {
					performanceFlg = false;
				} else if (carryOverFlg) {
					result.setCarryOver(Integer.parseInt(removeStr(text, ",", "円")));
					carryOverFlg = false;
				} else {
					prize.setPrice(Integer.parseInt(removeStr(text, ",", "円")));
				}
			} else if (contains(text, "販売実績額")) {
				performanceFlg = true;
			} else if (contains(text, "キャリーオーバー")) {
				carryOverFlg = true;
			}
		}
		return repos;
	}

	private boolean contains(String org, String... conds) {
		boolean result = true;
		for (String cond : conds) {
			result = result && org.contains(cond);
			if (!result) {
				break;
			}
		}
		return result;
	}

	private String removeStr(String org, String... removes) {
		String result = org;
		for (String remove : removes) {
			result = result.replaceAll(remove, "");
		}
		return result.trim();
	}

	private boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
