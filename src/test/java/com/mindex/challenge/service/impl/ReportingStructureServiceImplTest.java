package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String ReportingStructureIdUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        ReportingStructureIdUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testReadOneLevel() {
        Employee testEmployee = new Employee();
        List<Employee> directReports = new ArrayList<>();

        Employee one = new Employee();
        Employee two = new Employee();
        Employee three = new Employee();

        directReports.add(one);
        directReports.add(two);
        directReports.add(three);
        testEmployee.setDirectReports(directReports);

        int expectedNumberOfReports = 3;

        String employeeId = createNewEmployee(testEmployee);
        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(ReportingStructureIdUrl, ReportingStructure.class, employeeId).getBody();
        assertEquals(expectedNumberOfReports, readReportingStructure.getNumberOfReports());
    }

    @Test
    public void testReadTwoLevels() {
        Employee testEmployee = new Employee();
        List<Employee> testDirectReports = new ArrayList<>();

        Employee one = new Employee();
        Employee two = new Employee();
        Employee three = new Employee();
        Employee four = new Employee();

        List<Employee> oneDirectReports = new ArrayList<>();
        oneDirectReports.add(two);
        oneDirectReports.add(three);
        one.setDirectReports(oneDirectReports);

        testDirectReports.add(one);
        testDirectReports.add(four);
        testEmployee.setDirectReports(testDirectReports);

        int expectedNumberOfReports = 4;

        String employeeId = createNewEmployee(testEmployee);
        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(ReportingStructureIdUrl, ReportingStructure.class, employeeId).getBody();
        assertEquals(expectedNumberOfReports, readReportingStructure.getNumberOfReports());
    }

    @Test
    public void testReadThreeLevels() {
        Employee testEmployee = new Employee();
        List<Employee> testDirectReports = new ArrayList<>();

        Employee one = new Employee();
        Employee two = new Employee();
        Employee three = new Employee();
        Employee four = new Employee();
        Employee five = new Employee();
        Employee six = new Employee();

        List<Employee> twoDirectReports = new ArrayList<>();
        twoDirectReports.add(three);
        twoDirectReports.add(six);
        two.setDirectReports(twoDirectReports);

        List<Employee> oneDirectReports = new ArrayList<>();

        oneDirectReports.add(two);
        oneDirectReports.add(five);
        one.setDirectReports(oneDirectReports);

        testDirectReports.add(four);
        testDirectReports.add(one);
        testEmployee.setDirectReports(testDirectReports);

        int expectedNumberOfReports = 6;

        String employeeId = createNewEmployee(testEmployee);
        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(ReportingStructureIdUrl, ReportingStructure.class, employeeId).getBody();
        assertEquals(expectedNumberOfReports, readReportingStructure.getNumberOfReports());
    }

    @Test
    public void testReadNonTopLevel() {
        Employee testEmployee = new Employee();

        Employee one = new Employee();
        Employee two = new Employee();
        Employee three = new Employee();
        Employee four = new Employee();
        Employee five = new Employee();
        Employee six = new Employee();

        List<Employee> twoDirectReports = new ArrayList<>();
        twoDirectReports.add(three);
        twoDirectReports.add(six);
        two.setDirectReports(twoDirectReports);

        List<Employee> testDirectReports = new ArrayList<>();
        testDirectReports.add(two);
        testDirectReports.add(five);
        testEmployee.setDirectReports(testDirectReports);

        List<Employee> oneDirectReports = new ArrayList<>();
        oneDirectReports.add(four);
        oneDirectReports.add(testEmployee);
        one.setDirectReports(oneDirectReports);

        int expectedNumberOfReports = 4;

        String employeeId = createNewEmployee(testEmployee);
        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(ReportingStructureIdUrl, ReportingStructure.class, employeeId).getBody();
        assertEquals(expectedNumberOfReports, readReportingStructure.getNumberOfReports());
    }

    private String createNewEmployee(Employee employee) {
        return restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody().getEmployeeId();
    }
}
