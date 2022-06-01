
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
import com.biperf.core.value.ssi.SSIContestRankValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * SSIContestPayoutStackRankForm.
 * 
 * @author dudam
 * @since Feb 16, 2015
 * @version 1.0
 */
public class SSIContestPayoutStackRankForm extends SSIContestPayoutBaseForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String measureType;
  private String currencyTypeId;
  private String includeMinimumQualifier;
  private String minimumQualifier;
  private String payoutType;
  private String stackRankingOrder;
  private String otherPayoutTypeId;
  private String activityDescription;
  private String contestGoal;
  private ArrayList<SSIContestRankValueBean> ranks = new ArrayList<SSIContestRankValueBean>();

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
      validateSortOrder( actionErrors );
      validateMinimumQualifier( actionErrors );
      validateRanks( actionErrors );
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
      validateNonEmptyContestGoal( actionErrors );
    }
    return actionErrors;
  }

  private void validateRanks( ActionErrors actionErrors )
  {
    if ( ranks.size() == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stackrank.RANK" ) ) );
    }
  }

  private void validateSortOrder( ActionErrors actionErrors )
  {
    if ( StringUtil.isNullOrEmpty( stackRankingOrder ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stackrank.SORT_ORDER" ) ) );
    }
  }

  private void validateMinimumQualifier( ActionErrors actionErrors )
  {
    if ( StringUtil.isNullOrEmpty( includeMinimumQualifier ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stackrank.INC_MIN_QUALIFIER" ) ) );
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
    ssiContest.setActivityMeasureCurrencyCode( currencyTypeId );
    ssiContest.setIncludeStackRankQualifier( "yes".equals( includeMinimumQualifier ) );
    ssiContest.setStackRankQualifierAmount( !StringUtil.isNullOrEmpty( minimumQualifier ) ? Double.parseDouble( minimumQualifier ) : null );
    ssiContest.setStackRankOrder( stackRankingOrder );
    ssiContest.setPayoutType( SSIPayoutType.lookup( payoutType ) );
    ssiContest.setPayoutOtherCurrencyCode( otherPayoutTypeId );
    ssiContest.setContestGoal( !StringUtil.isNullOrEmpty( contestGoal ) ? Double.parseDouble( contestGoal ) : null );
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

  public String getIncludeMinimumQualifier()
  {
    return includeMinimumQualifier;
  }

  public void setIncludeMinimumQualifier( String includeMinimumQualifier )
  {
    this.includeMinimumQualifier = includeMinimumQualifier;
  }

  public String getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( String minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public String getStackRankingOrder()
  {
    return stackRankingOrder;
  }

  public void setStackRankingOrder( String stackRankingOrder )
  {
    this.stackRankingOrder = stackRankingOrder;
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

  public SSIContestRankValueBean getRanks( int index )
  {
    while ( index >= ranks.size() )
    {
      ranks.add( new SSIContestRankValueBean() );

    }
    return ranks.get( index );
  }

  public ArrayList<SSIContestRankValueBean> getRanksAsList()
  {
    return ranks;
  }

  public void setRanksAsList( SSIContestRankValueBean rank )
  {
    this.ranks.add( rank );
  }

}
