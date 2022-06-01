
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.ui.ssi.view.SSIContestApprovalLevelsView.SSIContestApprover;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestApproveSummaryView.
 * 
 * @author kandhi
 * @since Dec 15, 2014
 * @version 1.0
 */
public class SSIContestApproveSummaryView
{

  private Long id;
  private String contestType;
  private String startDate;
  private String endDate;
  private String tileStartDate;
  private String contestCreated;
  private String contestCreator;
  private boolean approvalRequired;
  private List<SSIContestApprovalLevelsView> approvalLevels;
  private String approvlStatus;
  private String status;
  private boolean includeMessage;
  private String message;
  private String attachmentTitle;
  private String attachmentUrl;
  private String activityDescription;
  private String activityMeasuredIn;
  private String activityMeasuredName;
  private String payoutType;
  private String payoutTypeName;
  private String name;
  private String description;
  private int participantsCount;
  private int managersCount;
  private int superViewersCount;
  private String clientState;
  private String chargeContestTo;
  private boolean includeStackRanking;
  private String role;
  private List<SSIContestBillCodeView> billCodes;
  private boolean billCodeRequired;

  public SSIContestApproveSummaryView()
  {
  }

  public SSIContestApproveSummaryView( SSIContest contest, SSIContestValueBean valueBean )
  {
    this.setId( contest.getId() );
    this.setContestType( contest.getContestTypeName() );
    this.setStartDate( DateUtils.toDisplayString( contest.getStartDate() ) );
    this.setEndDate( DateUtils.toDisplayString( contest.getEndDate() ) );
    this.setTileStartDate( DateUtils.toDisplayString( contest.getDisplayStartDate() ) );
    this.setContestCreated( DateUtils.toDisplayString( contest.getAuditCreateInfo().getDateCreated() ) );
    this.setContestCreator( valueBean.getContestCreatedBy() );
    SSIPromotion promotion = contest.getPromotion();
    setApprovalLevels( contest, promotion, valueBean.getLevel1Approver(), valueBean.getLevel2Approver(), valueBean.getSelectedContestApprovers() );
    this.setBillCodeRequired( promotion.isBillCodesActive() );
    this.setApprovlStatus( contest.getStatus().getCode() );
    this.setStatus( contest.getStatus().getCode() );
    this.setIncludeMessage( contest.getIncludePersonalMessage() != null ? contest.getIncludePersonalMessage() : false );
    if ( this.isIncludeMessage() )
    {
      this.setMessage( valueBean.getMessage() );
    }
    this.setAttachmentTitle( valueBean.getAttachmentTitle() );
    this.setAttachmentUrl( valueBean.getAttachmentUrl() );
    this.setActivityDescription( contest.getActivityDescription() );
    this.setActivityMeasuredIn( contest.getActivityMeasureType() != null ? contest.getActivityMeasureType().getCode() : null );
    this.setActivityMeasuredName( contest.getActivityMeasureType() != null ? contest.getActivityMeasureType().getName() : null );
    this.setPayoutType( contest.getPayoutType() != null ? contest.getPayoutType().getCode() : null );
    this.setPayoutTypeName( contest.getPayoutType() != null ? contest.getPayoutType().getName() : null );
    this.setName( valueBean.getContestName() );
    this.setDescription( valueBean.getDescription() );
    this.setChargeContestTo( contest.getBillPayoutCodeType() != null ? contest.getDisplayBillCode() : null );
    this.setIncludeStackRanking( contest.isIncludeStackRank() );
  }

