
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.client.JobChanges;
import com.biperf.core.domain.client.NewHires;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nameId",
    "name",
    "total",
    "itemsPerPage",
    "currentPage",
    "sortedOn",
    "sortedBy",
    "description",
    "list",
    "default",
    "isDefault",
    "tableColumns",
    "jobChanges",
    "newHires"
})
public class CareerMomentsDetailSet {

    @JsonProperty("nameId")
    private String nameId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("total")
    private Long total;
    @JsonProperty("itemsPerPage")
    private Long itemsPerPage;
    @JsonProperty("currentPage")
    private Long currentPage;
    @JsonProperty("sortedOn")
    private String sortedOn;
    @JsonProperty("sortedBy")
    private String sortedBy;
    @JsonProperty("description")
    private String description;
    @JsonProperty("list")
    private List<Object> list = new ArrayList<Object>();
    @JsonProperty("default")
    private Boolean _default;
    @JsonProperty("isDefault")
    private Boolean isDefault;
    @JsonProperty("tableColumns")
    private List<TableColumn> tableColumns = new ArrayList<TableColumn>();
    @JsonProperty("jobChanges")
    private JobChanges jobChanges;
    @JsonProperty("newHires")
    private NewHires newHires;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("nameId")
    public String getNameId() {
        return nameId;
    }

    @JsonProperty("nameId")
    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("total")
    public Long getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Long total) {
        this.total = total;
    }

    @JsonProperty("itemsPerPage")
    public Long getItemsPerPage() {
        return itemsPerPage;
    }

    @JsonProperty("itemsPerPage")
    public void setItemsPerPage(Long itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    @JsonProperty("currentPage")
    public Long getCurrentPage() {
        return currentPage;
    }

    @JsonProperty("currentPage")
    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    @JsonProperty("sortedOn")
    public String getSortedOn() {
        return sortedOn;
    }

    @JsonProperty("sortedOn")
    public void setSortedOn(String sortedOn) {
        this.sortedOn = sortedOn;
    }

    @JsonProperty("sortedBy")
    public String getSortedBy() {
        return sortedBy;
    }

    @JsonProperty("sortedBy")
    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("list")
    public List<Object> getList() {
        return list;
    }

    @JsonProperty("list")
    public void setList(List<Object> list) {
        this.list = list;
    }

    @JsonProperty("default")
    public Boolean getDefault() {
        return _default;
    }

    @JsonProperty("default")
    public void setDefault(Boolean _default) {
        this._default = _default;
    }

    @JsonProperty("isDefault")
    public Boolean getIsDefault() {
        return isDefault;
    }

    @JsonProperty("isDefault")
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @JsonProperty("tableColumns")
    public List<TableColumn> getTableColumns() {
        return tableColumns;
    }

    @JsonProperty("tableColumns")
    public void setTableColumns(List<TableColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }

    @JsonProperty("jobChanges")
    public JobChanges getJobChanges() {
        return jobChanges;
    }

    @JsonProperty("jobChanges")
    public void setJobChanges(JobChanges jobChanges) {
        this.jobChanges = jobChanges;
    }

    @JsonProperty("newHires")
    public NewHires getNewHires() {
        return newHires;
    }

    @JsonProperty("newHires")
    public void setNewHires(NewHires newHires) {
        this.newHires = newHires;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
