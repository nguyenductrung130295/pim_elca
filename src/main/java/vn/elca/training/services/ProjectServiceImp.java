package vn.elca.training.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQuery;

import vn.elca.training.dao.IEmployeeRepository;
import vn.elca.training.dao.IGroupRepository;
import vn.elca.training.dao.IProjectRepository;
import vn.elca.training.entities.Employee;
import vn.elca.training.entities.Group;
import vn.elca.training.entities.Project;
import vn.elca.training.entities.QProject;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.utils.AppUtils;
import vn.elca.training.utils.ProjectStatusEnum;

@Service
public class ProjectServiceImp implements IProjectService {
    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IGroupRepository groupRepository;
    @Autowired
    private IEmployeeRepository employeeRepository;
    @PersistenceContext
    private EntityManager em;

    /**
     * Find project by id
     */
    @Override
    public Project findProjectById(Long id) {
        QProject qpro = QProject.project;
        return new JPAQuery<Project>(em).select(qpro).from(qpro).where(qpro.id.eq(id)).fetchOne();
    }

    /**
     * Get all project list
     */
    @Override
    public List<Project> findProjectAll(int pageNum, int rowOnPage) {
        return projectRepository.findAllPaging(PageRequest.of(pageNum, rowOnPage));
    }

    /**
     * Search project to list by condition query search {@inheritDoc}
     */
    @Override
    public List<Project> findProjectByQuery(String queryStr, String queryStatus, int pageNum, int rowOnPage) {
        queryStr = queryStr.toUpperCase();
        List<Project> listResult = new ArrayList<>();
        if ("".equals(queryStr)) {
            if ("".equals(queryStatus)) {
                listResult = projectRepository.findAllPaging(PageRequest.of(pageNum, rowOnPage));
            } else {
                listResult = projectRepository.findByStatus(ProjectStatusEnum.getProjectStatusByCode(queryStatus),
                        PageRequest.of(pageNum, rowOnPage));
            }
        } else {
            try {
                int number_project = Integer.parseInt(queryStr);
                if ("".equals(queryStatus)) {
                    listResult = projectRepository.findByQuery(queryStr, number_project,
                            PageRequest.of(pageNum, rowOnPage));
                } else {
                    listResult = projectRepository.findByQuery(queryStr,
                            ProjectStatusEnum.getProjectStatusByCode(queryStatus), number_project,
                            PageRequest.of(pageNum, rowOnPage));
                }
            } catch (NumberFormatException e) {
                if ("".equals(queryStatus)) {
                    listResult = projectRepository.findByQuery(queryStr, PageRequest.of(pageNum, rowOnPage));
                } else {
                    listResult = projectRepository.findByQuery(queryStr,
                            ProjectStatusEnum.getProjectStatusByCode(queryStatus), PageRequest.of(pageNum, rowOnPage));
                }
            }
        }
        return listResult;
    }

    /**
     * Set member for project and save project to db with in transaction
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createProject(Project project, String[] visas) {
        project.setEmployees(employeeRepository.findByVisaList(visas));
        if (projectRepository.saveAndFlush(project) == null) {
            return false;
        }
        return true;
    }

    /**
     * Update project to database with in transaction. Handle concurrent update with version. {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProject(Project projectUpdate, String[] splitVisaMember) {
        projectUpdate.setEmployees(employeeRepository.findByVisaList(splitVisaMember));
        Project projectCurrent = projectRepository.findById(projectUpdate.getId()).get();
        if (projectCurrent.getVersion() == projectUpdate.getVersion()) {
            projectUpdate.setVersion(projectCurrent.getVersion() + 1);
        } else {
            return false;
        }
        return projectRepository.saveAndFlush(projectUpdate) == null ? false : true;
    }

    /**
     * Delete single project by id and status is NEW {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteProjectByIdAndNewStatus(Long id) throws Exception {
        projectRepository.deleteByIdAndStatus(id, ProjectStatusEnum.NEW);
    }

    /**
     * Delete all of selected project with in status is NEW {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void deleteProjectByListIdAndNewStatus(Long[] listProjectIds) throws Exception {
        for (Long id : listProjectIds) {
            projectRepository.deleteByIdAndStatus(id, ProjectStatusEnum.NEW);
        }
    }

    /**
     * Get number be counted by project number of project and throws exception if count greater than 0
     */
    @Override
    public void checkProjectNumberExits(int number) throws ProjectNumberAlreadyExistsException {
        if (projectRepository.countByProjectNumber(number) > 0) {
            throw new ProjectNumberAlreadyExistsException(AppUtils.MSG_NUMBER_ALREADY);
        }
    }

