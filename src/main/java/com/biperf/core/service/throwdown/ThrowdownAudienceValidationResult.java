
package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;

public class ThrowdownAudienceValidationResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Map<Division, List<Participant>> divPaxAudiences = new HashMap<Division, List<Participant>>();

  public Set<ParticipantAudienceConflictResult> getConflictingAudienceMembers()
  {
    Set<ParticipantAudienceConflictResult> conflicts = new HashSet<ParticipantAudienceConflictResult>();
    for ( Division division : divPaxAudiences.keySet() )
    {
      List<Participant> divisionParticipants = divPaxAudiences.get( division );
      // now check other divisions for this pax
      for ( Division otherDivision : divPaxAudiences.keySet() )
      {
        // skip ourselves!
        if ( otherDivision.getId().equals( division.getId() ) )
        {
          continue;
        }
        else
        {
          // ok, validate each participant
          List<Participant> otherDivisionsParticipants = divPaxAudiences.get( otherDivision );
          for ( Participant participant : divisionParticipants )
          {
            if ( otherDivisionsParticipants.contains( participant ) )
            {
              // ok, we have a conflict - lets insert a new one or update the existing...
              ParticipantAudienceConflictResult conflict = new ParticipantAudienceConflictResult();
              conflict.setParticipant( participant );
              if ( conflicts.contains( conflict ) )
              {
                // find it and add the division
                for ( ParticipantAudienceConflictResult existing : conflicts )
                {
                  if ( existing.equals( conflict ) )
                  {
                    existing.getDivisions().add( otherDivision );
                  }
                }
              }
              else
              {
                // add a new one
                conflict.getDivisions().add( otherDivision );
                conflicts.add( conflict );
              }
            }
          }
        }
      }
    }
    return conflicts;
  }

  public void addDivisionAudienceParticipants( Division division, List<Participant> paxesInAudience )
  {
    divPaxAudiences.put( division, paxesInAudience );
  }

  public Map<Division, List<Participant>> getDivisionParticipantAudiences()
  {
    return this.divPaxAudiences;
  }

  public Set<Division> getDivisionsForParticipant( Long participantId )
  {
    Set<Division> participantsDivisions = new HashSet<Division>();
    for ( Division division : divPaxAudiences.keySet() )
    {
      if ( divPaxAudiences.get( division ).contains( participantId ) )
      {
        participantsDivisions.add( division );
      }
    }
    return participantsDivisions;
  }

  public boolean isValidPromotionAudience()
  {
    return getConflictingAudienceMembers().isEmpty();
  }
}
