import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MotorPHPayrollSystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // --- LOGIN SYSTEM ---
        System.out.println("=======================================");
        System.out.println("       MOTORPH SYSTEM LOGIN            ");
        System.out.println("=======================================");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (!password.equals("12345") || (!username.equals("employee") && !username.equals("payroll_staff"))) {
            System.out.println("Incorrect username and/or password.");
            return; // Terminate program
        }

        // Load Data from CSV
        String filePath = "employee_data.csv";
        String[][] employees = readFromFile(filePath);

        if (username.equals("employee")) {
            handleEmployeeMenu(employees, sc);
        } else {
            handleStaffMenu(employees, sc);
        }
    }

    // --- MENU FOR USERNAME: employee ---
    public static void handleEmployeeMenu(String[][] employees, Scanner sc) {
        System.out.println("\n1. Enter your employee number");
        System.out.println("2. Exit the program");
        System.out.print("Choice: ");
        int choice = Integer.parseInt(sc.nextLine());

        if (choice == 1) {
            System.out.print("Enter Employee #: ");
            String id = sc.nextLine();
            int index = findEmployeeIndex(employees, id);

            if (index == -1) {
                System.out.println("Employee number does not exist.");
            } else {
                System.out.println("\nEmployee Number: " + employees[index][0]);
                System.out.println("Employee Name  : " + employees[index][1]);
                System.out.println("Birthday       : " + employees[index][2]);
            }
        }
    }

    // --- MENU FOR USERNAME: payroll_staff ---
    public static void handleStaffMenu(String[][] employees, Scanner sc) {
        System.out.println("\n1. Process Payroll");
        System.out.println("2. Exit the program");
        System.out.print("Choice: ");
        int choice = Integer.parseInt(sc.nextLine());

        if (choice == 1) {
            System.out.println("\n--- SUB-OPTIONS ---");
            System.out.println("1. One employee");
            System.out.println("2. All employees");
            System.out.println("3. Exit the program");
            System.out.print("Choice: ");
            int subChoice = Integer.parseInt(sc.nextLine());

            if (subChoice == 1) {
                System.out.print("Enter employee number: ");
                String id = sc.nextLine();
                int index = findEmployeeIndex(employees, id);
                if (index == -1) {
                    System.out.println("Employee number does not exist.");
                } else {
                    generatePayroll(employees, index);
                }
            } else if (subChoice == 2) {
                for (int i = 1; i < employees.length; i++) {
                    if (employees[i][0] != null) generatePayroll(employees, i);
                }
            }
        }
    }

    // --- PAYROLL GENERATION (June to December) ---
    public static void generatePayroll(String[][] employees, int idx) {
        String[] months = {"June", "July", "August", "September", "October", "November", "December"};
        
        System.out.println("\n------------------------------------------------------------");
        System.out.println("Employee #: " + employees[idx][0]);
        System.out.println("Employee Name: " + employees[idx][1]);
        System.out.println("Birthday: " + employees[idx][2]);

        double hourlyRate = parseCurrency(employees[idx][4]);

        for (String month : months) {
            // 1st Cutoff (1-15) - No Deductions
            double hours1 = calculateAttendanceHours(); // Placeholder for actual attendance logic
            double gross1 = hours1 * hourlyRate;
            System.out.println("\nCutoff Date: " + month + " 1 to " + month + " 15");
            System.out.println("Total Hours Worked: " + hours1);
            System.out.println("Gross Salary: " + gross1);
            System.out.println("Net Salary: " + gross1);

            // 2nd Cutoff (16-30) - Includes Deductions
            double hours2 = calculateAttendanceHours();
            double gross2 = hours2 * hourlyRate;
            
            // Per requirements: compute deductions based on combined monthly gross
            double monthlyGross = gross1 + gross2;
            double sss = monthlyGross * 0.045;
            double phil = monthlyGross * 0.03;
            double pagibig = 100.00;
            double tax = (monthlyGross > 10000) ? (monthlyGross - 10000) * 0.15 : 0;
            double totalDeduct = sss + phil + pagibig + tax;

            System.out.println("\nCutoff Date: " + month + " 16 to " + month + " 30 (Second payout includes all deductions)");
            System.out.println("Total Hours Worked: " + hours2);
            System.out.println("Gross Salary: " + gross2);
            System.out.println("SSS: " + sss);
            System.out.println("PhilHealth: " + phil);
            System.out.println("Pag-IBIG: " + pagibig);
            System.out.println("Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeduct);
            System.out.println("Net Salary: " + (gross2 - totalDeduct));
        }
    }

    /**
     * Logic for Hours Worked (8:00 AM - 5:00 PM)
     * Requirement: 8:05 AM login is not late (still 8 hours).
     * 8:30 AM login to 5:30 PM logout = 7.5 hours.
     */
    public static double calculateAttendanceHours() {
        // This is a logic placeholder. In a full system, you would parse the 
        // login/logout strings from your attendance CSV.
        // Example logic:
        // if (login <= 8:05) start = 8:00
        // if (logout >= 17:00) end = 17:00
        // return (end - start) - 1 hour break;
        return 80.0; // Placeholder for a 15-day period (approx 80 hours)
    }

    // --- CSV HELPER METHODS ---
    public static String[][] readFromFile(String filePath) {
        String[][] employees = new String[100][6];
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null && index < 100) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length > 18) {
                    employees[index][0] = data[0]; // ID
                    employees[index][1] = data[2].replace("\"", "") + " " + data[1].replace("\"", ""); // Name
                    employees[index][2] = data[3]; // Bday
                    employees[index][4] = data[18]; // Hourly
                }
                index++;
            }
        } catch (Exception e) { }
        return employees;
    }

    public static int findEmployeeIndex(String[][] employees, String id) {
        for (int i = 0; i < employees.length; i++) {
            if (employees[i][0] != null && employees[i][0].equals(id)) return i;
        }
        return -1;
    }

    public static double parseCurrency(String value) {
        if (value == null) return 0;
        try { return Double.parseDouble(value.replaceAll("[,\"]", "")); } catch (Exception e) { return 0; }
    }
}