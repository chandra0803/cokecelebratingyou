/**
 * 
 */

package com.biperf.core.value.recognitionreceivedbypax;

import java.util.Date;

public class RecognitionSummaryReceivedByParticipantValue
{
  private Long receiverId;
  private String receiver;
  private String country;
  private String orgName;
  private Long receiverNodeId;
  private String department;
  private String title;
  private Long recognitionsCnt;
  private Long pointsIssued;
  private Long plateauEarnedCnt;
  private Long claimId;
  private String giver;
  private Integer totalRecords;

  private Date dateApproved;
  private String promotion;

  public RecognitionSummaryReceivedByParticipantValue()
  {
    super();
  }

  public RecognitionSummaryReceivedByParticipantValue( Long receiverId,
                                                       String receiver,
                                                       String country,
                                                       String orgName,
                                                       Long receiverNodeId,
                                                       String department,
                                                       String title,
                                                       Long recognitionsCnt,
                                                       Long pointsIssued,
                                                       Long plateauEarnedCnt,
                                                       Integer totalRecords )
  {
    super();
    this.receiverId = receiverId;
    this.receiver = receiver;
    this.country = country;
    this.orgName = orgName;
    this.receiverNodeId = receiverNodeId;
    this.department = department;
    this.title = title;
    this.recognitionsCnt = recognitionsCnt;
    this.pointsIssued = pointsIssued;
    this.plateauEarnedCnt = plateauEarnedCnt;
    this.totalRecords = totalRecords;
  }

  public RecognitionSummaryReceivedByParticipantValue( Long recognitionsCnt, Long pointsIssued, Long plateauEarnedCnt )
  {
    super();
    this.recognitionsCnt = recognitionsCnt;
    this.pointsIssued = pointsIssued;
    this.plateauEarnedCnt = plateauEarnedCnt;
  }

  public RecognitionSummaryReceivedByParticipantValue( String receiver,
                                                       String country,
                                                       String orgName,
                                                       String department,
                                                       String title,
                                                       Long pointsIssued,
                                                       Long plateauEarnedCnt,
                                                       Integer totalRecords,
                                                       Date dateApproved,
                                                       String promotion,
                                                       Long claimId,
                                                       String giver )
  {
    super();
    this.receiver = receiver;
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
    this.setGiver( giver );
  }

  public RecognitionSummaryReceivedByParticipantValue( Long pointsIssued, Long plateauEarnedCnt )
  {
    super();
    this.pointsIssued = pointsIssued;
    this.plateauEarnedCnt = plateauEarnedCnt;
  }

  public Long getReceiverId()
  {
    return receiverId;
  }

  public void setReceiverId( Long receiverId )
  {
    this.receiverId = receiverId;
  }

  public String getReceiver()
  {
    return receiver;
  }

  public void setReceiver( String receiver )
  {
    this.receiver = receiver;
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

  public Long getReceiverNodeId()
  {
    return receiverNodeId;
  }

  public void setReceiverNodeId( Long receiverNodeId )
  {
    this.receiverNodeId = receiverNodeId;
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

  public String getGiver()
  {
    return giver;
  }

  public void setGiver( String giver )
  {
    this.giver = giver;
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
