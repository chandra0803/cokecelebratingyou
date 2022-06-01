
package com.biperf.core.ui.ssi.view;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestParticipantProgressValueBean;

public class SSIUpdateResultsDataView
{
  private String contestType;
  private String measureType; // currency/units
  private String payoutType;
  private String activityAsOfDate;
  private boolean uploadInProgress;
  private String forwardUrl;
  private List<ActivitiesTableDataView> activitiesTableData = new ArrayList<ActivitiesTableDataView>(); // only
                                                                                                        // for
                                                                                                        // DTGT
  private List<ParticipantProgressView> results = new ArrayList<ParticipantProgressView>();

  private String activityTotal; // activity Total at contest level
  private String sortedOn; // lastName
  private String sortedBy; // asc/desc
  private int total; // total results,
  private int perPage;// results per page
  private int current;// current page number
  private String errorFileUrl;
  private String errorCount;

  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  public SSIUpdateResultsDataView()
  {
  }

  public SSIUpdateResultsDataView( String forwardUrl )
  {
    this.forwardUrl = forwardUrl;
  }

  public SSIUpdateResultsDataView( String errorFileUrl, String errorCount )
  {
    this.errorFileUrl = errorFileUrl;
    this.errorCount = errorCount;
  }

  public SSIUpdateResultsDataView( SSIContest contest,
                                   Map<String, Double> contestActivityTotals,
                                   List<SSIContestParticipantProgressValueBean> paticipantProgressValueBeans,
                                   int total,
                                   int current,
                                   int perPage,
                                   String sortedOn,
                                   String sortedBy )
  {
    this.contestType = contest.getContestTypeName();
    this.measureType = contest.getActivityMeasureType().getCode();
    this.payoutType = contest.getPayoutType() != null ? contest.getPayoutType().getCode() : null;
    this.uploadInProgress = contest.isUploadInProgress() == null ? Boolean.FALSE : contest.isUploadInProgress();
    // this.forwardUrl = getContestDetailPageURL( contest.getId() );
    if ( contest.getLastProgressUpdateDate() != null )
    {
      this.activityAsOfDate = DateUtils.getStringFromTimeStamp( new Timestamp( contest.getLastProgressUpdateDate().getTime() ) );
    }
    else
    {
      this.activityAsOfDate = ""; // blank
    }

    this.total = total;
    this.current = current;
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
    this.perPage = perPage;

    int index = 0;
    int activityPrecision = SSIContestUtil.getPrecision( measureType );
    if ( !SSIContestType.DO_THIS_GET_THAT.equals( contest.getContestType().getCode() ) )
    {
      for ( SSIContestParticipantProgressValueBean participantProgress : paticipantProgressValueBeans )
      {
        Double activityAmount = participantProgress.getActivityAmount();
        String acitivty = activityAmount == null || new Double( 0.0 ).equals( activityAmount ) ? "" : SSIContestUtil.getFormattedValue( activityAmount, activityPrecision );
        this.results.add( new ParticipantProgressView( index++,
                                                       participantProgress.getPaxId(),
                                                       participantProgress.getParticipantName(),
                                                       SSIContestType.OBJECTIVES.equals( contest.getContestType().getCode() ) && !contest.isIncludeStackRank()
                                                           ? participantProgress.getActivityName()
                                                           : contest.getActivityDescription(),
                                                       acitivty,
                                                       null ) );
      }
    }
    else
    {
      // DO_THIS_GET_THAT Contest
      // build activitiesTableData
      Map<Long, ActivitiesTableDataView> contestActivitiesMap = new LinkedHashMap<Long, ActivitiesTableDataView>();
      for ( SSIContestActivity contestActivity : contest.getContestActivities() )
      {
        contestActivitiesMap.put( contestActivity.getId(),
                                  new ActivitiesTableDataView( contestActivity.getDescription(),
                                                               SSIContestUtil.getFormattedValue( contestActivityTotals.get( contestActivity.getId().toString() ), activityPrecision ) ) );
      }

      // All Participants Progress results
      Map<Long, ParticipantProgressView> participantProgressMap = new LinkedHashMap<Long, ParticipantProgressView>();
      // All Participants Activities Map
      Map<Long, Map<Long, ActivityView>> allParticipantActivitiesMap = new LinkedHashMap<Long, Map<Long, ActivityView>>();

      for ( SSIContestParticipantProgressValueBean participantProgress : paticipantProgressValueBeans )
      {
        Long participantId = participantProgress.getPaxId();
        Long participantActivityId = participantProgress.getActivityId();
        Double participantActivityAmount = participantProgress.getActivityAmount();

        if ( participantProgressMap.get( participantId ) == null )
        {
          // unique participant
          // initialize activities for this participant (add all contest level activities)
          Map<Long, ActivityView> participantActivitiesMap = new LinkedHashMap<Long, ActivityView>();
          int activityNumber = 0;
          for ( SSIContestActivity contestActivity : contest.getContestActivities() )
          {
            participantActivitiesMap.put( contestActivity.getId(), new ActivityView( contestActivity.getId(), activityNumber++, contestActivity.getDescription(), "" ) );
          }

          allParticipantActivitiesMap.put( participantId, participantActivitiesMap );
          participantProgressMap.put( participantId, new ParticipantProgressView( index++,
                                                                                  participantId,
                                                                                  participantProgress.getParticipantName(),
                                                                                  null,
                                                                                  null,
                                                                                  new ArrayList<ActivityView>( participantActivitiesMap.values() ) ) );
        }

        if ( participantActivityId != null )
        {
          // attach the activity amount from the progress table
          allParticipantActivitiesMap.get( participantId ).get( participantActivityId ).setActivity( SSIContestUtil.getFormattedValue( participantActivityAmount, activityPrecision ) );
        }

      }
      this.results.addAll( participantProgressMap.values() );
      this.activitiesTableData.addAll( contestActivitiesMap.values() );
    }
    Collections.sort( this.getResults(), new ParticipantProgressViewComparator() );
    this.activityTotal = SSIContestUtil.getFormattedValue( contestActivityTotals.values().iterator().next(), activityPrecision );
  }

