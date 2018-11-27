package vn.elca.training.entities;

import java.util.Date;

public class Employee extends Version {
    private String visa;
    private String firstName;
    private String lastName;
    private Date birthDate;

    public Employee(Long id, int version, String visa, String firstName, String lastName, Date birthDate) {
        super(id, version);
        this.visa = visa;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    @Override
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
}
