/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/SurveyPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.List;

/**
 * SurveyPromotion.
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
 * <td>Tammy Cheng</td>
 * <td>Jun 12, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SurveyPromotion extends Promotion
{
  private boolean corpAndMngr;
  private Survey survey;

  /**
   * Constructor
   */
  public SurveyPromotion()
  {
    super();
  }

  /**
   * Does a deep copy of the promotion and its children if specified. This is a customized
   * implementation of
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    return (SurveyPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );
  }

  public boolean hasParent()
  {
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.promotion.Promotion#isClaimFormUsed()
   */
  public boolean isClaimFormUsed()
  {
    return false;
  }

  public boolean isCorpAndMngr()
  {
    return corpAndMngr;
  }

  public void setCorpAndMngr( boolean corpAndMngr )
  {
    this.corpAndMngr = corpAndMngr;
  }

  public Survey getSurvey()
  {
    return survey;
  }

  public void setSurvey( Survey survey )
  {
    this.survey = survey;
  }

}
