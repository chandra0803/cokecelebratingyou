
package com.biperf.core.service.awardbanq.impl;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.biperf.awardbanq.value.participant.BRTCredentials;
import com.biperf.core.utils.DateUtils;

@XmlRootElement
public class AwardBanqStatementRequest implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String accountNbr;
  private String campaignId;
  private String startDate;
  private String endDate;
  private BRTCredentials brtCredentials;
  private String[] accounts = null;

  public AwardBanqStatementRequest()
  {

  }

  public AwardBanqStatementRequest( String accountNbr, String campaignId, Date startDate, Date endDate, BRTCredentials brtCredentials )
  {
    this.accountNbr = accountNbr;
    this.campaignId = campaignId;
    this.startDate = DateUtils.toDisplayUniversalDateString( startDate );
    this.endDate = DateUtils.toDisplayUniversalDateString( endDate );
    this.setBrtCredentials( brtCredentials );
  }

  public AwardBanqStatementRequest( String[] accounts, String campaignId, Date startDate, Date endDate, BRTCredentials brtCredentials )
  {
    this.campaignId = campaignId;
    this.startDate = DateUtils.toDisplayUniversalDateString( startDate );
    this.endDate = DateUtils.toDisplayUniversalDateString( endDate );
    this.setBrtCredentials( brtCredentials );
    this.accountNbr = "";
    this.accounts = accounts;
  }

  public String getAccountNbr()
  {
    return accountNbr;
  }

  public void setAccountNbr( String accountNbr )
  {
    this.accountNbr = accountNbr;
  }

  public String getCampaignId()
  {
    return campaignId;
  }

  public void setCampaignId( String campaignId )
  {
    this.campaignId = campaignId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public BRTCredentials getBrtCredentials()
  {
    return brtCredentials;
  }

  public void setBrtCredentials( BRTCredentials brtCredentials )
  {
    this.brtCredentials = brtCredentials;
  }

  public String[] getAccounts()
  {
    return accounts;
  }

  public void setAccounts( String[] accounts )
  {
    this.accounts = accounts;
  }
}
