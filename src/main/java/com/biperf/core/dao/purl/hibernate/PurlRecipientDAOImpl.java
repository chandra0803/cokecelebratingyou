/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/purl/hibernate/PurlRecipientDAOImpl.java,v $
 */

package com.biperf.core.dao.purl.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.purl.PurlRecipientDAO;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.enums.PurlRecipientType;
import com.biperf.core.domain.purl.PurlCelebrationsView;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.recognition.PurlRecipientValue;

/**
 * PurlRecipientDAOImpl.
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
 * <td>gadapa</td>
 * <td>Oct 07, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PurlRecipientDAOImpl extends BaseDAO implements PurlRecipientDAO
{
  private JdbcTemplate jdbcTemplate;
  private static final Log log = LogFactory.getLog( PurlRecipientDAOImpl.class );
  private DataSource dataSource;

  /**
   * Overridden from @see com.biperf.core.dao.purl.PurlRecipientDAO#delete(com.biperf.core.domain.purl.PurlRecipient)
   * @param payout
   */
  public void delete( PurlRecipient info )
  {
    getSession().delete( info );
  }

  /**
   * @param id
   * @return
   */
  public PurlRecipient getPurlRecipientById( Long id )
  {
    return (PurlRecipient)getSession().get( PurlRecipient.class, id );
  }

  /**
   * @param claimId
   * @return
   */
  public PurlRecipient getPurlRecipientByClaimId( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getPurlRecipientbyClaimId" );
    query.setParameter( "claimId", claimId );

    return (PurlRecipient)query.uniqueResult();
  }

  /**
   * Overridden from @see com.biperf.core.dao.purl.PurlRecipientDAO#save(com.biperf.core.domain.purl.PurlRecipient)
   * @param payout
   * @return
   */
  public PurlRecipient save( PurlRecipient info )
  {
    PurlRecipient savedPurlRecipient = (PurlRecipient)HibernateUtil.saveOrUpdateOrDeepMerge( info );

    // run lock to insure that sub-objects are reattached to this session, since some of
    // the subobjects may have come from another session.
    getSession().flush();
    getSession().refresh( info );

    return savedPurlRecipient;
  }

  public List<PurlRecipient> getPurlRecipientList( PurlRecipientQueryConstraint constraint )
  {
    return HibernateUtil.getObjectList( constraint );
  }

  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.PurlRecipientsForAutoInvite" );
    query.setParameter( "promotionId", promotionId );

    return query.list();
  }
  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId, Long numberOfDays )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.PurlRecipientsForAutoInvite" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "numberOfDays", numberOfDays );
    
    return query.list();
  }
  @Override
  public List<PurlRecipient> getGlobalUpcomingPurlRecipients( int rowNumStart, int rowNumEnd, String sortedBy, String sortedOn, int pageSize )
  {
    String queryString = "SELECT * FROM " + "(SELECT ROWNUM RANGE,A.* FROM " + "(SELECT PR.* FROM " + "PURL_RECIPIENT PR INNER JOIN PROMO_RECOGNITION PREC ON PR.PROMOTION_ID=PREC.PROMOTION_ID "
        + "LEFT OUTER JOIN PROMOTION PROMO ON PROMO.PROMOTION_ID=PREC.PROMOTION_ID " + "INNER JOIN APPLICATION_USER AP ON PR.USER_ID=AP.USER_ID "
        + "LEFT OUTER JOIN PARTICIPANT PAX ON AP.USER_ID=PAX.USER_ID " + "WHERE PR.STATE IN ('invitation','contribution')" + " AND  PR.AWARD_DATE > TO_DATE(:AWARD_DATE, 'mm/dd/yyyy') AND "
        + "AP.USER_ID <> :userID AND PREC.DISPLAY_PURL_IN_PURL_TILE= :display_purl_in_purl_tile" + " ORDER BY :sortedOn :sortedBy) A) WHERE RANGE BETWEEN :rangeStart AND :rangeEnd";
    if ( "1".equals( sortedOn ) )
    {
      queryString = queryString.replace( ":sortedOn", "AP.LAST_NAME" + "," + "AP.FIRST_NAME" );
    }
    else
    {
      queryString = queryString.replace( ":sortedOn", "PR.AWARD_DATE" );
    }
    queryString = queryString.replace( ":sortedBy", sortedBy );

    SQLQuery query = getSession().createSQLQuery( queryString );
    SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" );

    query.setParameter( "AWARD_DATE", sdf.format( DateUtils.toEndDate( DateUtils.getCurrentDate() ) ) );
    query.setParameter( "userID", UserManager.getUser().getUserId() );
    query.setParameter( "display_purl_in_purl_tile", 1 );
    query.setParameter( "rangeStart", rowNumStart + 1 );
    query.setParameter( "rangeEnd", rowNumEnd );
    query.addEntity( PurlRecipient.class );
    return query.list();
    /*
     * Criteria criteria = HibernateSessionManager.getSession().createCriteria( PurlRecipient.class,
     * "purlRecipient" ); criteria.add( Restrictions.in( "purlRecipient.state", new
     * PurlRecipientState[] { PurlRecipientState.lookup( PurlRecipientState.INVITATION ),
     * PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) } ) ); criteria.add(
     * Restrictions.gt( "purlRecipient.awardDate", DateUtils.toEndDate( DateUtils.getCurrentDate() )
     * ) ); criteria.createAlias( "purlRecipient.user", "user" ); criteria.add( Restrictions.ne(
     * "user.id", UserManager.getUser().getUserId() ) ); criteria.createAlias(
     * "purlRecipient.promotion", "promotion" ); criteria.add( Restrictions.eq(
     * "promotion.displayPurlInPurlTile", true )); buildCriteriaForSortParameters( criteria,
     * sortedBy, sortedOn ); criteria.setFirstResult( rowNumStart ); criteria.setMaxResults(
     * pageSize ); return criteria.list();
     */
  }

  private void buildCriteriaForSortParameters( Criteria criteria, final String sortedBy, String sortedOn )
  {
    if ( sortedOn != null && sortedOn.equals( "1" ) )
    {
      criteria.addOrder( Order.asc( "user.lastName" ) ).addOrder( Order.asc( "user.firstName" ) );
    }

    else if ( sortedOn != null && sortedOn.equals( "2" ) )
    {
      try
      {
        criteria.addOrder( new Order( sortedOn, true )
        {
          @Override
          public String toSqlString( Criteria criteria, CriteriaQuery criteriaQuery ) throws HibernateException
          {
            String aliasName = criteriaQuery.getColumnsUsingProjection( criteria, "promotion.promotionName" )[0];
            StringBuffer sb = new StringBuffer();
            sb.append( "regexp_substr(" + aliasName + ",'*[[^:punct:]]') " + sortedBy );
            sb.append( ", to_number(regexp_substr(" + aliasName + ",'[[:digit:]]*')) " + sortedBy );
            sb.append( ", " + aliasName + " " + sortedBy );
            return sb.toString();
          }
        } );
      }
      catch( HibernateException hibernateException )
      {
        criteria.addOrder( !sortedBy.equals( "asc" ) ? Order.desc( "promotion.promotionName" ) : Order.asc( "promotion.promotionName" ) );
      }
    }

    else if ( sortedOn != null && sortedOn.equals( "3" ) )
    {
      criteria.addOrder( !sortedBy.equals( "asc" ) ? Order.desc( "purlRecipient.awardDate" ) : Order.asc( "purlRecipient.awardDate" ) ).addOrder( Order.asc( "user.lastName" ) )
          .addOrder( Order.asc( "user.firstName" ) );

    }
    else if ( sortedOn != null && sortedOn.equals( "4" ) )
    {
      criteria.addOrder( !sortedBy.equals( "asc" ) ? Order.desc( "purlRecipient.awardDate" ) : Order.asc( "purlRecipient.awardDate" ) );
    }
  }

  @Override
  public int getGlobalUpcomingPurlRecipientsCount()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getGlobalUpcomingPurlRecipientsCount" );
    query.setParameter( "userId", UserManager.getUser().getUserId() );
    return (Integer)query.uniqueResult();
  }

  @Override
  public int getTeamUpcomingPurlRecipientsCount( List<Long> nodeIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getTeamUpcomingPurlRecipientsCount" );
    query.setParameterList( "nodeIds", nodeIds );
    query.setParameter( "userId", UserManager.getUser().getUserId() );
    return (Integer)query.uniqueResult();
  }

  @Override
  public int getFollowedUserUpcomingPurlRecipientsCount( List<Long> userIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getFollowedUserUpcomingPurlRecipientsCount" );
    query.setParameterList( "userIds", userIds );
    query.setParameter( "userId", UserManager.getUser().getUserId() );
    return (Integer)query.uniqueResult();
  }

  @Override
  public int getGlobalAwardedPurlRecipientsCount()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getGlobalAwardedPurlRecipientsCount" );
    return (Integer)query.uniqueResult();
  }

  @Override
  public int getTeamAwardedPurlRecipientsCount( List<Long> nodeIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getTeamAwardedPurlRecipientsCount" );
    query.setParameterList( "nodeIds", nodeIds );
    return (Integer)query.uniqueResult();
  }

  @Override
  public int getFollowedUserAwardedPurlRecipientsCount( List<Long> userIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getFollowedUserAwardedPurlRecipientsCount" );
    query.setParameterList( "userIds", userIds );
    return (Integer)query.uniqueResult();
  }

  public PurlRecipient getPurlRecipientByCelebrationManagerMessageId( Long celebrationManagerMessageId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getPurlRecipientByCelebrationManagerMessageId" );
    query.setParameter( "celebrationManagerMessageId", celebrationManagerMessageId );

    return (PurlRecipient)query.uniqueResult();
  }

  public int getPurlRecipientsCountForAutoInvite( Long promotionId, Long purlRecipientId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.PurlRecipientsCountForAutoInvite" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "purlRecipientId", purlRecipientId );
    Integer recipientCount = (Integer)query.uniqueResult();
    return recipientCount.intValue();
  }

  @Override
  public List<PurlCelebrationsView> getPurlRecipientsForCelebrationPage( Map<String, Object> queryParams )
  {
    CallPrcFetchRecognitionPurlsList procedure = new CallPrcFetchRecognitionPurlsList( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( queryParams );

    Integer returnCode = (Integer)outParams.get( "p_out_returncode" );
    if ( returnCode != 0 )
    {
      return new ArrayList<PurlCelebrationsView>();
    }
    return (ArrayList<PurlCelebrationsView>)outParams.get( "p_out_result_set" );
  }

  public void setDataSource( DataSource dataSource )
  {
    // TODO what if you want to use a JdbcTemplate by preference,
    // for a native extractor?
    this.jdbcTemplate = new JdbcTemplate( dataSource );
    this.dataSource = dataSource;
  }

  @Override
  public int getRecommendedPurlRecipientsCountForGivenContributor( Long userId, PurlRecipientType pastOrUpcoming )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getRecommendedPurlRecipientsCountForGivenContributor" );
    query.setParameter( "userId", userId );
    query.setParameter( "past_or_upcoming", pastOrUpcoming.getCode() );
    return (Integer)query.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  public List<PurlRecipientValue> getUpComingCelebrationList()
  {
    String[] purlRecipientStates = new String[] { PurlRecipientState.INVITATION, PurlRecipientState.CONTRIBUTION, PurlRecipientState.RECOGNITION, PurlRecipientState.COMPLETE };
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getUpComingCelebrationList" );
    query.setParameterList( "purlRecipientStates", purlRecipientStates );
    query.setResultTransformer( new UpComingCelebrationsResultTransformer() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private static class UpComingCelebrationsResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      PurlRecipientValue bean = new PurlRecipientValue();

      Long purlRecipientId = (Long)tuple[0];
      Long userId = (Long)tuple[1];
      Long promotionId = (Long)tuple[2];
      String anniversary = (String)tuple[3];
      Date awardDate = (Date)tuple[4];

      bean.setPurlRecipientId( purlRecipientId );
      bean.setUserId( userId );
      bean.setPromotionId( promotionId );
      bean.setAnniversary( anniversary );
      bean.setAwardDate( awardDate );

      return bean;
    }
  }

  public List<PurlRecipient> getPurlRecipientByUserId( Long userId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( PurlRecipient.class, "purlRecipient" );
    criteria.createAlias( "purlRecipient.user", "user" );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.in( "purlRecipient.state",
                                   new PurlRecipientState[] { PurlRecipientState.lookup( PurlRecipientState.INVITATION ), PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) } ) );

    return (List<PurlRecipient>)criteria.list();

  }

  public int getPurlAwardDate( Long userId, Long purlRecipientId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getPurlAwardDate" );
    query.setParameter( "userId", userId );
    query.setParameter( "purlRecipientId", purlRecipientId );
    return (Integer)query.uniqueResult();
  }

  public int getCelebrationAwardDate( Long userId, Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getCelebrationAwardDate" );
    query.setParameter( "userId", userId );
    query.setParameter( "claimId", claimId );
    return (Integer)query.uniqueResult();
  }

//Customization for the WIP#51332 start
  public List<PurlRecipient> getCustomSortOfUpcomingCelebration ( Long charaterticsDivisionId , Long userId , int pageNumber, int pageSize )
	  {

		  Query query = getSession()
		  	.getNamedQuery( "com.biperf.core.domain.purl.PurlRecipient.getCustomSortOfUpcomingCelebration" );
		  query.setParameter( "charaterticsDivisionId", charaterticsDivisionId );
		  query.setParameter( "userId", userId );
		  query.setMaxResults( pageSize );
		    if ( pageNumber > 1 )
		    {
		      query.setFirstResult( pageSize * ( pageNumber - 1 ) );
		    }
		  		  return query.list();
	  }
    
//Customization for the WIP#51332 end
}
