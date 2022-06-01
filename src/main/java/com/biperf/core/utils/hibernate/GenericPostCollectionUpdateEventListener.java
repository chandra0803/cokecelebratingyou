
package com.biperf.core.utils.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.PostCollectionUpdateEvent;
import org.hibernate.event.PostCollectionUpdateEventListener;

import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.utils.BeanLocator;

@SuppressWarnings( "serial" )
public class GenericPostCollectionUpdateEventListener implements PostCollectionUpdateEventListener
{
  public void onPostUpdateCollection( PostCollectionUpdateEvent event )
  {
    Long userId = null;
    PersistentCollection collection = event.getCollection();

    if ( Objects.nonNull( collection ) )
    {
      Object value = collection.getValue();

      if ( Collection.class.isInstance( value ) )
      {
        List presCollection = new ArrayList( (Collection)value );

        if ( !presCollection.isEmpty() )
        {
          for ( Object obj : presCollection )
          {
            if ( obj instanceof UserEmailAddress )
            {
              userId = ( (UserEmailAddress)obj ).getUser().getId();
            }

            if ( obj instanceof UserPhone )
            {
              userId = ( (UserPhone)obj ).getUser().getId();
            }

          }

          if ( Objects.nonNull( userId ) && isAutoCompleteServiceDefined() )
          {
            getAutoCompleteService().indexParticipants( Arrays.asList( userId ) );
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
