package vn.elca.training.utils;

public enum ProjectStatusEnum {
    NEW("NEW"), PLA("PLA"), INP("INP"), FIN("FIN");
    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    private ProjectStatusEnum(String statusCode) {
        this.statusCode = statusCode;
    }

    public static ProjectStatusEnum getProjectStatusByCode(String codeStatus) {
        for (ProjectStatusEnum proStatus : ProjectStatusEnum.values()) {
            if (proStatus.statusCode.equals(codeStatus)) {
                return proStatus;
            }
        }
        return null;
    }
}
