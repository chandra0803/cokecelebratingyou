
package com.biperf.core.dao.nomination;

import java.util.Map;

public interface NominationDAO
{

  String BEAN_NAME = "nominationDAO";

  public Map<String, Object> verifyImportFile( Long importFileId, String loadType, Long userId, Long promotionId );

  public Map<String, Object> refreshPendingNominationApprover( Map<String, Object> parameters );

  Map<String, Object> nominationsWinnersModule( Map<String, Object> parameters );

  public Map<String, Object> getNominationMoreInfoPageData( Long claimId, Long submitterId, String locale );
}
