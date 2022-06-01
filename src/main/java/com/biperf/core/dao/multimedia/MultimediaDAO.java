/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/multimedia/MultimediaDAO.java,v $
 */

package com.biperf.core.dao.multimedia;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.multimedia.EcardLocale;
import com.biperf.core.domain.multimedia.VideoUploadDetail;

/**
 * ImageDAO.
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
 */
public interface MultimediaDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "multimediaDAO";

  /**
   * @return List of all active Card Images
   */
  public List getAllActiveECards();

  /**
   * @param cardId
   * @return List
   */
  public Card getCardById( Long cardId );

  /**
   * @param card
   * @return Image
   */
  public Card saveCard( Card card );

  /**
   * @return List of all active Mobile Card Images
   */
  public List getAllActiveMobileECards();

  /**
   * Save or update details.
   */
  public VideoUploadDetail saveVideoUploadDetail( VideoUploadDetail videoUploadDetail );

  /**
   * @return VideoUploadDetail with the given ID, or null if one does not exist
   */
  public VideoUploadDetail getVideoUploadDetailById( Long videoUploadDetailId );

  /**
   * @return Thumbnail image URL for the upload with the given video URL. Null if no matching detail.
   */
  public String getThumbnailByVideo( String videoUrl );

  public Boolean isEcardExistForLocale( Long cardId, String recipientLocale );

  public List<Long> getAllECardIds();

  public ECard getECardById( Long cardId );

  public void saveEcardLocale( EcardLocale locale );

}
