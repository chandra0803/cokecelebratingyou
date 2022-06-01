
package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "userName",
    "contributorID",
    "signedIn",
    "profileLink",
    "profilePhoto",
    "firstName",
    "lastName"
})
public class UserInfo {

    @JsonProperty("userName")
    private String userName;
    @JsonProperty("contributorID")
    private Long contributorID;
    @JsonProperty("signedIn")
    private String signedIn;
    @JsonProperty("profileLink")
    private String profileLink;
    @JsonProperty("profilePhoto")
    private String profilePhoto;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserInfo withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @JsonProperty("contributorID")
    public Long getContributorID() {
        return contributorID;
    }

    @JsonProperty("contributorID")
    public void setContributorID(Long contributorID) {
        this.contributorID = contributorID;
    }

    public UserInfo withContributorID(Long contributorID) {
        this.contributorID = contributorID;
        return this;
    }

    @JsonProperty("signedIn")
    public String getSignedIn() {
        return signedIn;
    }

    @JsonProperty("signedIn")
    public void setSignedIn(String signedIn) {
        this.signedIn = signedIn;
    }

    public UserInfo withSignedIn(String signedIn) {
        this.signedIn = signedIn;
        return this;
    }

    @JsonProperty("profileLink")
    public String getProfileLink() {
        return profileLink;
    }

    @JsonProperty("profileLink")
    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public UserInfo withProfileLink(String profileLink) {
        this.profileLink = profileLink;
        return this;
    }

    @JsonProperty("profilePhoto")
    public String getProfilePhoto() {
        return profilePhoto;
    }

    @JsonProperty("profilePhoto")
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public UserInfo withProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
        return this;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UserInfo withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserInfo withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

}
