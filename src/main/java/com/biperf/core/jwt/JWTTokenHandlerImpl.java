
package com.biperf.core.jwt;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.biperf.core.utils.CommonConstants;

public class JWTTokenHandlerImpl implements JWTTokenHandler
{

  private static Log log = LogFactory.getLog( JWTTokenHandlerImpl.class );
  private JWTConfig jwtConfig;

  public JWTTokenHandlerImpl( JWTConfig jwtConfig )
  {
    this.jwtConfig = jwtConfig;
  }

  /**
   * Generate the JWT token
   * {@inheritDoc}
   */
  @Override
  public String generateToken() throws JWTException
  {
    try
    {
      Algorithm algorithm = Algorithm.HMAC256( jwtConfig.secret() );
      return JWT.create().withIssuer( jwtConfig.issuer() ).withExpiresAt( getExpiresTime() ).withNotBefore( getExpiresNotBefore() ).sign( algorithm );
    }
    catch( UnsupportedEncodingException exception )
    {
      log.error( "Un Supported Encoding Exception in JWTTokenHandlerImpl " + exception.getMessage(), exception );
      throw new JWTException( "An internal server error has occurred" );
    }
    catch( JWTCreationException exception )
    {
      log.error( "JWTCreation Exception in JWTTokenHandlerImpl " + exception.getMessage(), exception );
      throw new JWTException( "An internal server error has occurred" );
    }

  }

  /**
   * Validate the JWT token
   * {@inheritDoc}
   */
  @Override
  public boolean validate( String token ) throws JWTException
  {
    try
    {
      Algorithm algorithm = Algorithm.HMAC256( jwtConfig.secret() );
      JWTVerifier verifier = JWT.require( algorithm ).withIssuer( jwtConfig.issuer() ).build();
      DecodedJWT jwt = verifier.verify( token );
      return true;
    }
    catch( UnsupportedEncodingException exception )
    {
      log.error( "Un Supported Encoding Exception in JWTTokenHandlerImpl " + exception.getMessage(), exception );
    }
    catch( TokenExpiredException exception )
    {
      log.error( "TokenExpiredException in JWTTokenHandlerImpl " + exception.getMessage(), exception );
      throw new JWTException( CommonConstants.ERROR_CODE_JWT_TOKEN_EXPIRED, "Authorization token expired" );
    }
    catch( JWTVerificationException exception )
    {
      log.error( "JWTVerificationException in JWTTokenHandlerImpl " + exception.getMessage(), exception );
      // Invalid signature/claims
    }

    return false;
  }

  /**
   * Return expire time
   * 
   * @return
   */
  private Date getExpiresTime()
  {
    Date expiresDate = Date.from( LocalDateTime.now().plusHours( 1 ).atZone( ZoneId.systemDefault() ).toInstant() );

    return expiresDate;

  }

  /**
   * Expire Not before time
   * @return
   */
  private Date getExpiresNotBefore()
  {
    Date notBeforeDate = Date.from( LocalDateTime.now().atZone( ZoneId.systemDefault() ).toInstant() );

    return notBeforeDate;
  }

}
