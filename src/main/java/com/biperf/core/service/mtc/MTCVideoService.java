
package com.biperf.core.service.mtc;

import java.util.Map;

import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.service.SAO;

public interface MTCVideoService extends SAO
{
  public static final String BEAN_NAME = "mtcVideoService";

  public MTCVideo save( MTCVideo mtcVideo );

  public MTCVideo save( String requestId, Map<String, Object> details );

  public MTCVideo getMTCVideoByRequestId( String requestId );

  public String getMTCVideoByRequestIdAndType( String requestId, String type );

}
