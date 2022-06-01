
package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commentId",
    "levelOneId",
    "id",
    "commenter",
    "isMine",
    "comment",
    "isLiked",
    "numLikers"
})
public class LevelOneComment {

    @JsonProperty("commentId")
    private Long commentId;
    @JsonProperty("levelOneId")
    private Long levelOneId;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("commenter")
    private Commenter commenter;
    @JsonProperty("isMine")
    private Boolean isMine;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("isLiked")
    private Boolean isLiked;
    @JsonProperty("numLikers")
    private Long numLikers;

    @JsonProperty("commentId")
    public Long getCommentId() {
        return commentId;
    }

    @JsonProperty("commentId")
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public LevelOneComment withCommentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }

    @JsonProperty("levelOneId")
    public Long getLevelOneId() {
        return levelOneId;
    }

    @JsonProperty("levelOneId")
    public void setLevelOneId(Long levelOneId) {
        this.levelOneId = levelOneId;
    }

    public LevelOneComment withLevelOneId(Long levelOneId) {
        this.levelOneId = levelOneId;
        return this;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public LevelOneComment withId(Long id) {
        this.id = id;
        return this;
    }

    @JsonProperty("commenter")
    public Commenter getCommenter() {
        return commenter;
    }

    @JsonProperty("commenter")
    public void setCommenter(Commenter commenter) {
        this.commenter = commenter;
    }

    public LevelOneComment withCommenter(Commenter commenter) {
        this.commenter = commenter;
        return this;
    }

    @JsonProperty("isMine")
    public Boolean getIsMine() {
        return isMine;
    }

    @JsonProperty("isMine")
    public void setIsMine(Boolean isMine) {
        this.isMine = isMine;
    }

    public LevelOneComment withIsMine(Boolean isMine) {
        this.isMine = isMine;
        return this;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    public LevelOneComment withComment(String comment) {
        this.comment = comment;
        return this;
    }

    @JsonProperty("isLiked")
    public Boolean getIsLiked() {
        return isLiked;
    }

    @JsonProperty("isLiked")
    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public LevelOneComment withIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
        return this;
    }

    @JsonProperty("numLikers")
    public Long getNumLikers() {
        return numLikers;
    }

    @JsonProperty("numLikers")
    public void setNumLikers(Long numLikers) {
        this.numLikers = numLikers;
    }

    public LevelOneComment withNumLikers(Long numLikers) {
        this.numLikers = numLikers;
        return this;
    }

}
