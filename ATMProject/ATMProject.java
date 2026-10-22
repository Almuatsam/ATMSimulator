package ATMProject;

import java.util.Scanner;

public class ATMProject {

    static final Scanner scanner = new Scanner(System.in);

    static double balance = 1000.00;
    static int pin = 1234;

    static int depositCount = 0;
    static double depositTotal = 0.00;
    static int withdrawalCount = 0;
    static double withdrawalTotal = 0.00;

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNext()) {
                System.out.println();
                System.out.println("No more input. Exiting.");
                System.exit(0);
            }
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
            System.out.println("Invalid input: '" + scanner.next() + "' is not a whole number. Please try again.");
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNext()) {
                System.out.println();
                System.out.println("No more input. Exiting.");
                System.exit(0);
            }
            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            }
            System.out.println("Invalid input: '" + scanner.next() + "' is not a number. Please try again.");
        }
    }

    static boolean login() {
        final int maxAttempts = 3;
        int incorrectAttempts = 0;

        while (incorrectAttempts < maxAttempts) {
            int enteredPin = readInt("Enter PIN: ");

            if (enteredPin == pin) {
                System.out.println("Welcome!");
                return true;
            }

            incorrectAttempts++;
            int attemptsRemaining = maxAttempts - incorrectAttempts;
            if (attemptsRemaining > 0) {
                System.out.println("Incorrect PIN. Attempts remaining: " + attemptsRemaining);
            } else {
                System.out.println("Incorrect PIN. No attempts remaining.");
            }
        }

        System.out.println("Card locked");
        return false;
    }

    static void menu() {
        System.out.println();
        System.out.println("1. Check balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Account tier");
        System.out.println("5. Savings projection");
        System.out.println("6. Receipt summary");
        System.out.println("7. Change PIN");
        System.out.println("0. Exit");
    }

    static void checkBalance() {
        System.out.printf("Current balance: %.2f%n", balance);
    }

    static void deposit() {
        final double maxDeposit = 5000.00;

        double amount = readDouble("Enter deposit amount: ");

        if (amount > 0 && amount <= maxDeposit) {
            balance += amount;
            depositCount++;
            depositTotal += amount;
            System.out.printf("Deposit successful. New balance: %.2f%n", balance);
        } else {
            System.out.printf("Invalid amount. Deposits must be greater than 0 and no more than %.2f.%n", maxDeposit);
        }
    }

    static void withdraw() {
        final double lowFee = 2.00;
        final double highFee = 5.00;
        final double feeThreshold = 500.00;
        final double sessionLimit = 1000.00;

        double amount = readDouble("Enter withdrawal amount: ");

        if (amount <= 0) {
            System.out.println("Invalid amount. Withdrawal must be greater than 0.");
            return;
        }
        if (amount % 10 != 0) {
            System.out.println("Invalid amount. Withdrawal must be a multiple of 10.");
            return;
        }

        double fee = amount <= feeThreshold ? lowFee : highFee;
        double totalDeduction = amount + fee;

        if (totalDeduction > balance) {
            System.out.printf("Insufficient funds. You need %.2f (amount plus %.2f fee).%n", totalDeduction, fee);
            return;
        }
        if (withdrawalTotal + amount > sessionLimit) {
            System.out.printf("Session withdrawal limit of %.2f exceeded. Remaining allowance: %.2f.%n",
                    sessionLimit, sessionLimit - withdrawalTotal);
            return;
        }

        balance -= totalDeduction;
        withdrawalCount++;
        withdrawalTotal += amount;
        System.out.printf("Withdrawal successful. Amount: %.2f, fee: %.2f. New balance: %.2f%n",
                amount, fee, balance);
    }

    static void accountTier() {
        String tier;
        int interestRate;

        if (balance < 1000) {
            tier = "Bronze";
            interestRate = 1;
        } else if (balance < 5000) {
            tier = "Silver";
            interestRate = 2;
        } else if (balance < 20000) {
            tier = "Gold";
            interestRate = 3;
        } else {
            tier = "Platinum";
            interestRate = 4;
        }

        System.out.printf("Account tier: %s (interest rate: %d%%)%n", tier, interestRate);
    }

    static int currentInterestRate() {
        if (balance < 1000) {
            return 1;
        } else if (balance < 5000) {
            return 2;
        } else if (balance < 20000) {
            return 3;
        } else {
            return 4;
        }
    }

    static void savingsProjection() {
        final int minYears = 1;
        final int maxYears = 10;

        int years = readInt("Enter number of years to project (1-10): ");

        if (years < minYears || years > maxYears) {
            System.out.println("Invalid number of years. Enter a value from 1 to 10.");
            return;
        }

        int ratePercent = currentInterestRate();
        double projected = balance;

        System.out.printf("Projection at %d%% annual interest, starting from %.2f:%n", ratePercent, projected);
        for (int year = 1; year <= years; year++) {
            projected += projected * ratePercent / 100.0;
            System.out.printf("Year %d: %.2f%n", year, projected);
        }
    }

    static void receiptSummary() {
        System.out.println("--- Receipt Summary ---");
        System.out.println("Successful deposits: " + depositCount);
        System.out.printf("Total deposited: %.2f%n", depositTotal);
        System.out.println("Successful withdrawals: " + withdrawalCount);
        System.out.printf("Total withdrawn: %.2f%n", withdrawalTotal);
        System.out.printf("Current balance: %.2f%n", balance);
    }

    static void changePin() {
        final int minPin = 1000;
        final int maxPin = 9999;

        int currentPin = readInt("Enter current PIN: ");

        if (currentPin != pin) {
            System.out.println("Incorrect PIN. PIN not changed.");
            return;
        }

        int newPin = readInt("Enter new PIN: ");

        if (newPin < minPin || newPin > maxPin) {
            System.out.println("Invalid PIN. It must be exactly four digits (1000-9999).");
            return;
        }

        int confirmPin = readInt("Confirm new PIN: ");

        if (confirmPin != newPin) {
            System.out.println("PINs do not match. PIN not changed.");
            return;
        }

        pin = newPin;
        System.out.println("PIN changed successfully.");
    }

    public static void main(String[] args) {
        if (!login()) {
            return;
        }

        int choice;
        do {
            menu();
            choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    accountTier();
                    break;
                case 5:
                    savingsProjection();
                    break;
                case 6:
                    receiptSummary();
                    break;
                case 7:
                    changePin();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }
}
