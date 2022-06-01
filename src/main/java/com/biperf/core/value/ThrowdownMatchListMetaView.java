
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.objectpartners.cms.util.CmsResourceBundle;

public class ThrowdownMatchListMetaView
{

  private List<MatchListColumn> columns = new ArrayList<MatchListColumn>();

  public void setColumns( List<MatchListColumn> columns )
  {
    this.columns = columns;
  }

  public ThrowdownMatchListMetaView()
  {
    MatchListColumn column1 = new MatchListColumn();
    column1.setId( "1" );
    column1.setName( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYER" ) );
    column1.setDescription( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYERS_NAMES" ) );
    column1.setType( "string" );
    column1.setAlignment( "" );
    column1.setColSpan( 2 );
    column1.setNameId( "" );
    this.columns.add( column1 );

    MatchListColumn column2 = new MatchListColumn();
    column2.setId( "2" );
    column2.setName( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.RECORD" ) );
    column2.setDescription( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYERS_RECORDS" ) );
    column2.setType( "td-record" );
    column2.setAlignment( "" );
    column2.setNameId( "" );
    this.columns.add( column2 );

    MatchListColumn column3 = new MatchListColumn();
    column3.setId( "3" );
    column3.setName( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.SCORES" ) );
    column3.setDescription( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.PLAYERS_SCORES" ) );
    column3.setType( "string" );
    column3.setAlignment( "" );
    column3.setColSpan( 2 );
    column3.setNameId( "" );
    this.columns.add( column3 );
  }

  public List<MatchListColumn> getColumns()
  {
    return this.columns;
  }

}
