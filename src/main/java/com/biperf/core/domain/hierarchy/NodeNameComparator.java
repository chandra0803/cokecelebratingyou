
package com.biperf.core.domain.hierarchy;

import java.util.Comparator;

public class NodeNameComparator implements Comparator
{

  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof Node ) || ! ( o2 instanceof Node ) )
    {
      throw new ClassCastException( "Object is not a User domain object!" );
    }
    Node item1 = (Node)o1;
    Node item2 = (Node)o2;

    return ( (Comparable)item1.getName().toUpperCase() ).compareTo( item2.getName().toUpperCase() );
  }
}
