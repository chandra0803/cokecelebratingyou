/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.StackRankDAO;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

/*
 * StackRankDAOImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 7, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

/**
 * StackRankDAOImpl.
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
 * <td>zahler</td>
 * <td>Mar 17, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class StackRankDAOImpl extends BaseDAO implements StackRankDAO
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The datasource used by this class to call stored procedures.
   */
  private DataSource dataSource;

  // ---------------------------------------------------------------------------
  // Persistence Methods
  // ---------------------------------------------------------------------------

  /**
   * Creates the stack rank lists for the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank whoses stack rank lists this method will create.
   */
  public void createStackRankLists( Long stackRankId )
  {
    CallPrcCreateStackRank proc = new CallPrcCreateStackRank( dataSource );
    proc.executeProcedure( stackRankId );
  }

  /**
   * Deletes the given stack rank.
   * 
   * @param stackRank the stack rank to delete.
   */
  public void deleteStackRank( StackRank stackRank )
  {
    getSession().delete( stackRank );
  }

  /**
   * Get the stack rank which has the given state that was created last i.e. max(date_created) by a
   * promotion id.
   * 
   * @param promotionId
   * @param state
   * @param associationRequest
   * @return the timestamp of the date created of the stack rank
   */
  public StackRank getLatestStackRankByPromotionId( Long promotionId, String state, AssociationRequestCollection associationRequest )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getLatestStackRankByPromotionId" );

    query.setLong( "promotionId", promotionId.longValue() );
    query.setString( "state", state );

    StackRank stackRank = null;
    List stackRankList = query.list();

    if ( stackRankList.size() > 0 )
    {
      stackRank = (StackRank)stackRankList.get( 0 );

      if ( associationRequest != null )
      {
        associationRequest.process( stackRank );
      }
    }

    return stackRank;
  }

  /**
   * Returns the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank to get.
   * @return the specified stack rank.
   */
  public StackRank getStackRank( Long stackRankId )
  {
    return (StackRank)getSession().get( StackRank.class, stackRankId );
  }

  /**
   * Returns the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank to get.
   * @return the specified stack rank.
   */
  public StackRank getStackRank( Long stackRankId, AssociationRequestCollection associationRequest )
  {
    StackRank stackRank = (StackRank)getSession().get( StackRank.class, stackRankId );

    if ( associationRequest != null )
    {
      associationRequest.process( stackRank );
    }

    return stackRank;
  }

  /**
   * Returns the specified stack rank.
   * 
   * @param queryConstraint the queryConstraint object.
   * @param associationRequest
   * @return the list of specified stack ranks.
   */
  public List getStackRankList( StackRankQueryConstraint queryConstraint, AssociationRequestCollection associationRequest )
  {
    List stackRankList = HibernateUtil.getObjectList( queryConstraint );

    if ( associationRequest != null )
    {
      Iterator stackRankIter = stackRankList.iterator();
      while ( stackRankIter.hasNext() )
      {
        associationRequest.process( (StackRank)stackRankIter.next() );
      }
    }

    return stackRankList;
  }

  /**
   * Saves the given stack rank.
   * 
   * @param stackRank the stack rank to save.
   * @return the saved version of the stack rank.
   */
  public StackRank saveStackRank( StackRank stackRank )
  {
    return (StackRank)HibernateUtil.saveOrUpdateOrShallowMerge( stackRank );
  }

  /**
   * Returns a count of stack rank product claim promotions that meet the specified criteria. Any
   * parameter can be left null so that the query is not constrained by that parameter.
   * 
   * @param queryConstraint
   * @return int the stack rank promotion list count with a specified state(#StackRankState)
   */
  public int getStackRankListCount( StackRankQueryConstraint queryConstraint )
  {
    return HibernateUtil.getObjectListCount( queryConstraint );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public DataSource getDataSource()
  {
    return dataSource;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Calls the stored procedure that creates the stack rank lists for the specified stack rank.
   */
  private static class CallPrcCreateStackRank extends StoredProcedure
  {
    private static final String STORED_PROC_NAME = "prc_stack_ranking";
    private static final String INPUT_PARAM_NAME = "p_in_stack_rank_id";
    private static final String OUTPUT_PARAM_NAME = "p_out_result_code";

    /**
     * Creates a <code>CallPrcCreateStackRank</code> object.
     * 
     * @param datasource the datasource used to call the stored procedure.
     */
    public CallPrcCreateStackRank( DataSource datasource )
    {
      super( datasource, STORED_PROC_NAME );
      declareParameter( new SqlParameter( INPUT_PARAM_NAME, Types.NUMERIC ) );
      declareParameter( new SqlOutParameter( OUTPUT_PARAM_NAME, Types.VARCHAR ) );
      // declareParameter( new SqlParameter( OUTPUT_PARAM_NAME, Types.VARCHAR ) );
      compile();
    }

    /**
     * Calls a stored procedure that creates the stack rank lists for the specified stack rank.
     * 
     * @param stackRankId the ID of the stack rank for which the stored procedure will create stack
     *          rank lists.
     * @return information returned by the stored procedure.
     */
    public PrcCreateStackRankReturnCode executeProcedure( Long stackRankId )
    {
      PrcCreateStackRankReturnCode returnCode = null;

      HashMap inParams = new HashMap();
      inParams.put( INPUT_PARAM_NAME, stackRankId );
      // inParams.put( OUTPUT_PARAM_NAME, null );
      Map outParams = execute( inParams );
      if ( outParams.size() > 0 )
      {
        returnCode = new PrcCreateStackRankReturnCode( (String)outParams.get( OUTPUT_PARAM_NAME ) );
      }

      return returnCode;
    }
  }

  /**
   * Represents the information returned by the Create Stack Rank stored procedure.
   */
  private static class PrcCreateStackRankReturnCode
  {
    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    /**
     * Delimits components of the return code.
     */
    private static final String DELIMITER = "|";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * The total number of stack ranks processed by the Create Stack Rank stored procedure.
     */
    private Integer totalProcessed;

    /**
     * The number of stack ranks successfully processed by the Create Stack Rank stored procedure.
     */
    private Integer processedSuccessfully;

    /**
     * The number of stack ranks unsuccessfully processed by the Create Stack Rank stored procedure.
     */
    private Integer processedUnsuccessfully;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Constructs a <code>PrcCreateStackRankReturnCode</code> object.
     * 
     * @param returnCode information returned by the Create Stack Rank stored procedure.
     */
    public PrcCreateStackRankReturnCode( String returnCode )
    {
      if ( returnCode != null )
      {
        String[] components = StringUtils.split( returnCode, DELIMITER );
        if ( components.length >= 3 )
        {
          totalProcessed = new Integer( components[0] );
          processedSuccessfully = new Integer( components[1] );
          processedUnsuccessfully = new Integer( components[2] );
        }
      }
    }

    // ------------------------------------------------------------------------
    // Getter Methods
    // ------------------------------------------------------------------------

    public Integer getProcessedSuccessfully()
    {
      return processedSuccessfully;
    }

    public Integer getProcessedUnsuccessfully()
    {
      return processedUnsuccessfully;
    }

    public Integer getTotalProcessed()
    {
      return totalProcessed;
    }
  }
}
