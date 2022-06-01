/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/security/AuthenticatedUser.java,v $
 */

package com.biperf.core.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserTypeEnum;
import com.objectpartners.cms.provider.CmsUserDetails;

/**
 * AuthenticatedUser.
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
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuthenticatedUser implements UserDetails, CmsUserDetails, Serializable
{

  private static final long serialVersionUID = 3256726164994536240L;

  private Long userId;
  private Long honeycombUserId;
  private String password;
  private String username;
  private String firstName;
  private String middleName;
  private String lastName;
  private Long awardBanQBalance;
  private UserTypeEnum userType;
  private List<GrantedAuthority> authorities;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;
  private Locale locale;
  private String timeZoneId;
  private Set audienceNames;
  private String paxStatus;
  private Date paxTerminationDate;
  private String paxTermsAcceptance;
  private String userIDAcceptedTerms;
  private Date paxTermsAcceptedDate;
  private String sessionId;
  private String primaryCountryCode;
  private Long primaryNodeId;
  private boolean recoveryMethodsCollected;
  private UUID rosterUserId;

  // With Glassfish 2.1 we need to pass the route id along so that both this and CM goes to same
  // server.
  private String routeId;

  private boolean fromSSO;

  private boolean isDelegate;
  private boolean isLaunched;
  private List<Participant> delegateList = new ArrayList<Participant>();
  private AuthenticatedUser originalAuthenticatedUser;

  private boolean giftCodeOnlyPax = false;
  private boolean hasAwardbanqNbrAtLogin = false;

  // Change for bugzilla bug #55298
  private boolean manager;
  private boolean owner;

  private boolean isSSIAdmin;

  private boolean isOptOutOfProgram;

  private boolean optOutOfAward;
  private boolean isProfileSetup = true;

  @Override
  public String toString()
  {
    return String.format( "AuthenticatedUser [userId=%s, username=%s, firstName=%s, middleName=%s, lastName=%s]", userId, username, firstName, middleName, lastName );
  }

  public boolean isGiftCodeOnlyPax()
  {
    return giftCodeOnlyPax;
  }

  public void setGiftCodeOnlyPax( boolean giftCodeOnlyPax )
  {
    this.giftCodeOnlyPax = giftCodeOnlyPax;
  }

  public boolean isHasAwardbanqNbrAtLogin()
  {
    return hasAwardbanqNbrAtLogin;
  }

  public void setHasAwardbanqNbrAtLogin( boolean hasAwardbanqNbrAtLogin )
  {
    this.hasAwardbanqNbrAtLogin = hasAwardbanqNbrAtLogin;
  }

  public String getSessionId()
  {
    return sessionId;
  }

  public void setSessionId( String sessionId )
  {
    this.sessionId = sessionId;
  }

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
   * @return boolean
   */
  public boolean isAccountNonExpired()
  {
    return accountNonExpired;
  }

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
   * @return boolean
   */
  public boolean isAccountNonLocked()
  {
    return accountNonLocked;
  }

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
   * @return GrantedAuthority[]
   */

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
   * @return boolean
   */
  public boolean isCredentialsNonExpired()
  {
    return credentialsNonExpired;
  }

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
   * @return boolean
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.userdetails.UserDetails#getPassword()
   * @return String password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.userdetails.UserDetails#getUsername()
   * @return String username
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * @return value of userId property
   */
  public Long getUserId()
  {
    return userId;
  }

  /**
   * @param userId value for userId property
   */
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getHoneycombUserId()
  {
    return honeycombUserId;
  }

  public void setHoneycombUserId( Long honeycombUserId )
  {
    this.honeycombUserId = honeycombUserId;
  }

  /**
   * @param accountNonExpired value for accountNonExpired property
   */
  public void setAccountNonExpired( boolean accountNonExpired )
  {
    this.accountNonExpired = accountNonExpired;
  }

  /**
   * @param accountNonLocked value for accountNonLocked property
   */
  public void setAccountNonLocked( boolean accountNonLocked )
  {
    this.accountNonLocked = accountNonLocked;
  }

  /**
   * @param authorities value for authorities property
   */
  public void setAuthorities( List<GrantedAuthority> authorities )
  {
    this.authorities = authorities;
  }

  /**
   * @param credentialsNonExpired value for credentialsNonExpired property
   */
  public void setCredentialsNonExpired( boolean credentialsNonExpired )
  {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  /**
   * @param enabled value for enabled property
   */
  public void setEnabled( boolean enabled )
  {
    this.enabled = enabled;
  }

  /**
   * @param password value for password property
   */
  public void setPassword( String password )
  {
    this.password = password;
  }

  /**
   * @param username value for username property
   */
  public void setUsername( String username )
  {
    this.username = username;
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

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public UserTypeEnum getUserType()
  {
    return userType;
  }

  public void setUserType( UserTypeEnum userType )
  {
    this.userType = userType;
  }

  public boolean isParticipant()
  {
    return UserTypeEnum.PARTICIPANT.equals( userType );
  }

  public boolean isUser()
  {
    return UserTypeEnum.USER.equals( userType );
  }

  public Locale getLocale()
  {
    return locale;
  }

  public void setLocale( Locale locale )
  {
    this.locale = locale;
  }

  public String getTimeZoneId()
  {
    return timeZoneId;
  }

  public void setTimeZoneId( String timeZoneId )
  {
    this.timeZoneId = timeZoneId;
  }

  public Set getAudienceNames()
  {
    return audienceNames;
  }

  public void setAudienceNames( Set audienceNames )
  {
    this.audienceNames = audienceNames;
  }

  public Date getPaxTerminationDate()
  {
    return paxTerminationDate;
  }

  public void setPaxTerminationDate( Date paxTerminationDate )
  {
    this.paxTerminationDate = paxTerminationDate;
  }

  public String getPaxStatus()
  {
    return paxStatus;
  }

  public void setPaxStatus( String paxStatus )
  {
    this.paxStatus = paxStatus;
  }

  /**
   * @return true if the participant in inactive otherwise return false
   */
  public boolean isPaxInactive()
  {
    if ( this.getPaxStatus() != null && this.getPaxStatus().equals( ParticipantStatus.INACTIVE ) )
    {
      return true;
    }
    return false;
  }

  /**
   * @return true if the participant should see the T&Cs page otherwise return false
   */
  public boolean isShowTermsAndConditions()
  {
    if ( this.getPaxTermsAcceptance() != null && this.getPaxTermsAcceptance().equals( ParticipantTermsAcceptance.NOTACCEPTED ) )
    {
      return true;
    }
    return false;
  }

  public String getPaxTermsAcceptance()
  {
    return paxTermsAcceptance;
  }

  public void setPaxTermsAcceptance( String paxTermsAcceptance )
  {
    this.paxTermsAcceptance = paxTermsAcceptance;
  }

  public Date getPaxTermsAcceptedDate()
  {
    return paxTermsAcceptedDate;
  }

  public void setPaxTermsAcceptedDate( Date paxTermsAcceptedDate )
  {
    this.paxTermsAcceptedDate = paxTermsAcceptedDate;
  }

  public String getUserIDAcceptedTerms()
  {
    return userIDAcceptedTerms;
  }

  public void setUserIDAcceptedTerms( String userIDAcceptedTerms )
  {
    this.userIDAcceptedTerms = userIDAcceptedTerms;
  }

  public Long getAwardBanQBalance()
  {
    return awardBanQBalance;
  }

  public void setAwardBanQBalance( Long awardBanQBalance )
  {
    this.awardBanQBalance = awardBanQBalance;
  }

  public String getRouteId()
  {
    return routeId;
  }

  public void setRouteId( String routeId )
  {
    this.routeId = routeId;
  }

  public boolean isFromSSO()
  {
    return fromSSO;
  }

  public void setFromSSO( boolean fromSSO )
  {
    this.fromSSO = fromSSO;
  }

  public String getPrimaryCountryCode()
  {
    return primaryCountryCode;
  }

  public void setPrimaryCountryCode( String primaryCountryCode )
  {
    this.primaryCountryCode = primaryCountryCode;
  }

  public boolean isDelegate()
  {
    return isDelegate;
  }

  public void setDelegate( boolean isDelegate )
  {
    this.isDelegate = isDelegate;
  }

  public boolean isLaunched()
  {
    return isLaunched;
  }

  public void setLaunched( boolean isLaunched )
  {
    this.isLaunched = isLaunched;
  }

  public List<Participant> getDelegateList()
  {
    return delegateList;
  }

  public void setDelegateList( List<Participant> delegateList )
  {
    this.delegateList = delegateList;
  }

  public AuthenticatedUser getOriginalAuthenticatedUser()
  {
    return originalAuthenticatedUser;
  }

  public void setOriginalAuthenticatedUser( AuthenticatedUser originalAuthenticatedUser )
  {
    this.originalAuthenticatedUser = originalAuthenticatedUser;
  }

  public boolean isManager()
  {
    return manager;
  }

  public void setManager( boolean manager )
  {
    this.manager = manager;
  }

  public boolean isOwner()
  {
    return owner;
  }

  public void setOwner( boolean owner )
  {
    this.owner = owner;
  }

  @Override
  public List<GrantedAuthority> getAuthorities()
  {
    return authorities;
  }

  public boolean isSSIAdmin()
  {
    return isSSIAdmin;
  }

  public void setSSIAdmin( boolean isSSIAdmin )
  {
    this.isSSIAdmin = isSSIAdmin;
  }

  public Long getPrimaryNodeId()
  {
    return primaryNodeId;
  }

  public void setPrimaryNodeId( Long primaryNodeId )
  {
    this.primaryNodeId = primaryNodeId;
  }

  public boolean isOptOutOfProgram()
  {
    return isOptOutOfProgram;
  }

  public void setOptOutOfProgram( boolean isOptOutOfProgram )
  {
    this.isOptOutOfProgram = isOptOutOfProgram;
  }

  public boolean isOptOutOfAward()
  {
    return optOutOfAward;
  }

  public void setOptOutOfAward( boolean optOutOfAward )
  {
    this.optOutOfAward = optOutOfAward;
  }

  public boolean isRecoveryMethodsCollected()
  {
    return recoveryMethodsCollected;
  }

  public void setRecoveryMethodsCollected( boolean recoveryMethodsCollected )
  {
    this.recoveryMethodsCollected = recoveryMethodsCollected;
  }

  public boolean isProfileSetup()
  {
    return isProfileSetup;
  }

  public void setProfileSetup( boolean isProfileSetup )
  {
    this.isProfileSetup = isProfileSetup;
  }

  public UUID getRosterUserId()
  {
    return rosterUserId;
  }

  public void setRosterUserId( UUID rosterUserId )
  {
    this.rosterUserId = rosterUserId;
  }

}
