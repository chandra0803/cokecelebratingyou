
package com.biperf.core.ui.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.apache.taglibs.standard.tag.common.core.OutSupport;

import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.cms.CMSDebugUtil;
import com.objectpartners.cms.domain.Asset;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

@SuppressWarnings( "serial" )
public class DebugContentTextTag extends BodyTagSupport
{
  // required attribute
  private String key;

  // required attribute
  private String code;

  // optional attribute
  private String appCode;

  // optional attribute
  private String onErrorText;

  // optional attribute
  private boolean escapeHtml;

  // optional attribute
  private boolean escapeJavascript;

  public DebugContentTextTag()
  {
    super();
    setDefaults();
  }

  private void setDefaults()
  {
    this.key = null;
    this.code = null;
    this.appCode = null;
    this.onErrorText = null;
  }

  @Override
  public void release()
  {
    super.release();
    setDefaults();
  }

  @Override
  public int doStartTag() throws JspException
  {
    ContentReader reader = ContentReaderManager.getContentReader();
    String data = "";
    Translations translations = null;
    Content content = null;
    boolean escapeXml = true;
    if ( reader != null )
    {
      if ( appCode != null )
      {
        content = CmsUtil.getContentFromReaderObject( reader.getContent( code, appCode ) );
        translations = (Translations)content.getContentDataMapList().get( key );
        if ( translations != null )
        {
          if ( !StringUtils.isEmpty( translations.getValue() ) )
          {
            data = translations.getValue();
          }
          else
          {
            data = translations.getGoogleValue();
          }
        }
        else
        {
          data = null;
        }
      }
      else
      {
        content = CmsUtil.getContentFromReaderObject( reader.getContent( code ) );
        if ( content != null )
        {
          translations = (Translations)content.getContentDataMapList().get( key );
          if ( translations != null )
          {
            if ( !StringUtils.isEmpty( translations.getValue() ) )
            {
              data = translations.getValue();
            }
            else
            {
              data = translations.getGoogleValue();
            }
          }
          else
          {
            data = null;
          }
        }
        else
        {
          data = CmsResourceBundle.getCmsBundle().getString( code, key );
        }
      }
      if ( content != null && content.getDataType( key ) != null && content.getDataType( key ).isHtmlText() )
      {
        escapeXml = false;
      }
    }
    else
    {
      // null reader
      data = onErrorText;
    }
    boolean isValidDataElement = true;
    if ( data == null )
    {
      isValidDataElement = false;
      data = "???" + code + "." + key + "???";
    }
    if ( data == null || content == null )
    {
      isValidDataElement = false;
    }
    try
    {
      if ( escapeHtml )
      {
        // if escape XML is set to true, don't do it twice
        data = CmsUtil.escapeHtml( data, !escapeXml );
      }
      if ( escapeJavascript )
      {
        data = CmsUtil.escapeJavascript( data );
      }
      OutSupport.out( pageContext, escapeXml, data );
    }
    catch( IOException ioe )
    {
      throw new JspException( ioe.getMessage() );
    }
    // check - only peform this if all are true:
    // - the environment is NOT production
    // - the system variable for debug/asset locator is true
    // - he user is an admin (has admin role)
    if ( CMSDebugUtil.isCMSDebugEnabled() && isValidDataElement )
    {
      ContentElement element = new ContentElement();
      element.setContentKeyId( content.getContentKey().getId() );
      element.setKey( code + "." + key );
      element.setValue( data );
      element.addPage( pageContext.getPage() );
      Asset asset = reader.getAsset( code );
      element.setAssetId( asset.getId() );
      processElement( element );
    }
    // end check
    return SKIP_BODY;
  }

  private final void processElement( ContentElement element )
  {
    HttpSession session = pageContext.getSession();
    CMSContentLocator contentLocator = (CMSContentLocator)session.getAttribute( CMSContentLocator.CMS_DEBUG_LOCATOR );
    if ( null == contentLocator )
    {
      HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
      String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "-cm";
      contentLocator = new CMSContentLocator( session, url );
    }
    contentLocator.addContentElement( element );
    session.setAttribute( CMSContentLocator.CMS_DEBUG_LOCATOR, contentLocator );
  }

  public final void setKey( final String key ) throws JspException
  {
    this.key = (String)ExpressionEvaluatorManager.evaluate( "key", key, java.lang.String.class, this, pageContext );
  }

  public final void setCode( final String code ) throws JspException
  {
    this.code = (String)ExpressionEvaluatorManager.evaluate( "code", code, java.lang.String.class, this, pageContext );
  }

  public final void setAppCode( final String appCode ) throws JspException
  {
    this.appCode = (String)ExpressionEvaluatorManager.evaluate( "appCode", appCode, java.lang.String.class, this, pageContext );
  }

  public final void setOnError( final String text ) throws JspException
  {
    this.onErrorText = (String)ExpressionEvaluatorManager.evaluate( "onError", text, java.lang.String.class, this, pageContext );
  }

  public final void setEscapeHtml( final String text ) throws JspException
  {
    this.escapeHtml = (Boolean)ExpressionEvaluatorManager.evaluate( "escapeHtml", text, java.lang.Boolean.class, this, pageContext );
  }

  public void setEscapeJavascript( final String text ) throws JspException
  {
    this.escapeJavascript = (Boolean)ExpressionEvaluatorManager.evaluate( "escapeJavascript", text, java.lang.Boolean.class, this, pageContext );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }
}