  class ParticipantProgressViewComparator implements Comparator<ParticipantProgressView>
  {

    public int compare( ParticipantProgressView participantProgressViewOne, ParticipantProgressView participantProgressViewTwo )
    {
      return participantProgressViewOne.getParticipantName().compareToIgnoreCase( participantProgressViewTwo.getParticipantName() );

    }
  }

  public String getErrorFileUrl()
  {
    return errorFileUrl;
  }

  public void setErrorFileUrl( String errorFileUrl )
  {
    this.errorFileUrl = errorFileUrl;
  }

  public String getErrorCount()
  {
    return errorCount;
  }

  public void setErrorCount( String errorCount )
  {
    this.errorCount = errorCount;
  }

  public String getForwardUrl()
  {
    return forwardUrl;
  }

  public void setForwardUrl( String forwardUrl )
  {
    this.forwardUrl = forwardUrl;
  }

  public boolean isUploadInProgress()
  {
    return uploadInProgress;
  }

  public void setUploadInProgress( boolean uploadInProgress )
  {
    this.uploadInProgress = uploadInProgress;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public String getMeasureType()
  {
    return measureType;
  }

  public void setMeasureType( String measureType )
  {
    this.measureType = measureType;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getActivityAsOfDate()
  {
    return activityAsOfDate;
  }

  public void setActivityAsOfDate( String activityAsOfDate )
  {
    this.activityAsOfDate = activityAsOfDate;
  }

  public List<ActivitiesTableDataView> getActivitiesTableData()
  {
    return activitiesTableData;
  }

  public void setActivitiesTableData( List<ActivitiesTableDataView> activitiesTableData )
  {
    this.activitiesTableData = activitiesTableData;
  }

  public List<ParticipantProgressView> getResults()
  {
    return results;
  }

  public void setResults( List<ParticipantProgressView> results )
  {
    this.results = results;
  }

  public String getActivityTotal()
  {
    return activityTotal;
  }

  public void setActivityTotal( String activityTotal )
  {
    this.activityTotal = activityTotal;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public int getCurrent()
  {
    return current;
  }

  public void setCurrent( int current )
  {
    this.current = current;
  }

}

// DTGT Activity Totals
class ActivitiesTableDataView
{
  private String activityName;
  private String activityTotal;

  public ActivitiesTableDataView( String activityName, String activityTotal )
  {
    super();
    this.activityName = activityName;
    this.activityTotal = activityTotal;
  }

  public String getActivityName()
  {
    return activityName;
  }

  public void setActivityName( String activityName )
  {
    this.activityName = activityName;
  }

  public String getActivityTotal()
  {
    return activityTotal;
  }

  public void setActivityTotal( String activityTotal )
  {
    this.activityTotal = activityTotal;
  }
}

class ParticipantProgressView
{

  private int index; // order of results
  private Long id; // participant id
  private String participantName; // "LastName, FirstName",
  private String activityName; // only for objectives, step it up and stack rank
  private String activity; // Activity Amount; only for objectives, step it up and stack rank

  List<ActivityView> activities = new ArrayList<ActivityView>(); // only for DTGT

  public ParticipantProgressView( int index, Long id, String participantName, String activityName, String activity, List<ActivityView> activities )
  {
    super();
    this.index = index;
    this.id = id;
    this.participantName = participantName;
    this.activityName = activityName;
    this.activity = activity;
    this.activities = activities;
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex( int index )
  {
    this.index = index;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getParticipantName()
  {
    return participantName;
  }

  public void setParticipantName( String participantName )
  {
    this.participantName = participantName;
  }

  public String getActivityName()
  {
    return activityName;
  }

  public void setActivityName( String activityName )
  {
    this.activityName = activityName;
  }

  public String getActivity()
  {
    return activity;
  }

  public void setActivity( String activity )
  {
    this.activity = activity;
  }

  public List<ActivityView> getActivities()
  {
    return activities;
  }

  public void setActivities( List<ActivityView> activities )
  {
    this.activities = activities;
  }

}

class ActivityView
{
  private Long id; // activity id
  private String activityName;
  private String activity; // Activity Amount
  private int activityNumber;

  public ActivityView( Long id, int activityNumber, String activityName, String activity )
  {
    super();
    this.id = id;
    this.activityName = activityName;
    this.activity = activity;
    this.activityNumber = activityNumber;
  }

  public int getActivityNumber()
  {
    return activityNumber;
  }

  public void setActivityNumber( int activityNumber )
  {
    this.activityNumber = activityNumber;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getActivityName()
  {
    return activityName;
  }

  public void setActivityName( String activityName )
  {
    this.activityName = activityName;
  }

  public String getActivity()
  {
    return activity;
  }

  public void setActivity( String activity )
  {
    this.activity = activity;
  }
}
