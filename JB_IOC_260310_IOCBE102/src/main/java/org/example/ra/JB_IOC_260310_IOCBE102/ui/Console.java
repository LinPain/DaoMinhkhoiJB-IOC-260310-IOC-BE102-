package org.example.ra.JB_IOC_260310_IOCBE102.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Console {
    private final Scanner scanner;
    private final PrintStream out;

    public Console(InputStream input, PrintStream out) {
        this.scanner = new Scanner(input);
        this.out = out;
    }

    public void title(String text) {
        out.println();
        out.println("============================================================");
        out.println(text);
        out.println("============================================================");
    }

    public void line(String text) {
        out.println(text);
    }

    public void success(String text) {
        out.println("[OK] " + text);
    }

    public void error(String text) {
        out.println("[ERROR] " + text);
    }

    public String readLine(String label) {
        out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    public String readOptional(String label, String currentValue) {
        out.print(label + " [" + safe(currentValue) + "]: ");
        String value = scanner.nextLine().trim();
        return value.isBlank() ? currentValue : value;
    }

    public String readOptionalOrBack(String label, String currentValue) {
        out.print(label + " [" + safe(currentValue) + "] (type back to cancel): ");
        String value = scanner.nextLine().trim();
        cancelIfBack(value);
        return value.isBlank() ? currentValue : value;
    }

    public String readEmail(String label) {
        while (true) {
            String value = readLine(label);
            if (isValidEmail(value)) {
                return value;
            }
            error("Please enter a valid email address.");
        }
    }

    public String readOptionalEmail(String label, String currentValue) {
        while (true) {
            String value = readOptional(label, currentValue);
            if (isValidEmail(value)) {
                return value;
            }
            error("Please enter a valid email address.");
        }
    }

    public String readOptionalEmailOrBack(String label, String currentValue) {
        while (true) {
            String value = readOptionalOrBack(label, currentValue);
            if (isValidEmail(value)) {
                return value;
            }
            error("Please enter a valid email address.");
        }
    }

    public int readInt(String label) {
        while (true) {
            out.print(label + ": ");
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                error("Please enter a valid number.");
            }
        }
    }

    public int readIntOrBack(String label) {
        while (true) {
            out.print(label + " (type back to cancel): ");
            String value = scanner.nextLine().trim();
            cancelIfBack(value);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                error("Please enter a valid number.");
            }
        }
    }

    public int readOptionalInt(String label, int currentValue) {
        while (true) {
            out.print(label + " [" + currentValue + "]: ");
            String value = scanner.nextLine().trim();
            if (value.isBlank()) {
                return currentValue;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                error("Please enter a valid number.");
            }
        }
    }

    public int readOptionalIntOrBack(String label, int currentValue) {
        while (true) {
            out.print(label + " [" + currentValue + "] (type back to cancel): ");
            String value = scanner.nextLine().trim();
            cancelIfBack(value);
            if (value.isBlank()) {
                return currentValue;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                error("Please enter a valid number.");
            }
        }
    }

    public LocalDate readDate(String label) {
        while (true) {
            out.print(label + " (yyyy-MM-dd): ");
            String value = scanner.nextLine().trim();
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException exception) {
                error("Please enter a valid date, for example 2005-04-30.");
            }
        }
    }

    public LocalDate readOptionalDate(String label, LocalDate currentValue) {
        while (true) {
            out.print(label + " [" + currentValue + "] (yyyy-MM-dd): ");
            String value = scanner.nextLine().trim();
            if (value.isBlank()) {
                return currentValue;
            }
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException exception) {
                error("Please enter a valid date, for example 2005-04-30.");
            }
        }
    }

    public boolean readBoolean(String label) {
        while (true) {
            out.print(label + " (M/F): ");
            String value = scanner.nextLine().trim();
            if (value.equalsIgnoreCase("m") || value.equalsIgnoreCase("male")) {
                return true;
            }
            if (value.equalsIgnoreCase("f") || value.equalsIgnoreCase("female")) {
                return false;
            }
            error("Please enter M or F.");
        }
    }

    public boolean confirm(String label) {
        out.print(label + " (y/N): ");
        String value = scanner.nextLine().trim();
        return value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes");
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static void cancelIfBack(String value) {
        if (value.equalsIgnoreCase("back")) {
            throw new InputCancelledException();
        }
    }

    private static boolean isValidEmail(String value) {
        return value != null
                && value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
}
