package vn.elca.training.utils;

import java.util.ArrayList;
import java.util.List;

import vn.elca.training.entities.Project;

public class AppUtils {
    public static boolean isNeedMandatoryProjectField(Project project) {
        if (project.getProjectNumber() < 0 || project.getName().isEmpty() || project.getCustomer().isEmpty()
                || /* project.getGroupId() == null || project.getGroupId() < 0 || */ project.getStatus() == null
                || project.getStartDate() == null) {
            return true;
        }
        return false;
    }
    public static String[] splitVisaMember(String visaString) {
    	int n=0;
    	String[] visas = visaString.split(",");
    	String[]  results = new String[visas.length];
    	for(int i=0;i<visas.length;i++) {
    		if(visas[i].length()>0) {
    			results[n]=visas[i];
    			n++;
    		}
    	}
    	return results;
    }
}
