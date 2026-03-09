/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Katallino
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MotorPHPayrollSystem {

    public static void main(String[] args) {

        // Correct path to your uploaded file
        String filePath = "employee_data.csv";

        // We increase the array size to hold more data columns if needed
        // Format: [Row][0=Name, 1=Hourly Rate, 2=Basic Salary]
        String[][] employees = readFromFile(filePath);

        // Loop through employees (starting at index 1 to skip the header)
        for (int i = 1; i < employees.length; i++) {

            if (employees[i][0] == null) {
                continue;
            }

            String employeeName = employees[i][0];
            
            // NOTE: The CSV doesn't have "Hours Worked". 
            // I'm using a placeholder of 40 hours for demonstration.
            double hoursWorked = 40.0; 
            
            // Clean the numeric strings (remove commas and quotes)
            double hourlyRate = parseDoubleSafe(employees[i][1]);
            double basicSalary = parseDoubleSafe(employees[i][2]);

            // Compute payroll
            double grossPay = computeGrossPay(hoursWorked, hourlyRate);
            
            // In your CSV, the Gross Semi-monthly rate is often Basic Salary / 2
            double semiMonthlySalary = basicSalary / 2;

            // Government deductions
            double sss = computeSSS(semiMonthlySalary);
            double philhealth = computePhilhealth(semiMonthlySalary);
            double pagibig = computePagibig(semiMonthlySalary);

            double totalDeductions = sss + philhealth + pagibig;
            double netPay = semiMonthlySalary - totalDeductions;

            // Print payroll details
            System.out.println("Employee: " + employeeName);
            System.out.println("Hours Worked (Assumed): " + hoursWorked);
            System.out.println("Hourly Rate: " + hourlyRate);
            System.out.println("Basic Salary: " + basicSalary);
            System.out.println("Semi-Monthly Gross: " + semiMonthlySalary);

            System.out.println("--- Deductions ---");
            System.out.println("SSS: " + sss);
            System.out.println("PhilHealth: " + philhealth);
            System.out.println("Pag-IBIG: " + pagibig);

            System.out.println("Total Deductions: " + totalDeductions);
            System.out.println("Net Pay: " + netPay);
            System.out.println("--------------------------------------");
        }
    }

    public static String[][] readFromFile(String filePath) {
        String[][] employees = new String[100][3];
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && index < 100) {
                // regex to split by comma but ignore commas inside quotes (like in addresses)
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data.length > 18) {
                    // Index 2: First Name, Index 1: Last Name
                    employees[index][0] = data[2] + " " + data[1]; 
                    // Index 18: Hourly Rate
                    employees[index][1] = data[18]; 
                    // Index 13: Basic Salary
                    employees[index][2] = data[13]; 
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return employees;
    }

    // Helper to remove commas and quotes before converting to double
    public static double parseDoubleSafe(String value) {
        if (value == null || value.isEmpty()) return 0.0;
        // Remove quotes and commas
        String cleanValue = value.replace("\"", "").replace(",", "").trim();
        try {
            return Double.parseDouble(cleanValue);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static double computeGrossPay(double hoursWorked, double hourlyRate) {
        return hoursWorked * hourlyRate;
    }

    public static double computeSSS(double salary) {
        return salary * 0.045;
    }

    public static double computePhilhealth(double salary) {
        return salary * 0.03;
    }

    public static double computePagibig(double salary) {
        return salary * 0.02;
    }
}


    
