
package com.biperf.core.value;

import java.io.Serializable;

/**
 * 
 * CampaignTransferValueBean.
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
 * <td>Bala</td>
 * <td>Dec 09, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class CampaignTransferValueBean implements Serializable
{
  private String userName;
  private String firstName;
  private String lastName;
  private String oldCampaignNbr;
  private String oldBanqNbr;
  private String newCampaignNbr;
  private String newBanqNbr;
  private Long balance;
  private String message;
  private String nodePath;

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
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

  public String getOldCampaignNbr()
  {
    return oldCampaignNbr;
  }

  public void setOldCampaignNbr( String oldCampaignNbr )
  {
    this.oldCampaignNbr = oldCampaignNbr;
  }

  public String getOldBanqNbr()
  {
    return oldBanqNbr;
  }

  public void setOldBanqNbr( String oldBanqNbr )
  {
    this.oldBanqNbr = oldBanqNbr;
  }

  public String getNewCampaignNbr()
  {
    return newCampaignNbr;
  }

  public void setNewCampaignNbr( String newCampaignNbr )
  {
    this.newCampaignNbr = newCampaignNbr;
  }

  public String getNewBanqNbr()
  {
    return newBanqNbr;
  }

  public void setNewBanqNbr( String newBanqNbr )
  {
    this.newBanqNbr = newBanqNbr;
  }

  public Long getBalance()
  {
    return balance;
  }

  public void setBalance( Long balance )
  {
    this.balance = balance;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getNodePath()
  {
    return nodePath;
  }

  public void setNodePath( String nodePath )
  {
    this.nodePath = nodePath;
  }

}
