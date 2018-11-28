package vn.elca.training.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.utils.ProjectStatusEnum;

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
    public List<Project> findProjectByQuery(String queryStr, String queryStatus) {
        // TODO Auto-generated method stub
        List<Project> listResult = new ArrayList<>();
        if ("".equals(queryStr)) {
            if ("".equals(queryStatus)) {
                listResult = projectDao.getProjectAll();
            } else {
                listResult = projectDao.projectByQuery(ProjectStatusEnum.getProjectStatusByCode(queryStatus));
            }
        } else {
            if ("".equals(queryStatus)) {
                listResult = projectDao.projectByQuery(queryStr);
            } else {
                listResult = projectDao.projectByQuery(queryStr, ProjectStatusEnum.getProjectStatusByCode(queryStatus));
            }
        }
        return listResult;
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
    public List<Integer> delteProjectNumberList(int[] listNumber) {
        // TODO Auto-generated method stub
        List<Integer> error = new ArrayList<>();
        for (int num : listNumber) {
            if (projectDao.deleteByNumberProject(num) == false) {
                error.add(num);
            }
        }
        return error;
    }

    @Override
    public void checkProjectNumberExits(Long number) throws ProjectNumberAlreadyExistsException {
        // TODO Auto-generated method stub
        projectDao.checkNumber(number);
    }
}
