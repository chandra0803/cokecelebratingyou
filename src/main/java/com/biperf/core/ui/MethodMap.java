
package com.biperf.core.ui;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class uses the Map interface to mimic a non-void one-argument method. 
 * You can override the get() method to do whatever you want it to do, as long
 * as it requires exactly one argument and returns a value.<br/><br/>
 * 
 * The motivation for this class is that the JSP/JSTL expression language (EL)
 * lacks any sort of method syntax. However there is an array element syntax
 * (a[x]) which can be used with maps as well as with arrays and lists.  For a
 * map "a" with key "x", "${a[x]}" is the same as the Java method call 
 * "a.get(x)").<br/><br/>
 * 
 * By implementing a MethodMap bean property with the get method defined to
 * invoke the desired non-void one-argument method, you now have a back door to
 * "invoke" that method within the syntax constraints of in the JSP/JSTL
 * expression language.  
 *
 * @author glover (original)
 */
public abstract class MethodMap implements Map, Serializable
{
  private int mode = 1;
  private List<String> params = new LinkedList<String>();
  private final UnsupportedOperationException uoe = new UnsupportedOperationException( "only the get() method is allowed." );

  public final void clear()
  {
    throw uoe;
  }

  public final boolean containsKey( Object arg0 )
  {
    throw uoe;
  }

  public final boolean containsValue( Object arg0 )
  {
    throw uoe;
  }

  public final Set entrySet()
  {
    throw uoe;
  }

  public final boolean isEmpty()
  {
    throw uoe;
  }

  public final Set keySet()
  {
    throw uoe;
  }

  public final Object put( Object arg0, Object arg1 )
  {
    throw uoe;
  }

  public final void putAll( Map arg0 )
  {
    throw uoe;
  }

  public final Object remove( Object arg0 )
  {
    throw uoe;
  }

  public final int size()
  {
    throw uoe;
  }

  public final Collection values()
  {
    throw uoe;
  }

  /** All method maps of the same type are equal */
  @Override
  public final boolean equals( Object o )
  {
    return o == null ? false : getClass().equals( o.getClass() );
  }

  /** All method maps of the same type have the same hash code */
  @Override
  public final int hashCode()
  {
    return getClass().hashCode() * 17 + 1;
  }

  public List<String> getParams()
  {
    return params;
  }

  public void setParams( List<String> params )
  {
    this.params = params;
  }

  public int getMode()
  {
    return mode;
  }

  public void setMode( int mode )
  {
    this.mode = mode;
  }
}
