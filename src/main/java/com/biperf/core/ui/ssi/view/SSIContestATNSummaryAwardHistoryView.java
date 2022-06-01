
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SSIContestIssuanceStatusType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestAwardHistoryValueBean;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * SSIContestATNSummaryAwardHistoryView.
 * 
 * @author kandhi
 * @since Feb 11, 2015
 * @version 1.0
 */
public class SSIContestATNSummaryAwardHistoryView
{
  public static final int RECORDS_PER_PAGE = 5;
  private List<WebErrorMessage> messages;
  private int currentPage;
  private int recordsPerPage;
  private int recordsTotal;
  private List<SSIContestATNSummaryAwardHistoryRecordView> contests = new ArrayList<SSIContestATNSummaryAwardHistoryRecordView>();

  public SSIContestATNSummaryAwardHistoryView()
  {

  }

  public SSIContestATNSummaryAwardHistoryView( WebErrorMessage message )
  {
    this.messages = new ArrayList<WebErrorMessage>();
    this.messages.add( message );
  }

  public SSIContestATNSummaryAwardHistoryView( int currentPage,
                                               List<SSIContestAwardHistoryValueBean> awardHistoryBeans,
                                               int totalRecords,
                                               SSIContest ssiContest,
                                               String activityMeasureCurrencyCode,
                                               String payoutOtherCurrencyCode )
  {
    super();
    this.currentPage = currentPage;
    for ( SSIContestAwardHistoryValueBean awardHistoryBean : awardHistoryBeans )
    {
      this.contests.add( new SSIContestATNSummaryAwardHistoryRecordView( awardHistoryBean, ssiContest, activityMeasureCurrencyCode, payoutOtherCurrencyCode ) );
    }
    this.recordsPerPage = RECORDS_PER_PAGE;
    this.recordsTotal = totalRecords;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public int getRecordsPerPage()
  {
    return recordsPerPage;
  }

  public void setRecordsPerPage( int recordsPerPage )
  {
    this.recordsPerPage = recordsPerPage;
  }

  public int getRecordsTotal()
  {
    return recordsTotal;
  }

  public void setRecordsTotal( int recordsTotal )
  {
    this.recordsTotal = recordsTotal;
  }

  public List<SSIContestATNSummaryAwardHistoryRecordView> getContests()
  {
    return contests;
  }

  public void setContests( List<SSIContestATNSummaryAwardHistoryRecordView> contests )
  {
    this.contests = contests;
  }

  protected class SSIContestATNSummaryAwardHistoryRecordView
  {
    private String id;
    private int participantsCount;
    private String amount;
    private String payoutDescription;
    private String payoutAmount;
    private String dateCreated;
    private String status;
    private String statusDescription;
    private String deniedReason;
    @JsonProperty( "canApprove" )
    private boolean canApprove = Boolean.FALSE;

    public SSIContestATNSummaryAwardHistoryRecordView( SSIContestAwardHistoryValueBean awardHistoryBean, SSIContest ssiContest, String activityMeasureCurrencyCode, String payoutOtherCurrencyCode )
    {
      this.payoutDescription = awardHistoryBean.getPayoutDescription();
      this.dateCreated = DateUtils.toDisplayString( awardHistoryBean.getDateCreated() );
      this.setDeniedReason( awardHistoryBean.getDenialReason() );
      if ( !StringUtil.isNullOrEmpty( awardHistoryBean.getStatus() ) )
      {
        if ( isFinalizeResult( awardHistoryBean.getStatus() ) )
        {
          this.setStatusDescription( SSIContestIssuanceStatusType.lookup( "approved" ).getName() );
          this.status = SSIContestIssuanceStatusType.lookup( "approved" ).getCode();
        }
        else
        {
          this.setStatusDescription( SSIContestIssuanceStatusType.lookup( awardHistoryBean.getStatus() ).getName() );
          this.status = SSIContestIssuanceStatusType.lookup( awardHistoryBean.getStatus() ).getCode();
        }

      }
      if ( ssiContest.getActivityMeasureType().isCurrency() && awardHistoryBean.getAmount() != null )
      {
        this.amount = NumberFormatUtil.getCurrencyWithSymbol( awardHistoryBean.getAmount(), activityMeasureCurrencyCode );
      }
      else
      {
        this.amount = NumberFormatUtil.getLocaleBasedDobleNumberFormat( awardHistoryBean.getAmount() );
      }

      if ( ssiContest.getPayoutType() != null && ssiContest.getPayoutType().isOther() && ssiContest.getPayoutOtherCurrencyCode() != null )
      {
        this.payoutAmount = awardHistoryBean.getPayoutAmount() != null ? NumberFormatUtil.getCurrencyWithSymbol( awardHistoryBean.getPayoutAmount(), payoutOtherCurrencyCode ) : null;
      }
      else
      {
        this.payoutAmount = NumberFormatUtil.getLocaleBasedNumberFormat( awardHistoryBean.getPayoutAmount(), 0 );
      }

      this.participantsCount = awardHistoryBean.getParticipantsCount();
      // set the client State
      this.setId( awardHistoryBean.getClientStateId() );
      this.setCanApprove( awardHistoryBean.isCanApprove() );
    }

    private boolean isFinalizeResult( String status )
    {
      return "finalize_results".equalsIgnoreCase( status );
    }

    public int getParticipantsCount()
    {
      return participantsCount;
    }

    public void setParticipantsCount( int participantsCount )
    {
      this.participantsCount = participantsCount;
    }

    public String getAmount()
    {
      return amount;
    }

    public void setAmount( String amount )
    {
      this.amount = amount;
    }

    public String getPayoutDescription()
    {
      return payoutDescription;
    }

    public void setPayoutDescription( String payoutDescription )
    {
      this.payoutDescription = payoutDescription;
    }

    public String getPayoutAmount()
    {
      return payoutAmount;
    }

    public void setPayoutAmount( String payoutAmount )
    {
      this.payoutAmount = payoutAmount;
    }

    public String getDateCreated()
    {
      return dateCreated;
    }

    public void setDateCreated( String dateCreated )
    {
      this.dateCreated = dateCreated;
    }

    public String getStatus()
    {
      return status;
    }

    public void setStatus( String status )
    {
      this.status = status;
    }

    public String getId()
    {
      return id;
    }

    public void setId( String id )
    {
      this.id = id;
    }

    public String getStatusDescription()
    {
      return statusDescription;
    }

    public void setStatusDescription( String statusDescription )
    {
      this.statusDescription = statusDescription;
    }

    public String getDeniedReason()
    {
      return deniedReason;
    }

    public void setDeniedReason( String deniedReason )
    {
      this.deniedReason = deniedReason;
    }

    public boolean isCanApprove()
    {
      return canApprove;
    }

    public void setCanApprove( boolean canApprove )
    {
      this.canApprove = canApprove;
    }

  }
}
