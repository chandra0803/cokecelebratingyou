
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
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.ui.ssi.view.SSIContestActivityView;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPayoutDoThisGetThatForm.
 * 
 * @author kandhi
 * @since Dec 23, 2014
 * @version 1.0
 */
public class SSIContestPayoutDoThisGetThatForm extends SSIContestPayoutBaseForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String measureType;
  private String payoutType;
  private String currencyTypeId;
  private String otherPayoutTypeId;
  private Boolean includeStackRanking;
  private SSIContestActivityView activity = new SSIContestActivityView();
  private ArrayList<SSIContestActivityView> activities = new ArrayList<SSIContestActivityView>();

  /**
   * Validate the action form
   * {@inheritDoc}
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    if ( "save".equals( method ) )
    {
      requiredFieldValidations( actionErrors );
      validateBillCode( actionErrors );
      validateIfEntered( actionErrors );
    }
    else if ( "saveAsDraft".equals( method ) || "create".equals( method ) || "update".equals( method ) )
    {
      validateIfEntered( actionErrors );
    }
    return actionErrors;
  }

  /**
   * Validate the data entered for formatting
   * @param actionErrors
   */
  private void validateIfEntered( ActionErrors actionErrors )
  {
    if ( activities != null )
    {
      int allowedScale = SSIContestUtil.getPrecision( measureType );
      for ( SSIContestActivityView activityView : activities )
      {
        if ( !StringUtil.isNullOrEmpty( activityView.getDescription() ) && activityView.getDescription().length() > 50 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "ssi_contest.payout_dtgt.LENGTH_EXCEEDS_ERROR", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.ACTIVITY_DESCRIPTION" ), 50 ) );
        }
        if ( !StringUtil.isNullOrEmpty( measureType ) )
        {
          // Allow 2 digits after decimal for currency and 4 for units/numbers
          if ( !StringUtil.isNullOrEmpty( activityView.getForEvery() ) )
          {
            validateDouble( actionErrors, activityView.getForEvery(), "ssi_contest.payout_dtgt.FOR_EVERY", false, allowedScale );
          }
          if ( !StringUtil.isNullOrEmpty( activityView.getMinQualifier() ) )
          {
            validateMinimumQualifier( actionErrors, activityView.getMinQualifier(), "ssi_contest.payout_dtgt.MINIMUM_QUALIFIER", false, allowedScale );
          }
          if ( !StringUtil.isNullOrEmpty( activityView.getGoal() ) )
          {
            validateDouble( actionErrors, activityView.getGoal(), "ssi_contest.payout_dtgt.ACTIVITY_GOAL", true, allowedScale );
          }
        }
        if ( !StringUtil.isNullOrEmpty( activityView.getWillEarn() ) )
        {
          validateLong( actionErrors, activityView.getWillEarn(), "ssi_contest.payout_dtgt.WILL_EARN", false );
        }
        if ( !StringUtil.isNullOrEmpty( activityView.getMaxPayout() ) )
        {
          validateLong( actionErrors, activityView.getMaxPayout(), "ssi_contest.payout_dtgt.MAXIMUM_ACTIVITY_PAYOUT", false );
        }
        if ( !StringUtil.isNullOrEmpty( activityView.getIndividualPayoutCap() ) )
        {
          validateLong( actionErrors, activityView.getIndividualPayoutCap(), "ssi_contest.payout_dtgt.INDIVIDUAL_PAYOUT_CAP", false );
        }
      }
    }
  }

  /**
   * Make sure the entry is a valid number
   * @param actionErrors
   * @param strNumber
   * @param fieldCMKey
   * @param allowZero
   */
  private void validateMinimumQualifier( ActionErrors actionErrors, String strNumber, String fieldCMKey, boolean allowZero, int allowedScale )
  {
    double doubleNumber = 0;
    try
    {
      doubleNumber = Double.parseDouble( strNumber );

      if ( allowedScale < SSIContestUtil.getPrecision( doubleNumber ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "ssi_contest.payout_dtgt.MORE_DECIMALS_THAN_ALLOWED", allowedScale, CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
      }

    }
    catch( NumberFormatException ex )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
    }
  }

  /**
   * Make sure the entry is a valid number
   * @param actionErrors
   * @param strNumber
   * @param fieldCMKey
   */
  private void validateLong( ActionErrors actionErrors, String strNumber, String fieldCMKey, boolean allowZero )
  {
    long longNumber = 0;
    try
    {
      longNumber = Long.parseLong( strNumber );
      if ( longNumber == 0L && !allowZero )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_dtgt.NUM_CANNOT_BE_ZERO_ERROR", CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
      }
    }
    catch( NumberFormatException ex )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_dtgt.INVALID_NUMBER_ERROR", CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
    }
  }

  /**
   * Make sure the entry is a valid number
   * @param actionErrors
   * @param strNumber
   * @param fieldCMKey
   * @param allowZero
   */
  private void validateDouble( ActionErrors actionErrors, String strNumber, String fieldCMKey, boolean allowZero, int allowedScale )
  {
    double doubleNumber = 0;
    try
    {
      doubleNumber = Double.parseDouble( strNumber );
      if ( doubleNumber == 0 && !allowZero )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_dtgt.NUM_CANNOT_BE_ZERO_ERROR", CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
      }
      if ( allowedScale < SSIContestUtil.getPrecision( doubleNumber ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "ssi_contest.payout_dtgt.MORE_DECIMALS_THAN_ALLOWED", allowedScale, CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
      }
      if ( allowedScale == 2 && doubleNumber < 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_dtgt.NEGATIVES_NOT_ALLOWED", CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
      }
    }
    catch( NumberFormatException ex )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_dtgt.NUMBER_WITH_DECIMALS_ERROR", CmsResourceBundle.getCmsBundle().getString( fieldCMKey ) ) );
    }
  }

  /**
   * Validate the required fields on the page
   * @param actionErrors
   */
  private void requiredFieldValidations( ActionErrors actionErrors )
  {
    if ( StringUtil.isNullOrEmpty( measureType ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.ACTIVITY_TYPE" ) ) );
    }
    if ( StringUtil.isNullOrEmpty( payoutType ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.PAYOUT_TYPE" ) ) );
    }
    if ( activities == null || activities.isEmpty() )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.payout_dtgt.ADD_CONTEST_ACTIVITIES" ) );
    }
    else
    {
      for ( SSIContestActivityView activityView : activities )
      {
        if ( StringUtil.isNullOrEmpty( activityView.getDescription() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.ACTIVITY_DESCRIPTION" ) ) );
        }
        if ( StringUtil.isNullOrEmpty( activityView.getForEvery() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.FOR_EVERY" ) ) );
        }
        if ( StringUtil.isNullOrEmpty( activityView.getWillEarn() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.WILL_EARN" ) ) );
        }
        if ( StringUtil.isNullOrEmpty( activityView.getMinQualifier() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.MINIMUM_QUALIFIER" ) ) );
        }
        if ( StringUtil.isNullOrEmpty( activityView.getIndividualPayoutCap() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.INDIVIDUAL_PAYOUT_CAP" ) ) );
        }
        if ( StringUtil.isNullOrEmpty( activityView.getGoal() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.ACTIVITY_GOAL" ) ) );
        }
        if ( !StringUtil.isNullOrEmpty( payoutType ) && SSIPayoutType.OTHER_CODE.equals( payoutType ) )
        {
          if ( StringUtil.isNullOrEmpty( activityView.getValueDescription() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.VALUE_OF_AWARD" ) ) );
          }
        }
      }
    }
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

  public Boolean getIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( Boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public String getCurrencyTypeId()
  {
    return currencyTypeId;
  }

  public void setCurrencyTypeId( String currencyTypeId )
  {
    this.currencyTypeId = currencyTypeId;
  }

  public SSIContestActivityView getActivity()
  {
    return activity;
  }

  public void setActivity( SSIContestActivityView activity )
  {
    this.activity = activity;
  }

  public SSIContestActivityView getActivities( int index )
  {
    while ( index >= activities.size() )
    {
      activities.add( new SSIContestActivityView() );
    }
    return activities.get( index );
  }

  public ArrayList<SSIContestActivityView> getActivitiesAsList()
  {
    return activities;
  }

  public void setActivitiesAsList( SSIContestActivityView activity )
  {
    this.activities.add( activity );
  }

  /**
   * Convert from view to domain
   * @param contestId
   * @return SSIContest
   */
  public SSIContest toDomain( Long contestId )
  {
    SSIContest contest = new SSIContest();
    contest.setId( contestId );
    if ( !StringUtil.isNullOrEmpty( measureType ) )
    {
      contest.setActivityMeasureType( SSIActivityMeasureType.lookup( measureType ) );
      contest.setActivityMeasureCurrencyCode( currencyTypeId );
    }
    if ( !StringUtil.isNullOrEmpty( payoutType ) )
    {
      contest.setPayoutType( SSIPayoutType.lookup( payoutType ) );
      contest.setPayoutOtherCurrencyCode( otherPayoutTypeId );
    }
    if ( !StringUtil.isNullOrEmpty( otherPayoutTypeId ) )
    {
      contest.setPayoutOtherCurrencyCode( otherPayoutTypeId );
    }
    contest.setBillPayoutCodeType( !StringUtil.isNullOrEmpty( getBillTo() ) ? SSIBillPayoutCodeType.lookup( getBillTo() ) : null );
    contest.setIncludeStackRank( includeStackRanking == null ? Boolean.FALSE : includeStackRanking );
    populateBillCodes( contest );
    setBillCodeReuired( CollectionUtils.isNotEmpty( contest.getContestBillCodes() ) );
    return contest;
  }

  /**
   * Convert from view to domain
   * @return Set<SSIContestActivity>
   */
  public SSIContestActivity toActivityDomain()
  {
    SSIContestActivity contestActivity = null;
    if ( activity != null )
    {
      contestActivity = new SSIContestActivity();
      contestActivity.setId( activity.getId() );
      contestActivity.setDescription( activity.getDescription() );
      if ( !StringUtil.isNullOrEmpty( activity.getGoal() ) )
      {
        contestActivity.setGoalAmount( Double.parseDouble( activity.getGoal() ) );
      }
      if ( !StringUtil.isNullOrEmpty( payoutType ) && SSIPayoutType.OTHER_CODE.equals( payoutType ) )
      {
        contestActivity.setPayoutDescription( activity.getValueDescription() );
      }
      if ( !StringUtil.isNullOrEmpty( activity.getForEvery() ) )
      {
        contestActivity.setIncrementAmount( Double.parseDouble( activity.getForEvery() ) );
      }
      if ( !StringUtil.isNullOrEmpty( activity.getMinQualifier() ) )
      {
        contestActivity.setMinQualifier( Double.parseDouble( activity.getMinQualifier() ) );
      }
      if ( !StringUtil.isNullOrEmpty( activity.getWillEarn() ) )
      {
        contestActivity.setPayoutAmount( Long.parseLong( activity.getWillEarn() ) );
      }
      if ( !StringUtil.isNullOrEmpty( activity.getMaxPayout() ) )
      {
        contestActivity.setPayoutCapAmount( Long.parseLong( activity.getIndividualPayoutCap() ) );
      }
    }
    return contestActivity;
  }
}
