/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.SAO;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * PromotionTranslationsForm.
 * 
 * @author arasi
 * @since 16-Aug-2012
 * @version 1.0
 */
public class PromotionTranslationsForm extends BaseForm
{
  public static final String SESSION_KEY = "promotionTranslationsForm";

  private String promotionId;
  private String promotionName;
  private String promotionObjective;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private boolean webRulesActive;
  private String webRulesText;
  private boolean quizPromotion;
  private boolean goalQuestOrChallengePointPromotion;
  private Long version;
  private boolean expired;
  private String method;
  private String overviewDetailsText;

  private String managerWebRulesText;
  private String partnerWebRulesText = null;
  private boolean partnerAvailable;
  private String baseUnit;
  private List<PromotionTranslationLevelLabelFormBean> levelLabelsList = new ArrayList<PromotionTranslationLevelLabelFormBean>();
  private List<PromotionTranslationPayoutDescriptionFormBean> payoutDescriptionList = new ArrayList<PromotionTranslationPayoutDescriptionFormBean>();
  private List<PromotionTranslationTimePeriodNameFormBean> timePeriodNamesList = new ArrayList<PromotionTranslationTimePeriodNameFormBean>();
  private List<PromotionTranslationBudgetSegmentNamesFormBean> budgetSegmentNamesList = new ArrayList<PromotionTranslationBudgetSegmentNamesFormBean>();
  private List<PromotionTranslationFormBean> translationsTextList;

  private List<PromotionTranslationGoalAndPayoutFormBean> translationsGoalNameAndDescriptionList;

  private List<PromotionTranslationDivisionFormBean> translationsDivisionList;

