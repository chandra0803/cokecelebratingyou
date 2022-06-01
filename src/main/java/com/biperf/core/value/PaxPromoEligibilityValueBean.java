
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaxPromoEligibilityValueBean implements Serializable
{
  private Long participantId;
  private Map<Long, PromotionEligibility> promotionEligiblity = new ConcurrentHashMap<Long, PromotionEligibility>();

  private boolean matchUser( Long participantId )
  {
    return null != this.participantId ? this.participantId.equals( participantId ) : false;
  }

  public Boolean isInPrimaryAudience( Long participantId, Long promotionId )
  {
    if ( matchUser( participantId ) )
    {
      PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
      if ( null != eligiblity )
      {
        return eligiblity.getInPrimaryAudience();
      }
    }
    // Based on the functionality of this method we are returning null this is required and this is
    // verified by Find bugs tool
    return null;
  }

  public void setInPrimaryAudience( Long participantId, Long promotionId, Boolean status )
  {
    this.participantId = participantId;
    PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
    if ( null == eligiblity )
    {
      eligiblity = new PromotionEligibility();
      promotionEligiblity.put( promotionId, eligiblity );
    }
    eligiblity.setPromotionId( promotionId );
    eligiblity.setInPrimaryAudience( status );
  }

  public Boolean isInSecondaryAudience( Long participantId, Long promotionId )
  {
    if ( matchUser( participantId ) )
    {
      PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
      if ( null != eligiblity )
      {
        return eligiblity.getInSecondaryAudience();
      }
    }
    // Based on the functionality of this method we are returning null this is required and this is
    // verified by Find bugs tool
    return null;
  }

  public Boolean isInSecondaryAudience( Long participantId, Long promotionId, Long nodeId )
  {
    if ( matchUser( participantId ) )
    {
      PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
      if ( null != eligiblity )
      {
        if ( null != eligiblity.getInSecondaryAudience() )
        {
          if ( eligiblity.getInSecondaryAudience() && eligiblity.isValidSecondaryAudienceNode( nodeId ) )
          {
            return Boolean.TRUE;
          }
          else if ( eligiblity.isInvalidSecondaryAudienceNode( nodeId ) )
          {
            return Boolean.FALSE;
          }
        }
      }
    }
    // Based on the functionality of this method we are returning null this is required and this is
    // verified by Find bugs tool
    return null;
  }

  public void setInSecondaryAudience( Long participantId, Long promotionId, Boolean status )
  {
    this.participantId = participantId;
    PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
    if ( null == eligiblity )
    {
      eligiblity = new PromotionEligibility();
      promotionEligiblity.put( promotionId, eligiblity );
    }
    eligiblity.setPromotionId( promotionId );
    eligiblity.setInSecondaryAudience( status );
  }

  public void setInSecondaryAudience( Long participantId, Long promotionId, Boolean status, Long nodeId )
  {
    this.participantId = participantId;
    PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
    if ( null == eligiblity )
    {
      eligiblity = new PromotionEligibility();
      promotionEligiblity.put( promotionId, eligiblity );
    }
    eligiblity.setPromotionId( promotionId );
    if ( status )
    {
      eligiblity.setInSecondaryAudience( status );
      eligiblity.addToValidSecondaryAudienceNode( nodeId );
    }
    else
    {
      if ( null == eligiblity.getInSecondaryAudience() )
      {
        eligiblity.setInSecondaryAudience( status );
      }
      eligiblity.addToInvalidSecondaryAudienceNode( nodeId );
    }
  }

  public Boolean isInWebRulesAudience( Long participantId, Long promotionId )
  {
    if ( matchUser( participantId ) )
    {
      PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
      if ( null != eligiblity )
      {
        return eligiblity.getInWebRulesAudience();
      }
    }
    // Based on the functionality of this method we are returning null this is required and this is
    // verified by Find bugs tool
    return null;
  }

  public void setInWebRulesAudience( Long participantId, Long promotionId, Boolean status )
  {
    this.participantId = participantId;
    PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
    if ( null == eligiblity )
    {
      eligiblity = new PromotionEligibility();
      promotionEligiblity.put( promotionId, eligiblity );
    }
    eligiblity.setPromotionId( promotionId );
    eligiblity.setInWebRulesAudience( status );
  }

  public void setInPublicRecognigionAudience( Long participantId, Long promotionId, Boolean status )
  {
    this.participantId = participantId;
    PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
    if ( null == eligiblity )
    {
      eligiblity = new PromotionEligibility();
      promotionEligiblity.put( promotionId, eligiblity );
    }
    eligiblity.setPromotionId( promotionId );
    eligiblity.setInPublicRecognitionAudience( status );
  }

  public Boolean isInPublicRecognitionAudience( Long participantId, Long promotionId )
  {
    if ( matchUser( participantId ) )
    {
      PromotionEligibility eligiblity = promotionEligiblity.get( promotionId );
      if ( null != eligiblity )
      {
        return eligiblity.getInPublicRecognitionAudience();
      }
    }
    // Based on the functionality of this method we are returning null this is required and this is
    // verified by Find bugs tool
    return null;
  }

  class PromotionEligibility
  {
    private Long promotionId;
    private Boolean inPrimaryAudience;
    private Boolean inSecondaryAudience;
    private Boolean inWebRulesAudience;
    private List<Long> validSecondaryAudienceNodes = new ArrayList<Long>();
    private List<Long> invalidSecondaryAudienceNodes = new ArrayList<Long>();
    private Boolean inPublicRecognitionAudience;

    public Long getPromotionId()
    {
      return promotionId;
    }

    public void setPromotionId( Long promotionId )
    {
      this.promotionId = promotionId;
    }

    public Boolean getInPrimaryAudience()
    {
      return inPrimaryAudience;
    }

    public void setInPrimaryAudience( Boolean inPrimaryAudience )
    {
      this.inPrimaryAudience = inPrimaryAudience;
    }

    public Boolean getInSecondaryAudience()
    {
      return inSecondaryAudience;
    }

    public void setInSecondaryAudience( Boolean inSecondaryAudience )
    {
      this.inSecondaryAudience = inSecondaryAudience;
    }

    public void addToValidSecondaryAudienceNode( Long nodeId )
    {
      validSecondaryAudienceNodes.add( nodeId );
    }

    public void addToInvalidSecondaryAudienceNode( Long nodeId )
    {
      invalidSecondaryAudienceNodes.add( nodeId );
    }

    public boolean isValidSecondaryAudienceNode( Long nodeId )
    {
      return validSecondaryAudienceNodes.contains( nodeId );
    }

    public boolean isInvalidSecondaryAudienceNode( Long nodeId )
    {
      return invalidSecondaryAudienceNodes.contains( nodeId );
    }

    public Boolean getInWebRulesAudience()
    {
      return inWebRulesAudience;
    }

    public void setInWebRulesAudience( Boolean inWebRulesAudience )
    {
      this.inWebRulesAudience = inWebRulesAudience;
    }

    public Boolean getInPublicRecognitionAudience()
    {
      return inPublicRecognitionAudience;
    }

    public void setInPublicRecognitionAudience( Boolean inPublicRecognitionAudience )
    {
      this.inPublicRecognitionAudience = inPublicRecognitionAudience;
    }
  }
}
