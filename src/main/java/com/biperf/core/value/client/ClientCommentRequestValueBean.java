
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messages",
    "profileUserId",
    "commentId",
    "userInfo",
    "commentText",
    "numLikers",
    "videoWebLink",
    "media"
})
public class ClientCommentRequestValueBean {

    @JsonProperty("messages")
    private List<Object> messages = new ArrayList<Object>();
    @JsonProperty("profileUserId")
    private Long profileUserId;
    @JsonProperty("commentId")
    private Long commentId;
    @JsonProperty("userInfo")
    private List<UserInfo> userInfo = new ArrayList<UserInfo>();
    @JsonProperty("commentText")
    private String commentText;
    @JsonProperty("numLikers")
    private Long numLikers;
    @JsonProperty("videoWebLink")
    private String videoWebLink;
    @JsonProperty("media")
    private List<Medium> media = new ArrayList<Medium>();

    @JsonProperty("messages")
    public List<Object> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    @JsonProperty("profileUserId")
    public Long getProfileUserId() {
        return profileUserId;
    }

    @JsonProperty("profileUserId")
    public void setProfileUserId(Long profileUserId) {
        this.profileUserId = profileUserId;
    }

    @JsonProperty("commentId")
    public Long getCommentId() {
        return commentId;
    }

    @JsonProperty("commentId")
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @JsonProperty("userInfo")
    public List<UserInfo> getUserInfo() {
        return userInfo;
    }

    @JsonProperty("userInfo")
    public void setUserInfo(List<UserInfo> userInfo) {
        this.userInfo = userInfo;
    }

    @JsonProperty("commentText")
    public String getCommentText() {
        return commentText;
    }

    @JsonProperty("commentText")
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @JsonProperty("numLikers")
    public Long getNumLikers() {
        return numLikers;
    }

    @JsonProperty("numLikers")
    public void setNumLikers(Long numLikers) {
        this.numLikers = numLikers;
    }

    @JsonProperty("videoWebLink")
    public String getVideoWebLink() {
        return videoWebLink;
    }

    @JsonProperty("videoWebLink")
    public void setVideoWebLink(String videoWebLink) {
        this.videoWebLink = videoWebLink;
    }

    @JsonProperty("media")
    public List<Medium> getMedia() {
        return media;
    }

    @JsonProperty("media")
    public void setMedia(List<Medium> media) {
        this.media = media;
    }

}
