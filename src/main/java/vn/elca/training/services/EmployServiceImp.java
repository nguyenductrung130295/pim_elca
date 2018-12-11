package vn.elca.training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.dao.EmployeeImp;
import vn.elca.training.dao.IEmployeeRepository;
import vn.elca.training.entities.Employee;

@Service
public class EmployServiceImp implements IEmployeeService {
    @Autowired
    EmployeeImp employeeDao;
    @Autowired
    IEmployeeRepository employeeRepository;

    @Override
    public String checkedEmployee(String[] visa) {
        String result = "";
        for (int i = 0; i < visa.length; i++) {
            if (employeeRepository.countByVisa(visa[i]) == 0) {
                result += visa[i] + ",";
            }
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
        // return employeeDao.existsVISA(visa);
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
