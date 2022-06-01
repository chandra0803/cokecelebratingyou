
package com.biperf.core.service.awardbanq;

public class ConvertCertificateValueObject
{
  private String campaignNumber = "";
  private String omParticipantId = "";
  private String certificateNumber = "";
  private String certificatePin = "";
  private int returnCode;
  private String returnText = "";
  private Integer pointsDeposited = 0;
  private int errCode;
  private String errDescription = "";

  public ConvertCertificateValueObject()
  {

  }

  public ConvertCertificateValueObject( String campaignNumber, String centraxId, String certificateNumber, String certificatePIN )
  {
    this.campaignNumber = campaignNumber;
    this.omParticipantId = centraxId;
    this.certificateNumber = certificateNumber;
    this.certificatePin = certificatePIN;
  }

  public String getCampaignNumber()
  {
    return campaignNumber;
  }

  public void setCampaignNumber( String campaignNumber )
  {
    this.campaignNumber = campaignNumber;
  }

  public String getOmParticipantId()
  {
    return omParticipantId;
  }

  public void setOmParticipantId( String omParticipantId )
  {
    this.omParticipantId = omParticipantId;
  }

  public String getCertificateNumber()
  {
    return certificateNumber;
  }

  public void setCertificateNumber( String certificateNumber )
  {
    this.certificateNumber = certificateNumber;
  }

  public String getCertificatePin()
  {
    return certificatePin;
  }

  public void setCertificatePin( String certificatePin )
  {
    this.certificatePin = certificatePin;
  }

  public int getReturnCode()
  {
    return returnCode;
  }

  public void setReturnCode( int returnCode )
  {
    this.returnCode = returnCode;
  }

  public String getReturnText()
  {
    return returnText;
  }

  public void setReturnText( String returnText )
  {
    this.returnText = returnText;
  }

  public Integer getPointsDeposited()
  {
    return pointsDeposited;
  }

  public void setPointsDeposited( Integer pointsDeposited )
  {
    this.pointsDeposited = pointsDeposited;
  }

  public int getErrCode()
  {
    return errCode;
  }

  public void setErrCode( int errCode )
  {
    this.errCode = errCode;
  }

  public String getErrDescription()
  {
    return errDescription;
  }

  public void setErrDescription( String errDescription )
  {
    this.errDescription = errDescription;
  }

}
