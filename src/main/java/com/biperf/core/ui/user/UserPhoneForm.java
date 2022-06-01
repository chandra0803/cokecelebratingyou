/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserPhoneForm.java,v $
 */

package com.biperf.core.ui.user;

import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

/**
 * UserPhoneForm.
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
 * <td>zahler</td>
 * <td>Apr 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserPhoneForm extends BaseForm
{
  public static final String FORM_NAME = "userPhoneForm";

  private String userId;
  private String method;
  private String phoneType;
  private String countryPhoneCode;
  private String phoneNbr;
  private String phoneExt;
  private String phoneTypeDesc;
  private long version;
  private long id;
  private long dateCreated;
  private String createdBy;
  private boolean primary;
  private boolean fromPaxScreen;
  private String currentPhoneNbr;
  private String verificationStatus;
  private String verificationStatusDesc;

  private String rosterPhoneId;

  public String getRosterPhoneId()
  {
    return rosterPhoneId;
  }

  public void setRosterPhoneId( String rosterPhoneId )
  {
    this.rosterPhoneId = rosterPhoneId;
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
   * Load the values from the UserPhone to the form
   * 
   * @param userPhone
   */
  public void load( UserPhone userPhone )
  {
    this.userId = String.valueOf( userPhone.getUser().getId() );

    // Just in case some crafty admin made it to this page, mask the recovery phone number
    if ( PhoneType.RECOVERY.equals( userPhone.getPhoneType().getCode() ) && !UserManager.getUserId().toString().equals( userId ) )
    {
      phoneNbr = StringUtil.maskPhoneNumber( userPhone.getPhoneNbr() );
    }
    else
    {
      phoneNbr = userPhone.getPhoneNbr();
    }

    this.phoneType = userPhone.getPhoneType().getCode();
    this.phoneTypeDesc = userPhone.getPhoneType().getName();
    this.phoneExt = userPhone.getPhoneExt();
    this.countryPhoneCode = userPhone.getCountryPhoneCode();
    this.primary = userPhone.isPrimary();
    this.verificationStatus = userPhone.getVerificationStatus().getCode();
    this.verificationStatusDesc = userPhone.getVerificationStatus().getName();
    this.createdBy = userPhone.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = userPhone.getAuditCreateInfo().getDateCreated().getTime();
    this.id = userPhone.getId().longValue();
    this.version = userPhone.getVersion().longValue();

    if ( userPhone.getId() != null )
    {
      this.rosterPhoneId = userPhone.getRosterPhoneId().toString();
    }
  }

  /**
   * Builds a full domain object for update from the form.
   * 
   * @return UserPhone
   */
  public UserPhone toFullDomainObject()
  {
    UserPhone userPhone = new UserPhone();

    userPhone.setPhoneType( PhoneType.lookup( this.phoneType ) );
    userPhone.setPhoneNbr( this.phoneNbr );
    userPhone.setPhoneExt( this.phoneExt );
    userPhone.setCountryPhoneCode( countryPhoneCode );
    userPhone.setIsPrimary( Boolean.valueOf( primary ) );
    userPhone.setId( new Long( this.id ) );
    userPhone.setVersion( new Long( this.version ) );
    userPhone.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    userPhone.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );

    // Actually changing the phone number will reset verification to "unverified"
    if ( phoneNbr.equals( currentPhoneNbr ) )
    {
      userPhone.setVerificationStatus( VerificationStatusType.lookup( verificationStatus ) );
    }
    else
    {
      userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    }

    if ( this.rosterPhoneId != null && !StringUtils.isEmpty( this.rosterPhoneId ) )
    {
      userPhone.setRosterPhoneId( UUID.fromString( this.rosterPhoneId ) );
    }

    return userPhone;
  }

  /**
   * Builds a domain object from the form.
   * 
   * @return UserPhone
   */
  public UserPhone toInsertedDomainObject()
  {
    UserPhone userPhone = new UserPhone();

    userPhone.setPhoneType( PhoneType.lookup( this.phoneType ) );
    userPhone.setPhoneNbr( this.phoneNbr );
    userPhone.setPhoneExt( this.phoneExt );
    userPhone.setCountryPhoneCode( this.countryPhoneCode );
    userPhone.setIsPrimary( Boolean.valueOf( false ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );

    return userPhone;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPhoneExt()
  {
    return phoneExt;
  }

  public void setPhoneExt( String phoneExt )
  {
    this.phoneExt = phoneExt;
  }

  public String getPhoneNbr()
  {
    return phoneNbr;
  }

  public void setPhoneNbr( String phoneNbr )
  {
    this.phoneNbr = phoneNbr;
  }

  public String getPhoneType()
  {
    return phoneType;
  }

  public void setPhoneType( String phoneType )
  {
    this.phoneType = phoneType;
  }

  public String getPhoneTypeDesc()
  {
    return phoneTypeDesc;
  }

  public void setPhoneTypeDesc( String phoneTypeDesc )
  {
    this.phoneTypeDesc = phoneTypeDesc;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public boolean isPrimary()
  {
    return primary;
  }

  public void setPrimary( boolean primary )
  {
    this.primary = primary;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public String getCurrentPhoneNbr()
  {
    return currentPhoneNbr;
  }

  public void setCurrentPhoneNbr( String currentPhoneNbr )
  {
    this.currentPhoneNbr = currentPhoneNbr;
  }

  public String getVerificationStatus()
  {
    return verificationStatus;
  }

  public void setVerificationStatus( String verificationStatus )
  {
    this.verificationStatus = verificationStatus;
  }

  public String getVerificationStatusDesc()
  {
    return verificationStatusDesc;
  }

  public void setVerificationStatusDesc( String verificationStatusDesc )
  {
    this.verificationStatusDesc = verificationStatusDesc;
  }

}
