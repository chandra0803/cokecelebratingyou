/**
 * 
 */

package com.biperf.core.value.recognitionbypax;

import java.util.Date;

public class RecognitionSummaryGivenByParticipantValue
{

  private Long giverUserId;
  private String giver;
  private String country;
  private String orgName;
  private Long giverNodeId;
  private String department;
  private String title;
  private Long recognitionsCnt;
  private Long pointsIssued;
  private Long plateauEarnedCnt;
  private Long claimId;
  private Integer totalRecords;

  private Date dateApproved;
  private String promotion;

  public RecognitionSummaryGivenByParticipantValue()
  {
    super();
  }

  public RecognitionSummaryGivenByParticipantValue( Long giverUserId,
                                                    String giver,
                                                    String country,
                                                    String orgName,
                                                    Long giverNodeId,
                                                    String department,
                                                    String title,
                                                    Long recognitionsCnt,
                                                    Long pointsIssued,
                                                    Long plateauEarnedCnt,
                                                    Integer totalRecords )
  {
    super();
    this.giverUserId = giverUserId;
    this.giver = giver;
    this.country = country;
    this.orgName = orgName;
    this.giverNodeId = giverNodeId;
    this.department = department;
    this.title = title;
    this.recognitionsCnt = recognitionsCnt;
    this.pointsIssued = pointsIssued;
    this.plateauEarnedCnt = plateauEarnedCnt;
    this.totalRecords = totalRecords;
  }

  public RecognitionSummaryGivenByParticipantValue( Long recognitionsCnt, Long pointsIssued, Long plateauEarnedCnt )
  {
    super();
    this.recognitionsCnt = recognitionsCnt;
    this.pointsIssued = pointsIssued;
    this.plateauEarnedCnt = plateauEarnedCnt;
  }

  public RecognitionSummaryGivenByParticipantValue( String giver,
                                                    String country,
                                                    String orgName,
                                                    String department,
                                                    String title,
                                                    Long pointsIssued,
                                                    Long plateauEarnedCnt,
                                                    Integer totalRecords,
                                                    Date dateApproved,
                                                    String promotion,
                                                    Long claimId )
  {
    super();
    this.giver = giver;
    this.country = country;
    this.orgName = orgName;
    this.department = department;
    this.title = title;
    this.pointsIssued = pointsIssued;
    this.plateauEarnedCnt = plateauEarnedCnt;
    this.totalRecords = totalRecords;
    this.dateApproved = dateApproved;
    this.promotion = promotion;
    this.claimId = claimId;
  }

  public RecognitionSummaryGivenByParticipantValue( Long pointsIssued, Long plateauEarnedCnt )
  {
    super();
    this.pointsIssued = pointsIssued;
    this.plateauEarnedCnt = plateauEarnedCnt;
  }

  public Long getGiverUserId()
  {
    return giverUserId;
  }

  public void setGiverUserId( Long giverUserId )
  {
    this.giverUserId = giverUserId;
  }

  public String getGiver()
  {
    return giver;
  }

  public void setGiver( String giver )
  {
    this.giver = giver;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public Long getGiverNodeId()
  {
    return giverNodeId;
  }

  public void setGiverNodeId( Long giverNodeId )
  {
    this.giverNodeId = giverNodeId;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public Long getRecognitionsCnt()
  {
    return recognitionsCnt;
  }

  public void setRecognitionsCnt( Long recognitionsCnt )
  {
    this.recognitionsCnt = recognitionsCnt;
  }

  public Long getPointsIssued()
  {
    return pointsIssued;
  }

  public void setPointsIssued( Long pointsIssued )
  {
    this.pointsIssued = pointsIssued;
  }

  public Long getPlateauEarnedCnt()
  {
    return plateauEarnedCnt;
  }

  public void setPlateauEarnedCnt( Long plateauEarnedCnt )
  {
    this.plateauEarnedCnt = plateauEarnedCnt;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Integer getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Integer totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public Date getDateApproved()
  {
    return dateApproved;
  }

  public void setDateApproved( Date dateApproved )
  {
    this.dateApproved = dateApproved;
  }

  public String getPromotion()
  {
    return promotion;
  }

  public void setPromotion( String promotion )
  {
    this.promotion = promotion;
  }

}
