
package com.biperf.core.service.ecard;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ecard.ECardRequest;
import com.biperf.core.value.ecard.ECardResponse;
import com.biperf.core.value.ecard.OwnCardImageData;
import com.biperf.core.value.ecard.UploadEcardRequest;

public interface EcardService extends SAO
{

  public static final String BEAN_NAME = "eCardService";

  ResponseEntity<ECardResponse[]> getECards( ECardRequest ecardRequest ) throws ServiceErrorException, Exception;

  void saveECard( ECard ecard );

  String uploadEcard( UploadEcardRequest request ) throws ServiceErrorException, Exception;

  List<OwnCardImageData> getNotMigratedRecogOwnCardData();

  List<OwnCardImageData> getNotMigratedNomOwnCardData();

  void updateOwnCard( Long cardId, String cardUrl );

  void updateOwnCardForNomination( long l, String cardUrl );

}
