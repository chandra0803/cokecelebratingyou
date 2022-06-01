
package com.biperf.core.domain.user;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;

/**
 * UserCountryChanges.
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
 */
public class UserCountryChanges extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private Long importFileId;
  private Long userId;
  // private User user;
  private Long oldCountryId;
  private String oldCampaignNbr;
  private String oldAwardBanqNbr;
  private String oldCentraxId;
  private Long newCountryId;
  private String newCampaignNbr;
  private String newAwardBanqNbr;
  private String newCentraxId;
  private Long balanceToMove;
  private Boolean globalAssignee;
  private Boolean campaignMove;
  private Boolean processed;
  private String message;
  private Boolean initialEmailSent;

  private Date acctBalanceTransferDate;
  private Long acctBalanceTransferResultCode;

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Ensure equality between this and the object param. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof UserCountryChanges ) )
    {
      return false;
    }

    UserCountryChanges userCountryChanges = (UserCountryChanges)object;

    if ( this.getId() != null && this.getId().equals( userCountryChanges.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "UserCountryChange [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{importFileId=" ).append( getImportFileId() ).append( "}, " );
    buf.append( "{userId=" ).append( getUserId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getImportFileId()
  {
    return importFileId;
  }

  public void setImportFileId( Long importFileId )
  {
    this.importFileId = importFileId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getOldCampaignNbr()
  {
    return oldCampaignNbr;
  }

  public void setOldCampaignNbr( String oldCampaignNbr )
  {
    this.oldCampaignNbr = oldCampaignNbr;
  }

  public String getOldAwardBanqNbr()
  {
    return oldAwardBanqNbr;
  }

  public void setOldAwardBanqNbr( String oldAwardBanqNbr )
  {
    this.oldAwardBanqNbr = oldAwardBanqNbr;
  }

  public String getNewCampaignNbr()
  {
    return newCampaignNbr;
  }

  public void setNewCampaignNbr( String newCampaignNbr )
  {
    this.newCampaignNbr = newCampaignNbr;
  }

  public String getNewAwardBanqNbr()
  {
    return newAwardBanqNbr;
  }

  public void setNewAwardBanqNbr( String newAwardBanqNbr )
  {
    this.newAwardBanqNbr = newAwardBanqNbr;
  }

  public Long getOldCountryId()
  {
    return oldCountryId;
  }

  public void setOldCountryId( Long oldCountryId )
  {
    this.oldCountryId = oldCountryId;
  }

  public Long getNewCountryId()
  {
    return newCountryId;
  }

  public void setNewCountryId( Long newCountryId )
  {
    this.newCountryId = newCountryId;
  }

  public Long getBalanceToMove()
  {
    return balanceToMove;
  }

  public void setBalanceToMove( Long balanceToMove )
  {
    this.balanceToMove = balanceToMove;
  }

  public boolean isGlobalAssignee()
  {
    if ( globalAssignee == null )
    {
      return false;
    }
    else
    {
      return globalAssignee.booleanValue();
    }
  }

  public boolean isCampaignMove()
  {
    if ( campaignMove == null )
    {
      return false;
    }
    else
    {
      return campaignMove.booleanValue();
    }

  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public boolean isInitialEmailSent()
  {
    if ( initialEmailSent == null )
    {
      return false;
    }
    else
    {
      return initialEmailSent.booleanValue();
    }
  }

  public Boolean getGlobalAssignee()
  {
    return globalAssignee;
  }

  public void setGlobalAssignee( Boolean globalAssignee )
  {
    this.globalAssignee = globalAssignee;
  }

  public Boolean getCampaignMove()
  {
    return campaignMove;
  }

  public void setCampaignMove( Boolean campaignMove )
  {
    this.campaignMove = campaignMove;
  }

  public Boolean getProcessed()
  {
    return processed;
  }

  public void setProcessed( Boolean processed )
  {
    this.processed = processed;
  }

  public Boolean getInitialEmailSent()
  {
    return initialEmailSent;
  }

  public void setInitialEmailSent( Boolean initialEmailSent )
  {
    this.initialEmailSent = initialEmailSent;
  }

  public String getOldCentraxId()
  {
    return oldCentraxId;
  }

  public void setOldCentraxId( String oldCentraxId )
  {
    this.oldCentraxId = oldCentraxId;
  }

  public String getNewCentraxId()
  {
    return newCentraxId;
  }

  public void setNewCentraxId( String newCentraxId )
  {
    this.newCentraxId = newCentraxId;
  }

  public Date getAcctBalanceTransferDate()
  {
    return acctBalanceTransferDate;
  }

  public void setAcctBalanceTransferDate( Date acctBalanceTransferDate )
  {
    this.acctBalanceTransferDate = acctBalanceTransferDate;
  }

  public Long getAcctBalanceTransferResultCode()
  {
    return acctBalanceTransferResultCode;
  }

  public void setAcctBalanceTransferResultCode( Long acctBalanceTransferResultCode )
  {
    this.acctBalanceTransferResultCode = acctBalanceTransferResultCode;
  }
}
