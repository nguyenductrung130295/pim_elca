package vn.elca.training.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        return list;
    }

    @Override
    public Project projectById(Long id) {
        for (Project proj : list) {
            if (proj.getId().equals(id)) {
                return proj;
            }
        }
        return null;
    }

    @Override
    public int update(Project project) {
        return 0;
    }

    @Override
    public boolean create(Project project) {
        if (null != project) {
            list.add(project);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        for (Project pro : list) {
            if (pro.getId().equals(id)) {
                list.remove(pro);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Project> projectByQuery(String query, ProjectStatusEnum status) {
        int numberProject;
        try {
            numberProject = Integer.parseInt(query);
            return list.stream().filter(t -> t.getStatus().equals(status))
                    .filter(t -> (numberProject == t.getProjectNumber()
                            || ("%" + query + "%").toUpperCase().contains(t.getName().toUpperCase()))
                            || (("%" + query + "%").toUpperCase().contains(t.getCustomer().toUpperCase())))
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("cannot format string query search to number project integer");
            e.printStackTrace();
            return list.stream().filter(t -> t.getStatus().equals(status))
                    .filter(t -> (("%" + query + "%").toUpperCase().contains(t.getName().toUpperCase()))
                            || (("%" + query + "%").toUpperCase().contains(t.getCustomer().toUpperCase())))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Project> projectByQuery(String query) {
        int numberProject = -1;
        try {
            numberProject = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            // e.printStackTrace();
            numberProject = -1;
        }
        final int checkNumberProject = numberProject;
        return list.stream()
                .filter(t -> (checkNumberProject == t.getProjectNumber()
                        || ("%" + query + "%").toUpperCase().contains(t.getName().toUpperCase()))
                        || (("%" + query + "%").toUpperCase().contains(t.getCustomer().toUpperCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> projectByQuery(ProjectStatusEnum status) {
        return list.stream().filter(t -> t.getStatus().equals(status)).collect(Collectors.toList());
    }

    @Override
    public void checkNumber(int number) throws ProjectNumberAlreadyExistsException {
        for (Project p : list) {
            if (p.getProjectNumber() == (number)) {
                System.out.println(p.getProjectNumber() + "hhhhhhhhhhh:" + number);
                throw new ProjectNumberAlreadyExistsException("The projet number already existed.");
            }
        }
    }

    @Override
    public boolean deleteByNumberProject(int num) {
        for (Project pro : list) {
            if (pro.getProjectNumber() == num) {
                list.remove(pro);
                return true;
            }
        }
        return false;/// ?????????????
    }
}
