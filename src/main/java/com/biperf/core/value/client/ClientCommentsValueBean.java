
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messages",
    "activityPods",
    "userLang"
})
public class ClientCommentsValueBean {

    @JsonProperty("messages")
    private List<Object> messages = new ArrayList<Object>();
    @JsonProperty("activityPods")
    private List<ActivityPod> activityPods = new ArrayList<ActivityPod>();
    @JsonProperty("userLang")
    private String userLang;

    @JsonProperty("messages")
    public List<Object> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    public ClientCommentsValueBean withMessages(List<Object> messages) {
        this.messages = messages;
        return this;
    }

    @JsonProperty("activityPods")
    public List<ActivityPod> getActivityPods() {
        return activityPods;
    }

    @JsonProperty("activityPods")
    public void setActivityPods(List<ActivityPod> activityPods) {
        this.activityPods = activityPods;
    }

    public ClientCommentsValueBean withActivityPods(List<ActivityPod> activityPods) {
        this.activityPods = activityPods;
        return this;
    }

    @JsonProperty("userLang")
    public String getUserLang() {
        return userLang;
    }

    @JsonProperty("userLang")
    public void setUserLang(String userLang) {
        this.userLang = userLang;
    }

    public ClientCommentsValueBean withUserLang(String userLang) {
        this.userLang = userLang;
        return this;
    }

}
