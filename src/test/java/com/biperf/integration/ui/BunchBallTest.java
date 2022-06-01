
package com.biperf.integration.ui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpHost;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;

public class BunchBallTest
{

  public static void main( String[] args ) throws ClientProtocolException, IOException
  {
    try
    {

      List<NameValuePair> form = new ArrayList<>();
      form.add( new BasicNameValuePair( "grant_type", "client_credentials" ) );
      form.add( new BasicNameValuePair( "client_id", "lhrQsJlEQdv2A7bb" ) );
      form.add( new BasicNameValuePair( "client_secret", "3Bf5Ob2bIvsUz60OjpwhVlC7olYdSy" ) );
      form.add( new BasicNameValuePair( "user_id", "O37047" ) );

      UrlEncodedFormEntity entity = new UrlEncodedFormEntity( form, Consts.UTF_8 );

      RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout( 5000 ).setConnectTimeout( 5000 ).setConnectionRequestTimeout( 5000 ).setStaleConnectionCheckEnabled( true ).build();

      Credentials credentials = new UsernamePasswordCredentials( "yelamanc", "meedEveloper3366" );
      AuthScope authScope = new AuthScope( "corpuser.corp.biworldwide.com", 8080 );
      CredentialsProvider credsProvider = new BasicCredentialsProvider();
      credsProvider.setCredentials( authScope, credentials );
      CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider( credsProvider ).build();

      HttpPost httpPost = new HttpPost( "https://api.sandbox.bunchball.com/oauth/token" );
      RequestConfig requestConfig = RequestConfig.copy( defaultRequestConfig ).setProxy( new HttpHost( "corpuser.corp.biworldwide.com", 8080 ) ).build();
      httpPost.setConfig( requestConfig );
      httpPost.setEntity( entity );
      System.out.println( "Executing request " + httpPost.getRequestLine() );

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
      System.out.println( "----------------------------------------" );
      System.out.println( responseBody );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }

  }
}