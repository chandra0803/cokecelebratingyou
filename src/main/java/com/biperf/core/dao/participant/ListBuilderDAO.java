/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/ListBuilderDAO.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.CriteriaAudience;

/**
 * ListBuilderDAO.
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
 * <td>sharma</td>
 * <td>Jun 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ListBuilderDAO extends DAO
{
  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource );

  public List searchParticipants( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated );

  public List searchParticipants( CriteriaAudience criteriaAudience, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated, boolean isStandardClientAdmin );

  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated, boolean isStandardClientAdmin );

  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean preCalculated );

  public List getParticipantsListByAudienceId( Long audienceId );

  public List getParticipantsUserIdListByAudienceId( Long audienceId );

  public List searchParticipantsInactivePaxIncluded( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated );

  public List rosterSearch( AudienceCriteria audienceCriteria,
                            Long hierarchyId,
                            boolean produceUserIdOnly,
                            Set filterAudiences,
                            boolean preCalculated,
                            boolean isStandardClientAdmin,
                            boolean emailCheck,
                            String sortedOn,
                            String sortedBy,
                            int pageNbr,
                            int pageSize );
}
