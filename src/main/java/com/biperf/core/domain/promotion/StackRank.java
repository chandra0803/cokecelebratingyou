/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.promotion;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.utils.DateUtils;

/*
 * StackRank <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 6, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRank extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * Uniquely identifies this stack ranking. Acts as a surrogate business key.
   */
  private String guid;

  /**
   * Identifies the current state of this stack ranking.
   */
  private StackRankState state;

  /**
   * The date of the first day of the stack rank period.
   */
  private Date startDate;

  /**
   * The date of the last day of the stack rank period.
   */
  private Date endDate;

  /**
   * If true, calculate payout after calculating stack rank; if false, do not calculate payout.
   */
  private boolean calculatePayout;

  /**
   * The promotion whose sales activity is used to create this stack ranking.
   */
  private ProductClaimPromotion promotion;

  /**
   * Stack rank lists by node, as a <code>Set</code> of {@link StackRankNode} objects.
   */
  private Set stackRankNodes = new HashSet();

  // ---------------------------------------------------------------------------
  // Equals and Hash Code Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackRank ) )
    {
      equals = false;
    }
    else
    {
      StackRank that = (StackRank)object;

      if ( guid != null && !guid.equals( that.getGuid() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  public int hashCode()
  {
    return guid.hashCode();
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public void addStackRankNode( StackRankNode stackRankNode )
  {
    stackRankNode.setStackRank( this );
    stackRankNodes.add( stackRankNode );
  }

  public boolean isCalculatePayout()
  {
    return calculatePayout;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public String getGuid()
  {
    return guid;
  }

  public ProductClaimPromotion getPromotion()
  {
    return promotion;
  }

  public Set getStackRankNodes()
  {
    return stackRankNodes;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public StackRankState getState()
  {
    return state;
  }

  public void setCalculatePayout( boolean calculatePayout )
  {
    this.calculatePayout = calculatePayout;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = DateUtils.toEndDate( endDate );
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public void setPromotion( ProductClaimPromotion promotion )
  {
    this.promotion = promotion;
  }

  public void setStackRankNodes( Set stackRankNodes )
  {
    this.stackRankNodes = stackRankNodes;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = DateUtils.toStartDate( startDate );
  }

  public void setState( StackRankState state )
  {
    this.state = state;
  }

  public String getRankingPeriodWithDateSubmitted()
  {
    StringBuffer rankingPeriod = new StringBuffer();

    rankingPeriod.append( DateUtils.toDisplayString( startDate ) );
    rankingPeriod.append( "-" );
    rankingPeriod.append( DateUtils.toDisplayString( endDate ) );
    rankingPeriod.append( "(" );
    rankingPeriod.append( "rank and payout" );
    rankingPeriod.append( ")" );
    rankingPeriod.append( " Submitted on " );
    rankingPeriod.append( DateUtils.toDisplayTimeString( getAuditCreateInfo().getDateCreated() ) );

    return rankingPeriod.toString();
  }
}
