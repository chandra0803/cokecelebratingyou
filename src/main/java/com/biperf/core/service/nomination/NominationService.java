
package com.biperf.core.service.nomination;

import java.util.Map;

import com.biperf.core.service.SAO;

public interface NominationService extends SAO
{

  String BEAN_NAME = "nominationService";

  public Map<String, Object> verifyImportFile( Long importFileId, Long userId, Long promotionId );

  public Map<String, Object> importImportFile( Long importFileId, Long userId, Long promotionId );

  public Map<String, Object> getNominationMoreInfoPageData( Long claimId, Long submitterId, String locale );

}
