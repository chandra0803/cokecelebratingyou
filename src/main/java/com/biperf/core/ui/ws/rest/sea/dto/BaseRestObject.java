package com.biperf.core.ui.ws.rest.sea.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement  
public class BaseRestObject
{
  private int code = 1;
  // this value is a MD5 hash of a common parameter sent along with the common agreed upon salt.  The
  // recipient can then hash the value and compare them to determine if the security check has passed
  private String securityToken ;
  private String url;
  private String errorMessage;
  private String stack;

  public int getCode()
  {
    return code;
  }

  public void setCode( int code )
  {
    this.code = code;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getErrorMessage()
  {
    return errorMessage;
  }

  public void setErrorMessage( String errorMessage )
  {
    this.errorMessage = errorMessage;
  }

  public String getStack()
  {
    return stack;
  }

  public void setStack( String stack )
  {
    this.stack = stack ;
  }
  
  public String getSecurityToken()
  {
    return securityToken;
  }

  public void setSecurityToken( String securityToken )
  {
    this.securityToken = securityToken;
  }

  public void buildStack( Throwable t )
  {
    StringBuilder sb = new StringBuilder() ;
    StackTraceElement[] stackElements = t.getStackTrace() ;
    for( StackTraceElement element: stackElements )
    {
      sb.append( element.toString() + System.lineSeparator() ) ;
    }
    this.stack = sb.toString() ;
  }

  @Override
  public String toString()
  {
    return "BaseRestObject [code=" + code + ", url=" + url + ", errorMessage=" + errorMessage + ", stack=" + stack + "]";
  }
}
