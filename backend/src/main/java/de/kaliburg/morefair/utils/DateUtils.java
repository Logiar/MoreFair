package de.kaliburg.morefair.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

public class DateUtils {

  public static boolean isAprilFoolsDay() {
    LocalDate todayUtc = LocalDate.now(ZoneOffset.UTC);
    return todayUtc.getMonth() == Month.APRIL && todayUtc.getDayOfMonth() == 1;
  }
}
