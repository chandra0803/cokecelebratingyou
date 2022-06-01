
package com.biperf.core.service.ots;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.BeanLocator;

public class ProgramAudienceUpdateAssociation extends UpdateAssociationRequest
{
  public ProgramAudienceUpdateAssociation( OTSProgram detachedPromotion )
  {
    super( detachedPromotion );
  }

  public void execute( BaseDomain attachedDomain )
  {
    OTSProgram attachedPromotion = (OTSProgram)attachedDomain;
    updatePrimaryAudience( attachedPromotion );
  }

  private void updatePrimaryAudience( OTSProgram attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    OTSProgram detachedPromo = (OTSProgram)getDetachedDomain();
    attachedPromo.setAudienceType( detachedPromo.getAudienceType() );

    if ( detachedPromo.getAudienceType() != null && ( detachedPromo.getAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) ) )

    {
      attachedPromo.getProgramAudience().clear();
    }
    else
    {
      Set detachedPrimaryAudience = detachedPromo.getProgramAudience();
      Iterator detachedProgramAudienceIter = detachedPromo.getProgramAudience().iterator();

      ProgramAudience programAudience;
      Set attachedProgramAudiences = attachedPromo.getProgramAudience();

      // If the attached promotion contains any audiences not in the detached set
      // then it should be removed from the promotion
      Iterator attachedProgramAudienceIter = attachedProgramAudiences.iterator();
      while ( attachedProgramAudienceIter.hasNext() )
      {
        programAudience = (ProgramAudience)attachedProgramAudienceIter.next();
        if ( !detachedPrimaryAudience.contains( detachedPrimaryAudience ) )
        {
          attachedProgramAudienceIter.remove();
        }
      }

      // This will attempt to add all detached audiences to the promotion.
      // Since it is a set, then only non-duplicates will be added.
      while ( detachedProgramAudienceIter.hasNext() )
      {
        programAudience = (ProgramAudience)detachedProgramAudienceIter.next();
        programAudience.setAudience( audienceDAO.getAudienceById( programAudience.getAudience().getId() ) );
        attachedPromo.addProgramAudience( programAudience );
      }
    }
  }

  private Set getAudiencesToRemove( Set audiencesFromAttachedPromotion, Set audiencesFromDetachedPromotion )
  {
    Set audiencesToRemove = new HashSet();

    if ( audiencesFromAttachedPromotion != null && audiencesFromDetachedPromotion != null )
    {
      for ( Iterator iter = audiencesFromAttachedPromotion.iterator(); iter.hasNext(); )
      {
        Audience submitterAudience = (Audience)iter.next();
        if ( !audiencesFromDetachedPromotion.contains( submitterAudience ) )
        {
          audiencesToRemove.add( submitterAudience );
        }
      }
    }

    return audiencesToRemove;
  }

  private AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)BeanLocator.getBean( AudienceDAO.BEAN_NAME );
  }

}
