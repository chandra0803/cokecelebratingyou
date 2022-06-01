
package com.biperf.core.ui.news;

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/news/NewsModuleAction.java,v $
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.news.NewsDetailsView;
import com.biperf.core.domain.news.NewsView;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.ui.homepage.BaseXPromotionalAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * NewsModuleAction.
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
 * <td>sharafud</td>
 * <td>December 6, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NewsModuleAction extends BaseXPromotionalAction
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Get all available news for current user
   */
  @SuppressWarnings( "unchecked" )
  public ActionForward newsList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<NewsDetailsView> newsView = getParticipantDIYCommunicationsService().getNewsList( request.getContextPath() );
    if ( isXpromotionEnabled( request ) )
    {
      newsView = (List<NewsDetailsView>)shiftCollection( newsView );
    }
    super.writeAsJsonToResponse( new NewsView( newsView ), response );
    return null;
  }

  /**
   * Get news for a given messageUniqueId
   */
  public ActionForward newsDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<Content> existingNews = getNews();
    List<Content> diyNews = getParticipantDIYCommunicationsService().getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_NEWS, DIYCommunications.DIY_NEWS_SECTION_CODE );
    String baseUri = RequestUtils.getBaseURI( request );
    String messageUniqueId = null;

    List<Content> filteredExistingNews = new ArrayList<Content>();
    List<Content> filteredDiyNews = new ArrayList<Content>();

    if ( StringUtil.isValid( request.getParameter( "messageUniqueId" ) ) )
    {
      messageUniqueId = request.getParameter( "messageUniqueId" ).toString();
    }
    if ( messageUniqueId != null )
    {
      for ( Content content : existingNews )
      {
        Map<String, String> contentDataMap = content.getContentDataMap();
        String uniqueId = contentDataMap.get( "UNIQUE_NEWS_ID" );
        if ( uniqueId.equals( messageUniqueId ) )
        {
          filteredExistingNews.add( content );
          break;
        }
      }
      for ( Content content : diyNews )
      {
        Map<String, String> contentDataMap = content.getContentDataMap();
        String uniqueId = contentDataMap.get( "UNIQUE_NEWS_ID" );
        if ( uniqueId.equals( messageUniqueId ) )
        {
          filteredDiyNews.add( content );
          break;
        }
      }
    }
    List<NewsDetailsView> allNews = new ArrayList<NewsDetailsView>();
    for ( Content content : filteredDiyNews )
    {
      allNews.add( buildNewsJson( content, baseUri, true ) );
    }
    for ( Content content : filteredExistingNews )
    {
      allNews.add( buildNewsJson( content, baseUri, false ) );
    }
    PropertyComparator.sort( allNews, new MutableSortDefinition( "storyDate", false, false ) );
    super.writeAsJsonToResponse( new NewsView( allNews ), response );
    return null;
  }

  public NewsDetailsView buildNewsJson( Content newsContent, String baseUri, boolean diy )
  {
    DateFormat originalFormat = new SimpleDateFormat( "MM/dd/yyyy" );
    DateFormat targetFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

    Map<String, String> contentDataMap = newsContent.getContentDataMap();
    String startDate = "";

    if ( diy )
    {
      startDate = contentDataMap.get( "START_DATE" );
    }
    else
    {
      startDate = contentDataMap.get( "DATE" );
    }
    StringTokenizer st = new StringTokenizer( startDate, "/" );

    String month = st.nextToken();
    String date = st.nextToken();
    String year = st.nextToken();
    if ( month.length() == 1 )
    {
      month = "0" + month;
    }
    if ( date.length() == 1 )
    {
      date = "0" + date;
    }
    // month = getMonthForInt( Integer.parseInt( month ) - 1 );
    String sortDate = year + "-" + month + "-" + date;
    String storyName = contentDataMap.get( "TITLE" );
    String uniqueStoryId = contentDataMap.get( "UNIQUE_NEWS_ID" );
    String storySlug = baseUri + "/newsDetail.do";
    String storyContent = "<p>" + contentDataMap.get( "FULL_TXT" ) + "</p>";
    String storyContentShort = contentDataMap.get( "SHORT_TXT" );

    String storyImageUrl = contentDataMap.get( "NEWS_IMAGE" );
    String storyImageUrlMax = contentDataMap.get( "NEWS_IMAGE_MAX" );
    String storyImageUrlMobile = contentDataMap.get( "NEWS_IMAGE_MOBILE" );

    NewsDetailsView newsDetails = new NewsDetailsView();
    newsDetails.setSortDate( sortDate );
    newsDetails.setStoryName( storyName );
    newsDetails.setStorySlug( storySlug );
    newsDetails.setStoryDate( DateUtils.toConvertDateFormatString( originalFormat, targetFormat, startDate ) );
    newsDetails.setStoryContent( storyContent );
    if ( storyContentShort != null )
    {
      String shortText = "<p>" + storyContentShort + "</p>";
      newsDetails.setStoryContentShort( shortText );
    }
    newsDetails.setId( uniqueStoryId );
    newsDetails.setStoryImageUrl( storyImageUrl );
    newsDetails.setStoryImageUrl_max( storyImageUrlMax );
    newsDetails.setStoryImageUrl_mobile( storyImageUrlMobile );
    return newsDetails;
  }

  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.newsList( mapping, form, request, response );
    }
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private List<Content> getNews()
  {
    List<Content> contents = (List)ContentReaderManager.getContentReader().getContent( "home.news" );

    return contents;
  }

  public static ParticipantDIYCommunicationsService getParticipantDIYCommunicationsService()
  {
    return (ParticipantDIYCommunicationsService)getService( ParticipantDIYCommunicationsService.BEAN_NAME );
  }

}
