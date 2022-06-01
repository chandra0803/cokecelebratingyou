
package com.biperf.core.service.ots;

import java.util.List;
import java.util.Locale;

import com.biperf.core.domain.ots.OTSBatch;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.Program;
import com.biperf.core.vo.ots.OTSProgramVO;

public interface OTSProgramService extends SAO
{
  public static final String BEAN_NAME = "otsProgramService";

  public List<OTSProgramVO> getOTSProgram();

  public void save( OTSProgram program );

  public OTSBatch saveBatchCmAsset( OTSBatch batch, String batchName, Locale locale ) throws ServiceErrorException;

  public OTSProgram getOTSProgramByProgramNumber( Long programNumber );

  public OTSProgram getOTSProgramByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public Batch getBatchDetails( String batchNumber, Program program );

  public boolean isUserInAudience( Long userId, OTSProgram otsProgram );

  public void saveBatch( OTSBatch otsBatch );

  public OTSBatch getOTSBatchByBatchNumber( Long batchNumber );

  public ProgramAudience getOTSProgramAudienceByProgramNumberAndAudienceId( Long ProgramId, Long AudienceId );
}
