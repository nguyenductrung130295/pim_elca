package vn.elca.training.services;

import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
import vn.elca.training.exception.DifferenceStatusProjectDeleteException;
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
    public Page<Project> findProjectAll(int pageNum, int rowOnPage, String sortBy, String sortType) {
        Sort sort = AppUtils.SORT_TYPE_ASC.equals(sortType) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return projectRepository.findAllPaging(PageRequest.of(pageNum, rowOnPage, sort));
    }

    /**
     * Search project to list by condition query search {@inheritDoc}
     */
    @Override
    public Page<Project> findProjectByQuery(String queryStr, String queryStatus, int pageNum, int rowOnPage,
            String sortBy, String sortType) {
        queryStr = queryStr.toUpperCase();
        Sort sort = AppUtils.SORT_TYPE_ASC.equals(sortType) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Project> listResult;
        if ("".equals(queryStr)) {
            if ("".equals(queryStatus)) {
                listResult = projectRepository.findAllPaging(PageRequest.of(pageNum, rowOnPage, sort));
            } else {
                listResult = projectRepository.findByStatus(ProjectStatusEnum.getProjectStatusByCode(queryStatus),
                        PageRequest.of(pageNum, rowOnPage, sort));
            }
        } else {
            try {
                int number_project = Integer.parseInt(queryStr);
                if ("".equals(queryStatus)) {
                    listResult = projectRepository.findByQuery(queryStr, number_project,
                            PageRequest.of(pageNum, rowOnPage, sort));
                } else {
                    listResult = projectRepository.findByQuery(queryStr,
                            ProjectStatusEnum.getProjectStatusByCode(queryStatus), number_project,
                            PageRequest.of(pageNum, rowOnPage, sort));
                }
            } catch (NumberFormatException e) {
                if ("".equals(queryStatus)) {
                    listResult = projectRepository.findByQuery(queryStr, PageRequest.of(pageNum, rowOnPage, sort));
                } else {
                    listResult = projectRepository.findByQuery(queryStr,
                            ProjectStatusEnum.getProjectStatusByCode(queryStatus),
                            PageRequest.of(pageNum, rowOnPage, sort));
                }
            }
        }
        return listResult;
    }

    /**
     * Set member for project and save project to db with in transaction
     */
    @Override
    @Transactional
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
    @Transactional
    public boolean updateProject(Project projectUpdate, String[] splitVisaMember)
            throws ObjectOptimisticLockingFailureException {
        if (splitVisaMember.length > 0) {
            projectUpdate.setEmployees(employeeRepository.findByVisaList(splitVisaMember));
        }
        return projectRepository.saveAndFlush(projectUpdate) == null ? false : true;
    }

    /**
     * Delete single project by id and status is NEW {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteProjectByIdAndNewStatus(Long id) throws NoSuchElementException,
            ObjectOptimisticLockingFailureException, DifferenceStatusProjectDeleteException {
        Project proj = projectRepository.findById(id).get();
        if (proj != null) {
            if (ProjectStatusEnum.NEW.equals(proj.getStatus())) {
                projectRepository.deleteByIdAndStatus(id, ProjectStatusEnum.NEW);
            } else {
                throw new DifferenceStatusProjectDeleteException();
            }
        }
    }

    /**
     * Delete all of selected project with in status is NEW {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void deleteProjectByListIdAndNewStatus(Long[] listProjectIds) throws NoSuchElementException,
            ObjectOptimisticLockingFailureException, DifferenceStatusProjectDeleteException {
        for (Long id : listProjectIds) {
            deleteProjectByIdAndNewStatus(id);
            // projectRepository.deleteByIdAndStatus(id, ProjectStatusEnum.NEW);
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
        Employee em1 = new Employee("CCC", "Chi", "Pu", new Date());
        Employee em2 = new Employee("DDD", "Hoai", "Linh", new Date());
        Employee em3 = new Employee("FFF", "Truong", "Giang", new Date());
        Employee em4 = new Employee("AAA", "Tran", "Thanh", new Date());
        Employee em5 = new Employee("BBB", "Son", "Tung", new Date());
        Employee em6 = new Employee("EEE", "Bich", "Phuong", new Date());
        Employee em7 = new Employee("GGG", "Huong", "Giang", new Date());
        employeeRepository.saveAndFlush(em1);
        employeeRepository.saveAndFlush(em2);
        employeeRepository.saveAndFlush(em3);
        employeeRepository.saveAndFlush(em4);
        employeeRepository.saveAndFlush(em5);
        employeeRepository.saveAndFlush(em6);
        employeeRepository.saveAndFlush(em7);
        Group groupCim = new Group("CIM");
        Group groupJava = new Group("JAVA");
        Group groupNet = new Group(".NET");
        Group groupSecu = new Group("Secutix");
        groupCim.setLeader(em5);
        groupJava.setLeader(em2);
        groupNet.setLeader(em3);
        groupSecu.setLeader(em1);
        groupRepository.saveAndFlush(groupJava);
        groupRepository.saveAndFlush(groupNet);
        groupRepository.saveAndFlush(groupCim);
        groupRepository.saveAndFlush(groupSecu);
        Project proj1 = new Project(1, "Project Q", "Google", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj2 = new Project(2, "Project W", "Microsoft", ProjectStatusEnum.INP, new Date(), new Date());
        Project proj3 = new Project(3, "KSTA E", "Amazon", ProjectStatusEnum.FIN, new Date(), new Date());
        Project proj4 = new Project(4, "Project R", "Facebook", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj5 = new Project(5, "KSTA T", "Salesforce", ProjectStatusEnum.PLA, new Date(), new Date());
        Project proj6 = new Project(6, "Project Y", "Microsoft", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj7 = new Project(7, "Project U", "Google", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj8 = new Project(8, "KSTA I", "Microsoft", ProjectStatusEnum.INP, new Date(), new Date());
        Project proj9 = new Project(9, "Project O", "Microsoft", ProjectStatusEnum.FIN, new Date(), new Date());
        Project proj10 = new Project(10, "ProjecT P", "Google", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj11 = new Project(11, "Project A", "Microsoft", ProjectStatusEnum.PLA, new Date(), new Date());
        Project proj12 = new Project(12, "EFV S", "Amazon", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj13 = new Project(13, "Project D", "Google", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj14 = new Project(14, "Project F", "Amazon", ProjectStatusEnum.INP, new Date(), new Date());
        Project proj15 = new Project(15, "KSTA G", "Facebook", ProjectStatusEnum.FIN, new Date(), new Date());
        Project proj16 = new Project(16, "Project H", "Google", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj17 = new Project(17, "Project J", "Salesforce", ProjectStatusEnum.PLA, new Date(), new Date());
        Project proj18 = new Project(18, "EFV K", "Facebook", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj19 = new Project(19, "Project L", "Salesforce", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj20 = new Project(20, "EFV Z", "Facebook", ProjectStatusEnum.INP, new Date(), new Date());
        Project proj21 = new Project(21, "Project X", "Amazon", ProjectStatusEnum.FIN, new Date(), new Date());
        Project proj22 = new Project(22, "Project C", "Salesforce", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj23 = new Project(23, "Project V", "Amazon", ProjectStatusEnum.PLA, new Date(), new Date());
        Project proj24 = new Project(24, "Project B", "Amazon", ProjectStatusEnum.NEW, new Date(), new Date());
        Project proj25 = new Project(25, "Project N", "Salesforce", ProjectStatusEnum.PLA, new Date(), new Date());
        Project proj26 = new Project(26, "EFV M", "Customer M", ProjectStatusEnum.NEW, new Date(), new Date());
        proj1.setGroup(groupJava);
        proj2.setGroup(groupNet);
        proj3.setGroup(groupNet);
        proj4.setGroup(groupJava);
        proj5.setGroup(groupSecu);
        proj6.setGroup(groupCim);
        proj7.setGroup(groupJava);
        proj8.setGroup(groupNet);
        proj9.setGroup(groupNet);
        proj10.setGroup(groupJava);
        proj11.setGroup(groupSecu);
        proj12.setGroup(groupCim);
        proj13.setGroup(groupJava);
        proj14.setGroup(groupNet);
        proj15.setGroup(groupNet);
        proj16.setGroup(groupJava);
        proj17.setGroup(groupSecu);
        proj18.setGroup(groupCim);
        proj19.setGroup(groupJava);
        proj20.setGroup(groupNet);
        proj21.setGroup(groupNet);
        proj22.setGroup(groupJava);
        proj23.setGroup(groupSecu);
        proj24.setGroup(groupCim);
        proj25.setGroup(groupSecu);
        proj26.setGroup(groupCim);
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
        members = new HashSet<>();
        members.add(em7);
        members.add(em2);
        proj15.setEmployees(members);
        projectRepository.saveAndFlush(proj15);
        members = new HashSet<>();
        members.add(em6);
        members.add(em7);
        proj26.setEmployees(members);
        projectRepository.saveAndFlush(proj26);
        projectRepository.saveAndFlush(proj20);
        members = new HashSet<>();
        members.add(em1);
        members.add(em2);
        proj21.setEmployees(members);
        projectRepository.saveAndFlush(proj21);
        projectRepository.saveAndFlush(proj16);
        members = new HashSet<>();
        members.add(em5);
        members.add(em4);
        proj5.setEmployees(members);
        projectRepository.saveAndFlush(proj5);
        projectRepository.saveAndFlush(proj7);
        members = new HashSet<>();
        members.add(em7);
        members.add(em6);
        proj10.setEmployees(members);
        projectRepository.saveAndFlush(proj10);
        members = new HashSet<>();
        members.add(em3);
        members.add(em1);
        proj14.setEmployees(members);
        projectRepository.saveAndFlush(proj14);
        projectRepository.saveAndFlush(proj17);
        members = new HashSet<>();
        members.add(em1);
        members.add(em7);
        proj18.setEmployees(members);
        projectRepository.saveAndFlush(proj18);
        members = new HashSet<>();
        members.add(em2);
        members.add(em6);
        proj19.setEmployees(members);
        projectRepository.saveAndFlush(proj19);
        projectRepository.saveAndFlush(proj11);
        members = new HashSet<>();
        members.add(em5);
        members.add(em4);
        proj12.setEmployees(members);
        projectRepository.saveAndFlush(proj12);
        members = new HashSet<>();
        members.add(em5);
        members.add(em7);
        proj8.setEmployees(members);
        projectRepository.saveAndFlush(proj8);
        projectRepository.saveAndFlush(proj22);
        members = new HashSet<>();
        members.add(em3);
        members.add(em7);
        members.add(em4);
        members.add(em1);
        proj9.setEmployees(members);
        projectRepository.saveAndFlush(proj9);
        projectRepository.saveAndFlush(proj24);
        members = new HashSet<>();
        members.add(em3);
        members.add(em6);
        members.add(em7);
        proj25.setEmployees(members);
        projectRepository.saveAndFlush(proj25);
        projectRepository.saveAndFlush(proj3);
        projectRepository.saveAndFlush(proj13);
        members = new HashSet<>();
        members.add(em4);
        members.add(em6);
        members.add(em1);
        members.add(em2);
        proj23.setEmployees(members);
        projectRepository.saveAndFlush(proj23);
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
