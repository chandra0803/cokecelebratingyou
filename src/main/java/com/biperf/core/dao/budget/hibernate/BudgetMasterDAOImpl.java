/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/budget/hibernate/BudgetMasterDAOImpl.java,v $
 */

package com.biperf.core.dao.budget.hibernate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.budget.BudgetQueryConstraint;
import com.biperf.core.dao.budget.limits.CharacteristicConstraintLimits;
import com.biperf.core.dao.reports.hibernate.CallPrcBudgetTransfer;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetReallocationHistory;
import com.biperf.core.domain.budget.BudgetReallocationHistoryBean;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.BudgetMeterData;
import com.biperf.core.value.BudgetReallocationValueBean;

/**
 * BudgetMasterDAOImpl.
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
 * <td>sedey</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class BudgetMasterDAOImpl extends BaseDAO implements BudgetMasterDAO
{
  private DataSource dataSource;

  /**
   * Get the BudgetMaster by the budgetMaster Id.
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#getBudgetMasterById(Long id)
   * @param id
   * @return BudgetMaster
   */
  public BudgetMaster getBudgetMasterById( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    return (BudgetMaster)session.get( BudgetMaster.class, id );
  }

  /**
   * Save or update the BudgetMaster.
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#saveBudgetMaster(BudgetMaster budgetMaster)
   * @param budgetMaster
   * @return BudgetMaster
   */
  public BudgetMaster saveBudgetMaster( BudgetMaster budgetMaster )
  {
    Session session = HibernateSessionManager.getSession();
    // TODO Why doesn't session.contains(budgetMaster) work?
    // If the object already exists in the session, the
    // NonUniqueObjectException will occur, if that is the
    // case, try to merge.

    // modify save logic to add records to history table on budget update.
    if ( budgetMaster.getId() == null )
    {
      session.save( budgetMaster );
    }
    else
    {
      budgetMaster = (BudgetMaster)session.merge( budgetMaster );
    }
    // Do a flush to force create of a history record
    session.flush();

    return budgetMaster;
  }

  /**
   * Delete the budgetMaster. Overridden from
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#deleteBudgetMaster(com.biperf.core.domain.budget.BudgetMaster)
   * @param budgetMaster
   */
  public void deleteBudgetMaster( BudgetMaster budgetMaster )
  {
    Session session = HibernateSessionManager.getSession();
    session.delete( budgetMaster );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#updateBudget(com.biperf.core.domain.budget.Budget)
   * @param budget
   * @return Budget
   */
  public Budget updateBudget( Budget budget )
  {
    Session session = HibernateSessionManager.getSession();
    session.merge( budget );
    return budget;
  }

  /**
   * Gets a list of all budgetMaster. Overridden from
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.budget.AllBudgetMasterList" ).list();
  }

  /**
   * Gets a list of active budgetMaster. Overridden from
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#getAllActive()
   * @return List
   */
  public List getAllActive()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.budget.ActiveBudgetMasterList" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#getAllActiveWithPromotions()
   * @return List
   */
  public List getAllActiveWithPromotions()
  {
    Session session = HibernateSessionManager.getSession();

    List budgetMasterList = session.getNamedQuery( "com.biperf.core.domain.budget.ActiveBudgetMasterList" ).list();

    Iterator budgetMasterListIter = budgetMasterList.iterator();
    while ( budgetMasterListIter.hasNext() )
    {
      BudgetMaster budgetMaster = (BudgetMaster)budgetMasterListIter.next();
      Hibernate.initialize( budgetMaster.getPromotions() );
      Hibernate.initialize( budgetMaster.getCashPromotions() );
    }
    return budgetMasterList;
  }

  public List getAllActiveWithPromotionsAndSegments()
  {
    Session session = HibernateSessionManager.getSession();

    List budgetMasterList = session.getNamedQuery( "com.biperf.core.domain.budget.ActiveBudgetMasterList" ).list();

    Iterator budgetMasterListIter = budgetMasterList.iterator();
    while ( budgetMasterListIter.hasNext() )
    {
      BudgetMaster budgetMaster = (BudgetMaster)budgetMasterListIter.next();
      Hibernate.initialize( budgetMaster.getPromotions() );
      Hibernate.initialize( budgetMaster.getCashPromotions() );
      Hibernate.initialize( budgetMaster.getBudgetSegments() );
    }
    return budgetMasterList;
  }

  @Override
  public List getActivePointsWithPromotionsAndSegments()
  {
    Session session = HibernateSessionManager.getSession();

    List budgetMasterList = session.getNamedQuery( "com.biperf.core.domain.budget.ActivePointsBudgetMasterList" ).list();

    Iterator budgetMasterListIter = budgetMasterList.iterator();
    while ( budgetMasterListIter.hasNext() )
    {
      BudgetMaster budgetMaster = (BudgetMaster)budgetMasterListIter.next();
      Hibernate.initialize( budgetMaster.getPromotions() );
      Hibernate.initialize( budgetMaster.getCashPromotions() );
      Hibernate.initialize( budgetMaster.getBudgetSegments() );
    }
    return budgetMasterList;
  }

  @Override
  public List getActiveCashWithPromotionsAndSegments()
  {
    Session session = HibernateSessionManager.getSession();

    List budgetMasterList = session.getNamedQuery( "com.biperf.core.domain.budget.ActiveCashBudgetMasterList" ).list();

    Iterator budgetMasterListIter = budgetMasterList.iterator();
    while ( budgetMasterListIter.hasNext() )
    {
      BudgetMaster budgetMaster = (BudgetMaster)budgetMasterListIter.next();
      Hibernate.initialize( budgetMaster.getPromotions() );
      Hibernate.initialize( budgetMaster.getCashPromotions() );
      Hibernate.initialize( budgetMaster.getBudgetSegments() );
    }
    return budgetMasterList;
  }

  /**
   * Gets a list of all budgetMaster. Overridden from
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#getAllInactive()
   * @return List
   */
  public List getAllInactive()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.budget.InactiveBudgetMasterList" ).list();
  }

  /**
   * Gets a list of all budgets that are not active for a specified budget segment
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#getAllBudgetsNotActive()
   * @return List
   */
  public List getAllBudgetsNotActive( long budgetSegmentId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.budget.AllBudgetsNotActiveInBudgetSegment" );
    query.setParameter( "budgetSegmentId", new Long( budgetSegmentId ) );
    return query.list();
  }

  public List getAllBudgetsForUser( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.AllBudgetsForUser" );
    query.setParameter( "userId", userId );

    return query.list();
  }

  public Budget getBudgetForUserbyBudgetSegmentId( Long budgetSegmentId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.AvailableBudgetForUserByBudgetSegmentId" );
    query.setParameter( "userId", userId );
    query.setParameter( "budgetSegmentId", budgetSegmentId );

    return (Budget)query.uniqueResult();
  }

  public List<Budget> getAllBudgetsBySegmentIdAndUserId( Long budgetSegmentId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getAllBudgetsBySegmentIdAndUserId" );
    query.setParameter( "userId", userId );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    return query.list();
  }

  public boolean getAvailableUsersForBudgetTransfer( Long promoId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.AvailableUsersForBudgetTransfer" );
    query.setParameter( "userId", userId );
    query.setParameter( "promoId", promoId );
    List results = query.list();
    return !CollectionUtils.isEmpty( results );
  }

  public BudgetSegment saveBudgetSegment( BudgetSegment budgetSegment )
  {
    Session session = HibernateSessionManager.getSession();
    try
    {
      session.saveOrUpdate( budgetSegment );
    }
    catch( NonUniqueObjectException e )
    {
      budgetSegment = (BudgetSegment)session.merge( budgetSegment );
    }
    // Do a flush to force create of a history record
    session.flush();

    return budgetSegment;
  }

  public Budget getBudgetForNodeByBudgetSegmentId( Long budgetSegmentId, Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.AvailableBudgetForNodeByBudgetSegmentId" );
    query.setParameter( "nodeId", nodeId );
    query.setParameter( "budgetSegmentId", budgetSegmentId );

    return (Budget)query.uniqueResult();
  }

  public List getBudgetForNodeId( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.AllBudgetsForNodeId" );
    query.setParameter( "nodeId", nodeId );

    return query.list();
  }

  /**
   * @param budgetQueryConstraint
   * @return a List of Budgets that match the constraints
   */
  public List getBudgetList( BudgetQueryConstraint budgetQueryConstraint )
  {
    // It seems like it may be impossible or messy to do this all with a criteria
    // based query. For now we will filter the results of the criteria based on
    // characteristics. In the future could change to do in criteria or get rid of
    // criteria query altogether since it is currently not doing much.
    if ( !budgetQueryConstraint.isUserBasedConstraints() && ( budgetQueryConstraint.getNodeTypeIds() == null || budgetQueryConstraint.getNodeTypeIds().isEmpty() ) )
    {
      return new ArrayList();
    }
    List budgetList = HibernateUtil.getObjectList( budgetQueryConstraint );
    return filterBudgetList( budgetList, budgetQueryConstraint );
  }

  /**
   * @param budgetList a List of Budgets to filter
   * @param budgetQueryConstraint a BudgetQueryConstraint containing the constraints to filter on
   * @return a List of Budget objects with the filter applied
   */
  private List filterBudgetList( List budgetList, BudgetQueryConstraint budgetQueryConstraint )
  {
    if ( budgetQueryConstraint == null || budgetQueryConstraint.getCharacteristicConstraintLimits() == null || budgetQueryConstraint.getCharacteristicConstraintLimits().isEmpty() )
    {
      return budgetList;
    }
    List filteredBudgetList = new ArrayList();
    if ( budgetList != null )
    {
      for ( Iterator budgetIter = budgetList.iterator(); budgetIter.hasNext(); )
      {
        Budget budget = (Budget)budgetIter.next();
        if ( budgetQueryConstraint.isUserBasedConstraints() && budget.getUser() != null
            && passesUserConstraint( budget.getUser().getUserCharacteristics(), budgetQueryConstraint.getCharacteristicConstraintLimits().values() ) )
        {
          filteredBudgetList.add( budget );
        }
        else
        {
          if ( budget.getNode() != null
              && passesNodeConstraint( budget.getNode().getNodeType().getId(), budget.getNode().getNodeCharacteristics(), budgetQueryConstraint.getCharacteristicConstraintLimits().values() ) )
          {
            filteredBudgetList.add( budget );
          }
        }
      }
    }
    return filteredBudgetList;

  }

  /*
   * Checks if all the characteristics on the budget pass the given constrainLimits.
   */
  private boolean passesUserConstraint( Set characteristics, Collection constraintLimits )
  {
    if ( characteristics == null )
    {
      return false;
    }
    // Make a map of characteristics for easier lookup
    Map characteristicMap = new HashMap();
    for ( Iterator characteristicIter = characteristics.iterator(); characteristicIter.hasNext(); )
    {
      UserCharacteristic characteristic = (UserCharacteristic)characteristicIter.next();
      characteristicMap.put( characteristic.getUserCharacteristicType().getId(), characteristic );
    }
    for ( Iterator characteristicConstraintsIter = constraintLimits.iterator(); characteristicConstraintsIter.hasNext(); )
    {
      CharacteristicConstraintLimits constraintLimit = (CharacteristicConstraintLimits)characteristicConstraintsIter.next();
      UserCharacteristic characteristic = (UserCharacteristic)characteristicMap.get( constraintLimit.getCharacteristicId() );
      if ( !constraintLimit.inConstraints( characteristic ) )
      {
        return false;
      }
    }
    return true;
  }

  /*
   * Checks if all the characteristics on the budget pass the given constrainLimits.
   */
  private boolean passesNodeConstraint( Long nodeTypeId, Set characteristics, Collection constraintLimits )
  {
    if ( characteristics == null )
    {
      return false;
    }
    // Make a map of characteristics for easier lookup
    Map characteristicMap = new HashMap();
    for ( Iterator characteristicIter = characteristics.iterator(); characteristicIter.hasNext(); )
    {
      NodeCharacteristic characteristic = (NodeCharacteristic)characteristicIter.next();
      characteristicMap.put( characteristic.getNodeTypeCharacteristicType().getId(), characteristic );
    }
    for ( Iterator characteristicConstraintsIter = constraintLimits.iterator(); characteristicConstraintsIter.hasNext(); )
    {
      CharacteristicConstraintLimits constraintLimit = (CharacteristicConstraintLimits)characteristicConstraintsIter.next();
      NodeCharacteristic characteristic = (NodeCharacteristic)characteristicMap.get( constraintLimit.getCharacteristicId() );
      if ( !constraintLimit.inConstraints( nodeTypeId, characteristic ) )
      {
        return false;
      }
    }
    return true;
  }

  public List getUserIdsByPromoIdWithOriginalNodeBudgetValue( Long promoId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.UserIdsByPromoIdWithOriginalNodeBudgetValue" );

    query.setParameter( "promoId", promoId );

    return query.list();
  }

  public List getUserIdsByPromoIdWithOriginalPaxBudgetValue( Long promoId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.UserIdsByPromoIdWithOriginalPaxBudgetValue" );

    query.setParameter( "promoId", promoId );

    return query.list();
  }

  /**
   * Gets count of all budgets that are active for a specified budget master
   * @param budgetMasterId
   * @return Long
   */
  public Long getNumberOfActiveInBudgetSegment( Long budgetSegmentId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.budget.getNumberOfActiveInBudgetSegment" );

    query.setParameter( "budgetSegmentId", budgetSegmentId );
    return (Long)query.uniqueResult();

  }

  /**
   * Gets a list of all budgets that are active for a specified PAX based budget segment
   * @param budgetSegmentId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForUser( Long budgetSegmentId, Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.budget.getAllActiveInBudgetSegmentForUser" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    query.setParameter( "userId", userId );
    List<Budget> activeBudgets = query.list();
    return activeBudgets;
  }

  /**
   * Gets a the active budget for the given budget segment.  Will not load any data that is not on the Budget table
   * @param budgetSegmentId 
   * 
   * @return Budget
   */
  @Override
  public Budget getActiveBudgetForCentralBudgetMasterBySegment( Long budgetSegmentId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.budget.getBudgetForCentralBudgetMasterBySegment" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    return (Budget)query.uniqueResult();
  }

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget segment
   * @param budgetSegmentId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForUserNode( Long budgetSegmentId, Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.budget.getAllActiveInBudgetSegmentForUserNode" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    query.setParameter( "userId", userId );

    List<Budget> activeBudgets = query.list();
    return activeBudgets;
  }

  /**
   * Gets a list of all budgets that are active for a specified NODE based budget segment where user is a owner
   * @param budgetSegmentId 
   * @param userId 
   * 
   * @return List
   */
  public List<Budget> getAllActiveInBudgetSegmentForOwnerUserNode( Long budgetSegmentId, Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.budget.getAllActiveInBudgetSegmentForOwnerUserNode" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    query.setParameter( "userId", userId );

    List<Budget> activeBudgets = query.list();
    return activeBudgets;
  }

  @SuppressWarnings( "unchecked" )
  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForNode( Long nodeId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending )
  {
    Session session = HibernateSessionManager.getSession();
    final String ORDER_BY_CLAUSE = " order by 7 " + ( isOrderByDateAscending ? "asc" : "desc" );
    SQLQuery query = session.createSQLQuery( session.getNamedQuery( "com.biperf.core.domain.budget.getAllBudgetReallocationHistoryForNode" ).getQueryString() + ORDER_BY_CLAUSE );
    query.setLong( "nodeId", nodeId );
    query.setLong( "budgetSegmentId", budgetSegmentId );
    query.setString( "startDate", startDate );
    query.setString( "endDate", endDate );
    query.setString( "userLocale", getUserLocale() );
    query.setResultTransformer( new BudgetReallocationHistoryBeanTransformer() );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<BudgetReallocationHistoryBean> getBudgetAllocationHistoryForPax( Long userId, Long budgetSegmentId, String startDate, String endDate, boolean isOrderByDateAscending )
  {
    Session session = HibernateSessionManager.getSession();
    final String ORDER_BY_CLAUSE = " order by 7 " + ( isOrderByDateAscending ? "asc" : "desc" );
    SQLQuery query = session.createSQLQuery( session.getNamedQuery( "com.biperf.core.domain.budget.getAllBudgetReallocationHistoryForPax" ).getQueryString() + ORDER_BY_CLAUSE );
    query.setLong( "userId", userId );
    query.setLong( "budgetSegmentId", budgetSegmentId );
    query.setString( "startDate", startDate );
    query.setString( "endDate", endDate );
    query.setString( "userLocale", getUserLocale() );
    query.setResultTransformer( new BudgetReallocationHistoryBeanTransformer() );
    return query.list();
  }

  public String getUserLocale()
  {
    Locale locale = UserManager.getLocale();

    String userLocale = locale.toString();

    return userLocale;
  }

  public BudgetMaster getBudgetMasterByBudgetMasterId( Long budgetMasterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.budgetMasterById" );
    query.setParameter( "budgetMasterId", budgetMasterId );

    return (BudgetMaster)query.uniqueResult();
  }

  @Override
  public int getBudgetCountForBudgetSegment( Long budgetSegmentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.BudgetsAttachedCount" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    return (Integer)query.uniqueResult();
  }

  public BudgetReallocationHistory saveBudgetReallocationHistory( BudgetReallocationHistory budgetReallocationHistory )
  {
    getSession().save( budgetReallocationHistory );
    return budgetReallocationHistory;
  }

  /**
   * Get the Budget for a budgetId
   * 
   * @param budgetMasterId
   * @param budgetId
   * @return Budget
   */
  public Budget getBudgetbyId( Long budgetId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetByBudgetId" );
    query.setParameter( "budgetId", budgetId );

    return (Budget)query.uniqueResult();
  }

  public Budget getBudget( Long budgetSegmentId, Long budgetId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetByBudgetIdAndSegmentId" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    query.setParameter( "budgetId", budgetId );

    return (Budget)query.uniqueResult();
  }

  public List<Budget> getBudgetsByBudgetSegmentId( Long budgetSegmentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetsByBudgetSegmentId" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );

    List<Budget> budgets = query.list();
    return budgets;
  }

  @SuppressWarnings( { "unchecked" } )
  @Override
  public List<BudgetMeterData> getBudgetMeterDataForPax( Long userId, List<Long> eligiblePromotionIds, String today )
  {
    if ( eligiblePromotionIds == null || eligiblePromotionIds.isEmpty() )
    {
      return new ArrayList<BudgetMeterData>();
    }

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetMeterDataForPax" );
    query.setParameter( "userId", userId );
    query.setParameterList( "promotionIds", eligiblePromotionIds );
    query.setString( "locale", getUserLocale() );
    query.setString( "today", today );
    query.setResultTransformer( Transformers.aliasToBean( BudgetMeterData.class ) );
    return query.list();
  }

  public BudgetSegment getBudgetSegmentById( Long budgetSegmentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetSegmentById" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );

    return (BudgetSegment)query.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  public List<BudgetSegment> getBudgetSegmentsByBudgetMasterId( Long budgetMasterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetSegmentsByBudgetMasterId" );
    query.setParameter( "budgetMasterId", budgetMasterId );

    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<BudgetSegment> getBudgetSegmentsForDistribution( Long budgetMasterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetSegmentsForDistribution" );
    query.setParameter( "budgetMasterId", budgetMasterId );

    return query.list();
  }

  public List<BudgetSegment> getBudgetSegmentsToTransferByBudgetMasterId( Long budgetMasterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetSegmentsToTransferByBudgetMasterId" );
    query.setParameter( "budgetMasterId", budgetMasterId );

    return query.list();
  }

  public boolean isBudgetSegmentNameUnique( Long budgetMasterId, String segmentName, Long currentSegmentId )
  {
    boolean isUnique = true;

    if ( currentSegmentId == null )
    {
      currentSegmentId = new Long( 0 );
    }

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.BudgetSegmentByNameCount" );

    query.setParameter( "budgetMasterId", budgetMasterId );
    query.setParameter( "segmentName", segmentName.toLowerCase() );
    query.setParameter( "segmentId", currentSegmentId );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;

    return isUnique;
  }

  public boolean isAllowBudgetReallocActiveForBudgetMaster( Long budgetMasterId )
  {
    boolean isAllowBudgetRealloc = true;

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.AllowBudgetReallocationActiveCount" );

    query.setParameter( "budgetMasterId", budgetMasterId );

    Integer count = (Integer)query.uniqueResult();
    isAllowBudgetRealloc = count.intValue() > 0;

    return isAllowBudgetRealloc;
  }

  public int getActiveBudgetCountByBudgetMaster( Long budgetMasterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.GetActiveBudgetCountByBudgetMaster" );
    query.setParameter( "budgetMasterId", budgetMasterId );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  @Override
  public List<PromotionBudgetSweep> getPromotionBudgetSweepsByPromotionId( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getPromotionBudgetSweepsByPromotionId" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  @Override
  public void updatePromotionBudgetSweep( PromotionBudgetSweep promotionBudgetSweep )
  {
    Session session = HibernateSessionManager.getSession();
    try
    {
      session.update( promotionBudgetSweep );
    }
    catch( NonUniqueObjectException e )
    {
      session.merge( promotionBudgetSweep );
    }
    session.flush();
  }

  public int getActiveBudgetCountByBudgetSegment( Long budgetSegmentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.GetActiveBudgetCountByBudgetSegment" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  @Override
  public boolean isParticipantHasBudgetMeter( Long userId, List<Long> eligiblePromotionIds )
  {
    if ( eligiblePromotionIds == null || eligiblePromotionIds.isEmpty() )
    {
      return false;
    }

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.isParticipantHasBudgetMeter" );
    query.setParameter( "userId", userId );
    return !query.list().isEmpty();
  }

  @Override
  public Map<String, Object> verifyImportFile( Long fileId, Long promotionId )
  {
    CallPrBudgetVerifyImport badgeVerifyProc = new CallPrBudgetVerifyImport( dataSource );
    return badgeVerifyProc.executeProcedure( fileId, promotionId );
  }

  @Override
  public Map<String, Object> importImportFile( Long fileId, Long userId )
  {
    CallPrBudgetLoadImport badgeLoadProc = new CallPrBudgetLoadImport( dataSource );
    return badgeLoadProc.executeProcedure( fileId, userId );
  }

  @Override
  public Map<String, Object> verifyBudgetDistributionImportFile( Long fileId, Long budgetMasterId, Long budgetSegmentId )
  {
    CallPrcBudgetDistributionVerifyImport budgetDistributionVerifyImportProc = new CallPrcBudgetDistributionVerifyImport( dataSource );
    return budgetDistributionVerifyImportProc.executeProcedure( fileId, budgetMasterId, budgetSegmentId, "V" );
  }

  @Override
  public Map<String, Object> importBudgetDistributionImportFile( Long fileId, Long userId, Long budgetMasterId, Long budgetSegmentId )
  {
    CallPrcBudgetDistributionVerifyImport budgetDistributionVerifyImportProc = new CallPrcBudgetDistributionVerifyImport( dataSource );
    return budgetDistributionVerifyImportProc.executeProcedure( fileId, budgetMasterId, budgetSegmentId, "L" );
  }

  @Override
  public List<BudgetReallocationValueBean> fetchChildBudgets( Long budgetOwnerId, Long budgetMasterId, Long budgetSegmentId, BigDecimal mediaRatio )
  {
    CallPrcBudgetTransfer procedure = new CallPrcBudgetTransfer( dataSource );
    @SuppressWarnings( "rawtypes" )
    Map results = procedure.executeProcedure( budgetOwnerId, budgetMasterId, budgetSegmentId, mediaRatio );
    return (List<BudgetReallocationValueBean>)results.get( "p_out_result_set" );
  }

  @Override
  public Budget getCentralBudgetByMasterIdAndSegmentId( Long budgetMasterId, Long budgetSegmentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getCentralBudgetByMasterIdAndSegmentId" );
    query.setParameter( "budgetMasterId", budgetMasterId );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    Object result = query.uniqueResult();
    if ( result != null )
    {
      return (Budget)result;
    }

    return null;
  }

  @Override
  public Map<String, Object> getExtractInactiveBudgetsData( Map<String, Object> extractParameters )
  {
    CallPrcInactiveBudgetsExtract procedure = new CallPrcInactiveBudgetsExtract( dataSource );
    return procedure.executeProcedure( extractParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public List getBudgetMasterListForDistribution()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.budget.getBudgetMasterListForDistribution" ).list();
  }

  @SuppressWarnings( { "rawtypes", "serial" } )
  private class BudgetReallocationHistoryBeanTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      BudgetReallocationHistoryBean valueBean = new BudgetReallocationHistoryBean();

      valueBean.setId( extractLong( tuple[0] ) );
      valueBean.setTxNodeId( extractLong( tuple[1] ) );
      valueBean.setTxUserId( extractLong( tuple[2] ) );
      valueBean.setBudgetId( extractLong( tuple[3] ) );
      valueBean.setAmount( extractBigDecimal( tuple[4] ) );
      valueBean.setCreatedBy( extractLong( tuple[5] ) );
      valueBean.setDateCreated( (Timestamp)tuple[6] );
      valueBean.setSource( extractString( tuple[7] ) );
      return valueBean;
    }

    @Override
    public List transformList( List collection )
    {
      return collection;
    }
  }

  @SuppressWarnings( "unchecked" )
  public List<Budget> getPaxorNodeBudgetsBySegmentIdForViewingBudgets( Long budgetSegmentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getPaxorNodeBudgetsBySegmentId" );
    query.setParameter( "budgetSegmentId", budgetSegmentId );
    query.setResultTransformer( new BudgetResultsetTransformer() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class BudgetResultsetTransformer extends BaseResultTransformer
  {
    BudgetSegment segment = null;
    User user = null;
    Node node = null;
    Budget budget = null;

    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      segment = new BudgetSegment();
      user = new User();
      node = new Node();
      budget = new Budget();

      budget.setId( extractLong( tuple[0] ) );

      segment.setId( extractLong( tuple[1] ) );
      budget.setBudgetSegment( segment );

      if ( tuple[2] != null )
      {
        user.setId( extractLong( tuple[2] ) );
        user.setFirstName( (String)tuple[3] );
        user.setLastName( (String)tuple[4] );
        budget.setUser( user );
      }

      if ( tuple[5] != null )
      {
        node.setId( extractLong( tuple[5] ) );
        node.setName( (String)tuple[6] );
        budget.setNode( node );
      }

      budget.setOriginalValue( tuple[7] != null ? extractBigDecimal( tuple[7] ) : null );
      budget.setCurrentValue( tuple[8] != null ? extractBigDecimal( tuple[8] ) : null );
      budget.setStatus( BudgetStatusType.lookup( (String)tuple[9] ) );
      budget.setBudgetDeletable( "true".equals( (String)tuple[10] ) ? true : false );
      return budget;
    }
  }

  @Override
  public BudgetMaster getBudgetMasterByIdAndAwardType( Long id, String awardType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetMasterByIdAndAwardType" );
    query.setParameter( "budgetMasterId", id );
    query.setParameter( "awardType", awardType );
    return (BudgetMaster)query.uniqueResult();
  }

  /**
   * Save or update the Budget
   * 
   * @see com.biperf.core.dao.budget.BudgetMasterDAO#saveBudget(Budget budget)
   * @param budget
   * @return Budget
   */
  public Budget saveBudget( Budget budget )
  {
    Session session = HibernateSessionManager.getSession();
    // TODO Why doesn't session.contains(budgetMaster) work?
    // If the object already exists in the session, the
    // NonUniqueObjectException will occur, if that is the
    // case, try to merge.

    // modify save logic to add records to history table on budget update.
    if ( budget.getId() == null )
    {
      session.save( budget );
    }
    else
    {
      budget = (Budget)session.merge( budget );
    }
    // Do a flush to force create of a history record
    session.flush();

    return budget;
  }
  
//Client customization start
  public Budget getActiveBudgetForNodebyBudgetMasterId( Long budgetMasterId, Long nodeId, Long budgetSegmentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.ActiveBudgetForNodeByBudgetMasterId" );
    query.setParameter( "nodeId", nodeId );
    query.setParameter( "budgetMasterId", budgetMasterId);
    query.setParameter( "budgetSegmentId", budgetSegmentId);

    return (Budget)query.uniqueResult();
  }
  
  public List<Budget> getAllActiveInBudgetSegmentForParentNode( Long budgetSegmentId, Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getAllActiveInBudgetSegmentForParentNode" );
    query.setParameter( "nodeId", nodeId );
    query.setParameter( "budgetSegmentId", budgetSegmentId);
    List<Budget> activeBudgets = query.list();
    return activeBudgets;
  }
  //Client customization end

}
