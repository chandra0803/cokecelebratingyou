
package com.biperf.core.domain.enums;

import java.util.List;

public class NominationWizardOrder extends PickListItem
{

  private static final String PICKLIST_ASSET = "picklist.nominationwizardorder";

  public static final String INDIVIDUAL_OR_TEAM = "individual_or_team";
  public static final String NOMINEE = "nominee";
  public static final String BEHAVIOUR = "behaviour";
  public static final String ECARD = "ecard";
  public static final String WHY = "why";
  public static final String WHY_BEFORE = "why_before";
  public static final String WHY_AFTER = "why_after";

  public static List getList()
  {
    return getPickListFactory().getPickList( NominationWizardOrder.class );
  }

  public static NominationWizardOrder lookup( String code )
  {
    return (NominationWizardOrder)getPickListFactory().getPickListItem( NominationWizardOrder.class, code );
  }

  NominationWizardOrder()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}
