package vn.elca.training.utils;

import vn.elca.training.entities.Project;

public class AppUtils {
    public static boolean isNeedMandatoryProjectField(Project project) {
        if (project.getProjectNumber() < 0 || project.getName().isEmpty() || project.getCustomer().isEmpty()
                || project.getGroupId() == null || project.getGroupId() < 0 || project.getStatus() == null
                || project.getStartDate() == null || project.getEndDate() == null) {
            return true;
        }
        return false;
    }
}
