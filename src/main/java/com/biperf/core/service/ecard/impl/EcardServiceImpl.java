
package com.biperf.core.service.ecard.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.biperf.core.dao.ecard.ECardDAO;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ecard.ECardsRepositoryFactory;
import com.biperf.core.service.ecard.EcardService;
import com.biperf.core.service.imageservice.ImageServiceRepositoryFactory;
import com.biperf.core.value.ecard.ECardRequest;
import com.biperf.core.value.ecard.ECardResponse;
import com.biperf.core.value.ecard.OwnCardImageData;
import com.biperf.core.value.ecard.UploadEcardRequest;

@Service( "eCardService" )
public class EcardServiceImpl implements EcardService
{

  private static final Log logger = LogFactory.getLog( EcardServiceImpl.class );

  private static final String PERSONAL = "personal";

  @Autowired
  private ECardsRepositoryFactory eCardDtlsRepo;

  @Autowired
  private ImageServiceRepositoryFactory imageServiceRepo;

  @Autowired
  private ECardDAO eCardDAO;

  @Override
  public ResponseEntity<ECardResponse[]> getECards( ECardRequest ecardRequest ) throws ServiceErrorException, Exception
  {

    ResponseEntity<ECardResponse[]> ecardResponse = null;
    try
    {
      ecardResponse = eCardDtlsRepo.getRepo().getECards( ecardRequest );
    }
    catch( ServiceErrorException ex )
    {
      logger.error( "Exception while fetching the ecards : " + ex.getMessage() );
      throw new ServiceErrorException( ex.getMessage() );
    }
    return ecardResponse;
  }

  @Override
  public void saveECard( ECard ecard )
  {
    Long version = eCardDAO.getCardById( ecard.getId() );
    ecard.setVersion( version + 1 );
    eCardDAO.save( ecard );
  }

  @Override
  public String uploadEcard( UploadEcardRequest request ) throws ServiceErrorException, Exception
  {
    return imageServiceRepo.getRepo().uploadImage( request.getFile(), String.valueOf( request.getClaimId() ), PERSONAL );
  }

  /*
   * Below methods are used for own card migration. These methods only used for migration alone, so
   * kept in the thin layer instead of appropriate claims.
   */
  @Override
  public List<OwnCardImageData> getNotMigratedRecogOwnCardData()
  {
    return eCardDAO.getNotMigratedRecogOwnCardData();
  }

  @Override
  public void updateOwnCard( Long claimId, String cardUrl )
  {
    eCardDAO.updateOwnCard( claimId, cardUrl );

  }

  @Override
  public List<OwnCardImageData> getNotMigratedNomOwnCardData()
  {
    return eCardDAO.getNotMigratedNomOwnCardData();
  }

  @Override
  public void updateOwnCardForNomination( long claimId, String cardUrl )
  {
    eCardDAO.updateOwnCardForNomination( claimId, cardUrl );
  }

  /*
   * Above methods are used for own card migration. These methods only used for migration alone, so
   * kept in the thin layer instead of appropriate claims.
   */

}
