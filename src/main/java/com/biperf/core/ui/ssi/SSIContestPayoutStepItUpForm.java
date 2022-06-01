
package com.biperf.core.ui.ssi;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIBillPayoutCodeType;
import com.biperf.core.domain.enums.SSIIndividualBaselineType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.ui.ssi.view.SSIContestLevelView;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestStepItUpForm.
 * 
 * @author patelP
 * @since January 8, 2015
 * @version 1.0
 */

public class SSIContestPayoutStepItUpForm extends SSIContestPayoutBaseForm
{
  private static final long serialVersionUID = 1L;

  private final int TWO_DIGIT = 2;
  private final int FOUR_DIGIT = 4;

  private Long contestId;
  private String method;
  private String measureType;
  private String currencyTypeId;
  private String payoutType;
  private String otherPayoutTypeId;
  private boolean includeBonus;
  private boolean includeStackRanking;
  private boolean includeIndividualBaseline;
  private String measureOverBaseline;
  private String activityDescription;
  private String contestGoal;
  private Long bonusPayoutCap;
  private Long bonusPayout;
  private Double bonusForEvery;
  private String key;
  private SSIContestLevelForm level = new SSIContestLevelForm();
  private ArrayList<SSIContestLevelView> levels = new ArrayList<SSIContestLevelView>();
  private ArrayList<SSIContestParticipantValueBean> participants = new ArrayList<SSIContestParticipantValueBean>();

  @Override
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();

