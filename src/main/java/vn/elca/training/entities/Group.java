package vn.elca.training.entities;

import java.util.HashSet;
import java.util.Set;

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
@Table(name = "tbl_group")
public class Group extends Version {
    private String name;
    private Employee leader;
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

    @Column(nullable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Group(Long id, int version, String name) {
        super(id, version);
        this.name = name;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_leader_id")
    public Employee getLeader() {
        return leader;
    }

    public void setLeader(Employee leader) {
        this.leader = leader;
    }

    public Group(int version, String name) {
        super(version);
        this.name = name;
    }

    public Group() {
        super();
    }
}
