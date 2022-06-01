package com.biperf.core.ui.ws.rest.sea.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * TODO Javadoc for SubmitRecognitionRestObject.
 * 
 * @author Ravi Kancherla
 * @since Oct 27, 2014
 * @version 1.0
 */
@XmlRootElement
public class RecognitionResponseView extends BaseRestObject
{
  private EmailMessageBody emailMessageBody;
  private List<Long> claimIds = new ArrayList<Long>();
  
  private boolean emailEnabledPromotion;
  
  //Sender Validation
  private boolean senderNotFound;
  private boolean senderNotUnique;
  private boolean senderNotInAudience;
  
  //Recipient Validation
  private List<String> validRecipients = new ArrayList<String>();
  private List<String> invalidRecipients = new ArrayList<String>();
  
  private List<String> recipientsNotUnique = new ArrayList<String>();
  private List<String> recipientsNotInAudience = new ArrayList<String>();

  //Behavior Validation
  private boolean invalidBehavior;
  private boolean invalidRecipient;
  private List<String> validBehaviors = new ArrayList<String>();
  private boolean missingBehavior;
  private boolean behaviorRequired;
  
  // END tag missing
  private boolean endTagFound;
  
  // Message content (exists?)
  private boolean messageEmpty ;

  //Budget Validation
  //private boolean awardsActive;
  private boolean pointsAvailableToPromotion;
  private boolean pointsAvailableToEmail;
  private boolean noBudget;
  private boolean notEnoughBudget;
  private int pointsAvailable;
  private boolean isPromotionPointRange;
  private boolean isPromotionFixedPoints = false;
  private boolean pointsOutOfRange = false;
  private boolean fixedPointsDoesntMatch;
  private int fixedPoints;
  private int pointRangeMin;
  private int pointRangeMax;
  //private boolean doesNotSupportPoints;
  //private boolean pointsNotAvailable;

  //Recognition variables
  private String firstName;
  private String lastName;
  private String promotionName;
  private int expirationDays;
  private String programEmailAddress;
  private String budgetPromotionName;
  private String programName;

  private List<String> messages = new ArrayList<String>();
  
  private boolean isSameSenderAndRecipient;
  
  public EmailMessageBody getEmailMessageBody()
  {
    return emailMessageBody;
  }

  public void setEmailMessageBody( EmailMessageBody emailMessageBody )
  {
    this.emailMessageBody = emailMessageBody;
  }

  public List<Long> getClaimIds()
  {
    return claimIds;
  }

  public void setClaimIds( List<Long> claimIds )
  {
    this.claimIds = claimIds;
  }

  @JsonIgnore
  public boolean isEmailEnabledPromotion()
  {
    return emailEnabledPromotion;
  }

  public void setEmailEnabledPromotion( boolean emailEnabledPromotion )
  {
    this.emailEnabledPromotion = emailEnabledPromotion;
  }

  @JsonIgnore
  public boolean isSenderNotFound()
  {
    return senderNotFound;
  }

  public void setSenderNotFound( boolean senderNotFound )
  {
    this.senderNotFound = senderNotFound;
  }

  @JsonIgnore
  public boolean isSenderNotUnique()
  {
    return senderNotUnique;
  }

  public void setSenderNotUnique( boolean senderNotUnique )
  {
    this.senderNotUnique = senderNotUnique;
  }

  @JsonIgnore
  public boolean isSenderNotInAudience()
  {
    return senderNotInAudience;
  }

  public void setSenderNotInAudience( boolean senderNotInAudience )
  {
    this.senderNotInAudience = senderNotInAudience;
  }

  @JsonIgnore
  public List<String> getValidRecipients()
  {
    return validRecipients;
  }

  public void setValidRecipients( List<String> validRecipients )
  {
    this.validRecipients = validRecipients;
  }

  @JsonIgnore
  public List<String> getInvalidRecipients()
  {
    return invalidRecipients;
  }

  public void setInvalidRecipients( List<String> invalidRecipients )
  {
    this.invalidRecipients = invalidRecipients;
  }
  
  @JsonIgnore
  public boolean isInvalidRecipient()
  {
    return invalidRecipient;
  }

