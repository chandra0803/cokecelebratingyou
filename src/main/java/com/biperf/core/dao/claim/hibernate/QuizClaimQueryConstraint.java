/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/QuizClaimQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.claim.QuizClaim;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class QuizClaimQueryConstraint extends ClaimQueryConstraint
{

  /**
   * Constraint by pass(if true)/fail(if false)/neither(if null)
   */
  private Boolean pass;

  /**
   * Constrain by quiz id
   */
  private Long quizId;

  private Long diyQuizId;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return QuizClaim.class;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint#buildCriteria()
   */
  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    if ( pass != null )
    {
      criteria.add( Restrictions.eq( "claim.pass", pass ) );
    }

    if ( quizId != null )
    {
      criteria.createAlias( "claim.promotion", "promotion" ).add( Restrictions.eq( "promotion.quiz.id", quizId ) );
    }

    if ( diyQuizId != null )
    {
      criteria.createAlias( "claim.quiz", "quiz" ).add( Restrictions.eq( "quiz.id", diyQuizId ) );
    }

    return criteria;
  }

  /**
   * @return value of pass property
   */
  public Boolean getPass()
  {
    return pass;
  }

  /**
   * @param pass value for pass property
   */
  public void setPass( Boolean pass )
  {
    this.pass = pass;
  }

  /**
   * @return value of quizId property
   */
  public Long getQuizId()
  {
    return quizId;
  }

  /**
   * @param quizId value for quizId property
   */
  public void setQuizId( Long quizId )
  {
    this.quizId = quizId;
  }

  public Long getDiyQuizId()
  {
    return diyQuizId;
  }

  public void setDiyQuizId( Long diyQuizId )
  {
    this.diyQuizId = diyQuizId;
  }

}
