
package com.biperf.core.ui.ssi;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author Patelp
 * @since Feb 05, 2015
 * @version 1.0
 */
public class SSIContestPayoutAwardThemNowForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String currencyTypeId;
  private String otherPayoutTypeId;
  private String payoutType;
  private String key;
  private String message;
  private ArrayList<SSIContestParticipantValueBean> participants = new ArrayList<SSIContestParticipantValueBean>();

  @Override
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();

    if ( "save".equals( method ) || "calcTotal".equals( method ) )
    {
      validateParticipantRecords( actionErrors );
    }
    if ( "sameForAll".equals( method ) )
    {
      validateParticipantATNPayout( actionErrors );
    }

    return actionErrors;
  }

  private void validateParticipantRecords( ActionErrors actionErrors )
  {
    if ( !getParticipantsAsList().isEmpty() )
    {
      validateParticipantData( actionErrors );
    }
    else
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.INVALIDPARTICIPANTINFORMATION" ) ) );

    }
  }

  private void validateParticipantATNPayout( ActionErrors actionErrors )
  {
    for ( SSIContestParticipantValueBean ssiContestParticipant : getParticipantsAsList() )
    {
      try
      {
        if ( !StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectivePayout() ) )
        {
          Long.parseLong( ssiContestParticipant.getObjectivePayout() );
        }
        else if ( "objectivePayout".equalsIgnoreCase( getKey() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.atn.summary.PAYOUT" ) ) );
          break;
        }
        if ( !StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectiveAmount() ) )
        {
          Double.parseDouble( ssiContestParticipant.getObjectiveAmount() );
        }
        else if ( "objectiveAmount".equalsIgnoreCase( getKey() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.atn.summary.ACTIVITY_AMOUNT" ) ) );
          break;
        }
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.INVALIDPARTICIPANTINFORMATION" ) ) );
        break;
      }
    }
  }

  private void validateParticipantData( ActionErrors actionErrors )
  {
    for ( SSIContestParticipantValueBean ssiContestParticipant : getParticipantsAsList() )
    {
      if ( "points".equals( payoutType ) && ( StringUtil.isNullOrEmpty( ssiContestParticipant.getActivityDescription() ) || StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectiveAmount() )
          || StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectivePayout() ) ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.ALLPARTICIPANTFIELDSOULDBEFILLED" ) ) );
        break;
      }

      if ( "other".equals( payoutType )
          && ( StringUtil.isNullOrEmpty( ssiContestParticipant.getActivityDescription() ) || StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectivePayoutDescription() )
              || StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectiveAmount() ) || StringUtil.isNullOrEmpty( ssiContestParticipant.getObjectivePayout() ) ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.ALLPARTICIPANTFIELDSOULDBEFILLED" ) ) );
        break;
      }

    }
  }

  /**
   * Populate the contest with the request
   * @param contestId
   * @param issuanceNumber TODO
   * @return
   */
  public SSIContestAwardThemNow toAwardThemNowDomain( Long contestId, Short issuanceNumber )
  {

    SSIContestAwardThemNow contestAtn = new SSIContestAwardThemNow();

    contestAtn.setContest( toDomain( contestId ) );
    contestAtn.setIssuanceNumber( issuanceNumber );
    contestAtn.setNotificationMessageText( message );

    return contestAtn;
  }

  /**
   * Populate the contest with the request
   * @param contestId
   * @param issuanceNumber TODO
   * @return
   */
  public SSIContest toDomain( Long contestId )
  {

    SSIContest ssiContest = new SSIContest();
    ssiContest.setId( contestId );

    return ssiContest;
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

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public String getCurrencyTypeId()
  {
    return currencyTypeId;
  }

  public void setCurrencyTypeId( String currencyTypeId )
  {
    this.currencyTypeId = currencyTypeId;
  }

  public String getOtherPayoutTypeId()
  {
    return otherPayoutTypeId;
  }

  public void setOtherPayoutTypeId( String otherPayoutTypeId )
  {
    this.otherPayoutTypeId = otherPayoutTypeId;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }
}
