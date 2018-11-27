package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;

public interface IProjectDaoService {
    List<Project> getProjectAll();

    Project projectById(Long id);

    int update(Project project);

    int create(Project project);

    void delete(Long id);

    List<Project> projectByQuery(String query);

    boolean checkId(Long id) throws ProjectNumberAlreadyExistsException;
}
