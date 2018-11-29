package vn.elca.training.web;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class StoreSessionValue {
    private String locate;
    private String textQuery;
    private String statusQuery;

    public String getLocate() {
        return locate;
    }

    public StoreSessionValue() {
        this.locate = "en";
        this.textQuery = "";
        this.statusQuery = "";
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getTextQuery() {
        return textQuery;
    }

    public void setTextQuery(String textQuery) {
        this.textQuery = textQuery;
    }

    public String getStatusQuery() {
        return statusQuery;
    }

    public void setStatusQuery(String statusQuery) {
        this.statusQuery = statusQuery;
    }
}
