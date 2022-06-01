/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/value/ExternalSupplierValue.java,v $
 */

package com.biperf.core.value;

/**
 * ExternalSupplierValue.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>robinsra</td>
 * <td>Sep 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ExternalSupplierValue
{
  private String bankAccountSystemNumber = "";
  private String campaignID = "";
  private String languageCode = "";
  private String targetPageId = "";
  private String countryCode = "";
  private String actionURL = "";
  private String statementActionURL = "";
  private String statementTargetPageId = "";
  private boolean allowPartnerSSo;
  private String campaignCode = "";
  private String bankAccountNumber = "";

  public String getBankAccountNumber()
  {
    return bankAccountNumber;
  }

  public void setBankAccountNumber( String bankAccountNumber )
  {
    this.bankAccountNumber = bankAccountNumber;
  }

  public boolean isAllowPartnerSSo()
  {
    return allowPartnerSSo;
  }

  public void setAllowPartnerSSo( boolean allowPartnerSSo )
  {
    this.allowPartnerSSo = allowPartnerSSo;
  }

  public String getCampaignCode()
  {
    return campaignCode;
  }

  public void setCampaignCode( String campaignCode )
  {
    this.campaignCode = campaignCode;
  }

  public String getActionURL()
  {
    return actionURL;
  }

  public void setActionURL( String actionURL )
  {
    this.actionURL = actionURL;
  }

  public String getBankAccountSystemNumber()
  {
    return bankAccountSystemNumber;
  }

  public void setBankAccountSystemNumber( String bankAccountSystemNumber )
  {
    this.bankAccountSystemNumber = bankAccountSystemNumber;
  }

  public String getCampaignID()
  {
    return campaignID;
  }

  public void setCampaignID( String campaignID )
  {
    this.campaignID = campaignID;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getLanguageCode()
  {
    return languageCode;
  }

  public void setLanguageCode( String languageCode )
  {
    this.languageCode = languageCode;
  }

  public String getTargetPageId()
  {
    return targetPageId;
  }

  public void setTargetPageId( String targetPageId )
  {
    this.targetPageId = targetPageId;
  }

  public String getStatementActionURL()
  {
    return statementActionURL;
  }

  public void setStatementActionURL( String statementActionURL )
  {
    this.statementActionURL = statementActionURL;
  }

  public String getStatementTargetPageId()
  {
    return statementTargetPageId;
  }

  public void setStatementTargetPageId( String statementTargetPageId )
  {
    this.statementTargetPageId = statementTargetPageId;
  }

}
