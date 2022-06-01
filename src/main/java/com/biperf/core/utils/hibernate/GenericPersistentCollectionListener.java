
package com.biperf.core.utils.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.envers.event.AuditEventListener;
import org.hibernate.event.PostCollectionRecreateEvent;
import org.hibernate.event.PreCollectionRemoveEvent;
import org.hibernate.event.PreCollectionUpdateEvent;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.utils.BeanLocator;

@SuppressWarnings( "serial" )
public class GenericPersistentCollectionListener extends AuditEventListener
{
  @SuppressWarnings( "unchecked" )
  @Override
  public void onPostRecreateCollection( PostCollectionRecreateEvent event )
  {
    super.onPostRecreateCollection( event );
    Object affectedEntity = event.getAffectedOwnerOrNull();

    if ( affectedEntity instanceof Audience )
    {
      Audience audience = (Audience)affectedEntity;
      if ( audience instanceof PaxAudience )
      {
        if ( isAutoCompleteServiceDefined() )
        {
          Set<Long> paxIds = new HashSet<Long>( audience.getSize() );
          PaxAudience paxAudience = (PaxAudience)audience;
          List<AudienceParticipant> apa = paxAudience.getAudienceParticipants();
          apa.stream().forEach( pa -> paxIds.add( pa.getParticipant().getId() ) );
          getAutoCompleteService().indexParticipants( new ArrayList<Long>( paxIds ) );
        }
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void onPreRemoveCollection( PreCollectionRemoveEvent event )
  {
    super.onPreRemoveCollection( event );
    Object affectedEntity = event.getAffectedOwnerOrNull();
    if ( isAutoCompleteServiceDefined() )
    {
      if ( affectedEntity instanceof PaxAudience )
      {
        PaxAudience paxAudience = (PaxAudience)affectedEntity;
        Set<Long> paxIds = new HashSet<Long>( paxAudience.getSize() );
        List<AudienceParticipant> apa = paxAudience.getAudienceParticipants();
        apa.stream().forEach( pa -> paxIds.add( pa.getParticipant().getId() ) );
        getAutoCompleteService().indexParticipants( new ArrayList<Long>( paxIds ) );
      }
      else if ( affectedEntity instanceof CriteriaAudience )
      {
        CriteriaAudience audience = (CriteriaAudience)affectedEntity;
        Set<Long> paxIds = new HashSet<Long>( audience.getSize() );
        List<AudienceParticipant> apa = audience.getAudienceParticipants();
        apa.stream().forEach( pa -> paxIds.add( pa.getParticipant().getId() ) );
        getAutoCompleteService().indexParticipants( new ArrayList<Long>( paxIds ) );
      }
    }
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public void onPreUpdateCollection( PreCollectionUpdateEvent event )
  {
    super.onPreUpdateCollection( event );
    PersistentCollection collection = event.getCollection();

    Object value = collection.getValue();
    if ( Collection.class.isInstance( value ) )
    {
      List newCol = new ArrayList( (Collection)value );
      if ( !newCol.isEmpty() )
      {
        List ids = new ArrayList( newCol.size() );
        for ( Object obj : newCol )
        {
          if ( obj instanceof ParticipantEmployer )
          {
            ids.add( ( (ParticipantEmployer)obj ).getParticipant().getId() );
          }
        }
        if ( CollectionUtils.isNotEmpty( ids ) )
        {
          if ( isAutoCompleteServiceDefined() )
          {
            getAutoCompleteService().indexParticipants( new ArrayList<Long>( ids ) );
          }
        }
      }
    }
  }

  private static boolean isAutoCompleteServiceDefined()
  {
    return BeanLocator.hasBean( AutoCompleteService.BEAN_NAME );
  }
  
  private static AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)BeanLocator.getBean( AutoCompleteService.BEAN_NAME );
  }
}
