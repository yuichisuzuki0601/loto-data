package jp.co.sbro.loto_data.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jp.co.sbro.loto_data.data.WinResult.Prize;

public class Scraper {

	private final static String BASE_URL = "https://www.mizuhobank.co.jp/retail/takarakuji/loto/";
	private final static String PAST_URL = BASE_URL + "backnumber/loto6#time#.html";
	private final static String MAIN_URL = BASE_URL + "loto6/csv/A102#time#.CSV?#date#";

	private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy年MM月dd日");
	private final static SimpleDateFormat JSDF = new SimpleDateFormat("GGGGy年M月d日", new Locale("ja", "JP", "JP"));

	private final WinResultRepository repos;

	public Scraper() throws Exception {
		this(1);
	}

	public Scraper(int time) throws Exception {
		this.repos = scrape(time);
	}

	public WinResultRepository getWinResultRepository() {
		return repos;
	}

	private WinResultRepository scrape(int fromTime) throws Exception {
		WinResultRepository repos = new WinResultRepository();

		// 結果の取得(460回目まで)
		for (int i = 1; i <= 441; i += 20) {
			if (i >= fromTime) {
				repos.merge(scrapePastResults(i));
			}
		}

		// 結果の取得(461回目以降)
		for (int i = 461; i <= 99999; i++) {
			if (i >= fromTime) {
				try {
					repos.merge(scrapeResults(i));
				} catch (IOException e) {
					break;
				}
			}
		}

		return repos;
	}

	private WinResultRepository scrapePastResults(int timeUrl) throws IOException, ParseException {
		WinResultRepository repos = new WinResultRepository();
		String url = PAST_URL.replace("#time#", String.format("%04d", timeUrl));
		Document document = Jsoup.connect(url).get();
		Elements elms = document.select("tbody > tr");
		for (Element elm : elms) {
			String[] data = elm.text().split(" ");
			int time = 0;
			try {
				time = Integer.parseInt(removeStr(data[0], "第", "回"));
			} catch (NumberFormatException e) {
				// パース出来ない = 不要なタグ
				continue;
			}
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

	private WinResultRepository scrapeResults(int timeUrl) throws MalformedURLException, IOException, ParseException {
		WinResultRepository repos = new WinResultRepository();
		String url = MAIN_URL.replace("#time#", String.format("%04d", timeUrl));
		url = url.replace("#date#", String.valueOf(new Date().getTime()));
		Document document = Jsoup.parse(new URL(url).openStream(), "SJIS", url);
		String[] lines = document.body().toString().split(" ");

		WinResult result = null;
		for (String line : lines) {
			if (contains(line, "第", "回ロト６")) {
				String[] datas = line.split(",");
				int time = Integer.parseInt(removeStr(datas[0], "第", "回ロト６"));
				result = new WinResult(time);
				repos.put(time, result);
				result.setDate(JSDF.parse(datas[2]));
			} else if (contains(line, "本数字", "ボーナス数字") && !contains(line, "個")) {
				String[] datas = line.split(",");
				for (int i = 1; i <= 6; ++i) {
					result.addHits(Integer.parseInt(datas[i]));
				}
				result.setBonus(Integer.parseInt(datas[8]));
			} else if (contains(line, "等", "該当なし") || contains(line, "等", "口", "円")) {
				Prize prize = null;
				if (contains(line, "等", "該当なし")) {
					prize = result.addPrize(0);
				} else {
					String[] datas = line.split(",");
					prize = result.addPrize(Integer.parseInt(removeStr(datas[1], "口")));
					prize.setPrice(Integer.parseInt(removeStr(datas[2], "円")));
				}
			} else if (contains(line, "キャリーオーバー")) {
				result.setCarryOver(Integer.parseInt(removeStr(line.split(",")[1], "円")));
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

	@SuppressWarnings("unused")
	private boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
