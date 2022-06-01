
package com.biperf.core.ui.ssi;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIBillPayoutCodeType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPayoutObjectivesForm.
 * 
 * @author kandhi
 * @since Dec 2, 2014
 * @version 1.0
 */
public class SSIContestPayoutObjectivesForm extends SSIContestPayoutBaseForm
{
  private String method;
  private String measureType;
  private String currencyTypeId;
  private String payoutType;
  private String otherPayoutTypeId;
  private Long badgeId;
  private boolean includeStackRanking;
  private String stackRankingOrder;
  private boolean includeBonus;
  private Boolean sameActivityDescriptionForAll;
  private String activityDescription;
  private String contestGoal;
  private String key;
  private ArrayList<SSIContestParticipantValueBean> participants = new ArrayList<SSIContestParticipantValueBean>();

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();

    if ( "save".equals( method ) )
    {
      if ( StringUtil.isNullOrEmpty( measureType ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.MEASURE_ACTIVITY_IN" ) ) );
      }
      if ( StringUtil.isNullOrEmpty( payoutType ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_TYPE" ) ) );
      }
      validateCurrency( actionErrors );
      validateSameObjectiveDescription( actionErrors );
      validateParticipantObjectivePayoutBonus( actionErrors, method );
      validateParticipantRecords( actionErrors );
      validateContestGoal( actionErrors );
      validateBillCode( actionErrors );
    }
    if ( "saveAsDraft".equals( method ) )
    {
      validateCurrency( actionErrors );
      validateParticipantObjectivePayoutBonus( actionErrors, method );
      validateContestGoalIfEntered( actionErrors );

    }
    if ( "sameForAll".equals( method ) )
    {
      validateParticipantObjectivePayoutBonus( actionErrors, method );
    }
    if ( "calcTotal".equals( method ) )
    {
      validateSameObjectiveDescription( actionErrors );
      validateParticipantObjectivePayoutBonus( actionErrors, method );
    }
    return actionErrors;
  }

  private void validateParticipantObjectivePayoutBonus( ActionErrors actionErrors, String method )
  {
    for ( SSIContestParticipantValueBean ssiContestParticipant : getParticipantsAsList() )
    {
      try
      {
        if ( !StringUtil.isNullOrEmpty( ssiContestParticipant.getBonusPayout() ) )
        {
          Long.parseLong( ssiContestParticipant.getBonusPayout() );
        }
        else
        {
          if ( "bonusPayout".equalsIgnoreCase( getKey() ) )
          {
            addActionErrors( actionErrors, method, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT" ) );
            break;
          }
        }
        if ( !StringUtil.isNullOrEmpty( ssiContestParticipant.getBonusForEvery() ) )
        {
          Double.parseDouble( ssiContestParticipant.getBonusForEvery() );
        }
        else
        {
          if ( "bonusForEvery".equalsIgnoreCase( getKey() ) )
          {
            addActionErrors( actionErrors, method, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.FOR_EVERY" ) );
            break;
          }
        }
        if ( !StringUtil.isNullOrEmpty( ssiContestParticipant.getBonusPayoutCap() ) )
        {
          Long.parseLong( ssiContestParticipant.getBonusPayoutCap() );
        }
        else
        {
          if ( "bonusPayoutCap".equalsIgnoreCase( getKey() ) )
          {
            addActionErrors( actionErrors, method, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_CAP" ) );
            break;
          }
        }
        if ( !StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectivePayout() ) )
        {
          Long.parseLong( ssiContestParticipant.getObjectivePayout() );
        }
        else
        {
          if ( "objectivePayout".equalsIgnoreCase( getKey() ) )
          {
            if ( SSIPayoutType.POINTS_CODE.equals( this.payoutType ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT" ) ) );
            }
            else if ( SSIPayoutType.OTHER_CODE.equals( this.payoutType ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.VALUE" ) ) );
            }
            break;
          }
        }
        if ( !StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectiveAmount() ) )
        {
          Double.parseDouble( ssiContestParticipant.getObjectiveAmount() );
        }
        else
        {
          if ( "objectiveAmount".equalsIgnoreCase( getKey() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.AMOUNT" ) ) );
            break;
          }
        }
      }
      catch( NumberFormatException e )
      {
        addInvalidActionErrors( actionErrors, method );
        break;
      }
      if ( !"saveAsDraft".equals( method ) )
      {
        if ( !includeStackRanking && "activityDescription".equalsIgnoreCase( getKey() ) )
        {
          if ( StringUtil.isNullOrEmpty( ssiContestParticipant.getActivityDescription() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.OBJECTIVES_DESCRIPTION" ) ) );
            break;
          }
          else if ( ssiContestParticipant.getActivityDescription().length() > 50 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.EXCEEDING_MAX_LIMIT", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.OBJECTIVES_DESCRIPTION" ) ) );
            break;
          }
        }

        if ( !StringUtil.isNullOrEmpty( payoutType ) && SSIPayoutType.OTHER_CODE.equals( payoutType ) && "objectivePayoutDescription".equalsIgnoreCase( getKey() ) )
        {
          if ( StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectivePayoutDescription() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_DESCRIPTION" ) ) );
            break;
          }
          else if ( ssiContestParticipant.getObjectivePayoutDescription().length() > 50 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.EXCEEDING_MAX_LIMIT", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_DESCRIPTION" ) ) );
            break;
          }
        }
      }
    }

  }

