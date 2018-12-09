package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.utils.ProjectStatusEnum;

public interface IProjectDaoService {
    List<Project> getProjectAll();

    Project projectById(Long id);

    int update(Project project);

    boolean create(Project project);

    boolean delete(Long id);

    List<Project> projectByQuery(String query, ProjectStatusEnum status);

    List<Project> projectByQuery(String query);

    List<Project> projectByQuery(ProjectStatusEnum status);

    void checkNumber(int number) throws ProjectNumberAlreadyExistsException;

    boolean deleteByNumberProject(int num);
}
