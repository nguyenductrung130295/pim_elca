package vn.elca.training.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.services.IProjectDaoService;
import vn.elca.training.utils.ProjectStatusEnum;

@Service
public class ProjectDaoImp implements IProjectDaoService {
    private static List<Project> list = new ArrayList<Project>();

    public ProjectDaoImp() {
        System.out.println("-------DB: init project data starting");
        list.add(new Project(Long.valueOf("1"), 1, 1, "Project A", "Customer A", ProjectStatusEnum.NEW, new Date(),
                new Date(), Long.valueOf(1)));
        list.add(new Project(Long.valueOf("2"), 2, 2, "Project B", "Customer C", ProjectStatusEnum.INP, new Date(),
                new Date(), Long.valueOf(1)));
        list.add(new Project(Long.valueOf("3"), 3, 3, "Project C", "Customer B", ProjectStatusEnum.FIN, new Date(),
                new Date(), Long.valueOf(1)));
        list.add(new Project(Long.valueOf("4"), 4, 4, "Project D", "Customer c", ProjectStatusEnum.NEW, new Date(),
                new Date(), Long.valueOf(1)));
        list.add(new Project(Long.valueOf("5"), 5, 5, "Project E", "Customer B", ProjectStatusEnum.PLA, new Date(),
                new Date(), Long.valueOf(1)));
        list.add(new Project(Long.valueOf("6"), 6, 6, "Project F", "Customer A", ProjectStatusEnum.NEW, new Date(),
                new Date(), Long.valueOf(1)));
        System.out.println("-------DB: init project data done");
    }

    @Override
    public List<Project> getProjectAll() {
        // TODO Auto-generated method stub
        return list;
    }

    @Override
    public Project projectById(Long id) {
        // TODO Auto-generated method stub
        for (Project proj : list) {
            if (proj.getId().equals(id)) {
                return proj;
            }
        }
        return null;
    }

    @Override
    public int update(Project project) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int create(Project project) {
        // TODO Auto-generated method stub
        if (null != project) {
            list.add(project);
            return 1;
        }
        return 0;
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<Project> projectByQuery(String query) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean checkId(Long id) throws ProjectNumberAlreadyExistsException {
        // TODO Auto-generated method stub
        for (Project p : list) {
            if (p.getId().equals(id)) {
                throw new ProjectNumberAlreadyExistsException("The projet number already existed.");
            }
        }
        return false;
    }
}
