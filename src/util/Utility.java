package util;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Utility {
    public static String getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return "" + calendar.get(Calendar.DATE) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    public static int getInteger() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("[0-9]*")) {
                try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.printf("❌ Mismatched input...\nEnter an integer:\n");
                }
            } else
                System.out.printf("❌ Mismatched input...\nEnter an integer:\n");
        }
    }
}