  public void setInvalidRecipient( boolean invalidRecipient )
  {
    this.invalidRecipient = invalidRecipient;
  }

  @JsonIgnore
  public List<String> getRecipientsNotUnique()
  {
    return recipientsNotUnique;
  }

  public void setRecipientsNotUnique( List<String> recipientsNotUnique )
  {
    this.recipientsNotUnique = recipientsNotUnique;
  }

  @JsonIgnore
  public List<String> getRecipientsNotInAudience()
  {
    return recipientsNotInAudience;
  }

  public void setRecipientsNotInAudience( List<String> recipientsNotInAudience )
  {
    this.recipientsNotInAudience = recipientsNotInAudience;
  }

  @JsonIgnore
  public List<String> getValidBehaviors()
  {
    return validBehaviors;
  }

  public void setValidBehaviors( List<String> validBehaviors )
  {
    this.validBehaviors = validBehaviors;
  }
  
  @JsonIgnore
  public boolean isSameSenderAndRecipient()
  {
    return isSameSenderAndRecipient;
  }

  public void setSameSenderAndRecipient( boolean isSameSenderAndRecipient )
  {
    this.isSameSenderAndRecipient = isSameSenderAndRecipient;
  }
  
  @JsonIgnore
  public boolean isPointsAvailableToPromotion()
  {
    return pointsAvailableToPromotion;
  }

  public void setPointsAvailableToPromotion( boolean pointsAvailableToPromotion )
  {
    this.pointsAvailableToPromotion = pointsAvailableToPromotion;
  }

  @JsonIgnore
  public boolean isPointsAvailableToEmail()
  {
    return pointsAvailableToEmail;
  }

  public void setPointsAvailableToEmail( boolean pointsAvailableToEmail )
  {
    this.pointsAvailableToEmail = pointsAvailableToEmail;
  }

  @JsonIgnore
  public boolean isNoBudget()
  {
    return noBudget;
  }

  public void setNoBudget( boolean noBudget )
  {
    this.noBudget = noBudget;
  }

  @JsonIgnore
  public boolean isNotEnoughBudget()
  {
    return notEnoughBudget;
  }

  public void setNotEnoughBudget( boolean notEnoughBudget )
  {
    this.notEnoughBudget = notEnoughBudget;
  }

  @JsonIgnore
  public int getPointsAvailable()
  {
    return pointsAvailable;
  }

  public void setPointsAvailable( int pointsAvailable )
  {
    this.pointsAvailable = pointsAvailable;
  }

  @JsonIgnore
  public boolean isPromotionPointRange()
  {
    return isPromotionPointRange;
  }

  public void setPromotionPointRange( boolean isPromotionPointRange )
  {
    this.isPromotionPointRange = isPromotionPointRange;
  }

  @JsonIgnore
  public boolean isPromotionFixedPoints()
  {
    return isPromotionFixedPoints;
  }

  public void setPromotionFixedPoints( boolean isPromotionFixedPoints )
  {
    this.isPromotionFixedPoints = isPromotionFixedPoints;
  }

  @JsonIgnore
  public boolean isPointsOutOfRange()
  {
    return pointsOutOfRange;
  }

  public void setPointsOutOfRange( boolean pointsOutOfRange )
  {
    this.pointsOutOfRange = pointsOutOfRange;
  }

  @JsonIgnore
  public boolean isFixedPointsDoesntMatch()
  {
    return fixedPointsDoesntMatch;
  }

  public void setFixedPointsDoesntMatch( boolean fixedPointsDoesntMatch )
  {
    this.fixedPointsDoesntMatch = fixedPointsDoesntMatch;
  }

  @JsonIgnore
  public int getFixedPoints()
  {
    return fixedPoints;
  }

  public void setFixedPoints( int fixedPoints )
  {
    this.fixedPoints = fixedPoints;
  }

  @JsonIgnore
  public int getPointRangeMin()
  {
    return pointRangeMin;
  }

  public void setPointRangeMin( int pointRangeMin )
  {
    this.pointRangeMin = pointRangeMin;
  }

