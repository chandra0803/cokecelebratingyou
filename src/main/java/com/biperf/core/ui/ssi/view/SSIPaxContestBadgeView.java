
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ServiceLocator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.objectpartners.cms.util.CmsResourceBundle;

@JsonInclude( value = Include.NON_NULL )
public class SSIPaxContestBadgeView
{
  private Long id;
  private String type;
  private String name;
  private String howToEarnText;
  private String img;
  private static final String CM_BADGE_NAME_HTML_KEY = "HTML_KEY";

  public SSIPaxContestBadgeView()
  {

  }

  public SSIPaxContestBadgeView( Long badgeId, String badgeName, String badgeUrl )
  {
    this.id = badgeId;
    this.name = badgeName;
    this.img = badgeUrl;
  }

  public SSIPaxContestBadgeView( BadgeRule badgeRule )
  {
    if ( badgeRule != null )
    {
      List<BadgeLibrary> badgeLibraryList = getPromotionService().buildBadgeLibraryList();
      for ( BadgeLibrary badgeLibrary : badgeLibraryList )
      {
        if ( badgeRule.getBadgeLibraryCMKey().equals( badgeLibrary.getBadgeLibraryId() ) )
        {
          String badgeName = CmsResourceBundle.getCmsBundle().getString( badgeRule.getBadgeName().trim(), CM_BADGE_NAME_HTML_KEY );
          String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
          this.setId( badgeRule.getId() );
          this.setType( badgeLibrary.getLibraryname() );
          this.setName( badgeName );
          this.setHowToEarnText( BadgeType.EARNED_OR_NOT_EARNED );
          this.setImg( siteUrlPrefix + badgeLibrary.getEarnedImageSmall() );
        }
      }
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getHowToEarnText()
  {
    return howToEarnText;
  }

  public void setHowToEarnText( String howToEarnText )
  {
    this.howToEarnText = howToEarnText;
  }

  public String getImg()
  {
    return img;
  }

  public void setImg( String img )
  {
    this.img = img;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)ServiceLocator.getService( SystemVariableService.BEAN_NAME );
  }

}
