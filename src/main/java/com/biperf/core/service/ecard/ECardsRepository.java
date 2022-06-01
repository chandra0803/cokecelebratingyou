
package com.biperf.core.service.ecard;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.ecard.ECardMigrateView;
import com.biperf.core.value.ecard.ECardRequest;
import com.biperf.core.value.ecard.ECardResponse;

public interface ECardsRepository
{
  public static final String BEAN_NAME = "eCardsRepository";

  ResponseEntity<ECardResponse[]> getECards( ECardRequest eCardRequest ) throws ServiceErrorException, Exception;

  List<ECardMigrateView> getMigrateECards( String ecardMigrateRequest ) throws ServiceErrorException;
}
