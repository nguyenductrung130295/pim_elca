package vn.elca.training.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.elca.training.entities.Employee;

@Service
public class EmployeeImp {
    List<Employee> listEmp = new ArrayList<>();

    public EmployeeImp() {
        listEmp.add(new Employee(Long.valueOf("1"), 1, "tyu", "duc", "trung", new Date()));
        listEmp.add(new Employee(Long.valueOf("2"), 1, "hhh", "van", "toan", new Date()));
        listEmp.add(new Employee(Long.valueOf("3"), 1, "ggg", "thi", "nga", new Date()));
        listEmp.add(new Employee(Long.valueOf("4"), 1, "yyy", "minh", "thang", new Date()));
        listEmp.add(new Employee(Long.valueOf("5"), 1, "ttt", "huu", "loi", new Date()));
    }

    public boolean existsVISA(String visa) {
        for (Employee e : listEmp) {
            if (visa.equals(e.getVisa())) {
                return true;
            }
        }
        return false;
    }
}
