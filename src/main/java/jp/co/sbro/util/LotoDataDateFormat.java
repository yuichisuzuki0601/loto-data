package jp.co.sbro.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LotoDataDateFormat extends SimpleDateFormat {

	private static final long serialVersionUID = 1L;

	public LotoDataDateFormat() {
		super("yyyy/MM/dd(E)", Locale.JAPANESE);
	}

}
