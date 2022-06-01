
package com.biperf.core.value.client;

import java.util.ArrayList;
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
    "nameId",
    "name",
    "default",
    "isDefault",
    "list"
})
public class ClientCareerMomentsDropdownValueBean {

    @JsonProperty("nameId")
    private String nameId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("default")
    private Boolean _default;
    @JsonProperty("isDefault")
    private Boolean isDefault;
    @JsonProperty("list")
    private java.util.List<ClientDropdownList> list = new ArrayList<ClientDropdownList>();
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

    @JsonProperty("list")
    public java.util.List<ClientDropdownList> getList() {
        return list;
    }

    @JsonProperty("list")
    public void setList(java.util.List<ClientDropdownList> list) {
        this.list = list;
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
