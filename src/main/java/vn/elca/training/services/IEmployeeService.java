package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Employee;

public interface IEmployeeService {
    boolean checkedEmployee(String visa);

    List<Employee> getAllEmployee();

    String getEmployeeNameByVISA(String visa);

    Employee getEmployeyByVSIA(String visa);
}
