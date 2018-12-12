package vn.elca.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.dao.IEmployeeRepository;

@Service
public class EmployServiceImp implements IEmployeeService {
    @Autowired
    IEmployeeRepository employeeRepository;

    /**
     * Check existed employee in database return. Result list of visa not exist {@inheritDoc}
     */
    @Override
    public String checkedEmployee(String[] visa) {
        String result = "";
        for (int i = 0; i < visa.length; i++) {
            if (employeeRepository.countByVisa(visa[i].trim().toUpperCase()) == 0) {
                result += visa[i] + ",";
            }
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
