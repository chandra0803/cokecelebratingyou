/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/value/ShoppingValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;

/**
 * ShoppingValueBean.
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
 * <td>Sep 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ShoppingValueBean implements Serializable
{
  String remoteURL = "";
  String proxy = "";
  int proxyPort = 8080;
  String programId = "";
  String programPassword = "";
  boolean canShop = false;
  String firstName = "";
  String lastName = "";
  String account = "";
  String postLoginURL = "";
  String languagePreference = "";

  public String getAccount()
  {
    return account;
  }

  public void setAccount( String account )
  {
    this.account = account;
  }

  public boolean isCanShop()
  {
    if ( account == null || account.equals( "" ) )
    {
      return false;
    }

    return true;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public String getProgramPassword()
  {
    return programPassword;
  }

  public void setProgramPassword( String programPassword )
  {
    this.programPassword = programPassword;
  }

  public String getProxy()
  {
    return proxy;
  }

  public void setProxy( String proxy )
  {
    this.proxy = proxy;
  }

  public int getProxyPort()
  {
    return proxyPort;
  }

  public void setProxyPort( int proxyPort )
  {
    this.proxyPort = proxyPort;
  }

  public String getRemoteURL()
  {
    return remoteURL;
  }

  public void setRemoteURL( String remoteURL )
  {
    this.remoteURL = remoteURL;
  }

  public String getPostLoginURL()
  {
    return postLoginURL;
  }

  public void setPostLoginURL( String postLoginURL )
  {
    this.postLoginURL = postLoginURL;
  }

  public String getLanguagePreference()
  {
    return languagePreference;
  }

  public void setLanguagePreference( String languagePreference )
  {
    this.languagePreference = languagePreference;
  }

}
