/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/taglib/G3ReduxMenuTag.java,v $
 */

package com.biperf.core.ui.taglib;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.maincontent.Menu;
import com.biperf.core.service.maincontent.MenuOption;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * G3ReduxMenuTag.
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
 * <td>Rachel Robinson</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class G3ReduxMenuTag extends TagSupport
{
  private static final Log log = LogFactory.getLog( G3ReduxMenuTag.class );

  /**
   * Overridden from
   * 
   * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
   * @return status code
   * @throws JspException
   */
  public int doStartTag() throws JspException
  {
    try
    {
      HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
      String selTabId = (String)request.getSession().getAttribute( "selectedTabId" );
      log.debug( "selTabId=" + selTabId );

      JspWriter jspWriter = pageContext.getOut();

      // check if menuList already available in request
      List menuList = (List)request.getAttribute( ViewAttributeNames.TOP_NAV_MENUS );
      if ( menuList != null )
      {
        Iterator menuListIterator = menuList.iterator();
        while ( menuListIterator.hasNext() )
        {
          Menu menu = (Menu)menuListIterator.next();
          writeMenu( request, jspWriter, menu, selTabId );
        }
      }
      else
      {
        log.error( "Menu list is null" );
      }
    }
    catch( IOException e )
    {
      log.error( e.getMessage(), e );
      throw new JspException( "doStartTag: Exception", e );
    }
    return SKIP_BODY;
  }

  private void writeMenu( HttpServletRequest request, JspWriter out, Menu menu, String selTabId ) throws JspException, IOException
  {
    String menuLabel = getCMAssetValue( menu.getAsset(), menu.getKey() );
    String tabId = menu.getAsset() + "." + menu.getKey();
    StringBuffer liInfo = new StringBuffer( " id=\"" + tabId + "\"" );
    if ( tabId.equals( selTabId ) )
    {
      liInfo.append( " class=\"select\"" );
    }

    out.println( "<li" + liInfo.toString() + ">" );
    out.print( "<a href=\"" );

    if ( menu.getUrl() != null )
    {
      if ( menu.isJavascript() )
      {
        out.print( "javascript:" );
        out.print( menu.getUrl() );
      }
      else
      {
        out.print( RequestUtils.getBaseURI( request ) );
        out.print( menu.getUrl() );
      }
    }
    else
    {
      out.print( "#" );
    }
    out.print( "\"" );
    if ( menu.getTarget() != null )
    {
      out.print( " target=\"" + menu.getTarget() + "\"" );
    }
    out.print( " onclick=\"selTab(\'" + tabId + "\');\"" );

    out.print( "><span>" );
    out.print( menuLabel );
    out.println( "</span></a>" );

    List optionList = menu.getMenuOptionsList();
    if ( optionList.size() > 0 )
    {
      writeMenuOptionList( request, out, menu.getAsset(), optionList, tabId );
    }
    out.println( "</li>" );
  }

  private void writeMenuOptionList( HttpServletRequest request, JspWriter out, String asset, List optionList, String parentTabId ) throws JspException, IOException
  {
    out.println( "<ul>" );

    for ( Iterator iter = optionList.iterator(); iter.hasNext(); )
    {
      MenuOption option = (MenuOption)iter.next();
      List subOptionList = option.getSubMenuOptionList();
      out.println( "<li>" );
      String menuLabel = "";
      if ( option.shouldLookupKeyInCM() )
      {
        menuLabel = getCMAssetValue( asset, option.getKey() );
      }
      else
      {
        menuLabel = option.getKey();
      }

      if ( option.isDisplayAsLink() )
      {
        out.print( "<a href=\"" );
        if ( option.getUrl() != null )
        {
          if ( option.getUrl().startsWith( "http://" ) || option.getUrl().startsWith( "https://" ) )
          {
            out.print( option.getUrl() );
          }
          else
          {
            out.print( RequestUtils.getBaseURI( request ) );
            out.print( option.getUrl() );
          }
        }
        else
        {
          out.print( "#" );
        }
        out.print( "\"" );
        if ( option.getTarget() != null )
        {
          out.print( " target=\"" + option.getTarget() + "\"" );
        }
        out.print( " onclick=\"selTab(\'" + parentTabId + "\');\"" );
        out.print( ">" );
        out.print( menuLabel );
        out.println( "</a>" );
      }
      else
      {
        out.print( menuLabel );
      }

      if ( subOptionList.size() > 0 )
      {
        writeMenuOptionList( request, out, asset, subOptionList, parentTabId );
      }
      out.println( "</li>" );
    }
    out.println( "</ul>" );
  }

  private String getCMAssetValue( String asset, String key )
  {
    return CmsResourceBundle.getCmsBundle().getString( asset, key );
  }
}
