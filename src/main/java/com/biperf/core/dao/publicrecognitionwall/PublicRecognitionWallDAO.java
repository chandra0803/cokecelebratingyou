
package com.biperf.core.dao.publicrecognitionwall;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallBean;

/**
 * 
 * 
 * @author gandreds
 * @since Apr 08, 2016
 */

public interface PublicRecognitionWallDAO extends DAO
{

  /** name of bean in factory * */
  public static final String BEAN_NAME = "publicRecognitionWallDAO";

  /**
   * Return a list of PublicRecognitionWallBean for a client.
   *
   * @param PageCount
   * @return List<PublicRecognitionWallBean>
   */
  List<PublicRecognitionWallBean> getPublicRecognitionWallByPageCount( final int pageCount );
}
