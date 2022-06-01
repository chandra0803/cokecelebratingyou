
package com.biperf.core.domain.promotion;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.participant.Participant;

public class ScheduledRecognition extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private String triggerName = null;
  private String triggerGroup = null;
  private Promotion promotion = null;
  private Participant sender = null;
  private Participant recipient = null;
  private Date deliveryDate;
  private boolean fired = false;
  private Long claimId;

  private String isScheduled;
  private String submitterNodeId;
  private String proxyUserId;
  private String recipientNodeId;
  private String submitterComments;
  private String budgetId;
  private String teamId;
  private String behavior;
  private String certificateId;
  private String cardId;
  private String copySender;
  private String copyManager;
  private String copyOthers;
  private String sendCopyToOthers;
  private String awardQuantity;
  private String calculatorScore;
  private String levelId;
  private String productId;
  private String recipientCountryCode;
  private String claimElements;
  private String calculatorResponses;
  private String anniversaryNumberOfDays;
  private String anniversaryNumberOfYears;
  private String celebrationManagerMessageId;
  private boolean hidePublicRecognition;
  private RecognitionClaimSource source;
  private String ownCardName;

  public String getTriggerName()
  {
    return triggerName;
  }

  public void setTriggerName( String triggerName )
  {
    this.triggerName = triggerName;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Participant getSender()
  {
    return sender;
  }

  public void setSender( Participant sender )
  {
    this.sender = sender;
  }

  public Participant getRecipient()
  {
    return recipient;
  }

  public void setRecipient( Participant recipient )
  {
    this.recipient = recipient;
  }

  public String getTriggerGroup()
  {
    return triggerGroup;
  }

  public void setTriggerGroup( String triggerGroup )
  {
    this.triggerGroup = triggerGroup;
  }

  public boolean isFired()
  {
    return fired;
  }

  public void setFired( boolean fired )
  {
    this.fired = fired;
  }

  public boolean getFired()
  {
    return isFired();
  }

  public Date getDeliveryDate()
  {
    return deliveryDate;
  }

  public void setDeliveryDate( Date deliveryDate )
  {
    this.deliveryDate = deliveryDate;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getIsScheduled()
  {
    return isScheduled;
  }

  public void setIsScheduled( String isScheduled )
  {
    this.isScheduled = isScheduled;
  }

  public String getSubmitterNodeId()
  {
    return submitterNodeId;
  }

  public void setSubmitterNodeId( String submitterNodeId )
  {
    this.submitterNodeId = submitterNodeId;
  }

  public String getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( String proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public String getRecipientNodeId()
  {
    return recipientNodeId;
  }

  public void setRecipientNodeId( String recipientNodeId )
  {
    this.recipientNodeId = recipientNodeId;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public String getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( String budgetId )
  {
    this.budgetId = budgetId;
  }

  public String getTeamId()
  {
    return teamId;
  }

  public void setTeamId( String teamId )
  {
    this.teamId = teamId;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  public String getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( String certificateId )
  {
    this.certificateId = certificateId;
  }

  public String getCardId()
  {
    return cardId;
  }

  public void setCardId( String cardId )
  {
    this.cardId = cardId;
  }

  public String getCopySender()
  {
    return copySender;
  }

  public void setCopySender( String copySender )
  {
    this.copySender = copySender;
  }

  public String getCopyManager()
  {
    return copyManager;
  }

  public void setCopyManager( String copyManager )
  {
    this.copyManager = copyManager;
  }

  public String getCopyOthers()
  {
    return copyOthers;
  }

  public void setCopyOthers( String copyOthers )
  {
    this.copyOthers = copyOthers;
  }

  public String getSendCopyToOthers()
  {
    return sendCopyToOthers;
  }

  public void setSendCopyToOthers( String sendCopyToOthers )
  {
    this.sendCopyToOthers = sendCopyToOthers;
  }

  public String getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( String awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public String getCalculatorScore()
  {
    return calculatorScore;
  }

  public void setCalculatorScore( String calculatorScore )
  {
    this.calculatorScore = calculatorScore;
  }

  public String getLevelId()
  {
    return levelId;
  }

  public void setLevelId( String levelId )
  {
    this.levelId = levelId;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId( String productId )
  {
    this.productId = productId;
  }

  public String getRecipientCountryCode()
  {
    return recipientCountryCode;
  }

  public void setRecipientCountryCode( String recipientCountryCode )
  {
    this.recipientCountryCode = recipientCountryCode;
  }

  public String getClaimElements()
  {
    return claimElements;
  }

  public void setClaimElements( String claimElements )
  {
    this.claimElements = claimElements;
  }

  public String getCalculatorResponses()
  {
    return calculatorResponses;
  }

  public void setCalculatorResponses( String calculatorResponses )
  {
    this.calculatorResponses = calculatorResponses;
  }

  public String getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( String anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public String getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( String anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public String getCelebrationManagerMessageId()
  {
    return celebrationManagerMessageId;
  }

  public void setCelebrationManagerMessageId( String celebrationManagerMessageId )
  {
    this.celebrationManagerMessageId = celebrationManagerMessageId;
  }

  public boolean isHidePublicRecognition()
  {
    return hidePublicRecognition;
  }

  public void setHidePublicRecognition( boolean hidePublicRecognition )
  {
    this.hidePublicRecognition = hidePublicRecognition;
  }

  public RecognitionClaimSource getSource()
  {
    return source;
  }

  public void setSource( RecognitionClaimSource source )
  {
    this.source = source;
  }

  public String getOwnCardName()
  {
    return ownCardName;
  }

  public void setOwnCardName( String ownCardName )
  {
    this.ownCardName = ownCardName;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ScheduledRecognition ) )
    {
      return false;
    }

    final ScheduledRecognition trigger = (ScheduledRecognition)object;

    if ( getTriggerName() != null )
    {
      if ( !getTriggerName().equals( trigger.getTriggerName() ) )
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return triggerName != null ? triggerName.hashCode() : 0;
  }

}
