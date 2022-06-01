
package com.biperf.core.ui.servlet;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.jwt.JWTTokenHandler;
import com.biperf.core.jwt.JWTTokenHandlerFactory;
import com.biperf.core.utils.BeanLocator;

public class AppSecurityFilter implements Filter
{
  private static final Log logger = LogFactory.getLog( AppSecurityFilter.class );
  private static final String htmlBodyEnd = "</body>";

  @Override
  public void destroy()
  {
  }

  @Override
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain ) throws IOException, ServletException
  {
    CharResponseWrapper responseWrapper = new CharResponseWrapper( (HttpServletResponse)response );
    filterChain.doFilter( request, responseWrapper );
    insertRandomBytesToResponse( request, response, responseWrapper );
  }

  /**
   * 
   * This method inserts a Random UUID of variable lengths in the HTTP
   * response to counter BREACH attack.
   * 
   * 
   * @param response
   * @param wrapper
   * 
   */
  private void insertRandomBytesToResponse( ServletRequest request, ServletResponse response, CharResponseWrapper wrapper )
  {
    CharArrayWriter caw;
    PrintWriter out = null;
    StringBuilder sb;

    HttpServletResponse httpResponse = (HttpServletResponse)response;

    try
    {
      out = response.getWriter();
      boolean exclusion = exclusionURI( request );

      if ( wrapper.toString().contains( htmlBodyEnd ) && !exclusion )
      {
        StringBuilder htmlOutput = new StringBuilder( wrapper.toString() );

        JWTTokenHandlerFactory jwtTokenHandlerFactory = (JWTTokenHandlerFactory)BeanLocator.getBean( "JWTTokenHandlerFactory" );
        JWTTokenHandler secureJWTTokenHandler = jwtTokenHandlerFactory.getSecureJWTTokenHandler();

        String jwtToken = secureJWTTokenHandler.generateToken();

        String inputHiddenRandomWithBodyEnd = "<input type=\"hidden\" value=\"" + generateRandonUUID() + "\"/>\n";
        String secureHeaderJWTToken = "<script>window.jwt = { VFT :\'" + jwtToken + "\'}</script>";

        sb = new StringBuilder();
        sb.append( secureHeaderJWTToken );
        sb.append( inputHiddenRandomWithBodyEnd );

        addJwtCookie( httpResponse, jwtToken );

        htmlOutput.replace( htmlOutput.indexOf( htmlBodyEnd ) - 1, htmlOutput.indexOf( htmlBodyEnd ), sb.toString() );

        caw = new CharArrayWriter();
        caw.write( htmlOutput.toString() );
        out.write( caw.toString() );
      }
      else
      {
        out.write( wrapper.toString() );
      }
    }
    catch( Exception ex )
    {

    }
    finally
    {
      if ( out != null )
      {
        out.close();
      }
    }
  }

  private void addJwtCookie( HttpServletResponse response, String token )
  {
    try
    {
      response.setHeader( "Set-Cookie", "VFT=" + token + ";Path=/;SameSite=Strict" );
    }
    catch( Exception ex )
    {
      logger.error( "While adding Jwt cookie : ", ex );
    }
  }

  private boolean exclusionURI( ServletRequest request )
  {
    return ( (HttpServletRequest)request ).getRequestURI().contains( "/admin/launchProcess.do" );
  }

  /**
   * 
   * This method generates the UUID of variable length to be added to the HTML
   * response. it generates the randomNumber times random UUID and append all
   * those to be sent as a part of response in hidden input.
   * 
   * @return String
   * 
   */
  private String generateRandonUUID()
  {
    Random generator = new Random();
    int i = generator.nextInt( 10 ) + 1;
    String randomBytes = "";

    while ( i > 0 )
    {
      randomBytes = UUID.randomUUID().toString() + randomBytes;
      i--;
    }
    return randomBytes;
  }

  @Override
  public void init( FilterConfig config ) throws ServletException
  {
  }

  private class CharResponseWrapper extends HttpServletResponseWrapper
  {
    private CharArrayWriter output;

    public String toString()
    {
      return output.toString();
    }

    public CharResponseWrapper( HttpServletResponse response )
    {
      super( response );
      output = new CharArrayWriter();
    }

    public PrintWriter getWriter()
    {
      return new PrintWriter( output );
    }
  }
}
