
package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import botdetect.CaptchaHttpCommand;
import botdetect.CodeStyle;
import botdetect.ImageStyle;
import botdetect.support.CaptchaRandomization;
import botdetect.web.Captcha;

public class CaptchaFilter implements Filter
{

  public void init( FilterConfig filterConfig ) throws ServletException
  {
  }

  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
  {
    if ( CaptchaHttpCommand.getCaptchaCommand( (HttpServletRequest)request ) == CaptchaHttpCommand.GET_IMAGE )
    {
      Captcha captcha = Captcha.load( request );

      captcha.setCodeLength( CaptchaRandomization.getRandomCodeLength( 2, 4 ) );

      switch ( captcha.getCodeLength() )
      {
        case 2:
          captcha.setCodeStyle( CodeStyle.ALPHA );
          break;
        case 3:
          captcha.setCodeStyle( CodeStyle.ALPHANUMERIC );
          break;
        case 4:
          captcha.setCodeStyle( CodeStyle.NUMERIC );
          break;
        default:
          break;
      }

      // use an image style randomly selected from the given subset
      List<ImageStyle> imageStyles = new ArrayList<ImageStyle>();
      imageStyles.add( ImageStyle.BlackOverlap );
      imageStyles.add( ImageStyle.Bubbles );
      imageStyles.add( ImageStyle.Bullets );
      imageStyles.add( ImageStyle.Bullets2 );

      captcha.setImageStyle( CaptchaRandomization.getRandomImageStyle( imageStyles ) );

      request.setAttribute( captcha.getCaptchaId(), captcha );
    }

    chain.doFilter( request, response );
  }

  public void destroy()
  {
  }

}