    if ( "save".equals( method ) )
    {
      validateMeasureType( actionErrors );
      validatePayoutType( actionErrors );
      validateCurrency( actionErrors );
      validateActivityDescription( actionErrors );
      validateParticipantRecords( actionErrors );
      validateIncludeBonusForPercetgeOverBaseLine( actionErrors );
      validatePayoutBonus( actionErrors );
      validateBillCode( actionErrors );
      if ( this.getContestGoal() != null )
      {
        validateNonEmptyContestGoal( actionErrors );
      }
      else
      {
        validateContestGoal( actionErrors );
      }
    }
    if ( "saveAsDraft".equals( method ) )
    {
      validateCurrency( actionErrors );
      validatePayoutBonus( actionErrors );
      validateNonEmptyContestGoal( actionErrors );
    }
    // adding & updating contest levels
    if ( "create".equals( method ) || "update".equals( method ) )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_LEVELS ) );
      SSIContest contest = getSSIContestService().getContestByIdWithAssociations( contestId, associationRequestCollection );
      validatelevelPayout( actionErrors, contest );
      validateLevelAmount( actionErrors, contest );
    }
    if ( "sameForAll".equals( method ) )
    {
      validateParticipantRecords( actionErrors );
    }
    return actionErrors;
  }

  private void validateIncludeBonusForPercetgeOverBaseLine( ActionErrors actionErrors )
  {
    if ( !StringUtil.isNullOrEmpty( measureOverBaseline ) && SSIIndividualBaselineType.PERCENTAGE_OVER_BASELINE_CODE.equals( measureOverBaseline ) && includeBonus )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "ssi_contest.payout_dtgt.BONUS_NOT_ALLOWED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.INCLUDE_BONUS" ) ) );

    }

  }

  private void validateLevelAmount( ActionErrors actionErrors, SSIContest contest )
  {
    if ( level.getAmount() != null && !StringUtil.isNullOrEmpty( measureType ) )
    {
      if ( SSIActivityMeasureType.CURRENCY_CODE.equals( measureType ) )
      {
        if ( SSIContestUtil.getPrecision( level.getAmount() ) > TWO_DIGIT )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "ssi_contest.payout_dtgt.NUMBER_WITH_DECIMALS_ERROR", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.AMOUNT" ) ) );
        }
        if ( level.getAmount() <= 0 )
        {
          if ( !StringUtil.isNullOrEmpty( measureOverBaseline ) && !SSIIndividualBaselineType.NO_BASELINE_CODE.equals( measureOverBaseline ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "ssi_contest.payout_dtgt.NEGATIVES_NOT_ALLOWED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.CURRENCY" ) ) );
          }
          if ( !StringUtil.isNullOrEmpty( measureOverBaseline ) && !SSIIndividualBaselineType.PERCENTAGE_OVER_BASELINE_CODE.equals( measureOverBaseline ) )
          {
            actionErrors
                .add( ActionErrors.GLOBAL_MESSAGE,
                      new ActionMessage( "ssi_contest.payout_dtgt.NEGATIVES_NOT_ALLOWED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.PERCENTAGE_OVER_BASELINE" ) ) );
          }
          if ( !StringUtil.isNullOrEmpty( measureOverBaseline ) && !SSIIndividualBaselineType.CURRENCY_OVER_BASELINE_CODE.equals( measureOverBaseline ) )
          {
            actionErrors
                .add( ActionErrors.GLOBAL_MESSAGE,
                      new ActionMessage( "ssi_contest.payout_dtgt.NEGATIVES_NOT_ALLOWED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.CURRENCY_OVER_BASELINE" ) ) );
          }
        }
        if ( !validLevelAmount( contest ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_stepitup.AMOUNT_OUT_OF_ORDER" ) );
        }
      }
      else
      {
        if ( SSIContestUtil.getPrecision( level.getAmount() ) > FOUR_DIGIT )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "ssi_contest.payout_dtgt.NUMBER_WITH_DECIMALS_ERROR", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.AMOUNT" ) ) );
        }
        if ( !validLevelAmount( contest ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_stepitup.AMOUNT_OUT_OF_ORDER" ) );
        }
      }
    }
    else
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.AMOUNT" ) ) );
    }
  }

  private boolean validLevelAmount( SSIContest contest )
  {
    boolean valid = true;
    for ( SSIContestLevel contestLevel : contest.getContestLevels() )
    {
      if ( level.getSequenceNumber().intValue() > contestLevel.getSequenceNumber().intValue() )
      {
        if ( level.getAmount().doubleValue() <= contestLevel.getGoalAmount().doubleValue() )
        {
          valid = false;
          break;
        }
      }
    }
    return valid;
  }

  private boolean validPayoutAmount( SSIContest contest )
  {
    boolean valid = true;
    for ( SSIContestLevel contestLevel : contest.getContestLevels() )
    {
      if ( level.getSequenceNumber().intValue() > contestLevel.getSequenceNumber().intValue() )
      {
        if ( level.getPayout().longValue() <= contestLevel.getPayoutAmount().longValue() )
        {
          valid = false;
          break;
        }
      }
    }
    return valid;
  }

  private void validatelevelPayout( ActionErrors actionErrors, SSIContest contest )
  {
    if ( level.getPayout() == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAYOUT" ) ) );
    }
    else if ( level.getPayout().longValue() <= 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "ssi_contest.payout_dtgt.INVALID_NUMBER_ERROR", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAYOUT" ) ) );
    }
    else if ( !validPayoutAmount( contest ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_stepitup.PAYOUT_OUT_OF_ORDER" ) );
    }
  }

  private void validatePayoutType( ActionErrors actionErrors )
  {
    if ( StringUtil.isNullOrEmpty( payoutType ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_TYPE" ) ) );
    }
  }

  private void validateMeasureType( ActionErrors actionErrors )
  {
    if ( StringUtil.isNullOrEmpty( measureType ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.MEASURE_ACTIVITY_IN" ) ) );
    }
  }

  private void validateCurrency( ActionErrors actionErrors )
  {
    if ( !StringUtil.isNullOrEmpty( measureType ) && SSIActivityMeasureType.CURRENCY_CODE.equals( measureType ) )
    {
      if ( StringUtil.isNullOrEmpty( currencyTypeId ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.CURRENCY" ) ) );
      }
    }
    if ( !StringUtil.isNullOrEmpty( payoutType ) && SSIPayoutType.OTHER_CODE.equals( payoutType ) )
    {
      if ( StringUtil.isNullOrEmpty( otherPayoutTypeId ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.OTHER" ) ) );
      }
    }
  }

  private void validateContestGoal( ActionErrors actionErrors )
  {
    if ( contestGoal == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.CONTESTGOAL" ) ) );
    }
  }

  private void validateNonEmptyContestGoal( ActionErrors actionErrors )
  {
    try
    {
      if ( contestGoal != null )
      {
        Double.parseDouble( contestGoal.toString() );
      }
    }
    catch( NumberFormatException e )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.CONTESTGOAL" ) ) );
    }
  }

  private void validateActivityDescription( ActionErrors actionErrors )
  {
    if ( StringUtil.isNullOrEmpty( activityDescription ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.ACTIVITYDESCRIPTION" ) ) );
    }
    else if ( !StringUtil.isNullOrEmpty( activityDescription ) && activityDescription.length() > 50 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "ssi_contest.payout_dtgt.LENGTH_EXCEEDS_ERROR", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.ACTIVITY_DESCRIPTION" ), 50 ) );
    }

  }

  private void validateParticipantRecords( ActionErrors actionErrors )
  {
    Boolean isObjectiveInfoBlank = false;
    if ( !StringUtil.isNullOrEmpty( measureOverBaseline ) && !SSIIndividualBaselineType.NO_BASELINE_CODE.equals( measureOverBaseline ) )
    {
      if ( !getParticipantsAsList().isEmpty() )
      {
        for ( SSIContestParticipantValueBean ssiContestParticipant : getParticipantsAsList() )
        {
          if ( StringUtil.isNullOrEmpty( ssiContestParticipant.getBaselineAmount() ) )
          {
            isObjectiveInfoBlank = true;
            break;
          }
          else
          {
            try
            {
              if ( SSIIndividualBaselineType.PERCENTAGE_OVER_BASELINE_CODE.equals( measureOverBaseline ) && Double.parseDouble( ssiContestParticipant.getBaselineAmount() ) == 0 )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                  new ActionMessage( "ssi_contest.payout_stepitup.BASE_LINE_CANNOT_BE_ZERO", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.BASE_LINE" ) ) );
                break;
              }

            }
            catch( NumberFormatException e )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.BASE_LINE" ) ) );
              break;
            }
          }
        }
        if ( isObjectiveInfoBlank )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.BASE_LINE" ) ) );
        }
      }
      else
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.BASE_LINE" ) ) );

      }
    }

  }

  private void validatePayoutBonus( ActionErrors actionErrors )
  {

    try
    {
      if ( bonusPayoutCap != null )
      {
        if ( Double.parseDouble( bonusPayoutCap.toString() ) < 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_CAP" ) ) );
        }
      }
    }
    catch( NumberFormatException e )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_CAP" ) ) );
    }

    try
    {
      if ( bonusPayout != null )
      {
        if ( Double.parseDouble( bonusPayout.toString() ) < 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT" ) ) );
        }
      }
    }
    catch( NumberFormatException e )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT" ) ) );
    }

    try
    {
      if ( bonusForEvery != null )
      {
        if ( Double.parseDouble( bonusForEvery.toString() ) < 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.FOR_EVERY" ) ) );
        }
      }
    }
    catch( NumberFormatException e )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.FOR_EVERY" ) ) );
    }

  }

  /**
   * Populate the contest with the request
   * @param contestId
   * @return
   */
  public SSIContest toDomain( Long contestId )
  {

    SSIContest ssiContest = new SSIContest();
    ssiContest.setId( contestId );
    ssiContest.setActivityDescription( activityDescription );
    ssiContest.setActivityMeasureType( SSIActivityMeasureType.lookup( getMeasureType() ) );
    ssiContest.setIndividualBaselineType( SSIIndividualBaselineType.lookup( getMeasureOverBaseline() ) );
    ssiContest.setActivityMeasureCurrencyCode( currencyTypeId );
    ssiContest.setPayoutType( SSIPayoutType.lookup( payoutType ) );
    ssiContest.setPayoutOtherCurrencyCode( otherPayoutTypeId );
    ssiContest.setIncludeStackRank( includeStackRanking );
    ssiContest.setIncludeBonus( includeBonus );
    ssiContest.setStepItUpBonusCap( bonusPayoutCap );
    ssiContest.setStepItUpBonusPayout( bonusPayout );
    ssiContest.setStepItUpBonusIncrement( bonusForEvery );
    ssiContest.setContestGoal( !StringUtil.isNullOrEmpty( contestGoal ) ? Double.parseDouble( contestGoal ) : null );
    ssiContest.setBillPayoutCodeType( !StringUtil.isNullOrEmpty( getBillTo() ) ? SSIBillPayoutCodeType.lookup( getBillTo() ) : null );
    populateBillCodes( ssiContest );
    setBillCodeReuired( CollectionUtils.isNotEmpty( ssiContest.getContestBillCodes() ) );
    return ssiContest;
  }

  /**
   * Convert from view to domain
   * @return Set<SSIContestActivity>
   */
  public SSIContestLevel toLevelDomain()
  {
    SSIContestLevel contestLevel = null;
    if ( level != null )
    {
      contestLevel = new SSIContestLevel();
      contestLevel.setId( level.getId() );
      contestLevel.setPayoutAmount( level.getPayout() );
      contestLevel.setGoalAmount( level.getAmount() );
      contestLevel.setPayoutDesc( level.getPayoutDescription() );
      contestLevel.setSequenceNumber( level.getSequenceNumber() );
    }
    return contestLevel;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMeasureType()
  {
    return measureType;
  }

  public void setMeasureType( String measureType )
  {
    this.measureType = measureType;
  }

  public String getCurrencyTypeId()
  {
    return currencyTypeId;
  }

  public void setCurrencyTypeId( String currencyTypeId )
  {
    this.currencyTypeId = currencyTypeId;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getOtherPayoutTypeId()
  {
    return otherPayoutTypeId;
  }

  public void setOtherPayoutTypeId( String otherPayoutTypeId )
  {
    this.otherPayoutTypeId = otherPayoutTypeId;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public boolean isIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getContestGoal()
  {
    return contestGoal;
  }

  public void setContestGoal( String contestGoal )
  {
    this.contestGoal = contestGoal;
  }

  public SSIContestLevelView getLevels( int index )
  {
    while ( index >= levels.size() )
    {
      levels.add( new SSIContestLevelView() );
    }
    return levels.get( index );
  }

  public ArrayList<SSIContestLevelView> getLevelsAsList()
  {
    return levels;
  }

  public void setLevelsAsList( SSIContestLevelView level )
  {
    this.levels.add( level );
  }

  public boolean isIncludeIndividualBaseline()
  {
    return includeIndividualBaseline;
  }

  public void setIncludeIndividualBaseline( boolean includeIndividualBaseline )
  {
    this.includeIndividualBaseline = includeIndividualBaseline;
  }

  public SSIContestParticipantValueBean getParticipants( int index )
  {
    while ( index >= participants.size() )
    {
      participants.add( new SSIContestParticipantValueBean() );
    }
    return participants.get( index );
  }

  public ArrayList<SSIContestParticipantValueBean> getParticipantsAsList()
  {
    return participants;
  }

  public void setParticipantsAsList( SSIContestParticipantValueBean participant )
  {
    this.participants.add( participant );
  }

  public SSIContestLevelForm getLevel()
  {
    return level;
  }

  public void setLevel( SSIContestLevelForm level )
  {
    this.level = level;
  }

  public Long getBonusPayoutCap()
  {
    return bonusPayoutCap;
  }

  public void setBonusPayoutCap( Long bonusPayoutCap )
  {
    this.bonusPayoutCap = bonusPayoutCap;
  }

  public Long getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( Long bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public Double getBonusForEvery()
  {
    return bonusForEvery;
  }

  public void setBonusForEvery( Double bonusForEvery )
  {
    this.bonusForEvery = bonusForEvery;
  }

  public String getMeasureOverBaseline()
  {
    return measureOverBaseline;
  }

  public void setMeasureOverBaseline( String measureOverBaseline )
  {
    this.measureOverBaseline = measureOverBaseline;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)BeanLocator.getBean( SSIContestService.BEAN_NAME );
  }

}
