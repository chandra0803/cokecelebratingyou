/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.ots;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.ots.OTSBatch;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.vo.ots.OTSProgramVO;

/**
 * TODO Javadoc for OTSProgramDAO.
 * 
 * @author rajadura
 * @since Nov 22, 2017
 * 
 */
public interface OTSProgramDAO extends DAO
{
  public static final String BEAN_NAME = "otsProgramDAO";

  public List<OTSProgramVO> getOTSPrograms();

  public void save( OTSProgram program );

  public OTSProgram getOTSProgramByProgramNumber( Long programNumber );

  public OTSProgram getOTSProgramByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public void saveBatch( OTSBatch otsBatch );

  public OTSBatch getOTSBatchByBatchNumber( Long batchNumber );

  public ProgramAudience getOTSProgramAudienceByProgramNumberAndAudienceId( Long ProgramId, Long AudienceId );
}
