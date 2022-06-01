
package com.biperf.core.value.ssi;

import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.participant.Participant;

/**
 * @author dudam
 * @since Dec 31, 2014
 * @version 1.0
 */
public class SSIContestValueBean
{

  private String contestCreatedBy;
  private String contestName;
  private String description;
  private String message;
  private String attachmentTitle;
  private String attachmentOriginalName;
  private String attachmentUrl;
  private String attachmentType;
  private String level1Approver;
  private String level2Approver;
  private int participantCount;
  private Long payoutSumDtgt;
  private SSIPayoutType payoutType;
  private SSIActivityMeasureType activityMeasureType;
  private Currency activityMeasureCurrency;
  private Currency payoutOtherCurrency;

  private String descriptionAtn;
  private String descriptionDtgt;
  private String descriptionO;
  private String descriptionStu;
  private String descriptionSr;

  Map<String, Set<Participant>> selectedContestApprovers;

  public String getContestCreatedBy()
  {
    return contestCreatedBy;
  }

  public void setContestCreatedBy( String contestCreatedBy )
  {
    this.contestCreatedBy = contestCreatedBy;
  }

  public String getContestName()
  {
    return contestName;
  }

  public void setContestName( String contestName )
  {
    this.contestName = contestName;
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

  public String getAttachmentTitle()
  {
    return attachmentTitle;
  }

  public void setAttachmentTitle( String attachmentTitle )
  {
    this.attachmentTitle = attachmentTitle;
  }

  public String getAttachmentOriginalName()
  {
    return attachmentOriginalName;
  }

  public void setAttachmentOriginalName( String attachmentOriginalName )
  {
    this.attachmentOriginalName = attachmentOriginalName;
  }

  public String getAttachmentUrl()
  {
    return attachmentUrl;
  }

  public void setAttachmentUrl( String attachmentUrl )
  {
    this.attachmentUrl = attachmentUrl;
  }

  public String getAttachmentType()
  {
    return attachmentType;
  }

  public void setAttachmentType( String attachmentType )
  {
    this.attachmentType = attachmentType;
  }

  public String getLevel1Approver()
  {
    return level1Approver;
  }

  public void setLevel1Approver( String level1Approver )
  {
    this.level1Approver = level1Approver;
  }

  public String getLevel2Approver()
  {
    return level2Approver;
  }

  public void setLevel2Approver( String level2Approver )
  {
    this.level2Approver = level2Approver;
  }

  public int getParticipantCount()
  {
    return participantCount;
  }

  public void setParticipantCount( int participantCount )
  {
    this.participantCount = participantCount;
  }

  public Long getPayoutSumDtgt()
  {
    return payoutSumDtgt;
  }

  public void setPayoutSumDtgt( Long payoutSumDtgt )
  {
    this.payoutSumDtgt = payoutSumDtgt;
  }

  public SSIPayoutType getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( SSIPayoutType payoutType )
  {
    this.payoutType = payoutType;
  }

  public SSIActivityMeasureType getActivityMeasureType()
  {
    return activityMeasureType;
  }

  public void setActivityMeasureType( SSIActivityMeasureType activityMeasureType )
  {
    this.activityMeasureType = activityMeasureType;
  }

  public Currency getActivityMeasureCurrency()
  {
    return activityMeasureCurrency;
  }

  public void setActivityMeasureCurrency( Currency activityMeasureCurrency )
  {
    this.activityMeasureCurrency = activityMeasureCurrency;
  }

  public Currency getPayoutOtherCurrency()
  {
    return payoutOtherCurrency;
  }

  public void setPayoutOtherCurrency( Currency payoutOtherCurrency )
  {
    this.payoutOtherCurrency = payoutOtherCurrency;
  }

  public String getDescriptionAtn()
  {
    return descriptionAtn;
  }

  public void setDescriptionAtn( String descriptionAtn )
  {
    this.descriptionAtn = descriptionAtn;
  }

  public String getDescriptionDtgt()
  {
    return descriptionDtgt;
  }

  public void setDescriptionDtgt( String descriptionDtgt )
  {
    this.descriptionDtgt = descriptionDtgt;
  }

  public String getDescriptionO()
  {
    return descriptionO;
  }

  public void setDescriptionO( String descriptionO )
  {
    this.descriptionO = descriptionO;
  }

  public String getDescriptionStu()
  {
    return descriptionStu;
  }

  public void setDescriptionStu( String descriptionStu )
  {
    this.descriptionStu = descriptionStu;
  }

  public String getDescriptionSr()
  {
    return descriptionSr;
  }

  public void setDescriptionSr( String descriptionSr )
  {
    this.descriptionSr = descriptionSr;
  }

  public Map<String, Set<Participant>> getSelectedContestApprovers()
  {
    return selectedContestApprovers;
  }

  public void setSelectedContestApprovers( Map<String, Set<Participant>> selectedContestApprovers )
  {
    this.selectedContestApprovers = selectedContestApprovers;
  }

}
