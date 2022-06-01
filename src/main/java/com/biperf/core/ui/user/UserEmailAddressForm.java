/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserEmailAddressForm.java,v $
 */

package com.biperf.core.ui.user;

import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

/**
 * UserEmailAddressForm.
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
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserEmailAddressForm extends BaseActionForm
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3257567312814094136L;
  private String primary;
  private String[] delete;
  private String emailAddrType;
  private String verificationStatus;
  private String emailAddr;
  private String currentEmailAddr;

  private String userId;

  private boolean isPrimary;
  private long id;
  private long version;
  private long dateCreated;
  private String createdBy;
  private String method;
  private boolean fromPaxScreen;

  private String rosterEmailId;

  public String getRosterEmailId()
  {
    return rosterEmailId;
  }

  public void setRosterEmailId( String rosterEmailId )
  {
    this.rosterEmailId = rosterEmailId;
  }

  public boolean isFromPaxScreen()
  {
    return fromPaxScreen;
  }

  public void setFromPaxScreen( boolean fromPaxScreen )
  {
    this.fromPaxScreen = fromPaxScreen;
  }

  /**
   * @return value of delete property
   */
  public String[] getDelete()
  {
    return delete;
  }

  /**
   * @param delete value for delete property
   */
  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

  /**
   * @return value of primary property
   */
  public String getPrimary()
  {
    return primary;
  }

  /**
   * @param primary value for primary property
   */
  public void setPrimary( String primary )
  {
    this.primary = primary;
  }

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

  public String getCurrentEmailAddr()
  {
    return currentEmailAddr;
  }

  public void setCurrentEmailAddr( String currentEmailAddr )
  {
    this.currentEmailAddr = currentEmailAddr;
  }

  /**
   * @return value of emailAddrType property
   */
  public String getEmailAddrType()
  {
    return emailAddrType;
  }

  /**
   * @param emailAddrType value for emailAddrType property
   */
  public void setEmailAddrType( String emailAddrType )
  {
    this.emailAddrType = emailAddrType;
  }

  public String getVerificationStatus()
  {
    return verificationStatus;
  }

  public void setVerificationStatus( String verificationStatus )
  {
    this.verificationStatus = verificationStatus;
  }

  /**
   * @return value of createdBy property
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * @param createdBy value for createdBy property
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * @return value of dateCreated property
   */
  public long getDateCreated()
  {
    return dateCreated;
  }

  /**
   * @param dateCreated value for dateCreated property
   */
  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return value of id property
   */
  public long getId()
  {
    return id;
  }

  /**
   * @param id value for id property
   */
  public void setId( long id )
  {
    this.id = id;
  }

  /**
   * @return value of version property
   */
  public long getVersion()
  {
    return version;
  }

  /**
   * @param version value for version property
   */
  public void setVersion( long version )
  {
    this.version = version;
  }

  /**
   * @return value of isPrimary property
   */
  public boolean getIsPrimary()
  {
    return isPrimary;
  }

  /**
   * @param isPrimary value for isPrimary property
   */
  public void setIsPrimary( boolean isPrimary )
  {
    this.isPrimary = isPrimary;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  /**
   * Load the form fields from the UserEmailAddress domain object
   * 
   * @param userEmailAddress
   */
  public void load( UserEmailAddress userEmailAddress )
  {
    // Just in case some crafty admin made it to this page, mask the email
    if ( EmailAddressType.RECOVERY.equals( userEmailAddress.getEmailType().getCode() ) && !UserManager.getUserId().toString().equals( userId ) )
    {
      emailAddr = StringUtil.maskEmailAddress( userEmailAddress.getEmailAddr() );
    }
    else
    {
      emailAddr = userEmailAddress.getEmailAddr();
    }

    emailAddrType = userEmailAddress.getEmailType().getCode();
    verificationStatus = userEmailAddress.getVerificationStatus().getCode();
    isPrimary = userEmailAddress.getIsPrimary().booleanValue();
    id = userEmailAddress.getId().longValue();
    version = userEmailAddress.getVersion().longValue();
    dateCreated = userEmailAddress.getAuditCreateInfo().getDateCreated().getTime();
    createdBy = userEmailAddress.getAuditCreateInfo().getCreatedBy().toString();
    if ( userEmailAddress.getId() != null )
    {
      rosterEmailId = userEmailAddress.getRosterEmailId().toString();
    }
  }

  /**
   * Converts the form data to an updatable UserEmailAddress domain object
   * 
   * @return UserEmailAddress
   */
  public UserEmailAddress toUpdateDoaminObject()
  {
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( Long.valueOf( createdBy ) );
    auditCreateInfo.setDateCreated( new Timestamp( dateCreated ) );
    userEmailAddress.setAuditCreateInfo( auditCreateInfo );
    userEmailAddress.setId( new Long( id ) );
    userEmailAddress.setEmailAddr( emailAddr );
    userEmailAddress.setEmailType( EmailAddressType.lookup( emailAddrType ) );

    // Actually changing the email address will reset verification to "unverified"
    if ( emailAddr.equals( currentEmailAddr ) )
    {
      userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( verificationStatus ) );
    }
    else
    {
      userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    }

    if ( isPrimary )
    {
      userEmailAddress.setIsPrimary( Boolean.TRUE );
    }
    else
    {
      userEmailAddress.setIsPrimary( Boolean.FALSE );
    }
    userEmailAddress.setVersion( new Long( version ) );

    if ( rosterEmailId != null && !StringUtils.isEmpty( rosterEmailId ) )
    {
      userEmailAddress.setRosterEmailId( UUID.fromString( rosterEmailId ) );
    }
    return userEmailAddress;
  }

  /**
   * Converts the form data to an insertable UserEmailAddress domain object
   * 
   * @return UserEmailAddress
   */
  public UserEmailAddress toInsertDoaminObject()
  {
    UserEmailAddress userEmailAddress = new UserEmailAddress();

    userEmailAddress.setEmailAddr( emailAddr );
    userEmailAddress.setEmailType( EmailAddressType.lookup( emailAddrType ) );
    userEmailAddress.setIsPrimary( Boolean.FALSE );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    return userEmailAddress;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}
