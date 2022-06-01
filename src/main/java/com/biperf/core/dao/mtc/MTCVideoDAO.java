
package com.biperf.core.dao.mtc;

import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.mtc.MTCVideo;

public interface MTCVideoDAO extends DAO
{
  public static final String BEAN_NAME = "mtcVideoDAO";

  public MTCVideo save( MTCVideo mtcVideo );

  public MTCVideo getMTCVideoByRequestId( String requestId );

  public MTCVideo save( String requestId, Map<String, Object> details );

  public String getMTCVideoByRequestIdAndType( String requestId, String type );

}
