package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public int workingDaysForTheMonth(Long x){
		int res = 0;

		if (x == null || x < 0) return res;

		int totDays = (int) (x / 60 / 60 / 24);
		int remainingDays = totDays;

		int[] yearResult = subDaysTillCorrectYear(remainingDays);
        remainingDays = yearResult[0];
        int year = yearResult[1];

		int[] monthDays = {
            31, isYear(year) ? 29 : 28, 31, 30, 31, 30, 
            31, 31, 30, 31, 30, 31
        };

        int[] monthResult = subDaysTillCorrectMonth(remainingDays, monthDays);
        remainingDays = monthResult[0];
        int currMonth = monthResult[1];

        int daysCurrMonth = monthDays[currMonth];
        int firstDayOfMonth = totDays - remainingDays;
		int startDayOfWeek = (2 + firstDayOfMonth) % 7;

        for (int i = 0; i < daysCurrMonth; i++) {
            int currentDayOfWeek = (startDayOfWeek + i) % 7;
            
            if (currentDayOfWeek != 0 && currentDayOfWeek != 6) res++;

        }

        return res;
	}

	public int[] subDaysTillCorrectYear(int days) {
        int year = 1980;
        while (true) {
            int daysInYear = isYear(year) ? 366 : 365;
            if (days >= daysInYear) {
                days -= daysInYear;
                year++;
            } else {
                break;
            }
        }
        return new int[]{days, year};
    }

	public int[] subDaysTillCorrectMonth(int days, int[] monthDays) {
        int month = 0;
        while (true) {
            int daysInMonth = monthDays[month];
            if (days >= daysInMonth) {
                days -= daysInMonth;
                month++;
            } else {
                break;
            }
        }
        return new int[]{days, month};
    }

	public boolean isYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

}
