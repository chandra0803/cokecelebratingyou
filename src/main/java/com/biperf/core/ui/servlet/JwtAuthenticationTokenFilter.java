
package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.biperf.core.domain.company.Company;
import com.biperf.core.exception.JWTAuthExceptionView;
import com.biperf.core.jwt.JWTException;
import com.biperf.core.jwt.JWTTokenHandler;
import com.biperf.core.jwt.JWTTokenHandlerFactory;
import com.biperf.core.service.company.CompanyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.CommonConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationTokenFilter implements Filter
{
  private static final Log log = LogFactory.getLog( JwtAuthenticationTokenFilter.class );

  public void init( FilterConfig filterConfig ) throws ServletException
  {
  }

  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
  {
    HttpServletRequest httpServletRequest = (HttpServletRequest)request;
    HttpServletResponse httpServletResposne = (HttpServletResponse)response;

    try
    {
      String authToken = getToken( httpServletRequest );
      JWTTokenHandlerFactory jwtTokenHandlerFactory = (JWTTokenHandlerFactory)BeanLocator.getBean( "JWTTokenHandlerFactory" );
      JWTTokenHandler jwtTokenHandler = jwtTokenHandlerFactory.getJWTTokenHandler();

      String reqCompanId = getValueFromHeader( httpServletRequest, CommonConstants.COMPANY_ID );

      if ( Objects.isNull( authToken ) && Objects.isNull( reqCompanId ) )
      {
        writeAsJsonToResponse( httpServletResposne, "OOPs !. The request requires valid JWT token and company id.", HttpStatus.UNAUTHORIZED.value(), null );
      }
      else if ( Objects.isNull( authToken ) )
      {
        writeAsJsonToResponse( httpServletResposne, "OOPs !. The request requires valid JWT token.", HttpStatus.UNAUTHORIZED.value(), null );
      }
      else if ( Objects.isNull( reqCompanId ) )
      {
        writeAsJsonToResponse( httpServletResposne, "OOPs !. The request requires valid company id.", HttpStatus.UNAUTHORIZED.value(), null );
      }
      else
      {

        boolean isValidToken = jwtTokenHandler.validate( authToken );

        if ( isValidToken && validateCompanyId( UUID.fromString( reqCompanId ) ) )
        {
          chain.doFilter( request, response );
        }
        else
        {
          writeAsJsonToResponse( httpServletResposne, "OOPs !. Authentication failed !!, either invalid JWT token or company id.", HttpStatus.UNAUTHORIZED.value(), null );
        }
      }
    }
    catch( JWTException jwtException )
    {
      if ( CommonConstants.ERROR_CODE_JWT_TOKEN_EXPIRED.equalsIgnoreCase( jwtException.getErrorCode() ) )
      {
        writeAsJsonToResponse( httpServletResposne, jwtException.getMessage(), HttpStatus.UNAUTHORIZED.value(), jwtException );
      }
      else
      {
        writeAsJsonToResponse( httpServletResposne, jwtException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), jwtException );
      }

    }
    catch( Exception exception )
    {
      if ( CommonConstants.INVALID_TOKEN.contains( exception.getMessage() ) )
      {
        writeAsJsonToResponse( httpServletResposne, CommonConstants.INVALID_TOKEN, HttpStatus.UNAUTHORIZED.value(), exception );
      }
      else
      {
        writeAsJsonToResponse( httpServletResposne, "OOPs !. An internal server error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value(), exception );
      }

    }

  }

  public void destroy()
  {
  }

  /***
   * Get token from Request header
   * 
   * @param request
   * @return
   */
  public String getToken( HttpServletRequest request )
  {
    /**
     *  Getting the token from Authentication header
     *  e.g Bearer your_token
     */
    String authHeader = getAuthHeaderFromHeader( request );
    if ( authHeader != null && authHeader.startsWith( "Bearer " ) && authHeader.length() > 7 )
    {
      return authHeader.substring( 7 );
    }

    return null;
  }

  /**
   * Get Authorization header value
   * 
   * @param request
   * @return
   */
  public String getAuthHeaderFromHeader( HttpServletRequest request )
  {
    return request.getHeader( CommonConstants.JWT_AUTH_HEADER );
  }

  /**
   * Convert Object to JSON String
   * 
   * @param bean
   * @return
   */
  private String toJson( Object bean )
  {
    ObjectMapper mapper = new ObjectMapper();
    Writer writer = new StringWriter();

    try
    {
      mapper.writeValue( writer, bean );
    }
    catch( Throwable t )
    {
      log.error( "Exception in Converting Object to JSON" + t.getMessage(), t );
    }

    return writer.toString();
  }

  /**
   * Write the response to request output stream
   * 
   * @param response
   * @param msg
   * @param status
   * @param ex
   * @throws IOException
   */
  private void writeAsJsonToResponse( HttpServletResponse response, String msg, int status, Exception ex ) throws IOException
  {
    JWTAuthExceptionView jwtAuthExceptionView = getJWTAuthExceptionViewObject( msg, status, ex );

    response.setStatus( status );
    response.setContentType( "application/json" );

    PrintWriter out = response.getWriter();
    out.print( toJson( jwtAuthExceptionView ) );

    out.flush();
    out.close();
  }

  /**
   * Construct the JWTAuthExceptionView object
   * 
   * @param msg
   * @param status
   * @param ex
   * @return
   */
  private JWTAuthExceptionView getJWTAuthExceptionViewObject( String msg, int status, Exception ex )
  {
    JWTAuthExceptionView exceptionView = null;
    if ( ex != null )
    {
      exceptionView = new JWTAuthExceptionView( status, msg, ExceptionUtils.getStackTrace( ex ) );
    }
    else
    {
      exceptionView = new JWTAuthExceptionView( status, msg, null );
    }
    return exceptionView;

  }

  private String getValueFromHeader( HttpServletRequest request, String headerName )
  {
    String headerValue = request.getHeader( headerName );

    if ( Objects.nonNull( headerValue ) )
    {
      return headerValue;

    }
    return null;
  }

  private boolean validateCompanyId( UUID companyId ) throws Exception
  {
    if ( companyId.equals( getCompanyId() ) )
    {
      return true;
    }
    else
    {
      throw new Exception( CommonConstants.INVALID_TOKEN );
    }
  }

  protected UUID getCompanyId()
  {
    UUID companyUUID = null;

    Company company = getCompanyService().getCompanyDetail();

    if ( Objects.nonNull( company.getCompanyId() ) )
    {
      companyUUID = company.getCompanyId();
    }
    return companyUUID;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private CompanyService getCompanyService()
  {
    return (CompanyService)BeanLocator.getBean( CompanyService.BEAN_NAME );
  }
}