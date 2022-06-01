/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.engageprogram;

import java.util.List;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.engageprogram.EngageProgram;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public interface EngageProgramDAO extends DAO
{
  public static final String BEAN_NAME = "engageProgramDAO";

  void saveEngageProgramDetails( EngageProgram program );

  EngageProgram fetchEngageProgramByExternalProgramIdandType( UUID programId, String programType );

  EngageProgram fetchEngageProgramByExternalProgramId( UUID programId );

  List<EngageProgram> fetchAllLiveEngagePrograms();

  List<String> fetchEligibleProgramCmxCode();

  EngageProgram fetchEngageProgramById( Long programId );

}
