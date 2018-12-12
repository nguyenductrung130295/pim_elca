package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;

public interface IProjectService {
    Project findProjectById(Long id);

    List<Project> findProjectAll(int pageNumberDefault, int rowOnPage);

    List<Project> findProjectByQuery(String queryStr, String queryStatus, int pageNum, int rowOnPage);

    boolean createProject(Project project, String[] visas);

    void deleteProjectByIdAndNewStatus(Long id) throws Exception;

    void checkProjectNumberExits(int i) throws ProjectNumberAlreadyExistsException;

    void initData();

    void deleteProjectByListIdAndNewStatus(Long[] projectIds) throws Exception;

    String getListMemberVisaOfProject(Project project);

    boolean updateProject(Project project, String[] splitVisaMember);
}
