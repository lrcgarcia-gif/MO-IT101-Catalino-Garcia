/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MotorPHPayrollSystem {

    public static void main(String[] args) {
        String filePath = "employee_data.csv";
        
        // Load data from CSV
        String[][] employees = readFromFile(filePath);

        String[] months = {
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December"
        };

        // Loop 1: Each Employee
        for (int i = 1; i < employees.length; i++) {
            if (employees[i][0] == null) continue;

            String empID      = employees[i][0];
            String fullName   = employees[i][1];
            String birthday   = employees[i][2];
            String address    = employees[i][3];
            double hourlyRate = parseCurrency(employees[i][4]);

            // Employee Profile Header
            System.out.println("============================================================");
                System.out.println("                MOTORPH ANNUAL PAYROLL SUMMARY              ");
            System.out.println("============================================================");
            System.out.println("Employee ID : " + empID);
            System.out.println("Name        : " + fullName.toUpperCase());
            System.out.println("Birthday    : " + birthday);
            System.out.println("Address     : " + address);
            System.out.println("------------------------------------------------------------");

            // Loop 2: Each Month
            for (String month : months) {
                
                // Loop 3: Each Cutoff (The part you wanted to change)
                for (int cutoff = 1; cutoff <= 2; cutoff++) {
                    
                    // Specific requirement: "Month 1 to 15" and "Month 16 to 30"
                    String dateRange = (cutoff == 1) ? month + " 1 to 15" : month + " 16 to 30";

                    double totalHoursWorked = 40.0; 
                    double grossSalary = totalHoursWorked * hourlyRate;

                    double sss = computeSSS(grossSalary);
                    double philhealth = computePhilhealth(grossSalary);
                    double pagibig = 100.00; 
                    double totalStatutory = sss + philhealth + pagibig;

                    double taxableIncome = grossSalary - totalStatutory;
                    double withholdingTax = computeTax(taxableIncome);
                    
                    double totalDeductions = totalStatutory + withholdingTax;
                    double netPay = grossSalary - totalDeductions;

                    /* Output Display
                    Cutoff :
                    Total Hours Worked:
                    Gross Salary:
                    
                    Stattutory Deductions:
                    SSS:
                    Philhealth:
                    Pag-ibig:
                    
                    Taxable Incomet & Tax:
                    Withholding Tax:
                    
                    Total Deductions
                    NetPay
                    */
                    
                    System.out.printf("    Cutoff Period     : %s %n", dateRange);
                    System.out.printf("    Total Hours Worked: %.2f hrs %n", totalHoursWorked);
                    System.out.printf("    Gross Salary      : PHP %,.2f %n", grossSalary);
                    System.out.println(" ");
                    System.out.println("    Statutory Deductions ");
                    System.out.printf("    SSS               : PHP %,.2f %n", sss);
                    System.out.printf("    PhilHealth        : PHP %,.2f %n", philhealth);
                    System.out.printf("    Pag-IBIG          : PHP %,.2f %n", pagibig);
                    System.out.println(" ");
                    System.out.println("    Taxable Income & Tax");
                    System.out.printf("    Withholding Tax   : PHP %,.2f %n", withholdingTax);
                    System.out.println("    ----------------------------");
                    System.out.println(" ");
                    System.out.printf("    TOTAL DEDUCTIONS  : PHP %,.2f %n", totalDeductions);
                    System.out.printf("    NET PAY           : PHP %,.2f %n", netPay);
                    System.out.println(" ");
                    System.out.println("    ........................................................");
                    System.out.println(" ");
                }
            }
            System.out.println("\n\n"); 
        }
    }


    public static String[][] readFromFile(String filePath) {
        String[][] employees = new String[100][6]; 
        int index = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null && index < 100) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length > 18) {
                    employees[index][0] = data[0]; // ID
                    employees[index][1] = data[2].replace("\"", "") + " " + data[1].replace("\"", ""); // Name
                    employees[index][2] = data[3]; // Birthday
                    employees[index][3] = data[4].replace("\"", ""); // Address
                    employees[index][4] = data[18]; // Hourly Rate
                    employees[index][5] = data[13]; // Basic Salary
                }
                index++;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return employees;
    }

    public static double computeSSS(double salary) { return salary * 0.045; }
    public static double computePhilhealth(double salary) { return salary * 0.03; }

    public static double computeTax(double taxableIncome) {
        return (taxableIncome > 10000) ? (taxableIncome - 10000) * 0.15 : 0.0;
    }

    public static double parseCurrency(String value) {
        if (value == null || value.isEmpty()) return 0.0;
        String clean = value.replaceAll("[,\"]", "").trim();
        try { return Double.parseDouble(clean); } catch (Exception e) { return 0.0; }
    }
}