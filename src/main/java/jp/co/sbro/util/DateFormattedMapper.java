package jp.co.sbro.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DateFormattedMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public DateFormattedMapper() {
		setDateFormat(new LotoDataDateFormat());
	}

}
