package vn.elca.training;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQuery;

import vn.elca.training.entities.Project;
import vn.elca.training.entities.QProject;
import vn.elca.training.exception.DifferenceStatusProjectDeleteException;
import vn.elca.training.services.IProjectService;
import vn.elca.training.utils.ProjectStatusEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PimElcaApplicationTests {
    @Autowired
    private IProjectService projectService;
    @PersistenceContext
    private EntityManager em;

    @Test
    public void createProjectTest() {
        Project pro = new Project(123, "Project A", "Customer A1", ProjectStatusEnum.NEW, new Date(), new Date());
        String[] visas = new String[0];
        // test
        projectService.createProject(pro, visas);
        Project proTest = new JPAQuery<Project>(em).select(QProject.project).from(QProject.project)
                .where(QProject.project.projectNumber.eq(123)).fetchOne();
        //
        Assert.assertEquals(pro.getName(), proTest.getName());
    }

    @Test
    public void updateProjectTest() {
        Project pro = new Project(124, "Project B", "Customer A2", ProjectStatusEnum.NEW, new Date(), new Date());
        String[] visas = new String[0];
        projectService.createProject(pro, visas);
        Project proTest = new JPAQuery<Project>(em).select(QProject.project).from(QProject.project)
                .where(QProject.project.projectNumber.eq(124)).fetchOne();
        proTest.setName("B2B2B2");
        //
        projectService.updateProject(proTest, visas);
        //
        Project proTest2 = new JPAQuery<Project>(em).select(QProject.project).from(QProject.project)
                .where(QProject.project.projectNumber.eq(124)).fetchOne();
        Assert.assertEquals("B2B2B2", proTest2.getName());
    }

    @Test
    public void concurrentUpdateProjectTest() {
        Project pro = new Project(125, "Project C", "Customer A2", ProjectStatusEnum.NEW, new Date(), new Date());
        String[] visas = new String[0];
        projectService.createProject(pro, visas);
        Project pro1 = new JPAQuery<Project>(em).select(QProject.project).from(QProject.project)
                .where(QProject.project.projectNumber.eq(125)).fetchOne();
        pro1.setName("CCC000");
        Project pro2 = new JPAQuery<Project>(em).select(QProject.project).from(QProject.project)
                .where(QProject.project.projectNumber.eq(125)).fetchOne();
        pro2.setName("OOOCCC");
        projectService.updateProject(pro2, visas);
        try {
            projectService.updateProject(pro1, visas);
        } catch (ObjectOptimisticLockingFailureException e) {
            System.out.println("concurrent upate");
        }
        Project proUpdated = new JPAQuery<Project>(em).select(QProject.project).from(QProject.project)
                .where(QProject.project.projectNumber.eq(125)).fetchOne();
        Assert.assertEquals("OOOCCC", proUpdated.getName());
    }

    @Test
    public void deleteProjectTest() {
        Project pro1 = new Project(126, "Project C", "CuA2", ProjectStatusEnum.NEW, new Date(), new Date());
        Project pro2 = new Project(127, "Project D", "CuA2", ProjectStatusEnum.NEW, new Date(), new Date());
        projectService.createProject(pro1, new String[0]);
        projectService.createProject(pro2, new String[0]);
        try {
            projectService.deleteProjectByIdAndNewStatus(new JPAQuery<Project>(em).select(QProject.project)
                    .from(QProject.project).where(QProject.project.projectNumber.eq(126)).fetchOne().getId());
        } catch (ObjectOptimisticLockingFailureException | NonUniqueResultException | NullPointerException
                | DifferenceStatusProjectDeleteException e) {
            e.printStackTrace();
        }
        Long countDeleted = new JPAQuery<Project>(em).select(QProject.project).from(QProject.project)
                .where(QProject.project.customer.eq("CuA2")).fetchCount();
        Assert.assertEquals(Long.parseLong("1"), countDeleted.longValue());
    }
}