  private void addInvalidActionErrors( ActionErrors actionErrors, String method )
  {
    if ( !"saveAsDraft".equals( method ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.INVALIDPARTICIPANTINFORMATION" ) ) );
    }

  }

  private void addActionErrors( ActionErrors actionErrors, String method, String fieldName )
  {
    if ( !"saveAsDraft".equals( method ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", fieldName ) );
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
    if ( StringUtil.isNullOrEmpty( contestGoal ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.CONTESTGOAL" ) ) );
    }
    else
    {
      validateContestGoalIfEntered( actionErrors );
    }
  }

  private void validateContestGoalIfEntered( ActionErrors actionErrors )
  {
    if ( !StringUtil.isNullOrEmpty( contestGoal ) )
    {
      try
      {
        Double.parseDouble( contestGoal.toString() );
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.CONTESTGOAL" ) ) );
      }
    }
  }

  private void validateSameObjectiveDescription( ActionErrors actionErrors )
  {
    if ( sameActivityDescriptionForAll != null && sameActivityDescriptionForAll )
    {
      if ( StringUtil.isNullOrEmpty( activityDescription ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.OBJECTIVE_DESCRIPTION" ) ) );
      }
    }
  }

  private void validateParticipantRecords( ActionErrors actionErrors )
  {
    Boolean isActivityDescription = false;
    Boolean isBonusInfoBlank = false;
    Boolean isObjectiveInfoBlank = false;
    for ( SSIContestParticipantValueBean ssiContestParticipant : getParticipantsAsList() )
    {
      if ( sameActivityDescriptionForAll != null && !sameActivityDescriptionForAll )
      {
        if ( StringUtil.isNullOrEmpty( ssiContestParticipant.getActivityDescription() ) )
        {
          isActivityDescription = true;
          break;
        }
      }
      if ( includeBonus )
      {
        if ( ssiContestParticipant.getBonusPayout() == null || ssiContestParticipant.getBonusPayoutCap() == null || ssiContestParticipant.getBonusForEvery() == null )
        {
          isBonusInfoBlank = true;
          break;
        }
      }
      if ( ssiContestParticipant.getObjectiveAmount() == null || ssiContestParticipant.getObjectivePayout() == null )
      {
        isObjectiveInfoBlank = true;
        break;
      }
    }
    if ( isActivityDescription || isBonusInfoBlank || isObjectiveInfoBlank )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.ALLPARTICIPANTFIELDSOULDBEFILLED" ) ) );
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
    ssiContest.setActivityMeasureType( SSIActivityMeasureType.lookup( getMeasureType() ) );
    ssiContest.setActivityMeasureCurrencyCode( currencyTypeId );
    ssiContest.setPayoutType( SSIPayoutType.lookup( payoutType ) );
    ssiContest.setPayoutOtherCurrencyCode( otherPayoutTypeId );
    ssiContest.setIncludeBonus( includeBonus );
    ssiContest.setSameObjectiveDescription( sameActivityDescriptionForAll );
    ssiContest.setStackRankOrder( stackRankingOrder );
    ssiContest.setIncludeStackRank( includeStackRanking );
    ssiContest.setContestGoal( !StringUtil.isNullOrEmpty( contestGoal ) ? Double.parseDouble( contestGoal ) : null );
    ssiContest.setActivityDescription( activityDescription );
    ssiContest.setBillPayoutCodeType( !StringUtil.isNullOrEmpty( getBillTo() ) ? SSIBillPayoutCodeType.lookup( getBillTo() ) : null );
    populateBillCodes( ssiContest );
    setBillCodeReuired( CollectionUtils.isNotEmpty( ssiContest.getContestBillCodes() ) );
    return ssiContest;
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

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  public boolean isIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public String getStackRankingOrder()
  {
    return stackRankingOrder;
  }

  public void setStackRankingOrder( String stackRankingOrder )
  {
    this.stackRankingOrder = stackRankingOrder;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public boolean isSameActivityDescriptionForAll()
  {
    return sameActivityDescriptionForAll;
  }

  public void setSameActivityDescriptionForAll( boolean sameActivityDescriptionForAll )
  {
    this.sameActivityDescriptionForAll = sameActivityDescriptionForAll;
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

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public void setContestGoal( String contestGoal )
  {
    this.contestGoal = contestGoal;
  }

  public String getContestGoal()
  {
    return contestGoal;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

}
