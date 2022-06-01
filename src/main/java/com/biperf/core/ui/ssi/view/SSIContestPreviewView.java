
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.utils.DateUtils;

/**
 * 
 * SSIContestPreviewView.
 * 
 * @author patelp
 * @since Dec 26, 2014
 * @version 1.0
 */

public class SSIContestPreviewView
{

  private Long id;
  private String clientState;
  private String contestType;
  private String status;
  private String startDate;
  private String endDate;
  private String tileStartDate;
  private String name;
  private String description;
  private String attachmentTitle;
  private String attachmentUrl;
  private String chargeContestTo;
  private int participantsCount;
  private int managersCount;
  private int superViewersCount;
  private boolean includeMessage;
  private String message;
  private String nextUrl;
  private String activityMeasuredIn;
  private String activityMeasuredName;
  private String payoutType;
  private String payoutTypeName;
  private String collectDataMethod;
  List<SSIContestBillCodeView> billCodes;
  private boolean billCodeRequired;

  public List<SSIContestBillCodeView> getBillCodes()
  {
    return billCodes;
  }

  public void setBillCodes( List<SSIContestBillCodeView> billCodes )
  {
    this.billCodes = billCodes;
  }

  public SSIContestPreviewView( SSIContestPreviewViewWrapper contestPreviewViewWrapper )
  {
    this.setId( contestPreviewViewWrapper.getContest().getId() );
    this.setClientState( contestPreviewViewWrapper.getSsiContestClientState() );
    this.setContestType( contestPreviewViewWrapper.getContest().getContestTypeName() );
    this.setStatus( contestPreviewViewWrapper.getContest().getStatus() != null ? contestPreviewViewWrapper.getContest().getStatus().getCode() : null );
    this.setStartDate( DateUtils.toDisplayString( contestPreviewViewWrapper.getContest().getStartDate() ) );
    this.setEndDate( DateUtils.toDisplayString( contestPreviewViewWrapper.getContest().getEndDate() ) );
    this.setTileStartDate( DateUtils.toDisplayString( contestPreviewViewWrapper.getContest().getDisplayStartDate() ) );
    this.setIncludeMessage( contestPreviewViewWrapper.getContest().getIncludePersonalMessage() != null ? contestPreviewViewWrapper.getContest().getIncludePersonalMessage() : false );
    this.setAttachmentTitle( contestPreviewViewWrapper.getContestValueBean().getAttachmentTitle() );
    this.setAttachmentUrl( contestPreviewViewWrapper.getContestValueBean().getAttachmentUrl() );
    this.setName( contestPreviewViewWrapper.getContestValueBean().getContestName() );
    this.setDescription( contestPreviewViewWrapper.getContestValueBean().getDescription() );
    this.setParticipantsCount( contestPreviewViewWrapper.getParticipantsCount() );
    this.setManagersCount( contestPreviewViewWrapper.getManagersCount() );
    this.setSuperViewersCount( contestPreviewViewWrapper.getSuperViewersCount() );
    this.setChargeContestTo( contestPreviewViewWrapper.getContest().getBillPayoutCodeType() != null ? contestPreviewViewWrapper.getContest().getDisplayBillCode() : null );
    this.setNextUrl( contestPreviewViewWrapper.getSysUrl() + "/ssi/previewContest.do" );
    this.setActivityMeasuredIn( contestPreviewViewWrapper.getContest().getActivityMeasureType() != null ? contestPreviewViewWrapper.getContest().getActivityMeasureType().getCode() : null );
    this.setActivityMeasuredName( contestPreviewViewWrapper.getContest().getActivityMeasureType() != null ? contestPreviewViewWrapper.getContest().getActivityMeasureType().getName() : null );
    this.setPayoutType( contestPreviewViewWrapper.getContest().getPayoutType() != null ? contestPreviewViewWrapper.getContest().getPayoutType().getCode() : null );
    this.setPayoutTypeName( contestPreviewViewWrapper.getContest().getPayoutType() != null ? contestPreviewViewWrapper.getContest().getPayoutType().getName() : null );
    this.setBillCodeRequired( contestPreviewViewWrapper.getContest().getPromotion().isBillCodesActive() );
    if ( this.isIncludeMessage() )
    {
      this.setMessage( contestPreviewViewWrapper.getContestValueBean().getMessage() );
    }
    this.setCollectDataMethod( contestPreviewViewWrapper.getContest().getDataCollectionType() );
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

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String ssiContestClientState )
  {
    this.clientState = ssiContestClientState;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
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

  public String getTileStartDate()
  {
    return tileStartDate;
  }

  public void setTileStartDate( String tileStartDate )
  {
    this.tileStartDate = tileStartDate;
  }

  public boolean isIncludeMessage()
  {
    return includeMessage;
  }

  public void setIncludeMessage( boolean includeMessage )
  {
    this.includeMessage = includeMessage;
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

  public String getChargeContestTo()
  {
    return chargeContestTo;
  }

  public void setChargeContestTo( String chargeContestTo )
  {
    this.chargeContestTo = chargeContestTo;
  }

  public int getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( int participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public int getManagersCount()
  {
    return managersCount;
  }

  public void setManagersCount( int managersCount )
  {
    this.managersCount = managersCount;
  }

  public int getSuperViewersCount()
  {
    return superViewersCount;
  }

  public void setSuperViewersCount( int superViewersCount )
  {
    this.superViewersCount = superViewersCount;
  }

  public String getNextUrl()
  {
    return nextUrl;
  }

  public void setNextUrl( String nextUrl )
  {
    this.nextUrl = nextUrl;
  }

  public String getActivityMeasuredIn()
  {
    return activityMeasuredIn;
  }

  public void setActivityMeasuredIn( String activityMeasuredIn )
  {
    this.activityMeasuredIn = activityMeasuredIn;
  }

  public String getActivityMeasuredName()
  {
    return activityMeasuredName;
  }

  public void setActivityMeasuredName( String activityMeasuredName )
  {
    this.activityMeasuredName = activityMeasuredName;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getPayoutTypeName()
  {
    return payoutTypeName;
  }

  public void setPayoutTypeName( String payoutTypeName )
  {
    this.payoutTypeName = payoutTypeName;
  }

  public String getCollectDataMethod()
  {
    return collectDataMethod;
  }

  public void setCollectDataMethod( String collectDataMethod )
  {
    this.collectDataMethod = collectDataMethod;
  }

  public boolean isBillCodeRequired()
  {
    return billCodeRequired;
  }

  public void setBillCodeRequired( boolean billCodeRequired )
  {
    this.billCodeRequired = billCodeRequired;
  }

}
