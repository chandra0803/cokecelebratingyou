
package com.biperf.core.ui.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.struts.tiles.ComponentContext;
import org.json.JSONObject;

import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;

public class BunchBallWidgetController extends BaseController
{
  private static final Log logger = LogFactory.getLog( BunchBallWidgetController.class );

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    String token = (String)request.getSession().getAttribute( "nitro_token" );
    logger.debug( "Token from session-" + token );

    if ( StringUtils.isBlank( token ) )
    {
      logger.debug( "Token in not found in session. Getting new token." );
      token = getToken( request );
    }
    request.setAttribute( "tokenName", token );
    request.setAttribute( "userName", UserManager.getUserName() );
    request.setAttribute( "apiUrl", getSystemVariableService().getPropertyByName( SystemVariableService.COKE_BUNCHBALL_API_URL ).getStringVal() );
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    request.setAttribute( "siteUrlPrefix", siteUrlPrefix );
    request.setAttribute( "designTheme", getDefaultDesignTheme() );

  }

  public String getToken( HttpServletRequest request )
  {
    String token = "";
    try
    {
      logger.debug( "In getToken method" );

      List<NameValuePair> form = new ArrayList<>();
      form.add( new BasicNameValuePair( "grant_type", "client_credentials" ) );
      form.add( new BasicNameValuePair( "client_id", getSystemVariableService().getPropertyByName( SystemVariableService.COKE_BUNCHBALL_API_CLIENT_ID ).getStringVal() ) );
      form.add( new BasicNameValuePair( "client_secret", getSystemVariableService().getPropertyByName( SystemVariableService.COKE_BUNCHBALL_API_SECRET ).getStringVal() ) );
      form.add( new BasicNameValuePair( "user_id", UserManager.getUserName() ) );

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity( form, Consts.UTF_8 );

      RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout( 5000 ).setConnectTimeout( 5000 ).setConnectionRequestTimeout( 5000 ).setStaleConnectionCheckEnabled( true ).build();

      CloseableHttpClient httpclient = HttpClients.createDefault();

      HttpPost httpPost = new HttpPost( getSystemVariableService().getPropertyByName( SystemVariableService.COKE_BUNCHBALL_API_URL ).getStringVal()+"/oauth/token/" );
      RequestConfig requestConfig = RequestConfig.copy( defaultRequestConfig ).setProxy( new HttpHost( Environment.getProxy(), Environment.getProxyPort() ) ).build();
      httpPost.setConfig( requestConfig );
      httpPost.setEntity( entity );
      logger.debug( "Executing request " + httpPost.getRequestLine() );

      // Create a custom response handler
      ResponseHandler<String> responseHandler = response ->
      {
        int status = response.getStatusLine().getStatusCode();
        if ( status >= 200 && status < 300 )
        {
          HttpEntity responseEntity = response.getEntity();
          return responseEntity != null ? EntityUtils.toString( responseEntity ) : null;
        }
        else
        {
          throw new ClientProtocolException( "Unexpected response status: " + status );
        }
      };
      String responseBody = httpclient.execute( httpPost, responseHandler );
      logger.debug( "Response Body-" + responseBody );
      JSONObject jsonObject = new JSONObject( responseBody );
      token = jsonObject.getString( "access_token" );
      logger.debug( "Token from JSON response-" + token );
      request.setAttribute( "nitro_token", token );

    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    return token;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
  
  private String getDefaultDesignTheme()
  {
    return getDesignThemeService().getDefaultDesignTheme();
  }

  private DesignThemeService getDesignThemeService()
  {
    return (DesignThemeService)getService( DesignThemeService.BEAN_NAME );
  }

}