  @JsonIgnore
  public int getPointRangeMax()
  {
    return pointRangeMax;
  }

  public void setPointRangeMax( int pointRangeMax )
  {
    this.pointRangeMax = pointRangeMax;
  }

  @JsonIgnore
  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  @JsonIgnore
  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  @JsonIgnore
  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  @JsonIgnore
  public int getExpirationDays()
  {
    return expirationDays;
  }

  public void setExpirationDays( int expirationDays )
  {
    this.expirationDays = expirationDays;
  }

  @JsonIgnore
  public String getProgramEmailAddress()
  {
    return programEmailAddress;
  }

  public void setProgramEmailAddress( String programEmailAddress )
  {
    this.programEmailAddress = programEmailAddress;
  }

  @JsonIgnore
  public boolean isMissingBehavior()
  {
    return missingBehavior;
  }

  public void setMissingBehavior( boolean missingBehavior )
  {
    this.missingBehavior = missingBehavior;
  }

  /*
  @JsonIgnore
  public boolean isAwardsActive()
  {
    return awardsActive;
  }

  public void setAwardsActive( boolean awardsActive )
  {
    this.awardsActive = awardsActive;
  }
  */

  @JsonIgnore
  public boolean isBehaviorRequired()
  {
    return behaviorRequired;
  }

  public void setBehaviorRequired( boolean behaviorRequired )
  {
    this.behaviorRequired = behaviorRequired;
  }

  @JsonIgnore
  public boolean isEndTagFound()
  {
    return endTagFound;
  }

  public void setEndTagFound( boolean endTagFound )
  {
    this.endTagFound = endTagFound;
  }

  @JsonIgnore
  public boolean isInvalidBehavior()
  {
    return invalidBehavior;
  }

  public void setInvalidBehavior( boolean invalidBehavior )
  {
    this.invalidBehavior = invalidBehavior;
  }

  @JsonIgnore
  public List<String> getMessages()
  {
    return messages;
  }

  public void setMessages( List<String> messages )
  {
    this.messages = messages;
  }

  /*
  @JsonIgnore
  public boolean isDoesNotSupportPoints()
  {
    return doesNotSupportPoints;
  }

  public void setDoesNotSupportPoints( boolean doesNotSupportPoints )
  {
    this.doesNotSupportPoints = doesNotSupportPoints;
  }
  
  */
  
  /*
  @JsonIgnore
  public boolean isPointsNotAvailable()
  {
    return pointsNotAvailable;
  }

  public void setPointsNotAvailable( boolean pointsNotAvailable )
  {
    this.pointsNotAvailable = pointsNotAvailable;
  }
  */

  @JsonIgnore
  public String getBudgetPromotionName()
  {
    return budgetPromotionName;
  }

  public void setBudgetPromotionName( String budgetPromotionName )
  {
    this.budgetPromotionName = budgetPromotionName;
  }

  @JsonIgnore
  public String getProgramName()
  {
    return programName;
  }

  public void setProgramName( String programName )
  {
    this.programName = programName;
  }

  public boolean isMessageEmpty()
  {
    return messageEmpty;
  }

  public void setMessageEmpty( boolean messageEmpty )
  {
    this.messageEmpty = messageEmpty;
  }

  @JsonIgnore
  public boolean isValid()
  {
    if( isSenderNotFound() )
      return false ;
    if( isSenderNotInAudience() )
      return false ;
    if( isSenderNotUnique() )
      return false ;
    if( validRecipients.isEmpty() )
      return false ;
    if( isMissingBehavior() )
      return false ;
    if( isNoBudget() )
      return false ;
    if( isNotEnoughBudget() )
      return false ;
    if( isPointsOutOfRange() )
      return false ;
    if( isFixedPointsDoesntMatch() )
      return false ;
    if( !isEndTagFound() )
      return false ;
    if( isMessageEmpty() )
      return false ;
    if( !invalidRecipients.isEmpty() )
      return false ;
    if( !recipientsNotUnique.isEmpty() )
      return false ;
    if( !recipientsNotInAudience.isEmpty() )
      return false ;
    
    return true ;
  }
}
