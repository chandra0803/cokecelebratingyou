
package com.biperf.core.service.engageprogram.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.dao.engageprogram.EngageProgramDAO;
import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.service.engageprogram.EngageProgramService;

@Service( "engageProgramService" )
public class EngageProgramServiceImpl implements EngageProgramService
{

  private static final Log log = LogFactory.getLog( EngageProgramServiceImpl.class );

  @Autowired
  private EngageProgramDAO engageProgramDAO;

  @Override
  public boolean saveEngageProgramDetails( EngageProgram program ) throws Exception
  {
    engageProgramDAO.saveEngageProgramDetails( program );
    return true;
  }

  @Override
  public EngageProgram getEngageProgramByExternalProgramIdandType( UUID programId, String programType )
  {
    return engageProgramDAO.fetchEngageProgramByExternalProgramIdandType( programId, programType );
  }

  @Override
  public EngageProgram getEngageProgramByExternalProgramId( UUID programId )
  {
    return engageProgramDAO.fetchEngageProgramByExternalProgramId( programId );
  }

  @Override
  public List<EngageProgram> getAllLiveEngagePrograms()
  {
    return engageProgramDAO.fetchAllLiveEngagePrograms();
  }

  @Override
  public List<String> getEligibleProgramCmxCode()
  {
    return engageProgramDAO.fetchEligibleProgramCmxCode();
  }

}
