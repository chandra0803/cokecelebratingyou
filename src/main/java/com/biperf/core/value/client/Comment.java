
package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commentId",
    "levelOneId",
    "commenter",
    "isMine",
    "isLiked",
    "numLikers",
    "comment"
})
public class Comment {

    @JsonProperty("commentId")
    private Long commentId;
    @JsonProperty("levelOneId")
    private Long levelOneId;
    @JsonProperty("commenter")
    private Commenter commenter;
    @JsonProperty("isMine")
    private Boolean isMine;
    @JsonProperty("isLiked")
    private Boolean isLiked;
    @JsonProperty("numLikers")
    private Long numLikers;
    @JsonProperty("comment")
    private String comment;

    @JsonProperty("commentId")
    public Long getCommentId() {
        return commentId;
    }

    @JsonProperty("commentId")
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @JsonProperty("levelOneId")
    public Long getLevelOneId() {
        return levelOneId;
    }

    @JsonProperty("levelOneId")
    public void setLevelOneId(Long levelOneId) {
        this.levelOneId = levelOneId;
    }

    @JsonProperty("commenter")
    public Commenter getCommenter() {
        return commenter;
    }

    @JsonProperty("commenter")
    public void setCommenter(Commenter commenter) {
        this.commenter = commenter;
    }

    @JsonProperty("isMine")
    public Boolean getIsMine() {
        return isMine;
    }

    @JsonProperty("isMine")
    public void setIsMine(Boolean isMine) {
        this.isMine = isMine;
    }

    @JsonProperty("isLiked")
    public Boolean getIsLiked() {
        return isLiked;
    }

    @JsonProperty("isLiked")
    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    @JsonProperty("numLikers")
    public Long getNumLikers() {
        return numLikers;
    }

    @JsonProperty("numLikers")
    public void setNumLikers(Long numLikers) {
        this.numLikers = numLikers;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

}
