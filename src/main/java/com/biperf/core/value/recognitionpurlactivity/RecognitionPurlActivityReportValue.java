
package com.biperf.core.value.recognitionpurlactivity;

import java.math.BigDecimal;
import java.util.Date;

public class RecognitionPurlActivityReportValue
{

  private Long purlRecipientId;
  private String nodeName;
  private Long hierarchyNodeId;
  private Long recipientsCnt;
  private Long contributorsInvitedCnt;
  private Long contributedCnt;
  private Long notContributedCnt;
  private Long contributionPostedCnt;
  private BigDecimal contributionPct;
  private String promotionName;
  private String recipientCountry;
  private Date awardDate;
  private String purlStatus;
  private Long award;
  private String awardLevel;
  private String recipientFullName;
  private String managerFullName;
  private Integer totalRecords;

  public RecognitionPurlActivityReportValue()
  {
    super();
  }

  public RecognitionPurlActivityReportValue( String nodeName,
                                             Long hierarchyNodeId,
                                             Long recipientsCnt,
                                             Long contributorsInvitedCnt,
                                             Long contributedCnt,
                                             Long contributionPostedCnt,
                                             BigDecimal contributionPct,
                                             Integer totalRecords )
  {
    super();
    this.nodeName = nodeName;
    this.hierarchyNodeId = hierarchyNodeId;
    this.recipientsCnt = recipientsCnt;
    this.contributorsInvitedCnt = contributorsInvitedCnt;
    this.contributedCnt = contributedCnt;
    this.contributionPostedCnt = contributionPostedCnt;
    this.contributionPct = contributionPct;
    this.totalRecords = totalRecords;
  }

  public RecognitionPurlActivityReportValue( Long recipientsCnt, Long contributorsInvitedCnt, Long contributedCnt, Long contributionPostedCnt, BigDecimal contributionPct )
  {
    super();
    this.recipientsCnt = recipientsCnt;
    this.contributorsInvitedCnt = contributorsInvitedCnt;
    this.contributedCnt = contributedCnt;
    this.contributionPostedCnt = contributionPostedCnt;
    this.contributionPct = contributionPct;
  }

  public RecognitionPurlActivityReportValue( Long purlRecipientId,
                                             Long contributorsInvitedCnt,
                                             Long contributedCnt,
                                             Long contributionPostedCnt,
                                             BigDecimal contributionPct,
                                             String promotionName,
                                             String recipientCountry,
                                             Date awardDate,
                                             String purlStatus,
                                             String award,
                                             String recipientFullName,
                                             String managerFullName,
                                             Integer totalRecords )
  {
    super();
    this.purlRecipientId = purlRecipientId;
    this.contributorsInvitedCnt = contributorsInvitedCnt;
    this.contributedCnt = contributedCnt;
    this.contributionPostedCnt = contributionPostedCnt;
    this.contributionPct = contributionPct;
    this.promotionName = promotionName;
    this.recipientCountry = recipientCountry;
    this.awardDate = awardDate;
    this.purlStatus = purlStatus;
    try
    {
      this.award = ( award == null ? new BigDecimal( 0 ) : new BigDecimal( award ) ).longValue();
    }
    catch( NumberFormatException nfe )
    {
      // if award is of type level catch it and set it to awardLevel
      this.awardLevel = award;
    }
    this.recipientFullName = recipientFullName;
    this.managerFullName = managerFullName;
    this.totalRecords = totalRecords;
  }

  public RecognitionPurlActivityReportValue( Long contributorsInvitedCnt, Long contributedCnt, Long contributionPostedCnt, BigDecimal contributionPct )
  {
    super();
    this.contributorsInvitedCnt = contributorsInvitedCnt;
    this.contributedCnt = contributedCnt;
    this.contributionPostedCnt = contributionPostedCnt;
    this.contributionPct = contributionPct;
  }

  public RecognitionPurlActivityReportValue( String nodeName, Long contributorsInvitedCnt, Long contributedCnt, Long notContributedCnt )
  {
    super();
    this.nodeName = nodeName;
    this.contributorsInvitedCnt = contributorsInvitedCnt;
    this.contributedCnt = contributedCnt;
    this.notContributedCnt = notContributedCnt;
  }

  public RecognitionPurlActivityReportValue( Long contributedCnt, Long notContributedCnt )
  {
    super();
    this.contributedCnt = contributedCnt;
    this.notContributedCnt = notContributedCnt;
  }

  public RecognitionPurlActivityReportValue( String nodeName, Long recipientsCnt )
  {
    super();
    this.nodeName = nodeName;
    this.recipientsCnt = recipientsCnt;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getHierarchyNodeId()
  {
    return hierarchyNodeId;
  }

  public void setHierarchyNodeId( Long hierarchyNodeId )
  {
    this.hierarchyNodeId = hierarchyNodeId;
  }

  public Long getRecipientsCnt()
  {
    return recipientsCnt;
  }

  public void setRecipientsCnt( Long recipientsCnt )
  {
    this.recipientsCnt = recipientsCnt;
  }

  public Long getContributorsInvitedCnt()
  {
    return contributorsInvitedCnt;
  }

  public void setContributorsInvitedCnt( Long contributorsInvitedCnt )
  {
    this.contributorsInvitedCnt = contributorsInvitedCnt;
  }

  public Long getContributedCnt()
  {
    return contributedCnt;
  }

  public void setContributedCnt( Long contributedCnt )
  {
    this.contributedCnt = contributedCnt;
  }

  public Long getNotContributedCnt()
  {
    return notContributedCnt;
  }

  public void setNotContributedCnt( Long notContributedCnt )
  {
    this.notContributedCnt = notContributedCnt;
  }

  public Long getContributionPostedCnt()
  {
    return contributionPostedCnt;
  }

  public void setContributionPostedCnt( Long contributionPostedCnt )
  {
    this.contributionPostedCnt = contributionPostedCnt;
  }

  public BigDecimal getContributionPct()
  {
    return contributionPct;
  }

  public void setContributionPct( BigDecimal contributionPct )
  {
    this.contributionPct = contributionPct;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getRecipientCountry()
  {
    return recipientCountry;
  }

  public void setRecipientCountry( String recipientCountry )
  {
    this.recipientCountry = recipientCountry;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public String getPurlStatus()
  {
    return purlStatus;
  }

  public void setPurlStatus( String purlStatus )
  {
    this.purlStatus = purlStatus;
  }

  public Long getAward()
  {
    return award;
  }

  public void setAward( Long award )
  {
    this.award = award;
  }

  public String getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( String awardLevel )
  {
    this.awardLevel = awardLevel;
  }

  public String getRecipientFullName()
  {
    return recipientFullName;
  }

  public void setRecipientFullName( String recipientFullName )
  {
    this.recipientFullName = recipientFullName;
  }

  public String getManagerFullName()
  {
    return managerFullName;
  }

  public void setManagerFullName( String managerFullName )
  {
    this.managerFullName = managerFullName;
  }

  public Integer getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Integer totalRecords )
  {
    this.totalRecords = totalRecords;
  }

}
