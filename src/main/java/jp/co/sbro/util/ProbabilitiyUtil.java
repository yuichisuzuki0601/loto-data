package jp.co.sbro.util;

public final class ProbabilitiyUtil {

	private ProbabilitiyUtil() {
	}

	/**
	 * 下限付き階乗
	 * 
	 * @param n
	 * @param limit
	 * @return
	 */
	public static double factorial(int n, int limit) {
		long result = 1;
		for (int i = n; i > n - limit; --i) {
			result *= i;
		}
		return result;
	}

	/**
	 * 階乗
	 * 
	 * @param n
	 * @return
	 */
	public static double factorial(int n) {
		return factorial(n, n);
	}

	/**
	 * 順列
	 * 
	 * @param n
	 * @param r
	 * @return
	 */
	public static double permutation(int n, int r) {
		return factorial(n, r);
	}

	/**
	 * 組合せ
	 * 
	 * @param n
	 * @param r
	 * @return
	 */
	public static double conbination(int n, int r) {
		return permutation(n, r) / factorial(r);
	}

}
