package vn.elca.training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;

@Service
public class ProjectServiceImp implements IProjectService {
    @Autowired
    IProjectDaoService projectDao;

    @Override
    public Project findProjectById(Long id) {
        // TODO Auto-generated method stub
        return projectDao.projectById(id);
    }

    @Override
    public List<Project> findProjectAll() {
        // TODO Auto-generated method stub
        return projectDao.getProjectAll();
    }

    @Override
    public List<Project> findProjectByQuery(String queryStr) {
        // TODO Auto-generated method stub
        return projectDao.projectByQuery(queryStr);
    }

    @Override
    public int createProject(Project project) {
        // TODO Auto-generated method stub
        return projectDao.create(project);
        // return 0;
    }

    @Override
    public int updateProject(Project project) {
        // TODO Auto-generated method stub
        return projectDao.update(project);
        // return 0;
    }

    @Override
    public void deleteProjectById(Long id) {
        // TODO Auto-generated method stub
        projectDao.delete(id);
    }

    @Override
    public boolean checkIdProjectExits(Long id) throws ProjectNumberAlreadyExistsException {
        // TODO Auto-generated method stub
        return projectDao.checkId(id);
    }
}
