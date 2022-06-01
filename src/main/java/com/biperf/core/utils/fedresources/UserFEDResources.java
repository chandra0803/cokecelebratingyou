
package com.biperf.core.utils.fedresources;

import java.io.Serializable;

public class UserFEDResources implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean productClaimsEnabled = false;
  private boolean goalquestEnabled = false;
  private boolean engagementEnabled = false;
  private boolean diyQuizesEnabled = false;
  private boolean instantPollsEnabled = false;
  private boolean nominationsEnabled = false;
  private boolean quizesEnabled = false;
  private boolean recognitionsEnabled = false;
  private boolean ssiEnabled = false;
  private boolean surveysEnabled = false;
  private boolean throwdownEnabled = false;
  private boolean reportsEnabled = false;
  private boolean diyCommunicationsEnabled = false;

  public boolean isProductClaimsEnabled()
  {
    return productClaimsEnabled;
  }

  public void setProductClaimsEnabled( boolean productClaimsEnabled )
  {
    this.productClaimsEnabled = productClaimsEnabled;
  }

  public boolean isGoalquestEnabled()
  {
    return goalquestEnabled;
  }

  public void setGoalquestEnabled( boolean goalquestEnabled )
  {
    this.goalquestEnabled = goalquestEnabled;
  }

  public boolean isEngagementEnabled()
  {
    return engagementEnabled;
  }

  public void setEngagementEnabled( boolean engagementEnabled )
  {
    this.engagementEnabled = engagementEnabled;
  }

  public boolean isDiyQuizesEnabled()
  {
    return diyQuizesEnabled;
  }

  public void setDiyQuizesEnabled( boolean diyQuizesEnabled )
  {
    this.diyQuizesEnabled = diyQuizesEnabled;
  }

  public boolean isInstantPollsEnabled()
  {
    return instantPollsEnabled;
  }

  public void setInstantPollsEnabled( boolean instantPollsEnabled )
  {
    this.instantPollsEnabled = instantPollsEnabled;
  }

  public boolean isNominationsEnabled()
  {
    return nominationsEnabled;
  }

  public void setNominationsEnabled( boolean nominationsEnabled )
  {
    this.nominationsEnabled = nominationsEnabled;
  }

  public boolean isQuizesEnabled()
  {
    return quizesEnabled;
  }

  public void setQuizesEnabled( boolean quizesEnabled )
  {
    this.quizesEnabled = quizesEnabled;
  }

  public boolean isRecognitionsEnabled()
  {
    return recognitionsEnabled;
  }

  public void setRecognitionsEnabled( boolean recognitionsEnabled )
  {
    this.recognitionsEnabled = recognitionsEnabled;
  }

  public boolean isSsiEnabled()
  {
    return ssiEnabled;
  }

  public void setSsiEnabled( boolean ssiEnabled )
  {
    this.ssiEnabled = ssiEnabled;
  }

  public boolean isSurveysEnabled()
  {
    return surveysEnabled;
  }

  public void setSurveysEnabled( boolean surveysEnabled )
  {
    this.surveysEnabled = surveysEnabled;
  }

  public boolean isThrowdownEnabled()
  {
    return throwdownEnabled;
  }

  public void setThrowdownEnabled( boolean throwdownEnabled )
  {
    this.throwdownEnabled = throwdownEnabled;
  }

  public boolean isReportsEnabled()
  {
    return reportsEnabled;
  }

  public void setReportsEnabled( boolean reportsEnabled )
  {
    this.reportsEnabled = reportsEnabled;
  }

  public boolean isDiyCommunicationsEnabled()
  {
    return diyCommunicationsEnabled;
  }

  public void setDiyCommunicationsEnabled( boolean diyCommunicationsEnabled )
  {
    this.diyCommunicationsEnabled = diyCommunicationsEnabled;
  }

  @Override
  public String toString()
  {
    return "UserFEDResources [productClaimsEnabled=" + productClaimsEnabled + ", goalquestEnabled=" + goalquestEnabled + ", engagementEnabled=" + engagementEnabled + ", diyQuizesEnabled="
        + diyQuizesEnabled + ", instantPollsEnabled=" + instantPollsEnabled + ", nominationsEnabled=" + nominationsEnabled + ", quizesEnabled=" + quizesEnabled + ", recognitionsEnabled="
        + recognitionsEnabled + ", ssiEnabled=" + ssiEnabled + ", surveysEnabled=" + surveysEnabled + ", throwdownEnabled=" + throwdownEnabled + ", reportsEnabled=" + reportsEnabled
        + ", diyCommunicationsEnabled=" + diyCommunicationsEnabled + "]";
  }

  public static UserFEDResources enableAllModules()
  {

    UserFEDResources resource = new UserFEDResources();

    resource.setDiyCommunicationsEnabled( true );
    resource.setDiyQuizesEnabled( true );
    resource.setEngagementEnabled( true );
    resource.setGoalquestEnabled( true );
    resource.setInstantPollsEnabled( true );
    resource.setNominationsEnabled( true );
    resource.setProductClaimsEnabled( true );
    resource.setQuizesEnabled( true );
    resource.setRecognitionsEnabled( true );
    resource.setReportsEnabled( true );
    resource.setSsiEnabled( true );
    resource.setSurveysEnabled( true );
    resource.setThrowdownEnabled( true );

    return resource;

  }

}
