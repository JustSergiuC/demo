package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	private final DemoApplication app = new DemoApplication();

	@Test
	void contextLoads() {
	}

	@Test
	void epoch1980JanuaryFirstIsTuesday() {
		LocalDate epoch = LocalDate.of(1980, Month.JANUARY, 1);
		assertThat(epoch.getDayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
	}

	@Test
	void workingDaysForTheMonth_nullOrNegative_returnsZero() {
		assertThat(app.workingDaysForTheMonth(null)).isZero();
		assertThat(app.workingDaysForTheMonth(-1L)).isZero();
		assertThat(app.workingDaysForTheMonth(-86400L)).isZero();
	}

	@Test
	void workingDaysForTheMonth_january1980_matchesCalendar() {
		long secondsInJanuary1980 = 0L;
		assertThat(app.workingDaysForTheMonth(secondsInJanuary1980)).isEqualTo(23);
	}

	@Test
	void workingDaysForTheMonth_february1980LeapYear_matchesCalendar() {
		long secondsAtStartOfFebruary1980 = 31L * 24L * 60L * 60L;
		assertThat(app.workingDaysForTheMonth(secondsAtStartOfFebruary1980)).isEqualTo(21);
	}

	@Test
	void workingDaysForTheMonth_matchesJavaTimeAcrossTenYears() {
		LocalDate epoch = LocalDate.of(1980, Month.JANUARY, 1);
		int dayLimit = 365 * 10;
		for (long dayOffset = 0; dayOffset < dayLimit; dayOffset++) {
			long seconds = dayOffset * 86400L;
			int expected = expectedWorkingDaysInFullMonthContaining(epoch, seconds);
			assertThat(app.workingDaysForTheMonth(seconds))
					.as("dayOffset=%d seconds=%d", dayOffset, seconds)
					.isEqualTo(expected);
		}
	}

	private static int expectedWorkingDaysInFullMonthContaining(LocalDate epoch, long secondsSinceEpoch) {
		long totDays = secondsSinceEpoch / (24L * 60L * 60L);
		LocalDate dayInMonth = epoch.plusDays(totDays);
		YearMonth ym = YearMonth.from(dayInMonth);
		LocalDate first = ym.atDay(1);
		LocalDate last = ym.atEndOfMonth();
		int count = 0;
		for (LocalDate d = first; !d.isAfter(last); d = d.plusDays(1)) {
			DayOfWeek dow = d.getDayOfWeek();
			if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
				count++;
			}
		}
		return count;
	}

}
