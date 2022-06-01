
package com.biperf.core.value.recognitionadvisor;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RecognitionAdvisorValueBean
{
  private Long id;
  private String firstName;
  private String lastName;
  private Integer participantGroupType;
  private String avatarUrl;
  private String positionType;
  private String otherMgrApprovedDate;
  private String currentMgrApprovedDate;
  private Integer otherMgrLastRecogDays;
  private Integer currentMgrLastRecogDays;
  private Integer otherMgrAwardPoints;
  private Integer currentMgrAwardPoints;
  private String claimIdByCurrentMgr;
  private String claimIdByOtherMgr;
  private Long noOfDaysPastDueDate;
  private Long raTotalParticipants;
  private String participantRecDaysColour;
  private String participantRecDaysColourForOthers;
  private String claimUrlByCurrentMgr;
  private String claimUrlByOtherMgr;
  private int countryId;
  private String[] nodeId;
  private String recApprovalStatusType;
  private String countryCode;
  private String countryName;
  private String newHireAvailable;
  private String overDueAvailable;
  private String upComingAvailable;
  private Integer newHireTotalcount;
  private Integer overDueTotalcount;

  public Integer getParticipantGroupType()
  {
    return participantGroupType;
  }

  public void setParticipantGroupType( Integer participantGroupType )
  {
    this.participantGroupType = participantGroupType;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  public String getOtherMgrApprovedDate()
  {
    return otherMgrApprovedDate;
  }

  public void setOtherMgrApprovedDate( String otherMgrApprovedDate )
  {
    this.otherMgrApprovedDate = otherMgrApprovedDate;
  }

  public String getCurrentMgrApprovedDate()
  {
    return currentMgrApprovedDate;
  }

  public void setCurrentMgrApprovedDate( String currentMgrApprovedDate )
  {
    this.currentMgrApprovedDate = currentMgrApprovedDate;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public Long getNoOfDaysPastDueDate()
  {
    return noOfDaysPastDueDate;
  }

  public void setNoOfDaysPastDueDate( Long noOfDaysPastDueDate )
  {
    this.noOfDaysPastDueDate = noOfDaysPastDueDate;
  }

  @JsonIgnore
  public Long getRaTotalParticipants()
  {
    return raTotalParticipants;
  }

  public void setRaTotalParticipants( Long raTotalParticipants )
  {
    this.raTotalParticipants = raTotalParticipants;
  }

  public String getParticipantRecDaysColour()
  {
    return participantRecDaysColour;
  }

  public void setParticipantRecDaysColour( String participantRecDaysColour )
  {
    this.participantRecDaysColour = participantRecDaysColour;
  }

  public String getParticipantRecDaysColourForOthers()
  {
    return participantRecDaysColourForOthers;
  }

  public void setParticipantRecDaysColourForOthers( String participantRecDaysColourForOthers )
  {
    this.participantRecDaysColourForOthers = participantRecDaysColourForOthers;
  }

  public Integer getOtherMgrLastRecogDays()
  {
    return otherMgrLastRecogDays;
  }

  public void setOtherMgrLastRecogDays( Integer otherMgrLastRecogDays )
  {
    this.otherMgrLastRecogDays = otherMgrLastRecogDays;
  }

  public Integer getCurrentMgrLastRecogDays()
  {
    return currentMgrLastRecogDays;
  }

  public void setCurrentMgrLastRecogDays( Integer currentMgrLastRecogDays )
  {
    this.currentMgrLastRecogDays = currentMgrLastRecogDays;
  }

  public Integer getOtherMgrAwardPoints()
  {
    return otherMgrAwardPoints;
  }

  public void setOtherMgrAwardPoints( Integer otherMgrAwardPoints )
  {
    this.otherMgrAwardPoints = otherMgrAwardPoints;
  }

  public Integer getCurrentMgrAwardPoints()
  {
    return currentMgrAwardPoints;
  }

  public void setCurrentMgrAwardPoints( Integer currentMgrAwardPoints )
  {
    this.currentMgrAwardPoints = currentMgrAwardPoints;
  }

  public String getClaimUrlByCurrentMgr()
  {
    return claimUrlByCurrentMgr;
  }

  public void setClaimUrlByCurrentMgr( String claimUrlByCurrentMgr )
  {
    this.claimUrlByCurrentMgr = claimUrlByCurrentMgr;
  }

  public String getClaimUrlByOtherMgr()
  {
    return claimUrlByOtherMgr;
  }

  public void setClaimUrlByOtherMgr( String claimUrlByOtherMgr )
  {
    this.claimUrlByOtherMgr = claimUrlByOtherMgr;
  }

  public String getClaimIdByCurrentMgr()
  {
    return claimIdByCurrentMgr;
  }

  public void setClaimIdByCurrentMgr( String claimIdByCurrentMgr )
  {
    this.claimIdByCurrentMgr = claimIdByCurrentMgr;
  }

  public String getClaimIdByOtherMgr()
  {
    return claimIdByOtherMgr;
  }

  public void setClaimIdByOtherMgr( String claimIdByOtherMgr )
  {
    this.claimIdByOtherMgr = claimIdByOtherMgr;
  }

  public int getCountryId()
  {
    return countryId;
  }

  public void setCountryId( int countryId )
  {
    this.countryId = countryId;
  }

  public String[] getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( String[] nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getRecApprovalStatusType()
  {
    return recApprovalStatusType;
  }

  public void setRecApprovalStatusType( String recApprovalStatusType )
  {
    this.recApprovalStatusType = recApprovalStatusType;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  @JsonIgnore
  public String getNewHireAvailable()
  {
    return newHireAvailable;
  }

  public void setNewHireAvailable( String newHireAvailable )
  {
    this.newHireAvailable = newHireAvailable;
  }

  @JsonIgnore
  public String getOverDueAvailable()
  {
    return overDueAvailable;
  }

  public void setOverDueAvailable( String overDueAvailable )
  {
    this.overDueAvailable = overDueAvailable;
  }

  @JsonIgnore
  public String getUpComingAvailable()
  {
    return upComingAvailable;
  }

  public void setUpComingAvailable( String upComingAvailable )
  {
    this.upComingAvailable = upComingAvailable;
  }

  @JsonIgnore
  public Integer getNewHireTotalcount()
  {
    return newHireTotalcount;
  }

  public void setNewHireTotalcount( Integer newHireTotalcount )
  {
    this.newHireTotalcount = newHireTotalcount;
  }

  @JsonIgnore
  public Integer getOverDueTotalcount()
  {
    return overDueTotalcount;
  }

  public void setOverDueTotalcount( Integer overDueTotalcount )
  {
    this.overDueTotalcount = overDueTotalcount;
  }

}
