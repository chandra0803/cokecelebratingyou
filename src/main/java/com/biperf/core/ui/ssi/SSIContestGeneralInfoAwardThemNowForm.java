
package com.biperf.core.ui.ssi;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestGeneralInfoAwardThemNowForm.
 * 
 * @author kandhi
 * @since Feb 3, 2015
 * @version 1.0
 */
public class SSIContestGeneralInfoAwardThemNowForm extends SSIContestGeneralInfoForm
{
  private static final long serialVersionUID = 1L;

  private String measureType;
  private String currencyTypeId;
  private String payoutType;
  private String otherPayoutTypeId;
  private Long badgeId;

  @Override
  public ActionErrors validate( ActionMapping mapping, ServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    if ( "save".equals( getMethod() ) )
    {
      validateStartAndEndDate( actionErrors );
      validateDescription( actionErrors, true );
      validateDocuments( actionErrors );
      validateApprovers( actionErrors );

      validateMeasureType( actionErrors );
      validatePayoutType( actionErrors );
      validateCurrency( actionErrors );
    }
    else if ( "saveAtn".equals( getMethod() ) )
    {
      validateStartAndEndDate( actionErrors );
      validateDescription( actionErrors, true );
      validateDocuments( actionErrors );
      validateApprovers( actionErrors );
    }
    return actionErrors;
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

  /**
   * Populate the contest with the request
   * @param clientStateMap
   * @return
   */
  public SSIContest toDomain( Map<String, Object> clientStateMap )
  {
    SSIContest ssiContest = super.toDomain( clientStateMap );
    ssiContest.setActivityMeasureType( SSIActivityMeasureType.lookup( measureType ) );
    ssiContest.setActivityMeasureCurrencyCode( currencyTypeId );
    ssiContest.setPayoutType( SSIPayoutType.lookup( payoutType ) );
    ssiContest.setPayoutOtherCurrencyCode( otherPayoutTypeId );
    return ssiContest;
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

}
