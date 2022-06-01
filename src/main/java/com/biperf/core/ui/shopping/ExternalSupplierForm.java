/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/shopping/ExternalSupplierForm.java,v $
 */

package com.biperf.core.ui.shopping;

import com.biperf.core.ui.BaseForm;

/**
 * ExternalSupplierForm.
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
public class ExternalSupplierForm extends BaseForm
{
  private String bankAccountSystemNumber = "";
  private String campaignID = "";
  private String languageCode = "";
  private String targetPageId = "";
  private String countryCode = "";
  private String errorPage = "";
  private String accessDeniedPage = "";
  private String charset = "";
  private String encryptionType = "";
  private String actionURL = "";
  private String method = "";
  private String campaignCode = "";
  private String bankAccountNumber = "";
  private String centraxId = "";
  private boolean allowPartnerSso = false;

  public boolean isAllowPartnerSso()
  {
    return allowPartnerSso;
  }

  public void setAllowPartnerSso( boolean allowPartnerSso )
  {
    this.allowPartnerSso = allowPartnerSso;
  }

  public String getCentraxId()
  {
    return centraxId;
  }

  public void setCentraxId( String centraxId )
  {
    this.centraxId = centraxId;
  }

  public String getBankAccountNumber()
  {
    return bankAccountNumber;
  }

  public void setBankAccountNumber( String bankAccountNumber )
  {
    this.bankAccountNumber = bankAccountNumber;
  }

  public String getCampaignCode()
  {
    return campaignCode;
  }

  public void setCampaignCode( String campaignCode )
  {
    this.campaignCode = campaignCode;
  }

  public String getAccessDeniedPage()
  {
    return accessDeniedPage;
  }

  public void setAccessDeniedPage( String accessDeniedPage )
  {
    this.accessDeniedPage = accessDeniedPage;
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

  public String getCharset()
  {
    return charset;
  }

  public void setCharset( String charset )
  {
    this.charset = charset;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getEncryptionType()
  {
    return encryptionType;
  }

  public void setEncryptionType( String encryptionType )
  {
    this.encryptionType = encryptionType;
  }

  public String getErrorPage()
  {
    return errorPage;
  }

  public void setErrorPage( String errorPage )
  {
    this.errorPage = errorPage;
  }

  public String getLanguageCode()
  {
    return languageCode;
  }

  public void setLanguageCode( String languageCode )
  {
    this.languageCode = languageCode;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getTargetPageId()
  {
    return targetPageId;
  }

  public void setTargetPageId( String targetPageId )
  {
    this.targetPageId = targetPageId;
  }

}
