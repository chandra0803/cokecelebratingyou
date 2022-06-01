
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.objectpartners.cms.util.CmsResourceBundle;

public class DIYCommunicationsMetaView
{
  private List<DIYCommunicationsColumnList> columns = new ArrayList<DIYCommunicationsColumnList>();

  public DIYCommunicationsMetaView()
  {

  }

  public DIYCommunicationsMetaView( List<DIYCommunications> communicationList, String assetCode )
  {
    DIYCommunicationsColumnList column1 = new DIYCommunicationsColumnList();
    column1.setId( "1" );
    column1.setName( CmsResourceBundle.getCmsBundle().getString( assetCode ) );

    DIYCommunicationsColumnList column2 = new DIYCommunicationsColumnList();
    column2.setId( "2" );
    column2.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.common.labels.START_DATE" ) );

    DIYCommunicationsColumnList column3 = new DIYCommunicationsColumnList();
    column3.setId( "3" );
    column3.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.common.labels.END_DATE" ) );

    DIYCommunicationsColumnList column4 = new DIYCommunicationsColumnList();
    column4.setId( "4" );
    column4.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.common.labels.EDIT" ) );

    this.columns.add( column1 );
    this.columns.add( column2 );
    this.columns.add( column3 );
    this.columns.add( column4 );
  }

  public List<DIYCommunicationsColumnList> getColumns()
  {
    return columns;
  }

  public void setColumns( List<DIYCommunicationsColumnList> columns )
  {
    this.columns = columns;
  }

}
