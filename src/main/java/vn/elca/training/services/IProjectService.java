package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Project;

public interface IProjectService {
    Project findProjectById(Long id);

    List<Project> findProjectAll();

    List<Project> findProjectByQuery(String queryStr);

    int createProject(Project project);

    int updateProject(Project project);

    void deleteProjectById(Long id);
}
