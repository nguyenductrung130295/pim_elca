package vn.elca.training.utils;

import java.util.Set;

import vn.elca.training.entities.Employee;
import vn.elca.training.entities.Project;

public class AppUtils {
    public static final String MSG_NUMBER_ALREADY = "The projet number already existed.";
    public static final int ROW_ON_PAGE = 3;
    public static final int PAGE_NUMBER_DEFAULT = 0;

    /**
     * Function utility to check mandatory field
     * 
     * @param project
     * @return true if have any empty field,otherwise
     */
    public static boolean isNeedMandatoryProjectField(Project project) {
        if (project.getProjectNumber() < 0 || project.getName().isEmpty() || project.getCustomer().isEmpty()
                || project.getStatus() == null || project.getStartDate() == null) {
            return true;
        }
        return false;
    }

    /**
     * Convert string to array string visa
     * 
     * @param visaString
     * @return array
     */
    public static String[] splitVisaMember(String visaString) {
        int n = 0;
        String[] visas = visaString.split(",");
        String[] results = new String[visas.length];
        for (int i = 0; i < visas.length; i++) {
            if (visas[i].length() > 0) {
                results[n] = visas[i];
                n++;
            }
        }
        return results;
    }

    /**
     * get string of visa member list of project
     * 
     * @param members
     * @return string
     */
    public static String getListMemberVisaOfProject(Set<Employee> members) {
        String result = "";
        for (Employee e : members) {
            result += e.getVisa() + ",";
        }
        if (result.length() > 3) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
