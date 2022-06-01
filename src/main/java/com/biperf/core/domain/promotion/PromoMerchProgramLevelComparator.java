/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromoMerchProgramLevelComparator.java,v $
 *
 */

package com.biperf.core.domain.promotion;

import java.io.Serializable;
import java.util.Comparator;

/**
 * PromoMerchProgramLevelComparator <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jul 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 * @author dunne
 *
 */
public class PromoMerchProgramLevelComparator implements Comparator, Serializable
{

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
   * as the first argument is less than, equal to, or greater than the second.
   * <p>
   * <p/> The implementor must ensure that <tt>sgn(compare(x, y)) == -sgn(compare(y, x))</tt> for
   * all <tt>x</tt> and <tt>y</tt>. (This implies that <tt>compare(x, y)</tt> must throw an
   * exception if and only if <tt>compare(y, x)</tt> throws an exception.)
   * <p>
   * <p/> The implementor must also ensure that the relation is transitive:
   * <tt>((compare(x, y)&gt;0)
   * &amp;&amp; (compare(y, z)&gt;0))</tt> implies
   * <tt>compare(x, z)&gt;0</tt>.
   * <p>
   * <p/> Finally, the implementer must ensure that <tt>compare(x, y)==0</tt> implies that
   * <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all <tt>z</tt>.
   * <p>
   * <p/> It is generally the case, but <i>not</i> strictly required that <tt>(compare(x, y)==0) ==
   * (x.equals(y))</tt>.
   * Generally speaking, any comparator that violates this condition should clearly indicate this
   * fact. The recommended language is "Note: this comparator imposes orderings that are
   * inconsistent with equals."
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *         equal to, or greater than the second.
   * @throws ClassCastException if the arguments' types prevent them from being compared by this
   *           Comparator.
   */
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof PromoMerchProgramLevel ) || ! ( o2 instanceof PromoMerchProgramLevel ) )
    {
      throw new ClassCastException( "Object is not a PromoMerchProgramLevel domain object!" );
    }
    PromoMerchProgramLevel promoMerchProgramLevel1 = (PromoMerchProgramLevel)o1;
    PromoMerchProgramLevel promoMerchProgramLevel2 = (PromoMerchProgramLevel)o2;

    if ( promoMerchProgramLevel1 != null && promoMerchProgramLevel2 != null )
    {
      int compare1 = new Long( promoMerchProgramLevel1.getOrdinalPosition() ).compareTo( new Long( promoMerchProgramLevel2.getOrdinalPosition() ) );
      if ( compare1 != 0 )
      {
        return compare1;
      }
    }
    if ( promoMerchProgramLevel1 != null && promoMerchProgramLevel1.getLevelName() != null && promoMerchProgramLevel2 != null && promoMerchProgramLevel2.getLevelName() != null )
    {
      int compare2 = promoMerchProgramLevel1.getLevelName().compareTo( promoMerchProgramLevel2.getLevelName() );
      return compare2;
    }
    return 0;
  }
}
