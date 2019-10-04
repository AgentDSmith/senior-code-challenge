package com.mindex.challenge.data;

import org.springframework.util.CollectionUtils;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    public ReportingStructure(Employee employee) {
        this.employee = employee;
        this.numberOfReports = calculateNumberOfReports(employee);
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    private int calculateNumberOfReports(Employee employee) {
        int numReports = 0;
        if (!CollectionUtils.isEmpty(employee.getDirectReports())) {
            numReports = employee.getDirectReports().size();

            //recursively retrieve the number of reports for each employee in the list
            for (Employee report : employee.getDirectReports()) {
                numReports += calculateNumberOfReports(report);
            }
        }
        return numReports;
    }
}
