package jp.co.sbro.loto_data.probability;

import static jp.co.sbro.util.ProbabilitiyUtil.conbination;

import jp.co.sbro.util.Fraction;

public class WinProbabilityCalculate {

	public static void main(String[] args) throws Exception {
		Fraction first = new Fraction(1, conbination(43, 6));
		Fraction second = new Fraction(conbination(6, 5) * conbination(1, 1), conbination(43, 6));
		Fraction third = new Fraction(conbination(6, 5) * conbination(36, 1), conbination(43, 6));
		Fraction fourth = new Fraction(conbination(6, 4) * conbination(37, 2), conbination(43, 6));
		Fraction fifth = new Fraction(conbination(6, 3) * conbination(37, 3), conbination(43, 6));

		Fraction twoHit = new Fraction(conbination(6, 2) * conbination(37, 4), conbination(43, 6));
		Fraction oneHit = new Fraction(conbination(6, 1) * conbination(37, 5), conbination(43, 6));
		Fraction zeroHit = new Fraction(conbination(6, 0) * conbination(37, 6), conbination(43, 6));

		Fraction win = first.add(second).add(third).add(fourth).add(fifth);
		Fraction lose = twoHit.add(oneHit).add(zeroHit);
		Fraction total = win.add(lose);

		print("first", first);
		print("second", second);
		print("third", third);
		print("fourth", fourth);
		print("fifth", fifth);
		System.out.println("====================================================================");
		print("win", win);
		System.out.println();

		print("twoHit", twoHit);
		print("oneHit", oneHit);
		print("zeroHit", zeroHit);

		System.out.println("====================================================================");
		print("lose", lose);
		System.out.println();

		System.out.println("====================================================================");
		System.out.println("====================================================================");
		print("total", total);
	}

	private static void print(String key, Fraction fraction) {
		System.out.println(key + "	=	" + fraction + "		" + fraction.parcentage() + "	[%]");
	}

}
