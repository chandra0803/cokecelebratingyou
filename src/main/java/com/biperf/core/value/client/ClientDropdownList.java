
package com.biperf.core.value.client;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "countryCode",
    "countryName",
    "cmAssetCode",
    "budgetMediaValue",
    "awardbanqAbbrev",
    "code",
    "name",
    "desc",
    "abbr",
    "active",
    "sortOrder",
    "pickListAssetCode",
    "abbreviation",
    "pickListItemsAssetCode",
    "description"
})
public class ClientDropdownList {

    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("countryName")
    private String countryName;
    @JsonProperty("cmAssetCode")
    private Object cmAssetCode;
    @JsonProperty("budgetMediaValue")
    private Object budgetMediaValue;
    @JsonProperty("awardbanqAbbrev")
    private String awardbanqAbbrev;
    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("abbr")
    private Object abbr;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("sortOrder")
    private Long sortOrder;
    @JsonProperty("pickListAssetCode")
    private String pickListAssetCode;
    @JsonProperty("abbreviation")
    private Object abbreviation;
    @JsonProperty("pickListItemsAssetCode")
    private String pickListItemsAssetCode;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("countryName")
    public String getCountryName() {
        return countryName;
    }

    @JsonProperty("countryName")
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @JsonProperty("cmAssetCode")
    public Object getCmAssetCode() {
        return cmAssetCode;
    }

    @JsonProperty("cmAssetCode")
    public void setCmAssetCode(Object cmAssetCode) {
        this.cmAssetCode = cmAssetCode;
    }

    @JsonProperty("budgetMediaValue")
    public Object getBudgetMediaValue() {
        return budgetMediaValue;
    }

    @JsonProperty("budgetMediaValue")
    public void setBudgetMediaValue(Object budgetMediaValue) {
        this.budgetMediaValue = budgetMediaValue;
    }

    @JsonProperty("awardbanqAbbrev")
    public String getAwardbanqAbbrev() {
        return awardbanqAbbrev;
    }

    @JsonProperty("awardbanqAbbrev")
    public void setAwardbanqAbbrev(String awardbanqAbbrev) {
        this.awardbanqAbbrev = awardbanqAbbrev;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JsonProperty("abbr")
    public Object getAbbr() {
        return abbr;
    }

    @JsonProperty("abbr")
    public void setAbbr(Object abbr) {
        this.abbr = abbr;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("sortOrder")
    public Long getSortOrder() {
        return sortOrder;
    }

    @JsonProperty("sortOrder")
    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    @JsonProperty("pickListAssetCode")
    public String getPickListAssetCode() {
        return pickListAssetCode;
    }

    @JsonProperty("pickListAssetCode")
    public void setPickListAssetCode(String pickListAssetCode) {
        this.pickListAssetCode = pickListAssetCode;
    }

    @JsonProperty("abbreviation")
    public Object getAbbreviation() {
        return abbreviation;
    }

    @JsonProperty("abbreviation")
    public void setAbbreviation(Object abbreviation) {
        this.abbreviation = abbreviation;
    }

    @JsonProperty("pickListItemsAssetCode")
    public String getPickListItemsAssetCode() {
        return pickListItemsAssetCode;
    }

    @JsonProperty("pickListItemsAssetCode")
    public void setPickListItemsAssetCode(String pickListItemsAssetCode) {
        this.pickListItemsAssetCode = pickListItemsAssetCode;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
