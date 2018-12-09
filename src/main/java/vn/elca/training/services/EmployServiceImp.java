package vn.elca.training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.dao.EmployeeImp;
import vn.elca.training.entities.Employee;

@Service
public class EmployServiceImp implements IEmployeeService {
    @Autowired
    EmployeeImp employeeDao;

    @Override
    public boolean checkedEmployee(String visa) {
        // TODO Auto-generated method stub
        return employeeDao.existsVISA(visa);
    }

    @Override
    public List<Employee> getAllEmployee() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEmployeeNameByVISA(String visa) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeyByVSIA(String visa) {
        // TODO Auto-generated method stub
        return null;
    }
}
