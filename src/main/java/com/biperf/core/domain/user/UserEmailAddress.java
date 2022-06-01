/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/UserEmailAddress.java,v $
 */

package com.biperf.core.domain.user;

import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.VerificationStatusType;

/**
 * UserEmailAddress.
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
 * <td>sharma</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserEmailAddress extends BaseDomain
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3257288015486726963L;
  private User user;
  private EmailAddressType emailType;
  private String emailAddr;
  private Boolean isPrimary;
  private VerificationStatusType verificationStatus;
  private UUID rosterEmailId;

  /**
   * @return value of emailAddr property
   */
  public String getEmailAddr()
  {
    return emailAddr;
  }

  /**
   * @param emailAddr value for emailAddr property
   */
  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  /**
   * @return value of emailType property
   */
  public EmailAddressType getEmailType()
  {
    return emailType;
  }

  /**
   * @param emailType value for emailType property
   */
  public void setEmailType( EmailAddressType emailType )
  {
    this.emailType = emailType;
  }

  /**
   * @return value of isPrimary property
   */
  public Boolean getIsPrimary()
  {
    return isPrimary;
  }

  /**
   * @param isPrimary value for isPrimary property
   */
  public void setIsPrimary( Boolean isPrimary )
  {
    this.isPrimary = isPrimary;
  }

  public boolean isPrimary()
  {
    if ( isPrimary != null )
    {
      return isPrimary.booleanValue();
    }
    return false;
  }

  public UUID getRosterEmailId()
  {
    return rosterEmailId;
  }

  public void setRosterEmailId( UUID rosterEmailId )
  {
    this.rosterEmailId = rosterEmailId;
  }

  /**
   * @return value of user property
   */
  public User getUser()
  {
    return user;
  }

  /**
   * @param user value for user property
   */
  public void setUser( User user )
  {
    this.user = user;
  }

  public VerificationStatusType getVerificationStatus()
  {
    return verificationStatus;
  }

  public void setVerificationStatus( VerificationStatusType verificationStatus )
  {
    this.verificationStatus = verificationStatus;
  }

  /**
   * Overridden from
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
    if ( ! ( object instanceof UserEmailAddress ) )
    {
      return false;
    }

    final UserEmailAddress otherEmailAddress = (UserEmailAddress)object;
    if ( !getEmailAddr().equals( otherEmailAddress.getEmailAddr() ) )
    {
      return false;
    }
    if ( !getEmailType().getCode().equals( otherEmailAddress.getEmailType().getCode() ) )
    {
      return false;
    }
    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return getEmailAddr().hashCode() + ( getEmailType() == null ? 0 : getEmailType().getCode() == null ? 0 : getEmailType().getCode().hashCode() );
  }
}
