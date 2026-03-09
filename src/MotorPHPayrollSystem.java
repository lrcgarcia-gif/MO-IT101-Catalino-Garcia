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

        // Path to the source CSV file containing employee records
        String filePath = "employee_data.csv";

        // Step 1: Extract data from the CSV file into a 2D Array
        // We store Name, Hourly Rate, and Basic Salary
        String[][] employees = readFromFile(filePath);

        // Step 2: Iterate through the loaded employee data
        // Start from index 1 to skip the CSV Header row
        for (int i = 1; i < employees.length; i++) {
            
            // Check if the row is empty to avoid NullPointerExceptions
            if (employees[i][0] == null) {
                continue;
            }
            
            // Map data from the array to descriptive variables    
            String employeeName = employees[i][0];
            
            /* * NOTE: Since the CSV provides the 'Hourly Rate' and 'Basic Salary', 
             * we calculate based on a standard 40-hour work week placeholder.
             */
            double hoursWorked = 40.0; 
            
            double hourlyRate = parseDoubleSafe(employees[i][1]);
            double basicSalary = parseDoubleSafe(employees[i][2]);
  
           // Step 3: Core Payroll Calculations
            double grossPay = computeGrossPay(hoursWorked, hourlyRate);
            
            // Calculating Semi-Monthly base (usually Basic Salary divided by 2)
            double semiMonthlySalary = basicSalary / 2;
            
            // Step 4: Compute Mandatory Government Deductions
            // Government deductions
            double sss = computeSSS(semiMonthlySalary);
            double philhealth = computePhilhealth(semiMonthlySalary);
            double pagibig = computePagibig(semiMonthlySalary);

            double totalDeductions = sss + philhealth + pagibig;
            double netPay = semiMonthlySalary - totalDeductions;
            
            /**
     * Formats and prints the payroll results to the console.
     */
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
    /**
     * Reads a CSV file and maps specific columns to a 2D String Array.
     * Handles complex CSV lines that contain commas within quotes (like Addresses).
     * @param filePath The path to the employees.csv file
     * @return 2D String array containing [Name, Hourly Rate, Basic Salary]
     */
    
    public static String[][] readFromFile(String filePath) {
        String[][] employees = new String[100][3];
        int index = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && index < 100) {
                // Regex: Splits by comma only if the comma is NOT inside double quote
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data.length > 18) {
                    // Combine First and Last Name (Index 2 and 1)
                    employees[index][0] = data[2] + " " + data[1]; 
                   // Hourly Rate is found at Column Index 18
                    employees[index][1] = data[18]; 
                    // Basic Salary is found at Column Index 13
                    employees[index][2] = data[13]; 
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return employees;
    }

    /**
     * Helper method to convert String currency values to Doubles.
     * Removes commas and quotes found in CSV formatting.
     */
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
    /** Calculates Gross Pay based on hours and rate */
    public static double computeGrossPay(double hoursWorked, double hourlyRate) {
        return hoursWorked * hourlyRate;
    }
    /** Calculates SSS deduction (Placeholder rate: 4.5%) */
    public static double computeSSS(double salary) {
        return salary * 0.045;
    }
    /** Calculates PhilHealth deduction (Placeholder rate: 3.0%) */
    public static double computePhilhealth(double salary) {
        return salary * 0.03;
    }
    /** Calculates Pag-IBIG deduction (Placeholder rate: 2.0%) */
    public static double computePagibig(double salary) {
        return salary * 0.02;
    }
}


    
