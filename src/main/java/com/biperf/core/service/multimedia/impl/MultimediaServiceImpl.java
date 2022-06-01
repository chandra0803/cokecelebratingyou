/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/multimedia/impl/MultimediaServiceImpl.java,v $
 */

package com.biperf.core.service.multimedia.impl;

import java.util.List;
import java.util.Set;

import com.biperf.core.dao.multimedia.MultimediaDAO;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.multimedia.EcardLocale;
import com.biperf.core.domain.multimedia.VideoUploadDetail;
import com.biperf.core.service.multimedia.MultimediaService;

/**
 * MultimediaServiceImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>zahler</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MultimediaServiceImpl implements MultimediaService
{
  private MultimediaDAO multimediaDAO = null;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.multimedia.MultimediaService#getAllCardImages()
   * @return List
   */
  public List getAllCardImages()
  {
    return multimediaDAO.getAllActiveECards();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.multimedia.MultimediaService#getCardById(java.lang.Long)
   * @param cardId
   * @return Card
   */
  public Card getCardById( Long cardId )
  {
    return multimediaDAO.getCardById( cardId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.multimedia.MultimediaService#saveCard(com.biperf.core.domain.multimedia.Card)
   * @param card
   * @return Card
   */
  public Card saveCard( Card card )
  {
    return multimediaDAO.saveCard( card );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.multimedia.MultimediaService#getAllMobileCardImages()
   * @return List
   */
  public List getAllMobileCardImages()
  {
    return multimediaDAO.getAllActiveMobileECards();
  }

  @Override
  public VideoUploadDetail saveVideoUploadDetail( VideoUploadDetail videoUploadDetail )
  {
    return multimediaDAO.saveVideoUploadDetail( videoUploadDetail );
  }

  @Override
  public VideoUploadDetail getVideoUploadDetailById( Long videoUploadDetailId )
  {
    return multimediaDAO.getVideoUploadDetailById( videoUploadDetailId );
  }

  @Override
  public String getThumbnailByVideo( String videoUrl )
  {
    return multimediaDAO.getThumbnailByVideo( videoUrl );
  }

  /**
   * @param multimediaDAO
   */
  public void setMultimediaDAO( MultimediaDAO multimediaDAO )
  {
    this.multimediaDAO = multimediaDAO;
  }

  @Override
  public Boolean isEcardExistForLocale( Long cardId, String recipientLocale )
  {
    return multimediaDAO.isEcardExistForLocale( cardId, recipientLocale );
  }

  @Override
  public List<Long> getAllECardIds()
  {
    return multimediaDAO.getAllECardIds();
  }

  @Override
  public ECard getECardById( Long cardId )
  {
    return multimediaDAO.getECardById( cardId );
  }

  @Override
  public void saveLocale( Set<EcardLocale> localeSet )
  {
    for ( EcardLocale locale : localeSet )
    {
      multimediaDAO.saveEcardLocale( locale );
    }

  }

}
