
package com.biperf.core.value.participant;

public class PhoneNumbers
{
  private String phoneType;
  private String phoneNbr;
  private String countryPhonecode;
  private boolean isPrimary;

  public String getPhoneType()
  {
    return phoneType;
  }

  public void setPhoneType( String phoneType )
  {
    this.phoneType = phoneType;
  }

  public String getPhoneNbr()
  {
    return phoneNbr;
  }

  public void setPhoneNbr( String phoneNbr )
  {
    this.phoneNbr = phoneNbr;
  }

  public String getCountryPhonecode()
  {
    return countryPhonecode;
  }

  public void setCountryPhonecode( String countryPhonecode )
  {
    this.countryPhonecode = countryPhonecode;
  }

  public boolean isPrimary()
  {
    return isPrimary;
  }

  public void setPrimary( boolean isPrimary )
  {
    this.isPrimary = isPrimary;
  }

}
