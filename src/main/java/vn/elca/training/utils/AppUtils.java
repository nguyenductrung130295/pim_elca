package vn.elca.training.utils;

import java.util.Set;

import vn.elca.training.entities.Employee;
import vn.elca.training.entities.Project;

public class AppUtils {
    public static final String MSG_NUMBER_ALREADY = "The projet number already existed.";
    public static final int ROW_ON_PAGE = 7;
    public static final int PAGE_NUMBER_DEFAULT = 0;
    public static final String SORT_BY_DEFAULT = "projectNumber";
    public static final String SORT_BY_NAME = "name";
    public static final String SORT_BY_CUSTOMER = "customer";
    public static final String SORT_TYPE_ASC = "asc";
    public static final String SORT_TYPE_DESC = "desc";

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
        String[] results;
        if (visaString.length() > 0) {
            String[] visas = visaString.split(",");
            results = new String[visas.length];
            for (int i = 0; i < visas.length; i++) {
                if (visas[i].length() > 0) {
                    results[n] = visas[i].trim().toUpperCase();
                    n++;
                }
            }
            return results;
        }
        return null;
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

    public static String verifySortByParam(String sortBy) {
        switch (sortBy) {
        case "name":
            return SORT_BY_NAME;
        case "customer":
            return SORT_BY_CUSTOMER;
        default:
            return SORT_BY_DEFAULT;
        }
    }

    public static String verifySortTypeParam(String sortType) {
        if (SORT_TYPE_DESC.equals(sortType)) {
            return SORT_TYPE_DESC;
        }
        return SORT_TYPE_ASC;
    }
}