    /**
     * Init data first when start project localhost:8080/project/ {@inheritDoc}
     */
    @Override
    public void initData() {
        System.out.println("-------DB: init project data starting");
        Employee em1 = new Employee(0, "CCC", "Chi", "Pu", new Date());
        Employee em2 = new Employee(0, "DDD", "Hoai", "Linh", new Date());
        Employee em3 = new Employee(0, "FFF", "Truong", "Giang", new Date());
        Employee em4 = new Employee(0, "AAA", "Tran", "Thanh", new Date());
        Employee em5 = new Employee(0, "BBB", "Son", "Tung", new Date());
        employeeRepository.saveAndFlush(em1);
        employeeRepository.saveAndFlush(em2);
        employeeRepository.saveAndFlush(em3);
        employeeRepository.saveAndFlush(em4);
        employeeRepository.saveAndFlush(em5);
        Group groupCim = new Group(0, "CIM");
        Group groupJava = new Group(0, "JAVA");
        Group groupNet = new Group(0, ".NET");
        Group groupSecu = new Group(0, "Secutix");
        groupCim.setLeader(em5);
        groupJava.setLeader(em2);
        groupNet.setLeader(em3);
        groupSecu.setLeader(em1);
        groupRepository.saveAndFlush(groupJava);
        groupRepository.saveAndFlush(groupNet);
        groupRepository.saveAndFlush(groupCim);
        groupRepository.saveAndFlush(groupSecu);
        Project proj1 = new Project(0, 1, "Project A", "Customer A", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj2 = new Project(0, 2, "Project B", "Customer C", ProjectStatusEnum.INP, new Date(), new Date());
        Project proj3 = new Project(0, 3, "Project C", "Customer B", ProjectStatusEnum.FIN, new Date(), new Date());
        Project proj4 = new Project(0, 4, "Project D", "Customer c", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj5 = new Project(0, 5, "Project E", "Customer B", ProjectStatusEnum.PLA, new Date(), new Date());
        Project proj6 = new Project(0, 6, "Project F", "Customer A", ProjectStatusEnum.NEW, new Date(), new Date());
        proj1.setGroup(groupJava);
        proj2.setGroup(groupNet);
        proj3.setGroup(groupNet);
        proj4.setGroup(groupJava);
        proj5.setGroup(groupSecu);
        proj6.setGroup(groupCim);
        Set<Employee> members = new HashSet<>();
        members.add(em1);
        members.add(em5);
        members.add(em3);
        proj1.setEmployees(members);
        projectRepository.saveAndFlush(proj1);
        members = new HashSet<>();
        members.add(em5);
        members.add(em2);
        proj2.setEmployees(members);
        projectRepository.saveAndFlush(proj4);
        projectRepository.saveAndFlush(proj2);
        projectRepository.saveAndFlush(proj6);
        projectRepository.saveAndFlush(proj3);
        projectRepository.saveAndFlush(proj5);
        System.out.println("-------DB: init project data done");
    }

    /**
     * get list member visa of project to edit page
     */
    @Override
    public String getListMemberVisaOfProject(Project project) {
        return AppUtils.getListMemberVisaOfProject(project.getEmployees());
    }
}
