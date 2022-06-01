/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/awardgenerator/AwardGenAwardsComparator.java,v $
 *
 */

package com.biperf.core.domain.awardgenerator;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 
 * AwardGenAwardsComparator.
 * 
 * @author chowdhur
 * @since Jul 30, 2013
 */
public class AwardGenAwardsComparator implements Comparator, Serializable
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
    if ( ! ( o1 instanceof AwardGenAward ) || ! ( o2 instanceof AwardGenAward ) )
    {
      throw new ClassCastException( "Object is not a AwardGenAward domain object!" );
    }
    AwardGenAward awardGenAward1 = (AwardGenAward)o1;
    AwardGenAward awardGenAward2 = (AwardGenAward)o2;

    if ( awardGenAward1 != null && awardGenAward2 != null )
    {
      Integer year1 = awardGenAward1.getYears();
      Integer year2 = awardGenAward2.getYears();
      if ( year1 != null && year2 != null )
      {
        int compare1 = new Long( year1 ).compareTo( new Long( year2 ) );
        if ( compare1 != 0 )
        {
          return compare1;
        }
      }

      Integer days1 = awardGenAward1.getDays();
      Integer days2 = awardGenAward2.getDays();
      if ( days1 != null && days2 != null )
      {
        int compare2 = new Long( days1 ).compareTo( new Long( days2 ) );
        if ( compare2 != 0 )
        {
          return compare2;
        }
      }
    }
    return 0;
  }
}
