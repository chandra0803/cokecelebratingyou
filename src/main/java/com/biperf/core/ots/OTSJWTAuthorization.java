
package com.biperf.core.ots;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

/**
 * OTSJWTAuthorization.
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
 * <td>Saravanan Sivanandam</td>
 * <td>Nov 20, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

@Component
public class OTSJWTAuthorization
{
  private static final Log log = LogFactory.getLog( OTSJWTAuthorization.class );

  public String getOTSJWTAuthorization( String otsClientID, String otsClientSecret )
  {
    return getAuthorization( otsClientID, otsClientSecret );
  }

  private String getAuthorization( String otsClientID, String otsClientSecret )
  {
    String token = null;

    try
    {
      Algorithm algorithm = Algorithm.HMAC256( otsClientSecret );
      token = JWT.create().withIssuer( otsClientID ).withExpiresAt( getExpiresTime() ).withNotBefore( getExpiresNotBefore() ).sign( algorithm );

    }
    catch( UnsupportedEncodingException exception )
    {
      log.error( "Un Supported Encoding Exception in OTSJWTAuthorization " + exception.getMessage() );
    }
    catch( JWTCreationException exception )
    {
      log.error( "JWTCreation Exception in OTSJWTAuthorization " + exception.getMessage() );
    }

    return token;
  }

  private Date getExpiresTime()
  {
    return new Date( new Date().getTime() + 60 * 10000 );

  }

  private Date getExpiresNotBefore()
  {
    return new Date( new Date().getTime() + 60 * -10000 );
  }
}
