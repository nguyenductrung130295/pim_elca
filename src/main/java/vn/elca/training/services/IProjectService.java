package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;

public interface IProjectService {
    Project findProjectById(Long id);

    List<Project> findProjectAll();

    List<Project> findProjectByQuery(String queryStr, String queryStatus);

    boolean createProject(Project project);

    int updateProject(Project project);

    boolean deleteProjectById(Long id);

    List<Integer> delteProjectNumberList(int[] listNumber);

    void checkProjectNumberExits(int i) throws ProjectNumberAlreadyExistsException;
}
