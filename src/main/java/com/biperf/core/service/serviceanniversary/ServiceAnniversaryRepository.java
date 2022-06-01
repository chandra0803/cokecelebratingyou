
package com.biperf.core.service.serviceanniversary;

public interface ServiceAnniversaryRepository
{
  public static final String BEAN_NAME = "serviceAnniversaryRepository";

  String getContributePageUrl( String invitationId, String uuid ) throws Exception;

  String getGiftCodePageUrl( String celebrationId ) throws Exception;

}
