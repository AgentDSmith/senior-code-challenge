package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure create(String id) {
        LOG.debug("Creating reporting structure for employee id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        int numberOfReports = calculateNumberOfReports(employee);

        return new ReportingStructure(employee, numberOfReports);
    }

    private int calculateNumberOfReports(Employee employee) {
        int numReports = 0;
        if (!CollectionUtils.isEmpty(employee.getDirectReports())) {
            numReports = employee.getDirectReports().size();

            //recursively retrieve the number of reports for each employee in the list
            for (Employee report : employee.getDirectReports()) {
                numReports += calculateNumberOfReports(employeeRepository.findByEmployeeId(report.getEmployeeId()));
            }
        }
        return numReports;
    }
}
