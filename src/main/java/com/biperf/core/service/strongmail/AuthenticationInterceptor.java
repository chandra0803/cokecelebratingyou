/*
 * -------------------------------------------------------------------- *
 *  STRONGMAIL SYSTEMS                                                  *
 *                                                                      *
 *  Copyright 2010 StrongMail Systems, Inc. - All rights reserved.      *
 *                                                                      *
 *  Visit http://www.strongmail.com for more information                *
 *                                                                      *
 *  You may incorporate this Source Code in your application only if    *
 *  you own a valid license to do so from StrongMail Systems, Inc.      *
 *  and the copyright notices are not removed from the source code.     *
 *                                                                      *
 *  Distributing our source code outside your organization              *
 *  is strictly prohibited                                              *
 *                                                                      *
 * -------------------------------------------------------------------- *
 */

package com.biperf.core.service.strongmail;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.util.WSSecurityUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.biperf.core.service.SAO;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;

public class AuthenticationInterceptor extends WSS4JOutInterceptor
{

  /**
   * Schema namespace
   */
  private String schemaNS;

  /**
   * Interceptor to set headers: organizationName and subOrganizationId
   */
  private PhaseInterceptor phaseInterceptor;

  /**
   * organizationName and subOrganizationId to login
   */
  private String organizationName;
  private String subOrganizationId;

  private boolean isSSO;

  public AuthenticationInterceptor( String schemaNS, Map<String, Object> properties, boolean isSSO )
  {
    super( properties );
    this.schemaNS = schemaNS;
    this.phaseInterceptor = new WSS4JOutInterceptorInternal();
    this.isSSO = isSSO;
  }

  public void setOrganizationName()
  {
    this.organizationName = getSystemVariableService().getPropertyByName( SystemVariableService.STRONGMAIL_ORG_NAME ).getStringVal();
  }

  public void setSubOrganizationId()
  {
    this.subOrganizationId = getSystemVariableService().getPropertyByName( SystemVariableService.STRONGMAIL_SUB_ORG_ID ).getStringVal();
  }

  public void setUsername()
  {
    getProperties().put( WSHandlerConstants.USER, getSystemVariableService().getPropertyByName( SystemVariableService.STRONGMAIL_USER_NAME ).getStringVal() );
  }

  public void setPassword()
  {
    getProperties().put( WSHandlerConstants.PW_CALLBACK_REF, new ClientPasswordCallbackHandler() );
  }

  public void setSSO( boolean SSO )
  {
    isSSO = SSO;
  }

  public void handleMessage( SoapMessage message ) throws Fault
  {
    setOrganizationName();
    setSubOrganizationId();
    setUsername();
    setPassword();
    super.handleMessage( message );
    message.getInterceptorChain().add( phaseInterceptor );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  /**
   * Set SOAP header before sending out message. Header includes username, password,
   * organizationName and optionally subOrganizationId. This message set organizationName and
   * subOrganizationId, and parent class will set username/password.
   */
  /**
   * Set SOAP header before sending out message. Header includes username, password,
   * organizationName and optionally subOrganizationId. This message set organizationName and
   * subOrganizationId, and parent class will set username/password.
   */
  class WSS4JOutInterceptorInternal implements PhaseInterceptor<SoapMessage>
  {
    private static final String ORGANIZATION_TOKEN_ELEMENT_NAME = "OrganizationToken";
    private static final String ORGANIZATION_ELEMENT_NAME = "organizationName";
    private static final String SUB_ORGANIZATION_ID_ELEMENT_NAME = "subOrganizationId";
    private static final String ID_ELEMENT_NAME = "id";
    private static final String SSO_ELEMENT_NAME = "IsSSO";

    public WSS4JOutInterceptorInternal()
    {
      super();
    }

    public void handleMessage( SoapMessage message )
    {
      try
      {
        Element securityHeader = getSecurityHeader( message );

        // Create <OrganizationToken> and <organizationName> element,
        // and set <organizationName> as child of <OrganizationToken>
        Element e = securityHeader.getOwnerDocument().createElementNS( schemaNS, ORGANIZATION_TOKEN_ELEMENT_NAME );
        Element e2 = securityHeader.getOwnerDocument().createElementNS( schemaNS, ORGANIZATION_ELEMENT_NAME );
        Text t = securityHeader.getOwnerDocument().createTextNode( organizationName );
        e2.appendChild( t );
        e.appendChild( e2 );

        // Create <subOrganizationId> if necessary. It is also child of <OrganizationToken>
        if ( subOrganizationId != null )
        {
          Element e3 = securityHeader.getOwnerDocument().createElementNS( schemaNS, SUB_ORGANIZATION_ID_ELEMENT_NAME );
          Element e4 = securityHeader.getOwnerDocument().createElementNS( schemaNS, ID_ELEMENT_NAME );

          Text t2 = securityHeader.getOwnerDocument().createTextNode( subOrganizationId );
          e4.appendChild( t2 );
          e3.appendChild( e4 );
          e.appendChild( e3 );
        }
        securityHeader.appendChild( e );
        if ( isSSO )
        {
          Element ssoElement = securityHeader.getOwnerDocument().createElementNS( schemaNS, SSO_ELEMENT_NAME );
          securityHeader.appendChild( ssoElement );
        }

        // Print raw soap message
        // SOAPMessage soapMessage = message.getContent( SOAPMessage.class );
        // soapMessage.writeTo( System.out );
      }
      catch( Throwable e )
      {
        e.printStackTrace();
      }
    }

    private Element getSecurityHeader( SoapMessage message ) throws SOAPException, WSSecurityException
    {
      SOAPMessage doc = message.getContent( SOAPMessage.class );
      String actor = (String)getOption( WSHandlerConstants.ACTOR );
      return WSSecurityUtil.getSecurityHeader( doc.getSOAPPart(), actor );
    }

    public Set<String> getAfter()
    {
      return Collections.emptySet();
    }

    public Set<String> getBefore()
    {
      return Collections.emptySet();
    }

    public String getId()
    {
      return WSS4JOutInterceptorInternal.class.getName();
    }

    public String getPhase()
    {
      return Phase.POST_PROTOCOL;
    }

    public void handleFault( SoapMessage message )
    {
      // nothing
    }

    @Override
    public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors()
    {
      return null;
    }
  }
}
