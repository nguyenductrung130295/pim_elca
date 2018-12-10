package vn.elca.training.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_employee")
public class Employee extends Version {
    @Column(nullable = false, unique = true)
    private String visa;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private Date birthDate;
    private Group group;
    private Set<Project> projects = new HashSet<>();

    public Employee(Long id, int version, String visa, String firstName, String lastName, Date birthDate) {
        super(id, version);
        this.visa = visa;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Employee(int version, String visa, String firstName, String lastName, Date birthDate) {
        super(version);
        this.visa = visa;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
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

    public String getVisa() {
        return visa;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @OneToOne(mappedBy = "leader", fetch = FetchType.LAZY)
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
