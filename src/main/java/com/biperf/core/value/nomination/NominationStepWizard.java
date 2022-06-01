
package com.biperf.core.value.nomination;

import java.util.Locale;

import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

public enum NominationStepWizard
{

  NOMINATING( 1, "stepNominating", true, "unlocked", ".wizardTabsVertContent .stepNominatingContent", "1", "", "individual_or_team" ), NOMINEEE( 2, "stepNominee", false, "locked",
      ".wizardTabsVertContent .stepNomineeContent", "2", "",
      "nominee" ), BEHAVIOUR( 3, "stepBehavior", false, "locked", ".wizardTabsVertContent .stepBehaviorContent", "3", "", "behaviour" ), ECARD( 4, "stepEcard", false, "locked",
          ".wizardTabsVertContent .stepEcardContent", "4", "", "ecard" ), WHY( 5, "stepWhy", false, "locked", ".wizardTabsVertContent .stepWhyContent", "5", "", "why" );

  private int id;
  private String name;
  private boolean isActive;
  private String state;
  private String contentSel;
  private String wtvNumber;
  private String wtvName;
  private String dbOrderName;

  public static final String LOCKED = "locked";
  public static final String UN_LOCKED = "unlocked";

  private NominationStepWizard( int id, String name, boolean isActive, String state, String contentSel, String wtvNumber, String wtvName, String dbOrderName )
  {
    this.id = id;
    this.name = name;
    this.isActive = isActive;
    this.state = state;
    this.contentSel = contentSel;
    this.wtvNumber = wtvNumber;
    this.wtvName = wtvName;
    this.dbOrderName = dbOrderName;
  }

  public static NominationStepWizard getById( int id )
  {

    NominationStepWizard[] values = values();

    for ( NominationStepWizard wizard : values )
    {
      if ( wizard.getId() == id )
      {
        return wizard;
      }
    }

    return null;
  }

  public static NominationsSubmissionWizardTabValueBean getWhyTabStep()
  {

    NominationsSubmissionWizardTabValueBean whyTab = new NominationsSubmissionWizardTabValueBean();

    whyTab.setId( NominationStepWizard.WHY.getId() );
    whyTab.setName( NominationStepWizard.WHY.getName() );
    whyTab.setActive( NominationStepWizard.WHY.isActive() );
    whyTab.setState( NominationStepWizard.WHY.getState() );
    whyTab.setContentSel( NominationStepWizard.WHY.getContentSel() );
    whyTab.setWtvNumber( NominationStepWizard.WHY.getWtvNumber() );
    whyTab.setWtvName( NominationStepWizard.WHY.getWtvName() );

    return whyTab;
  }

  public static NominationStepWizard getByDBOrderName( String dbOrderName )
  {

    if ( dbOrderName == null )
    {
      return null;
    }

    NominationStepWizard[] values = values();

    for ( NominationStepWizard wizard : values )
    {
      if ( dbOrderName.equalsIgnoreCase( wizard.getDbOrderName() ) )
      {
        return wizard;
      }
    }

    return null;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public boolean isActive()
  {
    return isActive;
  }

  public void setActive( boolean isActive )
  {
    this.isActive = isActive;
  }

  public String getState()
  {
    return state;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  public String getContentSel()
  {
    return contentSel;
  }

  public void setContentSel( String contentSel )
  {
    this.contentSel = contentSel;
  }

  public String getWtvNumber()
  {
    return wtvNumber;
  }

  public void setWtvNumber( String wtvNumber )
  {
    this.wtvNumber = wtvNumber;
  }

  public String getWtvName()
  {
    return buildWTVNameByLocale( this.getName() );
  }

  public void setWtvName( String wtvName )
  {
    this.wtvName = wtvName;
  }

  public String getDbOrderName()
  {
    return dbOrderName;
  }

  public void setDbOrderName( String dbOrderName )
  {
    this.dbOrderName = dbOrderName;
  }

  private String buildWTVNameByLocale( String tabName )
  {
    String wtvName = "";
    Locale locale = UserManager.getLocale();
    Boolean defaultEmpty = Boolean.TRUE;

    switch ( tabName )
    {
      case "stepNominating":
        wtvName = getCMAssetService().getString( "promotion.nomination.submit", "INDIVIDUAL_TEAM_TAB", locale, defaultEmpty );
        break;
      case "stepNominee":
        wtvName = getCMAssetService().getString( "promotion.nomination.submit", "NOMINEE_TAB", locale, defaultEmpty );
        break;
      case "stepBehavior":
        wtvName = getCMAssetService().getString( "promotion.nomination.submit", "BEHAVIOR_TAB", locale, defaultEmpty );
        break;
      case "stepEcard":
        wtvName = getCMAssetService().getString( "promotion.nomination.submit", "ECARD_TAB", locale, defaultEmpty );
        break;
      case "stepWhy":
        wtvName = getCMAssetService().getString( "promotion.nomination.submit", "WHY_TAB", locale, defaultEmpty );
        break;
      default:
        break;
    }

    return wtvName;
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

}
