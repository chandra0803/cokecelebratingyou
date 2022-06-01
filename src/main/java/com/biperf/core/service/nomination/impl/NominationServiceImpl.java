
package com.biperf.core.service.nomination.impl;

import java.util.Map;

import com.biperf.core.dao.nomination.NominationDAO;
import com.biperf.core.service.nomination.NominationService;

public class NominationServiceImpl implements NominationService
{

  private NominationDAO nominationDAO;

  @Override
  public Map<String, Object> verifyImportFile( Long importFileId, Long userId, Long promotionId )
  {

    return nominationDAO.verifyImportFile( importFileId, "V", userId, promotionId );
  }

  @Override
  public Map<String, Object> importImportFile( Long importFileId, Long userId, Long promotionId )
  {

    return nominationDAO.verifyImportFile( importFileId, "L", userId, promotionId );
  }

  @Override
  public Map<String, Object> getNominationMoreInfoPageData( Long claimId, Long submitterId, String locale )
  {
    return nominationDAO.getNominationMoreInfoPageData( claimId, submitterId, locale );
  }

  public void setNominationDAO( NominationDAO nominationDAO )
  {
    this.nominationDAO = nominationDAO;
  }

}
