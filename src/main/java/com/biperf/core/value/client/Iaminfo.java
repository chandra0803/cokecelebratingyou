
package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "numLikers",
    "iamComment",
    "id"
})
public class Iaminfo {

    @JsonProperty("numLikers")
    private Long numLikers;
    @JsonProperty("iamComment")
    private String iamComment;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("isLiked")
    private boolean isLiked = false;

    @JsonProperty("numLikers")
    public Long getNumLikers() {
        return numLikers;
    }

    @JsonProperty("numLikers")
    public void setNumLikers(Long numLikers) {
        this.numLikers = numLikers;
    }

    public Iaminfo withNumLikers(Long numLikers) {
        this.numLikers = numLikers;
        return this;
    }

    @JsonProperty("iamComment")
    public String getIamComment() {
        return iamComment;
    }

    @JsonProperty("iamComment")
    public void setIamComment(String iamComment) {
        this.iamComment = iamComment;
    }

    public Iaminfo withIamComment(String iamComment) {
        this.iamComment = iamComment;
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

    public Iaminfo withId(Long id) {
        this.id = id;
        return this;
    }

    public boolean isLiked()
    {
      return isLiked;
    }

    public void setLiked( boolean isLiked )
    {
      this.isLiked = isLiked;
    }

}
