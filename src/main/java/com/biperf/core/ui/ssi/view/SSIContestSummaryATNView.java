
package com.biperf.core.ui.ssi.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.value.ssi.SSIContestAwardHistoryTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;

/**
 * 
 * SSIContestSummaryATNView.
 * 
 * @author kandhi
 * @since Feb 11, 2015
 * @version 1.0
 */
public class SSIContestSummaryATNView
{
  private String editUrl;
  private String issueUrl;
  private String id;
  private String startDate;
  private String endDate;
  private String name;
  private String description;
  private SSIPaxContestBadgeView badge;
  private int totalParticipantsCount;
  private String totalActivity;
  private String totalPayoutAmount;
  private String sortedOn;
  private String sortedBy;
  private String payoutType;
  private String measureType;
  private String contestCreator;
  private String attachmentTitle;
  private String attachmentUrl;
  private String status;
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  public SSIContestSummaryATNView( SSIContest contest, SSIContestValueBean valueBean, String sortedOn, String sortedBy )
  {
    this.id = getClientState( contest.getId() );
    this.startDate = DateUtils.toDisplayString( contest.getStartDate() );
    this.endDate = DateUtils.toDisplayString( contest.getEndDate() );
    String contestName = valueBean.getContestName();
    this.name = contestName.length() > 50 ? contestName.substring( 0, 50 ) : contestName;
    this.description = valueBean.getDescription();
    this.status = contest.getStatus().getCode();
    this.payoutType = contest.getPayoutType() != null ? contest.getPayoutType().getCode() : null;
    this.badge = new SSIPaxContestBadgeView( contest.getBadgeRule() );
    this.editUrl = "createGeneralInfoAwardThemNow.do?method=editInfo&contestId=" + this.id;
    this.issueUrl = "createGeneralInfoAwardThemNow.do?method=issueMoreAwards&contestId=" + this.id;
    this.measureType = contest.getActivityMeasureType() != null ? contest.getActivityMeasureType().getCode() : null;
    this.contestCreator = valueBean.getContestCreatedBy();
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
    this.setAttachmentTitle( valueBean.getAttachmentTitle() );
    this.setAttachmentUrl( valueBean.getAttachmentUrl() );
  }

  public void setTotalValues( SSIContestAwardHistoryTotalsValueBean totalsBean, int totalParticipantsCount, SSIContest contest, String activityMeasureCurrencyCode, String payoutOtherCurrencyCode )
  {
    this.totalParticipantsCount = totalParticipantsCount;
    if ( contest.getActivityMeasureType().isCurrency() )
    {
      this.totalActivity = totalsBean.getObjectiveAmount() != null ? NumberFormatUtil.getCurrencyWithSymbol( totalsBean.getObjectiveAmount(), activityMeasureCurrencyCode ) : null;
    }
    else
    {
      this.totalActivity = totalsBean.getObjectiveAmount() != null ? NumberFormatUtil.getLocaleBasedDobleNumberFormat( totalsBean.getObjectiveAmount() ) : null;
    }

    if ( contest.getPayoutType() != null && contest.getPayoutType().isOther() && contest.getPayoutOtherCurrencyCode() != null )
    {
      this.totalPayoutAmount = totalsBean.getObjectivePayout() != null ? NumberFormatUtil.getCurrencyWithSymbol( totalsBean.getObjectivePayout(), payoutOtherCurrencyCode ) : null;
    }
    else
    {
      totalPayoutAmount = NumberFormatUtil.getLocaleBasedNumberFormat( totalsBean.getObjectivePayout(), 0 );
    }

  }

  private String getClientState( Long id )
  {

    // put the client state instead of direct id
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( "contestId", id );
    String password = ClientStatePasswordManager.getPassword();
    try
    {
      return URLEncoder.encode( ClientStateSerializer.serialize( clientStateParamMap, password ), "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Encode Client State: " + e );
    }
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getEditUrl()
  {
    return editUrl;
  }

  public void setEditUrl( String editUrl )
  {
    this.editUrl = editUrl;
  }

  public String getIssueUrl()
  {
    return issueUrl;
  }

  public void setIssueUrl( String issueUrl )
  {
    this.issueUrl = issueUrl;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public SSIPaxContestBadgeView getBadge()
  {
    return badge;
  }

  public void setBadge( SSIPaxContestBadgeView badge )
  {
    this.badge = badge;
  }

  public int getTotalParticipantsCount()
  {
    return totalParticipantsCount;
  }

  public void setTotalParticipantsCount( int totalParticipantsCount )
  {
    this.totalParticipantsCount = totalParticipantsCount;
  }

  public String getTotalActivity()
  {
    return totalActivity;
  }

  public void setTotalActivity( String totalActivity )
  {
    this.totalActivity = totalActivity;
  }

  public String getTotalPayoutAmount()
  {
    return totalPayoutAmount;
  }

  public void setTotalPayoutAmount( String totalPayoutAmount )
  {
    this.totalPayoutAmount = totalPayoutAmount;
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

  public String getMeasureType()
  {
    return measureType;
  }

  public void setMeasureType( String measureType )
  {
    this.measureType = measureType;
  }

  public String getContestCreator()
  {
    return contestCreator;
  }

  public void setContestCreator( String contestCreator )
  {
    this.contestCreator = contestCreator;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public String getAttachmentTitle()
  {
    return attachmentTitle;
  }

  public void setAttachmentTitle( String attachmentTitle )
  {
    this.attachmentTitle = attachmentTitle;
  }

  public String getAttachmentUrl()
  {
    return attachmentUrl;
  }

  public void setAttachmentUrl( String attachmentUrl )
  {
    this.attachmentUrl = attachmentUrl;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

}
