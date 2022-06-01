/**
 * 
 */

package com.biperf.core.service.home.strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Asset;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * @author poddutur
 *
 */
public class NewsModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( UserManager.getUser().isParticipant() )
    {
      boolean activeContentFound = false;
      boolean activeDiyContentFound = false;
      if ( super.isUserInAudience( participant, filter, parameterMap ) )
      {
        List<Content> contents = (List)ContentReaderManager.getContentReader().getContent( "home.news" );
        if ( contents != null )
        {
          for ( Content content : contents )
          {
            Map contentDataMap = content.getContentDataMap();
            Date startDate = DateUtils.toDate( (String)contentDataMap.get( "START_DATE" ), UserManager.getDefaultLocale() );
            Date endDate = DateUtils.toDate( (String)contentDataMap.get( "END_DATE" ), UserManager.getDefaultLocale() );
            if ( DateUtils.isTodaysDateBetween( startDate, endDate ) )
            {
              activeContentFound = true;
              break;
            }
          }
        }
      }

      List<Content> newsList = new ArrayList<Content>();
      List<Content> assets = getCmAssetService().getAssetsForSection( DIYCommunications.DIY_NEWS_SECTION_CODE );

      for ( Iterator assetsIter = assets.iterator(); assetsIter.hasNext(); )
      {
        Object contentObject = null;
        Content content = null;

        Asset asset = (Asset)assetsIter.next();
        contentObject = ContentReaderManager.getContentReader().getContent( asset.getCode() );

        if ( contentObject instanceof Content )
        {
          content = (Content)contentObject;
        }
        else if ( contentObject instanceof List )
        {
          content = ! ( (List)contentObject ).isEmpty() ? (Content) ( (List)contentObject ).get( 0 ) : new Content();
        }

        if ( content != null )
        {
          newsList.add( content );
        }
      }

      if ( !newsList.isEmpty() )
      {

        for ( Content content1 : newsList )
        {
          Map contentDataMap1 = content1.getContentDataMap();
          Date startDate = DateUtils.toDate( (String)contentDataMap1.get( "START_DATE" ), UserManager.getDefaultLocale() );
          Date endDate = DateUtils.toDate( (String)contentDataMap1.get( "END_DATE" ), UserManager.getDefaultLocale() );
          if ( DateUtils.isTodaysDateBetween( startDate, endDate ) )
          {
            activeDiyContentFound = true;
            break;
          }
        }
      }

      if ( activeContentFound || activeDiyContentFound )
      {
        return true;
      }

      return false;
    }
    else
    {
      return false;
    }
  }
}
