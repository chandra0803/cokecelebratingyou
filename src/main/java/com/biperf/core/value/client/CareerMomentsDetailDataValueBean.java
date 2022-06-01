
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messages",
    "careerMomentsDetailSets"
})
public class CareerMomentsDetailDataValueBean {

    @JsonProperty("messages")
    private List<Object> messages = new ArrayList<Object>();
    @JsonProperty("careerMomentsDetailSets")
    private List<CareerMomentsDetailSet> careerMomentsDetailSets = new ArrayList<CareerMomentsDetailSet>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("messages")
    public List<Object> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    @JsonProperty("careerMomentsDetailSets")
    public List<CareerMomentsDetailSet> getCareerMomentsDetailSets() {
        return careerMomentsDetailSets;
    }

    @JsonProperty("careerMomentsDetailSets")
    public void setCareerMomentsDetailSets(List<CareerMomentsDetailSet> careerMomentsDetailSets) {
        this.careerMomentsDetailSets = careerMomentsDetailSets;
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
