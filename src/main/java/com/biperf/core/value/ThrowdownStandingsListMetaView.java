
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ThrowdownStandingsListMetaView
{

  private List<MatchListColumn> columns = new ArrayList<MatchListColumn>();

  public void setColumns( List<MatchListColumn> columns )
  {
    this.columns = columns;
  }

  public ThrowdownStandingsListMetaView()
  {
    MatchListColumn column1 = new MatchListColumn();
    column1.setId( "1" );
    column1.setName( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYER" ) );
    column1.setDescription( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYERS_NAMES" ) );
    column1.setType( "string" );
    column1.setAlignment( "" );
    column1.setColSpan( 2 );
    column1.setNameId( "" );
    column1.setSortable( true );
    column1.setSortUrl( getSiteUrlPrefix() + "/throwdown/standingsDetail.do?method=detailInfo&sortedOn=1&sortedBy=" );
    this.columns.add( column1 );

    MatchListColumn column2 = new MatchListColumn();
    column2.setId( "2" );
    column2.setName( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.WIN" ) );
    column2.setDescription( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYERS_RECORDS" ) );
    column2.setType( "string" );
    column2.setAlignment( "" );
    column2.setNameId( "" );
    column2.setSortable( true );
    column2.setSortUrl( getSiteUrlPrefix() + "/throwdown/standingsDetail.do?method=detailInfo&sortedOn=2&sortedBy=" );
    this.columns.add( column2 );

    MatchListColumn column3 = new MatchListColumn();
    column3.setId( "3" );
    column3.setName( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.LOSS" ) );
    column3.setDescription( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYERS_SCORES" ) );
    column3.setType( "string" );
    column3.setAlignment( "" );
    column3.setNameId( "" );
    column3.setSortable( true );
    column3.setSortUrl( getSiteUrlPrefix() + "/throwdown/standingsDetail.do?method=detailInfo&sortedOn=3&sortedBy=" );
    this.columns.add( column3 );

    MatchListColumn column4 = new MatchListColumn();
    column4.setId( "4" );
    column4.setName( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.TIE" ) );
    column4.setDescription( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.MATCH_DETAILS" ) );
    column4.setType( "string" );
    column4.setAlignment( "" );
    column4.setNameId( "" );
    column4.setSortable( true );
    column4.setSortUrl( getSiteUrlPrefix() + "/throwdown/standingsDetail.do?method=detailInfo&sortedOn=4&sortedBy=" );
    this.columns.add( column4 );
  }

  public List<MatchListColumn> getColumns()
  {
    return this.columns;
  }

  private String getSiteUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
