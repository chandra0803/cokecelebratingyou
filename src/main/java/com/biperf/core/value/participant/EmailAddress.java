
package com.biperf.core.value.participant;

public class EmailAddress
{
  private String emailType;
  private String emailAddress;
  private boolean isPrimary;

  public String getEmailType()
  {
    return emailType;
  }

  public void setEmailType( String emailType )
  {
    this.emailType = emailType;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
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
