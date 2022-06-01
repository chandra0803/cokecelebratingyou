
package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.util.List;

public class PromotionTranslationFormBean implements Serializable
{
  private String promotionName;
  private String overviewDetailsText;
  private String baseUnit;
  private String rulesText;
  private String localeCode;
  private String localeDesc;
  private String promotionObjective;
  private int count;

  private String managerRulesText;
  private String partnerRulesText;
  private List<PromotionTranslationLevelLabelFormBean> levelLabelsList;
  private List<PromotionTranslationPayoutDescriptionFormBean> payoutDescriptionList;
  private List<PromotionTranslationTimePeriodNameFormBean> timePeriodNamesList;
  private List<PromotionTranslationBudgetSegmentNamesFormBean> budgetSegmentNamesList;

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getLocaleCode()
  {
    return localeCode;
  }

  public void setLocaleCode( String localeCode )
  {
    this.localeCode = localeCode;
  }

  public String getLocaleDesc()
  {
    return localeDesc;
  }

  public void setLocaleDesc( String localeDesc )
  {
    this.localeDesc = localeDesc;
  }

  public String getRulesText()
  {
    return rulesText;
  }

  public void setRulesText( String rulesText )
  {
    this.rulesText = rulesText;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

  public String getPromotionObjective()
  {
    return promotionObjective;
  }

  public void setPromotionObjective( String promotionObjective )
  {
    this.promotionObjective = promotionObjective;
  }

  public String getManagerRulesText()
  {
    return managerRulesText;
  }

  public void setManagerRulesText( String managerRulesText )
  {
    this.managerRulesText = managerRulesText;
  }

  public String getPartnerRulesText()
  {
    return partnerRulesText;
  }

  public void setPartnerRulesText( String partnerRulesText )
  {
    this.partnerRulesText = partnerRulesText;
  }

  public String getOverviewDetailsText()
  {
    return overviewDetailsText;
  }

  public void setOverviewDetailsText( String overviewDetailsText )
  {
    this.overviewDetailsText = overviewDetailsText;
  }

  public String getBaseUnit()
  {
    return baseUnit;
  }

  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  public List<PromotionTranslationLevelLabelFormBean> getLevelLabelsList()
  {
    return levelLabelsList;
  }

  public void setLevelLabelsList( List<PromotionTranslationLevelLabelFormBean> levelLabelsList )
  {
    this.levelLabelsList = levelLabelsList;
  }

  public int getLevelLabelsListCount()
  {
    if ( this.levelLabelsList == null )
    {
      return 0;
    }

    return this.levelLabelsList.size();
  }

  public PromotionTranslationLevelLabelFormBean getLevelLabelsList( int index )
  {
    try
    {
      return (PromotionTranslationLevelLabelFormBean)levelLabelsList.get( index );
    }
    catch( Exception exception )
    {
      return null;
    }
  }

  public List<PromotionTranslationTimePeriodNameFormBean> getTimePeriodNamesList()
  {
    return timePeriodNamesList;
  }

  public void setTimePeriodNamesList( List<PromotionTranslationTimePeriodNameFormBean> timePeriodNamesList )
  {
    this.timePeriodNamesList = timePeriodNamesList;
  }

  public int getTimePeriodNamesListCount()
  {
    if ( this.timePeriodNamesList == null )
    {
      return 0;
    }

    return this.timePeriodNamesList.size();
  }

  public PromotionTranslationTimePeriodNameFormBean getTimePeriodNamesList( int index )
  {
    try
    {
      return (PromotionTranslationTimePeriodNameFormBean)timePeriodNamesList.get( index );
    }
    catch( Exception exception )
    {
      return null;
    }
  }

  public List<PromotionTranslationBudgetSegmentNamesFormBean> getBudgetSegmentNamesList()
  {
    return budgetSegmentNamesList;
  }

  public void setBudgetSegmentNamesList( List<PromotionTranslationBudgetSegmentNamesFormBean> budgetSegmentNamesList )
  {
    this.budgetSegmentNamesList = budgetSegmentNamesList;
  }

  public List<PromotionTranslationPayoutDescriptionFormBean> getPayoutDescriptionList()
  {
    return payoutDescriptionList;
  }

  public void setPayoutDescriptionList( List<PromotionTranslationPayoutDescriptionFormBean> payoutDescriptionList )
  {
    this.payoutDescriptionList = payoutDescriptionList;
  }

  public int getPayoutDescriptionListCount()
  {
    if ( this.payoutDescriptionList == null )
    {
      return 0;
    }

    return this.payoutDescriptionList.size();
  }

  public PromotionTranslationPayoutDescriptionFormBean getPayoutDescriptionList( int index )
  {
    try
    {
      return (PromotionTranslationPayoutDescriptionFormBean)payoutDescriptionList.get( index );
    }
    catch( Exception exception )
    {
      return null;
    }
  }

}
