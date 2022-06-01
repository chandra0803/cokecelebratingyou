
package com.biperf.core.domain.enums.nomination;

public interface NominationsInProgressConstants
{

  String CLAIM_ID = "claimId";
  String PROMO_ID = "promoId";
  String REMOVE_INPROGRESS_NOM_CLAIM_URL = "nomination/nomInProgress.do?method=remove";
  String MODIFY_INPROGRESS_NOM_CLAIM_URL = "recognitionWizard/sendRecognitionDisplay.do?method=editNom";
  String GET_INPROGRESS_NOM_CLAIM_URL = "nomination/nomInProgress.do?method=getInProgressNoms";
  String DISPLAY_INPROGRESS_NOM_CLAIM_LIST_URL = "nomination/nomInProgress.do?method=display";
  String DATE_STARTED = "date_created";
  String NOMINEE = "Name";
  String NOM_PROMO_NAME = "promotion_name";
  String NOM_INPROGRESS_COUNT = "nominationsInProgressCount";
  String PAGE_NUMBER = "pageNumber";
  String REMOVE_PARAM_NAME = "removeParams";

}
