
package com.biperf.core.dao.purl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.utils.HibernateSessionManager;

public class PurlRecipientQueryConstraint extends BaseQueryConstraint
{

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private Long purlRecipientId;

  private List<Long> nodeIds;

  private Long promotionId;

  private Long userId;

  private PurlRecipientState[] purlRecipientStates;

  private Date date;

  private Boolean before;

  private Boolean after;

  /**
    * Returns the class of the objects returned by the query specified by this query constraint.
    *
    * @return the class of the objects returned by the query specified by this query constraint.
    */
  public Class getResultClass()
  {
    return PurlRecipient.class;
  }

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a Hibernate {@link Criteria} object that represents the SQL query specified by this
   * <code>PurlRecipientQueryConstraint</code> object.
   *
   * @return a Hibernate {@link Criteria} object that represents the SQL query specified by this
   *         <code>PurlRecipientQueryConstraint</code> object.
   */
  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "purlRecipient" );

    if ( purlRecipientId != null )
    {
      criteria.add( Restrictions.eq( "purlRecipient.id", purlRecipientId ) );
    }

/*    if ( null != nodeIds && !nodeIds.isEmpty() )
    {
      criteria.add( Restrictions.in( "purlRecipient.node.id", nodeIds ) );
    }*/
    
    if( null!=nodeIds && !nodeIds.isEmpty() )
    {
      /* Customization for wip 26527 starts here */
      addCriteriaIn("purlRecipient.node.id", nodeIds, criteria);
      /* Customization for wip 26527 ends here */
    }
    
    if ( null != promotionId )
    {
      criteria.add( Restrictions.eq( "purlRecipient.promotion.id", promotionId ) );
    }

    if ( null != userId )
    {
      criteria.add( Restrictions.eq( "purlRecipient.user.id", userId ) );
    }

    if ( purlRecipientStates != null && purlRecipientStates.length > 0 )
    {
      criteria.add( Restrictions.in( "purlRecipient.state", purlRecipientStates ) );
    }

    if ( null != date )
    {
      if ( null != before && before )
      {
        criteria.add( Restrictions.ge( "purlRecipient.awardDate", date ) );
      }
      else if ( null != after && after )
      {
        criteria.add( Restrictions.le( "purlRecipient.awardDate", date ) );
      }
      else
      {
        criteria.add( Restrictions.eq( "purlRecipient.awardDate", date ) );
      }
    }

    return criteria;
  }
  /* Customization fix for WIP 26527 Starts */
  private void addCriteriaIn (String propertyName, List<Long> nodeIdList, Criteria criteria)
  {
    Disjunction or = Restrictions.disjunction();
    if(nodeIdList.size()>1000)
    {        
      while(nodeIdList.size()>1000)
      {
        List<Long> subList = nodeIdList.subList(0, 1000);
        or.add(Restrictions.in(propertyName, subList));
        nodeIdList.subList(0, 1000).clear();
      }
    }
    or.add(Restrictions.in(propertyName, nodeIdList));
    criteria.add(or);
  }
  /* Customization fix for WIP 26527 ends */
  
  public Date getDate()
  {
    return date;
  }

  public void setDate( Date date )
  {
    this.date = date;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public List<Long> getNodeIds()
  {
    return nodeIds;
  }

  public void setNodeIds( List<Long> nodeIds )
  {
    this.nodeIds = nodeIds;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public PurlRecipientState[] getPurlRecipientStates()
  {
    return purlRecipientStates;
  }

  public void setPurlRecipientStates( PurlRecipientState[] purlRecipientStates )
  {
    this.purlRecipientStates = purlRecipientStates;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Boolean getBefore()
  {
    return before;
  }

  public void setBefore( Boolean before )
  {
    this.before = before;
  }

  public Boolean getAfter()
  {
    return after;
  }

  public void setAfter( Boolean after )
  {
    this.after = after;
  }
}
