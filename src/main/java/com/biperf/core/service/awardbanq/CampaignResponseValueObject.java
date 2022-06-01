
package com.biperf.core.service.awardbanq;

public class CampaignResponseValueObject
{
  private String type;
  private int errCode;
  private String errDescription;
  private String campaign;
  private String extCampaign;

  public CampaignResponseValueObject()
  {

  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
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

  public String getCampaign()
  {
    return campaign;
  }

  public void setCampaign( String campaign )
  {
    this.campaign = campaign;
  }

  public String getExtCampaign()
  {
    return extCampaign;
  }

  public void setExtCampaign( String extCampaign )
  {
    this.extCampaign = extCampaign;
  }
}
