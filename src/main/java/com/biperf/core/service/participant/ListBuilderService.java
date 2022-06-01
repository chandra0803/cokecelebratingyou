/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/ListBuilderService.java,v $
 */

package com.biperf.core.service.participant;

import java.util.List;
import java.util.Set;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.service.SAO;

/**
 * ListBuilderSearchService.
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
 * <td>Sathish</td>
 * <td>Jan 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ListBuilderService extends SAO
{

  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "listBuilderService";

  public List searchParticipants( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated );

  public List searchParticipants( CriteriaAudience criteriaAudience, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean loadUsers, boolean preCalculated );

  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated, boolean isStandardClientAdmin );

  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean preCalculated );

  public List searchParticipants( Long parentNodeId, boolean includeDescendantNodes, Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated );

  /**
   * Get the audience with its size.
   * 
   * @param audienceId
   * @return Audience
   */
  public Audience getAudienceWithSize( Long audienceId, boolean preCalculated );

  public List getParticipantsListByAudienceId( Long audienceId );

  public List getCriteriaAudienceParticipants( Long audienceId, boolean loadUsers );

  public List searchParticipantsInactivePaxIncluded( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated );

  public List rosterSearch( AudienceCriteria audienceCriteria,
                            Long hierarchyId,
                            boolean produceUserIdOnly,
                            Set filterAudiences,
                            boolean preCalculated,
                            boolean isStandardClientAdmin,
                            boolean emailCheckssss,
                            String SortedOn,
                            String sortedBy,
                            int pageSize,
                            int pageNbr );
  /* end */
}
