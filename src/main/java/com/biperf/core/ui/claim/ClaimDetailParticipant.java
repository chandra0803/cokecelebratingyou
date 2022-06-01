/**
 * 
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.ui.claim.RecognitionDetailBean.CustomElements;
import com.biperf.core.ui.claim.RecognitionDetailBean.Ecard;
import com.biperf.core.ui.claim.RecognitionDetailBean.RecognitionDetailParticipant;
import com.biperf.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
public class ClaimDetailParticipant extends RecognitionDetailParticipant
{
  /**
   * 
   */
  private static final long serialVersionUID = -6559490112370059881L;
  private String comment;
  private String date;
  private String behavior;
  private String badgeUrl;
  private Ecard ecard;
  private List<CustomElements> extraFields = new ArrayList<CustomElements>();

  public ClaimDetailParticipant( Participant pax, Node org, String viewCertificateUrl, AbstractRecognitionClaim arc, String contextPath )
  {
    super( pax, org, viewCertificateUrl );

    if ( arc != null && arc instanceof NominationClaim )
    {
      NominationClaim nominationClaim = (NominationClaim)arc;
      if ( nominationClaim.getSubmitter() != null && nominationClaim.getSubmitterComments() != null )
      {
        this.comment = nominationClaim.getSubmitterComments().trim();
      }

      date = DateUtils.toDisplayString( nominationClaim.getSubmissionDate() );

      if ( nominationClaim.getBehavior() != null && !PromoRecognitionBehaviorType.getNoneItem().equals( nominationClaim.getBehavior() ) )
      {
        this.behavior = nominationClaim.getBehavior().getName();
      }

      if ( nominationClaim.getCard() != null )
      {
        this.ecard = new Ecard( nominationClaim.getCard(), contextPath );
      }
      else if ( nominationClaim.getOwnCardName() != null && nominationClaim.getOwnCardName().trim().length() > 0 )
      {
        this.ecard = new Ecard( nominationClaim.getOwnCardName() );
      }

      String cmAssetCode = "";
      if ( nominationClaim.getPromotion().getClaimForm() != null )
      {
        cmAssetCode = nominationClaim.getPromotion().getClaimForm().getCmAssetCode();
      }
      for ( ClaimElement claimElement : nominationClaim.getClaimElements() )
      {
        if ( !claimElement.getClaimFormStepElement().getClaimFormElementType().isCopyBlock() && !claimElement.getClaimFormStepElement().getClaimFormElementType().isLink() )
        {
          this.extraFields.add( new CustomElements( claimElement, cmAssetCode ) );
        }
      }
    }
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  public String getBadgeUrl()
  {
    return badgeUrl;
  }

  public void setBadgeUrl( String badgeUrl )
  {
    this.badgeUrl = badgeUrl;
  }

  @JsonProperty( "eCard" )
  public Ecard getEcard()
  {
    return ecard;
  }

  public void setEcard( Ecard ecard )
  {
    this.ecard = ecard;
  }

  public List<CustomElements> getExtraFields()
  {
    return extraFields;
  }

  public void setExtraFields( List<CustomElements> extraFields )
  {
    this.extraFields = extraFields;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

}
