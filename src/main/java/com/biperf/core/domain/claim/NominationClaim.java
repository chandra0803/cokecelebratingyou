/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/NominationClaim.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.biperf.core.domain.client.TcccClaimFile;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.TcccClaimFileStatusType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.UserManager;

public class NominationClaim extends AbstractRecognitionClaim implements ClaimGroupable
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If this claim recognizes the behavior of a team, then this property is the
   * name of the team.  If this claim recognizes the behavior of an individual,
   * then this property is null.
   */

  private NominationAwardGroupType awardGroupType;

  private String teamName;

  private boolean managerAward = false;

  private boolean activeNomClaim = true;

  private NominationClaimStatusType nominationStatusType;

  private NominationPromotionTimePeriod timPeriod;

  private Integer stepNumber;

  private Long certificateId;

  private Set<NominationClaimBehaviors> nominationClaimBehaviors = new HashSet<NominationClaimBehaviors>();

  private String whyAttachmentUrl;
  private String whyAttachmentName;

  private String moreInfoComments;

  /** Drawing layer from a user-drawn card */
  private String drawingDataUrl;
  /** See NominationSubmitForm for enumeration of values */
  private String cardType;

  private transient boolean submitNomination = false;
  //Client customization start
  private String giverDevisionKey;  
  private String recieverDivisonKey; 
  //Client customization end

  // Client customizations for WIP #39189 starts
  private Set<TcccClaimFile> claimFiles = new LinkedHashSet<TcccClaimFile>();
  // Client customizations for WIP #39189 ends
  
  /**
   * If this claim recognizes the behavior of a team, then this property specifies
   * the team members.  This property is a <code>Set</code> of
   * {@link ProductClaimParticipant} objects.
   */
  private Set<ProductClaimParticipant> teamMembers = new LinkedHashSet<ProductClaimParticipant>();

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>NominationClaim</code> object.
   */
  public NominationClaim()
  {
    // empty constructor
  }

  /**
   * Constructs a <code>NominationClaim</code> object.
   *
   * @param claimFormStep  a claim form step.
   */
  public NominationClaim( ClaimFormStep claimFormStep )
  {
    super( claimFormStep );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  /**
   * Using this instead of isTeam() since it appears to be more reliable - isTeam will return true even if is individual but
   * the promotion was set up as individual or team. 
   * Note that this will be incorrect if the claim has not been set up yet...
   * @return True if the team name is not empty
   */
  public boolean hasTeamName()
  {
    return teamName != null && teamName.length() > 0;
  }

  /**
   * Returns true if this is a claim for a team-based nomination promotion;
   * returns false otherwise.
   *
   * @return true if this is a claim for a team-based nomination promotion;
   *         return false otherwise.
   */
  public boolean isTeam()
  {
    boolean isTeam = false;

    NominationAwardGroupType awardGroupType = ( (NominationPromotion)getPromotion() ).getAwardGroupType();
    NominationAwardGroupSizeType awardGroupSizeType = ( (NominationPromotion)getPromotion() ).getAwardGroupSizeType();

    if ( ( awardGroupType.isTeam() || awardGroupType.isIndividualOrTeam() ) && ( awardGroupSizeType.isLimited() || awardGroupSizeType.isUnlimited() ) )
    {
      isTeam = true;
    }

    return isTeam;
  }
//Client customization for WIP #39189 starts
 public void addClaimFile( TcccClaimFile claimFile )
 {
   claimFile.setClaim( this );
   claimFile.setStatus( TcccClaimFileStatusType.lookup( TcccClaimFileStatusType.ACTIVE ) );
   claimFiles.add( claimFile );
 }
 // Client customization for WIP #39189 ends

  public boolean isManagerAward()
  {
    return managerAward;
  }

  public void setManagerAward( boolean managerAward )
  {
    this.managerAward = managerAward;
  }

  public boolean isActiveNomClaim()
  {
    return activeNomClaim;
  }

  public void setActiveNomClaim( boolean activeNomClaim )
  {
    this.activeNomClaim = activeNomClaim;
  }

  public NominationClaimStatusType getNominationStatusType()
  {
    return nominationStatusType;
  }

  public void setNominationStatusType( NominationClaimStatusType nominationStatusType )
  {
    this.nominationStatusType = nominationStatusType;
  }

  public NominationPromotionTimePeriod getTimPeriod()
  {
    return timPeriod;
  }

  public void setTimPeriod( NominationPromotionTimePeriod timPeriod )
  {
    this.timPeriod = timPeriod;
  }

  public Integer getStepNumber()
  {
    return stepNumber;
  }

  public void setStepNumber( Integer stepNumber )
  {
    this.stepNumber = stepNumber;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public Set<NominationClaimBehaviors> getNominationClaimBehaviors()
  {
    return nominationClaimBehaviors;
  }

  public void setNominationClaimBehaviors( Set<NominationClaimBehaviors> nominationClaimBehaviors )
  {
    this.nominationClaimBehaviors = nominationClaimBehaviors;
  }

  public void addNominationClaimBehaviors( NominationClaimBehaviors nominationClaimBehavior )
  {
    nominationClaimBehavior.setNominationClaim( this );
    this.nominationClaimBehaviors.add( nominationClaimBehavior );
  }

  public String getWhyAttachmentUrl()
  {
    return whyAttachmentUrl;
  }

  public boolean whyAttachmentUrlExist()
  {
    return !StringUtils.isEmpty( whyAttachmentUrl );
  }

  public void setWhyAttachmentUrl( String whyAttachmentUrl )
  {
    this.whyAttachmentUrl = whyAttachmentUrl;
  }

  public String getWhyAttachmentName()
  {
    return whyAttachmentName;
  }

  public void setWhyAttachmentName( String whyAttachmentName )
  {
    this.whyAttachmentName = whyAttachmentName;
  }

  public String getMoreInfoComments()
  {
    if ( this.moreInfoComments != null )
    {
      String moreInfoComments1 = this.moreInfoComments;
      moreInfoComments1 = moreInfoComments1.replaceAll( "&lt;", "<" );
      moreInfoComments1 = moreInfoComments1.replaceAll( "&gt;", ">" );
      moreInfoComments1 = moreInfoComments1.replaceAll( "&quot;", "\"" );
      moreInfoComments1 = moreInfoComments1.replaceAll( "&amp;", "&" );
      moreInfoComments1 = moreInfoComments1.replaceAll( "&apos;", "'" );
      moreInfoComments1 = moreInfoComments1.replaceAll( "&#39;", "'" );
      this.moreInfoComments = moreInfoComments1;
    }
    return moreInfoComments;
  }

  // Client customizations for WIP #39189 starts
  public Set<TcccClaimFile> getClaimFiles()
  {
    return claimFiles;
  }

  public void setClaimFiles( Set<TcccClaimFile> claimFiles )
  {
    this.claimFiles = claimFiles;
  }
  // Client customizations for WIP #39189 ends
  

  public void setMoreInfoComments( String moreInfoComments )
  {
    this.moreInfoComments = moreInfoComments;
  }

  public String getDrawingDataUrl()
  {
    return drawingDataUrl;
  }

  public void setDrawingDataUrl( String drawingDataUrl )
  {
    this.drawingDataUrl = drawingDataUrl;
  }

  public String getCardType()
  {
    return cardType;
  }

  public void setCardType( String cardType )
  {
    this.cardType = cardType;
  }

  public boolean isSubmitNomination()
  {
    return submitNomination;
  }

  public void setSubmitNomination( boolean submitNomination )
  {
    this.submitNomination = submitNomination;
  }

  public NominationAwardGroupType getAwardGroupType()
  {
    return awardGroupType;
  }

  public void setAwardGroupType( NominationAwardGroupType awardGroupType )
  {
    this.awardGroupType = awardGroupType;
  }

  public String getActualCardUrl( String path )
  {
    return path.substring( 0, path.lastIndexOf( ActionConstants.REQUEST_ID ) );
  }

  public void addTeamMember( ProductClaimParticipant teamMember )
  {
    teamMember.setClaim( this );
    teamMembers.add( teamMember );
  }
  public Set<ProductClaimParticipant> getTeamMembers()
  {
    return teamMembers;
  }
  
  public int getTeamMembersSize()
  {
    return teamMembers.size();
  }
 
  public void setTeamMembers( Set<ProductClaimParticipant> teamMembers )
  {
    this.teamMembers = teamMembers;
  }
//Client customizations for WIP #39189 ends
public String getGiverDevisionKey()
{
 return giverDevisionKey;
}

public void setGiverDevisionKey( String giverDevisionKey )
{
 this.giverDevisionKey = giverDevisionKey;
}

public String getRecieverDivisonKey()
{
 return recieverDivisonKey;
}

public void setRecieverDivisonKey( String recieverDivisonKey )
{
 this.recieverDivisonKey = recieverDivisonKey;
}
//Client customization end


/**
 * Get the claim recipient for the specified id
 * @param claimRecipientId
 * @return
 */
public ProductClaimParticipant getClaimParticipant( Long claimParticipantId )
{
  if ( claimParticipantId != null && getTeamMembers() != null )
  {
    for ( ProductClaimParticipant claimParticipant : getTeamMembers() )
    {
      if ( claimParticipant.getParticipant().getId().equals( claimParticipantId ) )
      {
        return claimParticipant;
      }
    }
  }
  return null;
}

//Client customization for WIP #58122
public boolean isMine()
{
  boolean mine = false;
  for ( ProductClaimParticipant claimParticipant : getTeamMembers())
  {
    if ( claimParticipant.getParticipant().getId().equals( UserManager.getUserId() ) )
    {
      mine = true;
    }
  }
  return mine;
}

  
}
