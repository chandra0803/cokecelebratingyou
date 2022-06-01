
package com.biperf.core.dao.ecard;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.value.ecard.OwnCardImageData;

public interface ECardDAO extends DAO
{

  public static final String BEAN_NAME = "eCardDAO";

  public void save( ECard card );

  public Long getCardById( Long long1 );

  public List<OwnCardImageData> getNotMigratedRecogOwnCardData();

  public void updateOwnCard( Long claimId, String cardUrl );

  public List<OwnCardImageData> getNotMigratedNomOwnCardData();

  public void updateOwnCardForNomination( long claimId, String cardUrl );

}
