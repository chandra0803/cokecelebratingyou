
package com.biperf.core.ui.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.exception.ExceptionView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class UserActivationView extends ExceptionView implements Serializable
{
  private boolean exists = false;
  private boolean userActivated = false;
  private List<PaxContactType> contactInfo = new ArrayList<PaxContactType>();
  private List<ActivationField> activationFields = new ArrayList<ActivationField>();
  private List<CountryPhoneView> countryPhones = new ArrayList<CountryPhoneView>();
  private boolean nonPax; // Not a participant (i.e. an admin) - they skip activation attributes

  public boolean isExists()
  {
    return exists;
  }

  public void setExists( boolean exists )
  {
    this.exists = exists;
  }

  public boolean isUserActivated()
  {
    return userActivated;
  }

  public void setUserActivated( boolean userActivated )
  {
    this.userActivated = userActivated;
  }

  public List<PaxContactType> getContactMethods()
  {
    return contactInfo;
  }

  public void setContactMethods( List<PaxContactType> contactInfo )
  {
    this.contactInfo = contactInfo;
  }

  public List<ActivationField> getActivationFields()
  {
    return activationFields;
  }

  public void setActivationFields( List<ActivationField> activationFields )
  {
    this.activationFields = activationFields;
  }

  public List<CountryPhoneView> getCountryPhones()
  {
    return countryPhones;
  }

  public void setCountryPhones( List<CountryPhoneView> countryPhones )
  {
    this.countryPhones = countryPhones;
  }

  @Override
  public String toString()
  {
    return "UserActivationView [exists=" + exists + ", active=" + userActivated + ", contactMethods=" + contactInfo + "]";
  }

  public boolean isNonPax()
  {
    return nonPax;
  }

  public void setNonPax( boolean nonPax )
  {
    this.nonPax = nonPax;
  }
}
