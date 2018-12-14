package vn.elca.training.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import vn.elca.training.utils.ProjectStatusEnum;

@Entity
@Table(name = "tbl_project")
public class Project extends Version {
    private int projectNumber;
    private String name;
    private String customer;
    private ProjectStatusEnum status;
    private Date startDate;
    private Date endDate;
    private Group group;
    private Set<Employee> employees = new HashSet<>();

    public Project() {
        super();
    }

    public Project(int projectNumber, String name, String customer, ProjectStatusEnum status, Date startDate,
            Date endDate) {
        super();
        this.projectNumber = projectNumber;
        this.name = name;
        this.customer = customer;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_project")
    @SequenceGenerator(name = "seq_project", sequenceName = "SEQUENCE_PROJECT", initialValue = 1)
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    @javax.persistence.Version
    @Column(nullable = false)
    public int getVersion() {
        return super.getVersion();
    }

    @Override
    public void setVersion(int version) {
        super.setVersion(version);
    }

    @Column(nullable = false, updatable = false, unique = true)
    public int getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    public ProjectStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ProjectStatusEnum status) {
        this.status = status;
    }

    @Column(nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(nullable = true)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_project_employee", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
