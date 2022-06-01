
package com.biperf.core.ui.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.exception.ExceptionView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PaxPasswordResetView
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
 * <td>mattam</td>
 * <td>June 02, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 *
 *
 */

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class PaxPasswordResetView extends ExceptionView implements Serializable
{
  @JsonProperty( "contactExists" )
  private boolean contactExists;
  @JsonProperty( "emailExists" )
  private boolean emailExists;
  @JsonProperty( "userExists" )
  private boolean userExists;
  @JsonProperty( "unique" )
  private boolean unique;
  @JsonProperty( "single" )
  private boolean single;
  @JsonProperty( "validToken" )
  private boolean validToken;
  @JsonProperty( "tokenAlreadyUsed" )
  private boolean tokenAlreadyUsed;
  @JsonProperty( "passwordResetSuccess" )
  private boolean passwordResetSuccess;
  @JsonProperty( "paxIDs" )
  private Set<Long> paxIDs = new HashSet<Long>();
  @JsonProperty( "userName" )
  private String userName;
  @JsonProperty( "contactMethods" )
  private List<PaxContactType> contactMethods = new ArrayList<PaxContactType>();
  @JsonProperty( "totalResults" )
  private int totalResults;
  @JsonProperty( "showAutocomplete" )
  private boolean showAutocomplete;
  @JsonProperty( "nonPax" )
  private boolean nonPax; // Not a participant (i.e. an admin) - they skip activation attributes

  private String contactValue;
  private List<ActivationField> activationFields = new ArrayList<ActivationField>();
  private boolean accountLocked = false;
  private boolean userActivated = false;
  
  public boolean isContactExists()
  {
    return contactExists;
  }

  public void setContactExists( boolean contactExists )
  {
    this.contactExists = contactExists;
  }

  public boolean isEmailExists()
  {
    return emailExists;
  }

  public void setEmailExists( boolean emailExists )
  {
    this.emailExists = emailExists;
  }

  public boolean isSingle()
  {
    return single;
  }

  public void setSingle( boolean single )
  {
    this.single = single;
  }

  public boolean isUnique()
  {
    return unique;
  }

  public void setUnique( boolean unique )
  {
    this.unique = unique;
  }

  public List<PaxContactType> getContactMethods()
  {
    return contactMethods;
  }

  public void setContactMethods( List<PaxContactType> contactMethods )
  {
    this.contactMethods = contactMethods;
  }

  public Set<Long> getPaxIDs()
  {
    return paxIDs;
  }

  public void setPaxIDs( Set<Long> paxIDs )
  {
    this.paxIDs = paxIDs;
  }

  public boolean isValidToken()
  {
    return validToken;
  }
 
  public boolean isUserActivated()
  {
    return userActivated;
  }

  public void setUserActivated( boolean userActivated )
  {
    this.userActivated = userActivated;
  }

  public void setValidToken( boolean validToken )
  {
    this.validToken = validToken;
  }

  public boolean isUserExists()
  {
    return userExists;
  }

  public void setUserExists( boolean userExists )
  {
    this.userExists = userExists;
  }

  public boolean isPasswordResetSuccess()
  {
    return passwordResetSuccess;
  }

  public void setPasswordResetSuccess( boolean passwordResetSuccess )
  {
    this.passwordResetSuccess = passwordResetSuccess;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public List<ActivationField> getActivationFields()
  {
    return activationFields;
  }

  public void setActivationFields( List<ActivationField> activationFields )
  {
    this.activationFields = activationFields;
  }

  public boolean isTokenAlreadyUsed()
  {
    return tokenAlreadyUsed;
  }

  public void setTokenAlreadyUsed( boolean tokenAlreadyUsed )
  {
    this.tokenAlreadyUsed = tokenAlreadyUsed;
  }

  public int getTotalResults()
  {
    return totalResults;
  }

  public void setTotalResults( int totalResults )
  {
    this.totalResults = totalResults;
  }

  public boolean isShowAutocomplete()
  {
    return showAutocomplete;
  }

  public void setShowAutocomplete( boolean showAutocomplete )
  {
    this.showAutocomplete = showAutocomplete;
  }
  
  public String getContactValue()
  {
    return contactValue;
  }

  public void setContactValue( String contactValue )
  {
    this.contactValue = contactValue;
  }

  public boolean isNonPax()
  {
    return nonPax;
  }

  public void setNonPax( boolean nonPax )
  {
    this.nonPax = nonPax;
  }

  public boolean isAccountLocked()
  {
    return accountLocked;
  }

  public void setAccountLocked( boolean accountLocked )
  {
    this.accountLocked = accountLocked;
  }
}
