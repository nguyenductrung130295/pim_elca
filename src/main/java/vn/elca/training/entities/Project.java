package vn.elca.training.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import vn.elca.training.utils.ProjectStatusEnum;
@Entity
@Table(name ="tbl_project")
public class Project extends Version {
	@Column(nullable = false)
    private int projectNumber;
	@Column(nullable = false)
    private String name;
	@Column(nullable = false)
    private String customer;
    @Column(nullable = false)
    private ProjectStatusEnum status;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = true)
    private Date endDate;
   
    private Group group;
    
    private Set<Employee> employees = new HashSet<>();
    
	public Project() {
        super();
    }

    public Project(Long id, int version, int projectNumber, String name, String customer, ProjectStatusEnum status,
            Date startDate, Date endDate) {
        super(id, version);
        this.projectNumber = projectNumber;
        this.name = name;
        this.customer = customer;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    @Id
    @GeneratedValue
    public Long getId() {
        // TODO Auto-generated method stub
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        // TODO Auto-generated method stub
        super.setId(id);
    }

    @Override
    @Column(nullable = false)
    public int getVersion() {
        // TODO Auto-generated method stub
        return super.getVersion();
    }

    @Override
    public void setVersion(int version) {
        // TODO Auto-generated method stub
        super.setVersion(version);
    }

    public int getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public ProjectStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ProjectStatusEnum status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="tbl_project_employee", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name ="employee_id"))
	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}


}
