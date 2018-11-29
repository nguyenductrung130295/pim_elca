package vn.elca.training.utils;

import java.util.Comparator;

import vn.elca.training.entities.Project;

public class SortASCProjectNumber implements Comparator<Project> {
    @Override
    public int compare(Project pro1, Project pro2) {
        // TODO Auto-generated method stub
        int num1 = pro1.getProjectNumber();
        int num2 = pro2.getProjectNumber();
        return num1 - num2;
    }
}
