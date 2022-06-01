/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/AbstractRecognitionClaim.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;

public abstract class AbstractRecognitionClaim extends Claim
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The e-card to be sent to each claim recipient.
   */
  private Card card;

  /**
   * Identifies the behavior recognized by this claim.
   */
  private PromotionBehaviorType behavior;

  /**
   * The source of this claim, such as the web or a mobile device.
   */
  private RecognitionClaimSource source;

  /**
   * If true, send a copy of the e-mail message sent the recipient to the
   * submitter; if false, do not.
   */
  private boolean copySender;

  /**
   * The submitter's comments.
   */
  private String submitterComments;

  private LanguageType submitterCommentsLanguageType;

  /**
   * own card name.
   */
  private String ownCardName;

  /**
   * The participants recognized by this claim.
   */
  private Set<ClaimRecipient> claimRecipients = new LinkedHashSet<>();

  /**
   * calculator responses sorted by sequence number
   */
  private Set<CalculatorResponse> calculatorResponses = new LinkedHashSet<>();

  private Long teamId;

  private boolean hidePublicRecognition;

  // transient
  private List<AbstractRecognitionClaim> teamClaims;

  /** Uploaded video URL */
  private String cardVideoUrl;
  /** Upload video, but the thumbnail image */
  private String cardVideoImageUrl;
  /** Added in parent class from child class */
  private boolean isReversal = false;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------
  /**
   * Constructs a <code>AbstractRecognitionClaim</code> object.
   */
  public AbstractRecognitionClaim()
  {
    // empty constructor
  }

  /**
   * Constructs a <code>RecognitionClaim</code> object.
   *
   * @param claimFormStep  a claim form step.
   */
  public AbstractRecognitionClaim( ClaimFormStep claimFormStep )
  {
    super( claimFormStep );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------
  public void addClaimRecipient( ClaimRecipient claimRecipient )
  {
    if ( claimRecipient.getSerialId() == null || claimRecipient.getSerialId().equals( "" ) )
    {
      claimRecipient.setSerialId( GuidUtils.generateGuid() );
    }

    claimRecipient.setClaim( this );
    claimRecipients.add( claimRecipient );
  }

  @Override
  public Set<ClaimRecipient> getApprovableItems()
  {
    return claimRecipients;
  }

  public PromotionBehaviorType getBehavior()
  {
    return behavior;
  }

  public RecognitionClaimSource getSource()
  {
    return source;
  }

  public void setSource( RecognitionClaimSource source )
  {
    this.source = source;
  }

  public Card getCard()
  {
    return card;
  }

  public Set<ClaimRecipient> getClaimRecipients()
  {
    return claimRecipients;
  }

  public Map<Long, ClaimRecipient> getClaimItemRecipientsMap()
  {
    Map<Long, ClaimRecipient> map = new HashMap<Long, ClaimRecipient>();
    for ( ClaimRecipient r : claimRecipients )
    {
      map.put( r.getId(), r );
    }
    return map;
  }

  public Map<Long, ClaimRecipient> getClaimPaxRecipientsMap()
  {
    Map<Long, ClaimRecipient> map = new HashMap<Long, ClaimRecipient>();
    for ( ClaimRecipient r : claimRecipients )
    {
      map.put( r.getRecipient().getId(), r );
    }
    return map;
  }

  public String getSubmitterComments()
  {
    if ( this.submitterComments != null )
    {
      String submitterComments1 = this.submitterComments;
      submitterComments1 = submitterComments1.replaceAll( "&lt;", "<" );
      submitterComments1 = submitterComments1.replaceAll( "&gt;", ">" );
      submitterComments1 = submitterComments1.replaceAll( "&quot;", "\"" );
      submitterComments1 = submitterComments1.replaceAll( "&amp;", "&" );
      submitterComments1 = submitterComments1.replaceAll( "&apos;", "'" );
      submitterComments1 = submitterComments1.replaceAll( "&#39;", "'" );
      this.submitterComments = submitterComments1;
    }
    return submitterComments;
  }

  @Override
  public boolean isApprovableClaimType()
  {
    return true;
  }

  public boolean isCopySender()
  {
    return copySender;
  }

  public void setBehavior( PromotionBehaviorType behavior )
  {
    this.behavior = behavior;
  }

  public void setCard( Card card )
  {
    this.card = card;
  }

  public void setClaimRecipients( Set<ClaimRecipient> claimRecipients )
  {
    this.claimRecipients = claimRecipients;
  }

  public void setCopySender( boolean copySender )
  {
    this.copySender = copySender;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public LanguageType getSubmitterCommentsLanguageType()
  {
    return submitterCommentsLanguageType;
  }

  public void setSubmitterCommentsLanguageType( LanguageType submitterCommentsLanguageType )
  {
    this.submitterCommentsLanguageType = submitterCommentsLanguageType;
  }

  public Set<CalculatorResponse> getCalculatorResponses()
  {
    return calculatorResponses;
  }

  public void setCalculatorResponses( Set<CalculatorResponse> calculatorResponses )
  {
    this.calculatorResponses = calculatorResponses;
  }

  public void addCalculatorResponse( CalculatorResponse calculatorResponse )
  {
    calculatorResponse.setClaim( this );
    calculatorResponses.add( calculatorResponse );
  }

  
  // Client customization for WIP #43735 starts
  public boolean isMine()
  {
    boolean mine = false;
    for ( ClaimRecipient claimRecipient : getClaimRecipients() )
    {
      if ( claimRecipient.getRecipient().getId().equals( UserManager.getUserId() ) )
      {
        mine = true;
      }
    }
    return mine;
  }
  
  public boolean isAwardClaimed()
  {
    boolean awardClaimed = false;
    for ( ClaimRecipient claimRecipient : getClaimRecipients() )
    {
      awardClaimed = claimRecipient.isCashPaxClaimed();
    }
    return awardClaimed;
  }
  // Client customization for WIP #43735 ends
  /**
   * Get the claim recipient for the specified id
   * @param claimRecipientId
   * @return
   */
  public ClaimRecipient getClaimRecipient( Long claimRecipientId )
  {
    if ( claimRecipientId != null && getClaimRecipients() != null )
    {
      for ( ClaimRecipient claimRecipient : getClaimRecipients() )
      {
        if ( claimRecipient.getId().equals( claimRecipientId ) )
        {
          return claimRecipient;
        }
      }
    }
    return null;
  }

  public void setOwnCardName( String ownCardName )
  {
    this.ownCardName = ownCardName;
  }

  public String getOwnCardName()
  {
    return ownCardName;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public void setHidePublicRecognition( boolean hidePublicRecognition )
  {
    this.hidePublicRecognition = hidePublicRecognition;
  }

  public boolean isHidePublicRecognition()
  {
    return hidePublicRecognition;
  }

  public void setTeamClaims( List<AbstractRecognitionClaim> teamClaims )
  {
    this.teamClaims = teamClaims;
  }

  public List<AbstractRecognitionClaim> getTeamClaims()
  {
    return teamClaims;
  }

  public String getCardVideoUrl()
  {
    return cardVideoUrl;
  }

  public void setCardVideoUrl( String cardVideoUrl )
  {
    this.cardVideoUrl = cardVideoUrl;
  }

  public String getCardVideoImageUrl()
  {
    return cardVideoImageUrl;
  }

  public void setCardVideoImageUrl( String cardVideoImageUrl )
  {
    this.cardVideoImageUrl = cardVideoImageUrl;
  }

  public boolean isReversal()
  {
    return isReversal;
  }

  public void setReversal( boolean isReversal )
  {
    this.isReversal = isReversal;
  }

  public String getRequestId( String videoUrl )
  {

    return videoUrl.substring( videoUrl.lastIndexOf( ":" ) + 1 );
  }

  public String getActualCardUrl( String path )
  {
    return path.substring( 0, path.lastIndexOf( ActionConstants.REQUEST_ID ) );
  }
}
