
package com.biperf.core.ui.mobilerecogapp.view;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class ContributionsTAndCView implements Serializable
{

  StringBuffer termsAndConditions;
  private String celebrationLink;

  public StringBuffer getTermsAndConditions()
  {
    return termsAndConditions;
  }

  public void setTermsAndConditions( StringBuffer termsAndConditions )
  {
    this.termsAndConditions = termsAndConditions;
  }

  public String getCelebrationLink()
  {
    return celebrationLink;
  }

  public void setCelebrationLink( String celebrationLink )
  {
    this.celebrationLink = celebrationLink;
  }

}
