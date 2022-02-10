package com.noti.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Service;

@Service
public class DateProcess {

	/*
	 * 오늘 날짜를 반환 합니다.
	 */
	public String getTodayDate( String format ) {
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}
}
