package vn.elca.training.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_group")
public class Group extends Version {
    @Column(nullable = true)
    private String name;
    
    private Employee employee;
    
    private Set<Project> projects = new HashSet<>();
    
	@Override
    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    @Column
    public int getVersion() {
        return super.getVersion();
    }

    @Override
    public void setVersion(int version) {
        super.setVersion(version);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @OneToMany(mappedBy="group", fetch = FetchType.EAGER)
    public Set<Project> getProjects() {
		return projects;
	}
    
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "group_leader_id")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Group(Long id, int version, String name) {
        super(id, version);
        this.name = name;
    }

    public Group() {
		super();
	}

	
    
}
