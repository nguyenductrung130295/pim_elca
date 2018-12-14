package vn.elca.training.web;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import vn.elca.training.utils.AppUtils;

@Component
@Scope("session")
public class StoreSessionValue {
    private String locate;
    private String textQuery;
    private String statusQuery;
    private String orderBy;
    private String sortType;

    public String getLocate() {
        return locate;
    }

    public StoreSessionValue() {
        this.locate = "en";
        this.textQuery = "";
        this.statusQuery = "";
        this.orderBy = AppUtils.SORT_BY_DEFAULT;
        this.sortType = AppUtils.SORT_TYPE_ASC;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
