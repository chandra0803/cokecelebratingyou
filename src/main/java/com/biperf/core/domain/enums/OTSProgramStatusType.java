
package com.biperf.core.domain.enums;

import java.util.List;

public class OTSProgramStatusType extends PickListItem
{
  private static final String PICKLIST_ASSET = "picklist.ots.statustype";

  // Picklist codes
  public static final String INCOMPLETED = "incompleted";
  public static final String COMPLETED = "completed";

  protected OTSProgramStatusType()
  {
    super();
  }

  public static List getList()
  {
    return getPickListFactory().getPickList( OTSProgramStatusType.class );
  }

  public static OTSProgramStatusType lookup( String code )
  {
    return (OTSProgramStatusType)getPickListFactory().getPickListItem( OTSProgramStatusType.class, code );
  }

  @Override
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public boolean isCompleted()
  {
    return getCode().equalsIgnoreCase( COMPLETED );
  }

  public boolean isInCompleted()
  {
    return getCode().equalsIgnoreCase( INCOMPLETED );
  }

}
