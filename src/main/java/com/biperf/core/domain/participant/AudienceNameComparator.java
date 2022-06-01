/**
 * 
 */

package com.biperf.core.domain.participant;

import java.util.Comparator;

/**
 * @author poddutur
 *
 */
public class AudienceNameComparator implements Comparator
{

  @SuppressWarnings( "unchecked" )
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof Audience ) || ! ( o2 instanceof Audience ) )
    {
      throw new ClassCastException( "Object is not a Audience domain object!" );
    }
    Audience item1 = (Audience)o1;
    Audience item2 = (Audience)o2;

    return ( (Comparable)item1.getName().toUpperCase() ).compareTo( item2.getName().toUpperCase() );
  }

}
