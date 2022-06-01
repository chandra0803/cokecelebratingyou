/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionAudience.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Audience;

/**
 * PromotionAudience.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Aug 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class PromotionAudience extends BaseDomain
{

  private Promotion promotion;
  private Audience audience;

  /**
   * @return audience
   */
  public Audience getAudience()
  {
    return this.audience;
  }

  /**
   * 
   */
  public PromotionAudience()
  {
    super();
  }

  /**
   * @param audience
   */
  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  /**
   * @return promotion
   */
  public Promotion getPromotion()
  {
    return this.promotion;
  }

  /**
   * @param promotion
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  /**
   * Indicates whether some other object is "equal to" this one. <p/> The <code>equals</code>
   * method implements an equivalence relation on non-null object references:
   * <ul>
   * <li>It is <i>reflexive</i>: for any non-null reference value <code>x</code>,
   * <code>x.equals(x)</code> should return <code>true</code>.
   * <li>It is <i>symmetric</i>: for any non-null reference values <code>x</code> and
   * <code>y</code>, <code>x.equals(y)</code> should return <code>true</code> if and only if
   * <code>y.equals(x)</code> returns <code>true</code>.
   * <li>It is <i>transitive</i>: for any non-null reference values <code>x</code>,
   * <code>y</code>, and <code>z</code>, if <code>x.equals(y)</code> returns
   * <code>true</code> and <code>y.equals(z)</code> returns <code>true</code>, then
   * <code>x.equals(z)</code> should return <code>true</code>.
   * <li>It is <i>consistent</i>: for any non-null reference values <code>x</code> and
   * <code>y</code>, multiple invocations of <tt>x.equals(y)</tt> consistently return
   * <code>true</code> or consistently return <code>false</code>, provided no information used
   * in <code>equals</code> comparisons on the objects is modified.
   * <li>For any non-null reference value <code>x</code>, <code>x.equals(null)</code> should
   * return <code>false</code>.
   * </ul>
   * <p/> The <tt>equals</tt> method for class <code>Object</code> implements the most
   * discriminating possible equivalence relation on objects; that is, for any non-null reference
   * values <code>x</code> and <code>y</code>, this method returns <code>true</code> if and
   * only if <code>x</code> and <code>y</code> refer to the same object (<code>x == y</code>
   * has the value <code>true</code>). <p/> Note that it is generally necessary to override the
   * <tt>hashCode</tt> method whenever this method is overridden, so as to maintain the general
   * contract for the <tt>hashCode</tt> method, which states that equal objects must have equal
   * hash codes.
   * 
   * @param obj the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
   *         otherwise.
   * @see #hashCode()
   * @see java.util.Hashtable
   */
  public boolean equals( Object obj )
  {
    PromotionAudience promoAudience = (PromotionAudience)obj;

    boolean equals = false;

    equals = promotion.equals( promoAudience.getPromotion() );

    if ( equals )
    {
      equals = audience.equals( promoAudience.getAudience() );
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int the hashCode
   */
  public int hashCode()
  {

    int hashCode = 0;

    hashCode += getPromotion() != null ? getPromotion().hashCode() : 13;
    hashCode += getAudience() != null ? getAudience().hashCode() : 17;

    return hashCode;
  }

  /**
   * Builds a String representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( "PromotionAudience [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{promotion.id =" + this.getPromotion().getId() + "}, " );
    sb.append( "{audience.id=" + this.getAudience().getId() + "}" );
    sb.append( "]" );

    return sb.toString();

  }

}
