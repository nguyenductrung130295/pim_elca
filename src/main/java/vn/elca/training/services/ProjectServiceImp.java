package vn.elca.training.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.dao.IEmployeeRepository;
import vn.elca.training.dao.IGroupRepository;
import vn.elca.training.dao.IProjectRepository;
import vn.elca.training.entities.Employee;
import vn.elca.training.entities.Group;
import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.utils.ProjectStatusEnum;

@Service
public class ProjectServiceImp implements IProjectService {
    @Autowired
    IProjectDaoService projectDao;
    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IGroupRepository groupRepository;
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Override
    public Project findProjectById(Long id) {
        return JPAQuery;
    }

    @Override
    public List<Project> findProjectAll() {
        // TODO Auto-generated method stub
        return projectRepository.findAll();
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
    public boolean createProject(Project project) {
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
    public boolean deleteProjectById(Long id) {
        // TODO Auto-generated method stub
        return projectDao.delete(id);
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
    public void checkProjectNumberExits(int number) throws ProjectNumberAlreadyExistsException {
        // TODO Auto-generated method stub
        projectDao.checkNumber(number);
    }

    @Override
    public void initData() {
        System.out.println("-------DB: init project data starting");
        Employee em1 = new Employee(1, "CCC", "Chi", "Pu", new Date());
        Employee em2 = new Employee(1, "DDD", "Hoai", "Linh", new Date());
        Employee em3 = new Employee(1, "FFF", "Truong", "Giang", new Date());
        Employee em4 = new Employee(1, "AAA", "Tran", "Thanh", new Date());
        Employee em5 = new Employee(1, "BBB", "Son", "Tung", new Date());
        employeeRepository.saveAndFlush(em1);
        employeeRepository.saveAndFlush(em2);
        employeeRepository.saveAndFlush(em3);
        employeeRepository.saveAndFlush(em4);
        employeeRepository.saveAndFlush(em5);
        Group groupCim = new Group(1, "CIM");
        Group groupJava = new Group(1, "JAVA");
        Group groupNet = new Group(1, ".NET");
        Group groupSecu = new Group(1, "Secutix");
        groupCim.setLeader(em5);
        groupJava.setLeader(em2);
        groupNet.setLeader(em3);
        groupSecu.setLeader(em1);
        groupRepository.saveAndFlush(groupJava);
        groupRepository.saveAndFlush(groupNet);
        groupRepository.saveAndFlush(groupCim);
        groupRepository.saveAndFlush(groupSecu);
        Project proj1 = new Project(1, 1, "Project A", "Customer A", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj2 = new Project(1, 2, "Project B", "Customer C", ProjectStatusEnum.INP, new Date(), new Date());
        Project proj3 = new Project(1, 3, "Project C", "Customer B", ProjectStatusEnum.FIN, new Date(), new Date());
        Project proj4 = new Project(1, 4, "Project D", "Customer c", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj5 = new Project(1, 5, "Project E", "Customer B", ProjectStatusEnum.PLA, new Date(), new Date());
        Project proj6 = new Project(1, 6, "Project F", "Customer A", ProjectStatusEnum.NEW, new Date(), new Date());
        proj1.setGroup(groupJava);
        proj2.setGroup(groupNet);
        proj3.setGroup(groupNet);
        proj4.setGroup(groupJava);
        proj5.setGroup(groupSecu);
        proj6.setGroup(groupCim);
        projectRepository.saveAndFlush(proj1);
        projectRepository.saveAndFlush(proj2);
        projectRepository.saveAndFlush(proj3);
        projectRepository.saveAndFlush(proj4);
        projectRepository.saveAndFlush(proj5);
        projectRepository.saveAndFlush(proj6);
        System.out.println("-------DB: init project data done");
    }
}
