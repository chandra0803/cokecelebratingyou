/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/BudgetMeterDetailBeanMultipleNodeComparator.java,v $
 *
 */

package com.biperf.core.value;

import java.util.Comparator;

import com.biperf.core.domain.enums.BudgetType;

public class BudgetMeterDetailBeanMultipleNodeComparator implements Comparator<BudgetMeterDetailBean>
{
  private static final Integer PAX_BUDGET_TYPE_SORT_VALUE = 1;
  private static final Integer NODE_BUDGET_TYPE_SORT_VALUE = 2;

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
  public int compare( BudgetMeterDetailBean bean1, BudgetMeterDetailBean bean2 )
  {
    int comparison = 0;

    // First sort = budget type
    Integer bean1BudgetTypeSortValue = BudgetType.PAX_BUDGET_TYPE.equals( bean1.getBudgetType() ) ? PAX_BUDGET_TYPE_SORT_VALUE : NODE_BUDGET_TYPE_SORT_VALUE;
    Integer bean2BudgetTypeSortValue = BudgetType.PAX_BUDGET_TYPE.equals( bean2.getBudgetType() ) ? PAX_BUDGET_TYPE_SORT_VALUE : NODE_BUDGET_TYPE_SORT_VALUE;
    comparison = bean1BudgetTypeSortValue.compareTo( bean2BudgetTypeSortValue );

    // Second sort = primary node vs. not-primary node ( if node-based )
    if ( comparison == 0 && BudgetType.NODE_BUDGET_TYPE.equals( bean2.getBudgetType() ) )
    {
      // want primary nodes to come first (so invert order)
      comparison = bean2.getIsPrimaryNode().compareTo( bean1.getIsPrimaryNode() );
    }

    // Third sort = node name ( if node-based )
    if ( comparison == 0 && BudgetType.NODE_BUDGET_TYPE.equals( bean2.getBudgetType() ) )
    {
      comparison = bean1.getNodeName().compareTo( bean2.getNodeName() );
    }

    // Fourth sort = non-shared budget vs. shared budget
    if ( comparison == 0 )
    {
      comparison = bean1.getIsShared().compareTo( bean2.getIsShared() );
    }

    // Fifth sort = promotion name ( first in list if shared )
    if ( comparison == 0 )
    {
      String promoName1 = bean1.getPromoList().isEmpty() ? "" : bean1.getPromoList().get( 0 ).getPromoName().toUpperCase();
      String promoName2 = bean2.getPromoList().isEmpty() ? "" : bean2.getPromoList().get( 0 ).getPromoName().toUpperCase();
      comparison = promoName1.compareTo( promoName2 );
    }

    return comparison;
  }
}
