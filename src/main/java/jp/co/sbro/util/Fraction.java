package jp.co.sbro.util;

import java.math.BigDecimal;

public class Fraction {

	private double numerator = 0;
	private double denominator = 1;

	public Fraction(double numerator, double denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public double getNumerator() {
		return numerator;
	}

	public double getDenominator() {
		return denominator;
	}

	public Fraction add(Fraction f) throws Exception {
		if (this.denominator != f.denominator) {
			throw new Exception("cant plus deferrent denominator");
		}
		return new Fraction(this.numerator + f.numerator, this.denominator);
	}

	public Fraction sub(Fraction f) throws Exception {
		if (this.denominator != f.denominator) {
			throw new Exception("cant sub deferrent denominator");
		}
		return new Fraction(this.numerator - f.numerator, this.denominator);
	}

	public double decimal() {
		return numerator / denominator;
	}

	public String parcentage() {
		return BigDecimal.valueOf(decimal() * 100).toPlainString();
	}

	private boolean hasDecimal(double d) {
		return d - Math.floor(d) != 0;
	}

	@Override
	public String toString() {
		String n;
		if (hasDecimal(numerator)) {
			n = String.valueOf(numerator);
		} else {
			n = String.valueOf((int) numerator);
		}
		String d;
		if (hasDecimal(denominator)) {
			d = String.valueOf(denominator);
		} else {
			d = String.valueOf((int) denominator);
		}
		return n + "/" + d;
	}

}