  protected void setApprovalLevels( SSIContest contest, SSIPromotion promotion, String lvl1Approver, String lvl2Approver, Map<String, Set<Participant>> selectedContestApprovers )
  {
    this.approvalRequired = promotion.getContestApprovalLevels() > 0 ? true : false;
    if ( this.approvalRequired )
    {
      int contestApprovalLevels = promotion.getContestApprovalLevels();
      approvalLevels = new ArrayList<SSIContestApprovalLevelsView>();
      SSIContestApprovalLevelsView approvalLevel = new SSIContestApprovalLevelsView();

      approvalLevel.setName( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.LEVEL1_NAME" ) );
      if ( contest.getLevelApproved() > 0 )
      {
        if ( contest.getDateApprovedLevel1() != null && contest.getApprovedByLevel1() != null )
        {
          approvalLevel.setApprovedBy( lvl1Approver );
          approvalLevel.setApproved( true );
        }
      }
      // populate the contest approver
      if ( contest.getStatus().isWaitingForApproval() && selectedContestApprovers != null )
      {
        Set<Participant> selectedLevel1Approvers = selectedContestApprovers.get( "selected_contest_approver_level_1" );
        getApproversList( approvalLevel, selectedLevel1Approvers );
      }
      approvalLevels.add( approvalLevel );
      if ( contestApprovalLevels > 1 )
      {
        approvalLevel = new SSIContestApprovalLevelsView();
        approvalLevel.setName( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.LEVEL2_NAME" ) );
        if ( contest.getLevelApproved() > 1 )
        {
          if ( contest.getDateApprovedLevel2() != null && contest.getApprovedByLevel2() != null )
          {
            approvalLevel.setApprovedBy( lvl2Approver );
            approvalLevel.setApproved( true );
          }
        }
        if ( contest.getStatus().isWaitingForApproval() && selectedContestApprovers != null )
        {
          Set<Participant> selectedLevel2Approvers = selectedContestApprovers.get( "selected_contest_approver_level_2" );
          getApproversList( approvalLevel, selectedLevel2Approvers );
        }
        approvalLevels.add( approvalLevel );
      }
    }
  }

  private void getApproversList( SSIContestApprovalLevelsView approvalLevel, Set<Participant> selectedLevel1Approvers )
  {
    for ( Participant participant : selectedLevel1Approvers )
    {
      SSIContestApprover contestApprover = approvalLevel.new SSIContestApprover();
      contestApprover.setId( participant.getId() );
      contestApprover.setName( participant.getFirstName() + ", " + participant.getLastName() );
      approvalLevel.getApproverList().add( contestApprover );
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
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

  public String getContestCreated()
  {
    return contestCreated;
  }

  public void setContestCreated( String contestCreated )
  {
    this.contestCreated = contestCreated;
  }

  public String getContestCreator()
  {
    return contestCreator;
  }

  public void setContestCreator( String contestCreator )
  {
    this.contestCreator = contestCreator;
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

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

  public boolean isApprovalRequired()
  {
    return approvalRequired;
  }

  public void setApprovalRequired( boolean approvalRequired )
  {
    this.approvalRequired = approvalRequired;
  }

  public List<SSIContestApprovalLevelsView> getApprovalLevels()
  {
    return approvalLevels;
  }

  public void setApprovalLevels( List<SSIContestApprovalLevelsView> approvalLevels )
  {
    this.approvalLevels = approvalLevels;
  }

  public String getApprovlStatus()
  {
    return approvlStatus;
  }

  public void setApprovlStatus( String approvlStatus )
  {
    this.approvlStatus = approvlStatus;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public boolean isIncludeMessage()
  {
    return includeMessage;
  }

  public void setIncludeMessage( boolean includeMessage )
  {
    this.includeMessage = includeMessage;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
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

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
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

  public String getPayoutTypeName()
  {
    return payoutTypeName;
  }

  public void setPayoutTypeName( String payoutTypeName )
  {
    this.payoutTypeName = payoutTypeName;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
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

  public String getChargeContestTo()
  {
    return chargeContestTo;
  }

  public void setChargeContestTo( String chargeContestTo )
  {
    this.chargeContestTo = chargeContestTo;
  }

  public boolean isIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public int getSuperViewersCount()
  {
    return superViewersCount;
  }

  public void setSuperViewersCount( int superViewersCount )
  {
    this.superViewersCount = superViewersCount;
  }

  public List<SSIContestBillCodeView> getBillCodes()
  {
    return billCodes;
  }

  public void setBillCodes( List<SSIContestBillCodeView> billCodes )
  {
    this.billCodes = billCodes;
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
