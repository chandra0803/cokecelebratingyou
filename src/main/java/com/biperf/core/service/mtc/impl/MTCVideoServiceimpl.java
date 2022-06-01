
package com.biperf.core.service.mtc.impl;

import java.util.Map;

import com.biperf.core.dao.mtc.MTCVideoDAO;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.service.mtc.MTCVideoService;

public class MTCVideoServiceimpl implements MTCVideoService
{

  private MTCVideoDAO mtcVideoDAO;

  @Override
  public MTCVideo save( MTCVideo mtcVideo )
  {
    return mtcVideoDAO.save( mtcVideo );
  }

  @Override
  public MTCVideo getMTCVideoByRequestId( String requestId )
  {

    return mtcVideoDAO.getMTCVideoByRequestId( requestId );
  }

  public MTCVideoDAO getMtcVideoDAO()
  {
    return mtcVideoDAO;
  }

  public void setMtcVideoDAO( MTCVideoDAO mtcVideoDAO )
  {
    this.mtcVideoDAO = mtcVideoDAO;
  }

  @Override
  public MTCVideo save( String requestId, Map<String, Object> details )
  {

    return mtcVideoDAO.save( requestId, details );
  }

  @Override
  public String getMTCVideoByRequestIdAndType( String requestId, String type )
  {

    return mtcVideoDAO.getMTCVideoByRequestIdAndType( requestId, type );
  }

}
