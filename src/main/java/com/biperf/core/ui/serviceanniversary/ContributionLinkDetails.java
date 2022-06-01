
package com.biperf.core.ui.serviceanniversary;

public class ContributionLinkDetails
{
  private String celebrationId;
  private Long programId;
  private String url;
  private String recepientName;

  public Long getProgramId()
  {
    return programId;
  }

  public void setProgramId( Long programId )
  {
    this.programId = programId;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( String celebrationId )
  {
    this.celebrationId = celebrationId;
  }

  public String getRecepientName()
  {
    return recepientName;
  }

  public void setRecepientName( String recepientName )
  {
    this.recepientName = recepientName;
  }

}