  public void load( Promotion promotion )
  {
    this.promotionId = promotion.getId().toString();
    this.promotionName = promotion.getName();
    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;

      if ( promotion.getBudgetMaster() != null )
      {
        for ( BudgetSegment budgetSegment : promotion.getBudgetMaster().getBudgetSegments() )
        {
          PromotionTranslationBudgetSegmentNamesFormBean promotionTranslationBudgetSegmentNamesFormBean = new PromotionTranslationBudgetSegmentNamesFormBean();
          promotionTranslationBudgetSegmentNamesFormBean.setBudgetTimePeriodName( budgetSegment.getName() );
          this.budgetSegmentNamesList.add( promotionTranslationBudgetSegmentNamesFormBean );
        }
      }
      if ( promotion.getCashBudgetMaster() != null )
      {
        for ( BudgetSegment cashBudgetSegment : promotion.getCashBudgetMaster().getBudgetSegments() )
        {
          PromotionTranslationBudgetSegmentNamesFormBean promotionTranslationBudgetSegmentNamesFormBean = new PromotionTranslationBudgetSegmentNamesFormBean();
          promotionTranslationBudgetSegmentNamesFormBean.setBudgetTimePeriodName( cashBudgetSegment.getName() );
          this.budgetSegmentNamesList.add( promotionTranslationBudgetSegmentNamesFormBean );
        }
      }

      for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
      {
        PromotionTranslationLevelLabelFormBean promotionTranslationLevelLabelFormBean = new PromotionTranslationLevelLabelFormBean();
        promotionTranslationLevelLabelFormBean.setLevelIndex( nominationPromotionLevel.getLevelIndex() );
        promotionTranslationLevelLabelFormBean.setLevelLabel( nominationPromotionLevel.getLevelLabel() );
        this.levelLabelsList.add( promotionTranslationLevelLabelFormBean );

        if ( nominationPromotionLevel.getAwardPayoutType().isOtherAwardType() )
        {
          PromotionTranslationPayoutDescriptionFormBean promotionTranslationPayoutDescriptionFormBean = new PromotionTranslationPayoutDescriptionFormBean();
          promotionTranslationPayoutDescriptionFormBean.setLevelIndex( nominationPromotionLevel.getLevelIndex() );
          promotionTranslationPayoutDescriptionFormBean.setPayoutDescription( nominationPromotionLevel.getPayoutDescription() );
          this.payoutDescriptionList.add( promotionTranslationPayoutDescriptionFormBean );
        }
      }

      for ( NominationPromotionTimePeriod nominationPromotionTimePeriod : nominationPromotion.getNominationTimePeriods() )
      {
        PromotionTranslationTimePeriodNameFormBean promotionTranslationTimePeriodNameFormBean = new PromotionTranslationTimePeriodNameFormBean();
        promotionTranslationTimePeriodNameFormBean.setTimePeriodName( nominationPromotionTimePeriod.getTimePeriodName() );
        this.timePeriodNamesList.add( promotionTranslationTimePeriodNameFormBean );
      }
    }
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.webRulesActive = promotion.isWebRulesActive();
    this.version = promotion.getVersion();
    this.expired = promotion.isExpired();
    this.quizPromotion = promotion.isQuizPromotion();
    this.goalQuestOrChallengePointPromotion = promotion.isGoalQuestOrChallengePointPromotion();
    if ( this.quizPromotion )
    {
      this.overviewDetailsText = promotion.getOverviewDetailsText();
    }
    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      this.promotionObjective = ( (GoalQuestPromotion)promotion ).getObjective();
      this.overviewDetailsText = promotion.getOverviewDetailsText();
    }
    if ( promotion.isThrowdownPromotion() )
    {
      this.overviewDetailsText = promotion.getOverviewDetailsText();
      this.baseUnit = ( (ThrowdownPromotion)promotion ).getBaseUnitText();
    }

    if ( promotion.getCmAssetCode() != null && promotion.getWebRulesCmKey() != null )
    {
      this.webRulesText = CmsResourceBundle.getCmsBundle().getString( promotion.getCmAssetCode(), promotion.getWebRulesCmKey() );
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      if ( ( (GoalQuestPromotion)promotion ).getManagerCmAssetCode() != null && ( (GoalQuestPromotion)promotion ).getManagerWebRulesCmKey() != null )
      {
        this.managerWebRulesText = CmsResourceBundle.getCmsBundle().getString( ( (GoalQuestPromotion)promotion ).getManagerCmAssetCode(), ( (GoalQuestPromotion)promotion ).getManagerWebRulesCmKey() );
      }
      if ( promotion.getPartnerAudienceType() != null )
      {
        this.partnerAvailable = isPartnerAvailable( promotion );
        if ( ( (GoalQuestPromotion)promotion ).getPartnerCmAssetCode() != null && ( (GoalQuestPromotion)promotion ).getPartnerWebRulesCmKey() != null )
        {
          this.partnerWebRulesText = CmsResourceBundle.getCmsBundle().getString( ( (GoalQuestPromotion)promotion ).getPartnerCmAssetCode(),
                                                                                 ( (GoalQuestPromotion)promotion ).getPartnerWebRulesCmKey() );
        }
      }
    }

    List localeItems = getCMAssetService().getSupportedLocales( true );

    int count = 0;
    for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      if ( content != null )
      {
        PromotionTranslationFormBean bean = new PromotionTranslationFormBean();
        String localeCode = (String)content.getContentDataMap().get( "CODE" );
        Locale locale = CmsUtil.getLocale( localeCode );
        String localeDesc = (String)content.getContentDataMap().get( "DESC" );

        String translatedRulesText = "";
        if ( promotion.isWebRulesActive() && promotion.getCmAssetCode() != null && promotion.getWebRulesCmKey() != null )
        {
          translatedRulesText = getCMAssetService().getString( promotion.getCmAssetCode(), promotion.getWebRulesCmKey(), locale, true );
        }
        String translatedOverViewText = "";
        if ( promotion.isQuizPromotion() )
        {
          translatedOverViewText = getCMAssetService().getString( promotion.getOverview(), Promotion.PROMO_OVERVIEW_CM_ASSET_TYPE_KEY, locale, true );
        }

        String promotionName = getCMAssetService().getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );

        List<PromotionTranslationLevelLabelFormBean> levelLabelsList = new ArrayList<PromotionTranslationLevelLabelFormBean>();
        List<PromotionTranslationPayoutDescriptionFormBean> payoutDescriptionList = new ArrayList<PromotionTranslationPayoutDescriptionFormBean>();
        List<PromotionTranslationTimePeriodNameFormBean> timePeriodNamesList = new ArrayList<PromotionTranslationTimePeriodNameFormBean>();
        List<PromotionTranslationBudgetSegmentNamesFormBean> budgetSegmentNamesList = new ArrayList<PromotionTranslationBudgetSegmentNamesFormBean>();

        if ( promotion.isNominationPromotion() )
        {
          NominationPromotion nominationPromotion = (NominationPromotion)promotion;

          if ( promotion.getBudgetMaster() != null )
          {
            for ( BudgetSegment budgetSegment : promotion.getBudgetMaster().getBudgetSegments() )
            {
              PromotionTranslationBudgetSegmentNamesFormBean promotionTranslationBudgetSegmentNamesFormBean = new PromotionTranslationBudgetSegmentNamesFormBean();
              promotionTranslationBudgetSegmentNamesFormBean
                  .setBudgetTimePeriodName( getCMAssetService().getString( budgetSegment.getCmAssetCode(), BudgetSegment.BUDGET_PERIOD_NAME_KEY, locale, true ) );
              budgetSegmentNamesList.add( promotionTranslationBudgetSegmentNamesFormBean );
            }
          }
          if ( promotion.getCashBudgetMaster() != null )
          {
            for ( BudgetSegment cashBudgetSegment : promotion.getCashBudgetMaster().getBudgetSegments() )
            {
              PromotionTranslationBudgetSegmentNamesFormBean promotionTranslationBudgetSegmentNamesFormBean = new PromotionTranslationBudgetSegmentNamesFormBean();
              promotionTranslationBudgetSegmentNamesFormBean
                  .setBudgetTimePeriodName( getCMAssetService().getString( cashBudgetSegment.getCmAssetCode(), BudgetSegment.BUDGET_PERIOD_NAME_KEY, locale, true ) );
              budgetSegmentNamesList.add( promotionTranslationBudgetSegmentNamesFormBean );
            }
          }

          for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
          {
            PromotionTranslationLevelLabelFormBean promotionTranslationLevelLabelFormBean = new PromotionTranslationLevelLabelFormBean();
            promotionTranslationLevelLabelFormBean.setLevelIndex( nominationPromotionLevel.getLevelIndex() );
            promotionTranslationLevelLabelFormBean
                .setLevelLabel( getCMAssetService().getString( nominationPromotionLevel.getLevelLabelAssetCode(), Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX, locale, true ) );
            levelLabelsList.add( promotionTranslationLevelLabelFormBean );

            if ( nominationPromotionLevel.getAwardPayoutType().isOtherAwardType() )
            {
              PromotionTranslationPayoutDescriptionFormBean promotionTranslationPayoutDescriptionFormBean = new PromotionTranslationPayoutDescriptionFormBean();
              promotionTranslationPayoutDescriptionFormBean.setLevelIndex( nominationPromotionLevel.getLevelIndex() );
              promotionTranslationPayoutDescriptionFormBean
                  .setPayoutDescription( getCMAssetService().getString( nominationPromotionLevel.getPayoutDescriptionAssetCode(), Promotion.PAYOUT_DESCRIPTION_KEY_PREFIX, locale, true ) );
              payoutDescriptionList.add( promotionTranslationPayoutDescriptionFormBean );
            }

          }

          for ( NominationPromotionTimePeriod nominationPromotionTimePeriod : nominationPromotion.getNominationTimePeriods() )
          {
            PromotionTranslationTimePeriodNameFormBean promotionTranslationTimePeriodNameFormBean = new PromotionTranslationTimePeriodNameFormBean();
            promotionTranslationTimePeriodNameFormBean
                .setTimePeriodName( getCMAssetService().getString( nominationPromotionTimePeriod.getTimePeriodNameAssetCode(), Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX, locale, true ) );
            timePeriodNamesList.add( promotionTranslationTimePeriodNameFormBean );
          }
        }

        String promotionObjective = "";
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          promotionObjective = getCMAssetService().getString( ( (GoalQuestPromotion)promotion ).getObjectiveAssetCode(), Promotion.GQ_CP_PROMO_OBJECTIVE_KEY_PREFIX, locale, true );

          translatedOverViewText = getCMAssetService().getString( promotion.getOverview(), Promotion.PROMO_OVERVIEW_CM_ASSET_TYPE_KEY, locale, true );
        }

        String translatedManagerRulesText = "";
        String translatedPartnerRulesText = "";
        if ( promotion.isWebRulesActive() )
        {
          if ( promotion.isGoalQuestOrChallengePointPromotion() && ( (GoalQuestPromotion)promotion ).getManagerCmAssetCode() != null
              && ( (GoalQuestPromotion)promotion ).getManagerWebRulesCmKey() != null )
          {
            translatedManagerRulesText = getCMAssetService().getString( ( (GoalQuestPromotion)promotion ).getManagerCmAssetCode(),
                                                                        ( (GoalQuestPromotion)promotion ).getManagerWebRulesCmKey(),
                                                                        locale,
                                                                        true );
          }

          if ( promotion.isGoalQuestOrChallengePointPromotion() && ( (GoalQuestPromotion)promotion ).getPartnerCmAssetCode() != null
              && ( (GoalQuestPromotion)promotion ).getPartnerWebRulesCmKey() != null )
          {
            translatedPartnerRulesText = getCMAssetService().getString( ( (GoalQuestPromotion)promotion ).getPartnerCmAssetCode(),
                                                                        ( (GoalQuestPromotion)promotion ).getPartnerWebRulesCmKey(),
                                                                        locale,
                                                                        true );
          }
        }
        String baseUnit = "";
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          baseUnit = getCMAssetService().getString( ( (GoalQuestPromotion)promotion ).getBaseUnit(), Promotion.GQ_CP_PROMO_BASE_UNIT_KEY_PREFIX, locale, true );
        }
        if ( promotion.isThrowdownPromotion() )
        {
          translatedOverViewText = getCMAssetService().getString( promotion.getOverview(), Promotion.PROMO_OVERVIEW_CM_ASSET_TYPE_KEY, locale, true );
          baseUnit = getCMAssetService().getString( ( (ThrowdownPromotion)promotion ).getBaseUnit(), Promotion.TD_PROMO_BASE_UNIT_KEY_PREFIX, locale, true );
        }

        if ( !ContentReaderManager.getCurrentLocale().equals( locale ) )
        {
          bean.setLocaleCode( localeCode );
          bean.setLocaleDesc( localeDesc );
          bean.setPromotionName( promotionName );
          bean.setLevelLabelsList( levelLabelsList );
          bean.setPayoutDescriptionList( payoutDescriptionList );
          bean.setTimePeriodNamesList( timePeriodNamesList );
          bean.setBudgetSegmentNamesList( budgetSegmentNamesList );
          bean.setPromotionObjective( promotionObjective );
          bean.setRulesText( translatedRulesText );
          bean.setOverviewDetailsText( translatedOverViewText );
          bean.setManagerRulesText( translatedManagerRulesText );
          bean.setPartnerRulesText( translatedPartnerRulesText );
          bean.setBaseUnit( baseUnit );
          bean.setCount( count );
          count = count + 1;
          translationsTextList.add( bean );
        }
      }
    }
    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
      {
        Content content = (Content)iter.next();
        if ( content != null )
        {
          String localeCode = (String)content.getContentDataMap().get( "CODE" );
          Locale locale = CmsUtil.getLocale( localeCode );
          Set<AbstractGoalLevel> goalLevels = ( (GoalQuestPromotion)promotion ).getGoalLevels();
          if ( !ContentReaderManager.getCurrentLocale().equals( locale ) )
          {
            for ( Iterator<AbstractGoalLevel> goalLevelIter = goalLevels.iterator(); goalLevelIter.hasNext(); )
            {
              GoalLevel currentGoalLevel = (GoalLevel)goalLevelIter.next();
              PromotionTranslationGoalAndPayoutFormBean goalAndPayoutTranslationFormBean = new PromotionTranslationGoalAndPayoutFormBean();
              goalAndPayoutTranslationFormBean.setLocaleCode( localeCode );
              goalAndPayoutTranslationFormBean.setSequenceNumber( currentGoalLevel.getSequenceNumber() );
              goalAndPayoutTranslationFormBean.setGoalLevelId( currentGoalLevel.getId() );
              goalAndPayoutTranslationFormBean.setName( getCMAssetService().getString( currentGoalLevel.getGoalLevelcmAssetCode(), Promotion.CM_GOALS_KEY, locale, true ) );
              goalAndPayoutTranslationFormBean.setDescription( getCMAssetService().getString( currentGoalLevel.getGoalLevelcmAssetCode(), Promotion.CM_GOAL_DESCRIPTION_KEY, locale, true ) );

              goalAndPayoutTranslationFormBean.setNameKey( currentGoalLevel.getGoalLevelNameKey() );
              goalAndPayoutTranslationFormBean.setDescriptionKey( currentGoalLevel.getGoalLevelDescriptionKey() );
              goalAndPayoutTranslationFormBean.setGoalLevelcmAssetCode( currentGoalLevel.getGoalLevelcmAssetCode() );
              translationsGoalNameAndDescriptionList.add( goalAndPayoutTranslationFormBean );
            }
          }
        }
      }
    }

    localeItems = getCMAssetService().getSupportedLocales( false );

    if ( promotion.isThrowdownPromotion() )
    {
      for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
      {
        Content content = (Content)iter.next();
        if ( content != null )
        {
          String localeCode = (String)content.getContentDataMap().get( "CODE" );
          Locale locale = CmsUtil.getLocale( localeCode );
          Set<Division> divisions = ( (ThrowdownPromotion)promotion ).getDivisions();
          for ( Iterator<Division> divisionsIter = divisions.iterator(); divisionsIter.hasNext(); )
          {
            Division division = divisionsIter.next();
            PromotionTranslationDivisionFormBean divisionTranslationFormBean = new PromotionTranslationDivisionFormBean();
            divisionTranslationFormBean.setLocaleCode( localeCode );
            divisionTranslationFormBean.setDivisionId( division.getId() );
            divisionTranslationFormBean.setName( getCMAssetService().getString( division.getDivisionNameAssetCode(), Division.DIVISION_NAME_KEY_PREFIX, locale, true ) );
            divisionTranslationFormBean.setNameKey( Division.DIVISION_NAME_KEY_PREFIX );
            divisionTranslationFormBean.setDivisionNameCmAssetCode( division.getDivisionNameAssetCode() );
            translationsDivisionList.add( divisionTranslationFormBean );
          }
        }
      }
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // PromotionTranslationFormBeans. If this is not done, the form wont initialize
    // properly.
    translationsTextList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "translationListCount" ),
                                              RequestUtils.getOptionalParamInt( request, "levelLabelsListCount" ),
                                              RequestUtils.getOptionalParamInt( request, "payoutDescriptionListCount" ),
                                              RequestUtils.getOptionalParamInt( request, "timePeriodNamesListCount" ),
                                              RequestUtils.getOptionalParamInt( request, "budgetSegmentNamesListCount" ) );
    translationsGoalNameAndDescriptionList = getEmptyPayoutValueList( RequestUtils.getOptionalParamInt( request, "translationsPayoutListCount" ) );
    translationsDivisionList = getEmptyDivisionValueList( RequestUtils.getOptionalParamInt( request, "translationsDivisionListCount" ) );
  }

  private List getEmptyValueList( int valueListCount, int labelListCount, int payoutDescriptionListCount, int timePeriodNamesListCount, int budgetSegmentNamesListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionTranslationFormBean
      PromotionTranslationFormBean translationBean = new PromotionTranslationFormBean();
      List<PromotionTranslationLevelLabelFormBean> levelLabelsList = new ArrayList<PromotionTranslationLevelLabelFormBean>();
      List<PromotionTranslationPayoutDescriptionFormBean> payoutDescriptionList = new ArrayList<PromotionTranslationPayoutDescriptionFormBean>();
      List<PromotionTranslationTimePeriodNameFormBean> timePeriodNamesList = new ArrayList<PromotionTranslationTimePeriodNameFormBean>();
      List<PromotionTranslationBudgetSegmentNamesFormBean> budgetSegmentNamesList = new ArrayList<PromotionTranslationBudgetSegmentNamesFormBean>();

      for ( int j = 0; j < labelListCount; j++ )
      {
        PromotionTranslationLevelLabelFormBean promotionTranslationLevelLabelFormBean = new PromotionTranslationLevelLabelFormBean();
        levelLabelsList.add( promotionTranslationLevelLabelFormBean );
      }
      translationBean.setLevelLabelsList( levelLabelsList );

      for ( int j = 0; j < payoutDescriptionListCount; j++ )
      {
        PromotionTranslationPayoutDescriptionFormBean promotionTranslationPayoutDescriptionFormBean = new PromotionTranslationPayoutDescriptionFormBean();
        payoutDescriptionList.add( promotionTranslationPayoutDescriptionFormBean );
      }
      translationBean.setPayoutDescriptionList( payoutDescriptionList );

      for ( int k = 0; k < timePeriodNamesListCount; k++ )
      {
        PromotionTranslationTimePeriodNameFormBean promotionTranslationTimePeriodNameFormBean = new PromotionTranslationTimePeriodNameFormBean();
        timePeriodNamesList.add( promotionTranslationTimePeriodNameFormBean );
      }
      translationBean.setTimePeriodNamesList( timePeriodNamesList );

      for ( int b = 0; b < budgetSegmentNamesListCount; b++ )
      {
        PromotionTranslationBudgetSegmentNamesFormBean promotionTranslationBudgetSegmentNamesFormBean = new PromotionTranslationBudgetSegmentNamesFormBean();
        budgetSegmentNamesList.add( promotionTranslationBudgetSegmentNamesFormBean );
      }
      translationBean.setBudgetSegmentNamesList( budgetSegmentNamesList );

      valueList.add( translationBean );
    }

    return valueList;
  }

  public int getTranslationListCount()
  {
    if ( this.translationsTextList == null )
    {
      return 0;
    }

    return this.translationsTextList.size();
  }

  public PromotionTranslationFormBean getTranslationList( int index )
  {
    try
    {
      return (PromotionTranslationFormBean)translationsTextList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  private List getEmptyPayoutValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionTranslationGoalAndPayoutFormBean
      PromotionTranslationGoalAndPayoutFormBean translationBean = new PromotionTranslationGoalAndPayoutFormBean();
      valueList.add( translationBean );
    }

    return valueList;
  }

  private List getEmptyDivisionValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionTranslationGoalAndPayoutFormBean
      PromotionTranslationDivisionFormBean translationBean = new PromotionTranslationDivisionFormBean();
      valueList.add( translationBean );
    }

    return valueList;
  }

  public int getTranslationsPayoutListCount()
  {
    if ( this.translationsGoalNameAndDescriptionList == null )
    {
      return 0;
    }

    return this.translationsGoalNameAndDescriptionList.size();
  }

  public int getTranslationsDivisionListCount()
  {
    if ( this.translationsDivisionList == null )
    {
      return 0;
    }

    return this.translationsDivisionList.size();
  }

  public PromotionTranslationGoalAndPayoutFormBean getTranslationsPayoutList( int index )
  {
    try
    {
      return (PromotionTranslationGoalAndPayoutFormBean)translationsGoalNameAndDescriptionList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public PromotionTranslationDivisionFormBean getTranslationsDivisionList( int index )
  {
    try
    {
      return (PromotionTranslationDivisionFormBean)translationsDivisionList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public boolean isWebRulesActive()
  {
    return webRulesActive;
  }

  public void setWebRulesActive( boolean webRulesActive )
  {
    this.webRulesActive = webRulesActive;
  }

  public String getWebRulesText()
  {
    return webRulesText;
  }

  public void setWebRulesText( String webRulesText )
  {
    this.webRulesText = webRulesText;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public boolean isExpired()
  {
    return expired;
  }

  public void setExpired( boolean expired )
  {
    this.expired = expired;
  }

  public List<PromotionTranslationFormBean> getTranslationsTextList()
  {
    return translationsTextList;
  }

  public void setTranslationsTextList( List<PromotionTranslationFormBean> translationsTextList )
  {
    this.translationsTextList = translationsTextList;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

  public static String getJsString( String input )
  {
    String output = null;
    if ( input != null )
    {
      output = input.replace( "\'", "\\'" );
      output = output.replace( "\"", "\\\"" );
      output = output.replace( "\r", "\\r" );
      output = output.replace( "\n", "\\n" );
    }
    return output;
  }

  public boolean isQuizPromotion()
  {
    return quizPromotion;
  }

  public void setQuizPromotion( boolean quizPromotion )
  {
    this.quizPromotion = quizPromotion;
  }

  public String getPromotionObjective()
  {
    return promotionObjective;
  }

  public void setPromotionObjective( String promotionObjective )
  {
    this.promotionObjective = promotionObjective;
  }

  public String getManagerWebRulesText()
  {
    return managerWebRulesText;
  }

  public void setManagerWebRulesText( String managerWebRulesText )
  {
    this.managerWebRulesText = managerWebRulesText;
  }

  public String getPartnerWebRulesText()
  {
    return partnerWebRulesText;
  }

  public void setPartnerWebRulesText( String partnerWebRulesText )
  {
    this.partnerWebRulesText = partnerWebRulesText;
  }

  public boolean isPartnerAvailable( Promotion promotion )
  {
    if ( promotion.getPartnerAudienceType() != null )
    {
      partnerAvailable = true;
    }
    else
    {
      partnerAvailable = false;
    }
    return partnerAvailable;
  }

  public boolean getPartnerAvailable()
  {
    return partnerAvailable;
  }

  public void setPartnerAvailable( boolean partnerAvailable )
  {
    this.partnerAvailable = partnerAvailable;
  }

  public boolean isGoalQuestOrChallengePointPromotion()
  {
    return goalQuestOrChallengePointPromotion;
  }

  public void setGoalQuestOrChallengePointPromotion( boolean goalQuestOrChallengePointPromotion )
  {
    this.goalQuestOrChallengePointPromotion = goalQuestOrChallengePointPromotion;
  }

  public String getOverviewDetailsText()
  {
    return overviewDetailsText;
  }

  public void setOverviewDetailsText( String overviewDetailsText )
  {
    this.overviewDetailsText = overviewDetailsText;
  }

  public List<PromotionTranslationGoalAndPayoutFormBean> getTranslationsGoalNameAndDescriptionList()
  {
    return translationsGoalNameAndDescriptionList;
  }

  public void setTranslationsGoalNameAndDescriptionList( List<PromotionTranslationGoalAndPayoutFormBean> translationsGoalNameAndDescriptionList )
  {
    this.translationsGoalNameAndDescriptionList = translationsGoalNameAndDescriptionList;
  }

  public String getBaseUnit()
  {
    return baseUnit;
  }

  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  public List<PromotionTranslationDivisionFormBean> getTranslationsDivisionAsList()
  {
    return translationsDivisionList;
  }

  public void setTranslationsDivisionAsList( List<PromotionTranslationDivisionFormBean> translationsDivisionList )
  {
    this.translationsDivisionList = translationsDivisionList;
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

  public List<PromotionTranslationPayoutDescriptionFormBean> getPayoutDescriptionList()
  {
    return payoutDescriptionList;
  }

  public void setPayoutDescriptionList( List<PromotionTranslationPayoutDescriptionFormBean> payoutDescriptionList )
  {
    this.payoutDescriptionList = payoutDescriptionList;
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

  public int getBudgetSegmentNamesListCount()
  {
    if ( this.budgetSegmentNamesList == null )
    {
      return 0;
    }

    return this.budgetSegmentNamesList.size();
  }

  public PromotionTranslationBudgetSegmentNamesFormBean getBudgetSegmentNamesList( int index )
  {
    try
    {
      return (PromotionTranslationBudgetSegmentNamesFormBean)budgetSegmentNamesList.get( index );
    }
    catch( Exception exception )
    {
      return null;
    }
  }

}
