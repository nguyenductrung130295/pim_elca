package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Project;

public interface IProjectDaoService {
    List<Project> getProjectAll();

    Project projectById(Long id);

    int update(Project project);

    int create(Project project);

    void delete(Long id);

    List<Project> projectByQuery(String query);
}
