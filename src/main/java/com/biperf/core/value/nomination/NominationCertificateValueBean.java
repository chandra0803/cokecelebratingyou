
package com.biperf.core.value.nomination;

import java.sql.Date;

public class NominationCertificateValueBean
{
  private String certType;
  private String nomineeFirstName;
  private String nomineeLastName;
  private String nominatorFirstName;
  private String nominatorLastName;
  private Date submissionDate;
  private Integer levelIndex;
  private String levelName;
  private String teamName;
  private String timePeriodName;
  private String promotionName;
  private String submitterComments;
  private String submitterLangId;
  private Long certificateId;

  public String getCertType()
  {
    return certType;
  }

  public void setCertType( String certType )
  {
    this.certType = certType;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public Integer getLevelIndex()
  {
    return levelIndex;
  }

  public void setLevelIndex( Integer levelIndex )
  {
    this.levelIndex = levelIndex;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public String getSubmitterLangId()
  {
    return submitterLangId;
  }

  public void setSubmitterLangId( String submitterLangId )
  {
    this.submitterLangId = submitterLangId;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public String getNomineeFirstName()
  {
    return nomineeFirstName;
  }

  public void setNomineeFirstName( String nomineeFirstName )
  {
    this.nomineeFirstName = nomineeFirstName;
  }

  public String getNomineeLastName()
  {
    return nomineeLastName;
  }

  public void setNomineeLastName( String nomineeLastName )
  {
    this.nomineeLastName = nomineeLastName;
  }

  public String getNominatorFirstName()
  {
    return nominatorFirstName;
  }

  public void setNominatorFirstName( String nominatorFirstName )
  {
    this.nominatorFirstName = nominatorFirstName;
  }

  public String getNominatorLastName()
  {
    return nominatorLastName;
  }

  public void setNominatorLastName( String nominatorLastName )
  {
    this.nominatorLastName = nominatorLastName;
  }

}
