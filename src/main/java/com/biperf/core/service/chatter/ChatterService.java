/**
 * 
 */

package com.biperf.core.service.chatter;

import org.json.JSONObject;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

/**
 * ChatterService.
 * 
 * @author kandhi
 * @since Nov 21, 2013
 * @version 1.0
 */
public interface ChatterService extends SAO
{
  public static final String BEAN_NAME = "chatterService";

  public String getChatterAuthorizationCode( String state ) throws ServiceErrorException;

  public JSONObject getChatterAccessToken( String code, String state ) throws ServiceErrorException;

  public JSONObject postMessageToWall( String message, String accessToken, String instanceUrl ) throws ServiceErrorException;
}
