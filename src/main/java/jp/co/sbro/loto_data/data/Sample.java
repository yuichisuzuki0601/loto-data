package jp.co.sbro.loto_data.data;

import java.util.Map;

public class Sample {

	public static void main(String[] args) throws Exception {

		// スクレイピングの開始
		Scraper scraper = new Scraper(1200);

		// リポジトリの取得
		WinResultRepository repos = scraper.getWinResultRepository();

		// 最新の結果を表示してみる
		System.out.println("最新の結果");
		System.out.println(repos.getLatestWinResult());
		System.out.println();

		// リポジトリを直近100回に絞り込み
		int cnt = 100;
		repos = repos.filterByCnt(cnt);
		System.out.println("直近" + cnt + "回の結果");
		System.out.println(repos);
		System.out.println();

		// 各数字の出現頻度を取得し出現が多い順に並び替えて表示
		System.out.println("各数字の出現回数");
		Map<Integer, Integer> frequency = repos.frequency().sortByKey().reverse().sortByValue().reverse();
		frequency.entrySet().stream().map(e -> String.format("%02d", e.getKey()) + " = " + e.getValue() + "回")
				.forEach(System.out::println);
		System.out.println();
	}

}
