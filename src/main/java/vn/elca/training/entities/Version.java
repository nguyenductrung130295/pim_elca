package vn.elca.training.entities;

public abstract class Version {
    protected Long id;
    protected int version;

    public Version() {
    }

    public Version(int version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Version(Long id, int version) {
        this.id = id;
        this.version = version;
    }
}
