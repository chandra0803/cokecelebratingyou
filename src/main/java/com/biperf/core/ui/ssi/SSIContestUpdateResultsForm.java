
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIConetstParticipantActivityValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestUpdateResultsForm extends BaseForm
{
  private int page; // current page number
  private List<SSIConetstParticipantActivityValueBean> participant = new ArrayList<SSIConetstParticipantActivityValueBean>();
  private int perPage; // results per page
  private String sortedBy; // "asc"
  private String sortedOn; // "lastName"
  private String ssiEnterActivityDate;
  private String contestType;
  private String measureType;
  private String id; // contest id
  private String method; // action method called
  private String initializationJson = "";
  private String finalizeResults;
  private String saveAndSendProgressUpdate;
  private String cancelAndSendProgress;

  private static int ALLOWED_DECIAL_COUNT_MEASURE_TYPE_CURRENCY_CODE = 2;
  private static int ALLOWED_DECIAL_COUNT_MEASURE_TYPE_UNIT_CODE = 4;
  private static int NON_DECIMAL_ALLOWABLE_LIMIT = 9;
  private static int WITH_DECIMAL_ALLOWABLE_LIMIT = 14;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getMeasureType()
  {
    return measureType;
  }

  public void setMeasureType( String measureType )
  {
    this.measureType = measureType;
  }

  public List<String> validate()
  {
    List<String> validationErrors = new ArrayList<String>();
    if ( participant.size() > 0 )
    {
      for ( SSIConetstParticipantActivityValueBean participantActitActivityValueBean : participant )
      {
        if ( participantActitActivityValueBean.getActivityAsList() != null && participantActitActivityValueBean.getActivityAsList().size() > 0 )
        {
          validateActivityAmounts( validationErrors, participantActitActivityValueBean );
        }
      }
    }

    return validationErrors;
  }

  protected void validateActivityAmounts( List<String> validationErrors, SSIConetstParticipantActivityValueBean participantActitActivityValueBean )
  {
    for ( SSIConetstParticipantActivityValueBean.Activity activty : participantActitActivityValueBean.getActivityAsList() )
    {
      if ( !StringUtil.isNullOrEmpty( activty.getTotalActivity() ) )
      {
        try
        {
          Double activityAmount = Double.parseDouble( SSIContestUtil.removeComma( activty.getTotalActivity() ) );
          int decimalPointStartIndex = activty.getTotalActivity().indexOf( "." );

          if ( decimalPointStartIndex == -1 && activty.getTotalActivity().length() > NON_DECIMAL_ALLOWABLE_LIMIT
              || decimalPointStartIndex != -1 && activty.getTotalActivity().length() > WITH_DECIMAL_ALLOWABLE_LIMIT )
          {
            validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.TOTAL_ACTIVITY_AMOUNT_LIMIT" ) );
            break;
          }
          // measureType decimal validation

          if ( decimalPointStartIndex != -1 )
          {
            int decimalCount = activty.getTotalActivity().length() - ( decimalPointStartIndex + 1 );
            if ( SSIActivityMeasureType.CURRENCY_CODE.equalsIgnoreCase( measureType ) && decimalCount > ALLOWED_DECIAL_COUNT_MEASURE_TYPE_CURRENCY_CODE )
            {
              validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.ACTIVITY_AMOUNT_PRECISION" ) + ALLOWED_DECIAL_COUNT_MEASURE_TYPE_CURRENCY_CODE );
            }
            else if ( SSIActivityMeasureType.UNIT_CODE.equalsIgnoreCase( measureType ) && decimalCount > ALLOWED_DECIAL_COUNT_MEASURE_TYPE_UNIT_CODE )
            {
              validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.ACTIVITY_AMOUNT_PRECISION" ) + ALLOWED_DECIAL_COUNT_MEASURE_TYPE_UNIT_CODE );
            }
          }
        }
        catch( NumberFormatException e )
        {
          validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.INVALID_TOTAL_ACTIVITY_AMOUNT" ) );
        }
      }
    }
  }

  public SSIConetstParticipantActivityValueBean getParticipant( int index )
  {
    while ( index >= participant.size() )
    {
      participant.add( new SSIConetstParticipantActivityValueBean() );
    }
    return participant.get( index );
  }

  public List<SSIConetstParticipantActivityValueBean> getParticipantAsList()
  {
    return participant;
  }

  public void setParticipantAsList( SSIConetstParticipantActivityValueBean participant )
  {
    this.participant.add( participant );
  }

  public int getParticipantSize()
  {
    if ( this.participant != null )
    {
      return this.participant.size();
    }
    return 0;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSsiEnterActivityDate()
  {
    return ssiEnterActivityDate;
  }

  public void setSsiEnterActivityDate( String ssiEnterActivityDate )
  {
    this.ssiEnterActivityDate = ssiEnterActivityDate;
  }

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public String getFinalizeResults()
  {
    return finalizeResults;
  }

  public void setFinalizeResults( String finalizeResults )
  {
    this.finalizeResults = finalizeResults;
  }

  public String getSaveAndSendProgressUpdate()
  {
    return saveAndSendProgressUpdate;
  }

  public void setSaveAndSendProgressUpdate( String saveAndSendProgressUpdate )
  {
    this.saveAndSendProgressUpdate = saveAndSendProgressUpdate;
  }

  public String getCancelAndSendProgress()
  {
    return cancelAndSendProgress;
  }

  public void setCancelAndSendProgress( String cancelAndSendProgress )
  {
    this.cancelAndSendProgress = cancelAndSendProgress;
  }

}
