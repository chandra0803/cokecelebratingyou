
package com.biperf.core.service.engageprogram;

import java.util.List;
import java.util.UUID;

import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.service.SAO;

public interface EngageProgramService extends SAO
{
  public static final String BEAN_NAME = "engageProgramService";

  boolean saveEngageProgramDetails( EngageProgram program ) throws Exception;

  EngageProgram getEngageProgramByExternalProgramIdandType( UUID programId, String programType );

  EngageProgram getEngageProgramByExternalProgramId( UUID programId );

  List<EngageProgram> getAllLiveEngagePrograms();

  List<String> getEligibleProgramCmxCode();

}
