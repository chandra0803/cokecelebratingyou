
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
import com.biperf.core.ui.utils.RequestUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * G3ReduxParticipantAdminTag.
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
 * <td>January 14,2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class G3ReduxParticipantAdminTag extends TagSupport
{
  private static final Log log = LogFactory.getLog( G3ReduxParticipantAdminTag.class );

  /**
   * Overridden from
   * 
   * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
   * @return status code
   * @throws JspException
   */
  public int doStartTag() throws JspException
  {
    int optionCount = 0;
    try
    {
      HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
      JspWriter jspWriter = pageContext.getOut();

      // check if menuList already available in request
      Menu menu = (Menu)request.getAttribute( "paxAdminMenu" );
      if ( menu != null )
      {
        List optionList = menu.getMenuOptionsList();
        if ( optionList.size() > 0 )
        {
          jspWriter.println( "<div class=\"column2a\">" );
          jspWriter.println( "<div class=\"module modcolor1\">" );
          jspWriter.println( "<div class=\"guts\">" );

          writeMenuOptionList( request, jspWriter, menu.getAsset(), optionList, false, optionCount );

          jspWriter.println( "</div>" );
          jspWriter.println( "</div>" );
          jspWriter.println( "</div>" );
        }

      }
      else
      {
        log.error( "Menu is null" );
      }
    }
    catch( IOException e )
    {
      log.error( e.getMessage(), e );
      throw new JspException( "doStartTag: Exception", e );
    }
    return SKIP_BODY;
  }

  private void writeMenuOptionList( HttpServletRequest request, JspWriter out, String asset, List optionList, boolean isSubOption, int optionCount ) throws JspException, IOException
  {
    if ( isSubOption )
    {
      out.println( "<dl>" );
    }
    for ( Iterator iter = optionList.iterator(); iter.hasNext(); )
    {
      MenuOption option = (MenuOption)iter.next();
      List subOptionList = option.getSubMenuOptionList();
      if ( isSubOption )
      {
        out.println( "<dt>" );
      }
      else
      {
        optionCount++;
        if ( optionCount == 5 )
        {
          out.println( "</div>" );
          out.println( "</div>" );
          out.println( "</div>" );

          out.println( "<div class=\"column2b\">" );
          out.println( "<div class=\"module modcolor1\">" );
          out.println( "<div class=\"guts\">" );
        }
        out.println( "<h3>" );
      }
      String menuLabel = "";
      if ( option.shouldLookupKeyInCM() )
      {
        menuLabel = getCMAssetValue( asset, option.getKey() );
      }
      else
      {
        menuLabel = option.getKey();
      }

      if ( option.isDisplayAsLink() && option.getUrl() != null )
      {
        out.print( "<a href=\"" );
        if ( option.getUrl().startsWith( "http://" ) || option.getUrl().startsWith( "https://" ) )
        {
          out.print( option.getUrl() );
        }
        else
        {
          out.print( RequestUtils.getBaseURI( request ) );
          out.print( option.getUrl() );
        }
        out.print( "\"" );
        if ( option.getTarget() != null )
        {
          out.print( " target=\"" + option.getTarget() + "\"" );
        }
        out.print( ">" );
        out.print( menuLabel );
        out.println( "</a>" );
      }
      else
      {
        out.print( menuLabel );
      }
      if ( !isSubOption )
      {
        out.println( "</h3>" );
      }

      if ( subOptionList.size() > 0 )
      {
        writeMenuOptionList( request, out, asset, subOptionList, true, optionCount );
      }
      if ( isSubOption )
      {
        out.println( "</dt>" );
      }
    } // end for
    if ( isSubOption )
    {
      out.println( "</dl>" );
    }

  }

  private String getCMAssetValue( String asset, String key )
  {
    return CmsResourceBundle.getCmsBundle().getString( asset, key );
  }
}
