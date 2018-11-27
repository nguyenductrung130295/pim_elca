package vn.elca.training.entities;

public class Group extends Version {
    private Long groupLeaderId;
    private String name;

    public Group(Long id, int version, Long groupLeaderId, String name) {
        super(id, version);
        this.groupLeaderId = groupLeaderId;
        this.name = name;
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

    public Long getGroupLeaderId() {
        return groupLeaderId;
    }

    public void setGroupLeaderId(Long groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
