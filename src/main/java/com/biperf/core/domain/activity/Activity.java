/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/activity/Activity.java,v $
 */

package com.biperf.core.domain.activity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;

/*
 * Activity <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug 16, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class Activity extends BaseDomain
{
  protected String guid;
  protected Participant participant;
  protected Node node;
  protected Claim claim;
  protected Promotion promotion;
  protected Date submissionDate;
  protected String claimNumber;
  protected boolean isPosted;
  protected Country country;
  protected Long approvalRound;
  protected Set activityJournals = new HashSet();
  protected Set activityMerchOrders = new HashSet();

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  protected Activity()
  {
    // non-public empty constructor
  }

  protected Activity( String guid )
  {
    this.guid = guid;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public boolean isPosted()
  {
    return isPosted;
  }

  public void setPosted( boolean posted )
  {
    isPosted = posted;
  }

  /**
   * Adds a journal transaction to this activity.
   * 
   * @param activityJournal the journal transaction to add to this activity.
   */
  public void addActivityJournal( ActivityJournal activityJournal )
  {
    activityJournal.setActivity( this );
    this.activityJournals.add( activityJournal );
  }

  /**
   * Returns the journal transactions associated with this activity.
   * 
   * @return the journal transactions associated with this activity, as a <code>Set</code> of
   *         {@link com.biperf.core.domain.journal.ActivityJournal} objects.
   */
  public Set getActivityJournals()
  {
    return activityJournals;
  }

  /**
   * Sets the journal transactions associated with this activity.
   * 
   * @param activityJournals the journal transactions associated with this activity, as a
   *          <code>Set</code> of {@link com.biperf.core.domain.journal.ActivityJournal} objects.
   */
  public void setActivityJournals( Set activityJournals )
  {
    this.activityJournals = activityJournals;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof Activity ) )
    {
      return false;
    }

    final Activity activity = (Activity)o;

    if ( !guid.equals( activity.getGuid() ) )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return guid == null ? 0 : guid.hashCode();
  }

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public Set getActivityMerchOrders()
  {
    return activityMerchOrders;
  }

  public void setActivityMerchOrders( Set activityMerchOrders )
  {
    this.activityMerchOrders = activityMerchOrders;
  }

  public Country getCountry()
  {
    return country;
  }

  public void setCountry( Country country )
  {
    this.country = country;
  }

  public Long getApprovalRound()
  {
    return approvalRound;
  }

  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }

}
