
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.CmsUtil;

public class SSIContestTypesView
{
  private String language;
  private String contestGuideUrl;
  private List<SSIContestTypeView> contestTypes = new ArrayList<SSIContestTypeView>();

  public SSIContestTypesView( SSIPromotion ssiPromotion, String contestGuideUrl, SSIContestValueBean valueBean )
  {
    // defaulting to en_US
    this.language = "en_US";
    this.contestGuideUrl = contestGuideUrl;
    Locale locale = CmsUtil.getLocale( language );
    List<SSIContestType> contestTypesFromPcikList = (List<SSIContestType>)SSIContestType.getList();

    if ( ssiPromotion.isObjectivesSelected() )
    {
      String contestTypeName = getContestTypeName( contestTypesFromPcikList, SSIContestType.OBJECTIVES );
      contestTypes.add( new SSIContestTypeView( SSIContest.CONTEST_TYPE_OBJECTIVES, contestTypeName, valueBean.getDescriptionO() ) );
    }
    if ( ssiPromotion.isStepItUpSelected() )
    {
      String contestTypeName = getContestTypeName( contestTypesFromPcikList, SSIContestType.STEP_IT_UP );
      contestTypes.add( new SSIContestTypeView( SSIContest.CONTEST_TYPE_STEP_IT_UP, contestTypeName, valueBean.getDescriptionStu() ) );
    }
    if ( ssiPromotion.isDoThisGetThatSelected() )
    {
      String contestTypeName = getContestTypeName( contestTypesFromPcikList, SSIContestType.DO_THIS_GET_THAT );
      contestTypes.add( new SSIContestTypeView( SSIContest.CONTEST_TYPE_DO_THIS_GET_THAT, contestTypeName, valueBean.getDescriptionDtgt() ) );
    }
    if ( ssiPromotion.isStackRankSelected() )
    {
      String contestTypeName = getContestTypeName( contestTypesFromPcikList, SSIContestType.STACK_RANK );
      contestTypes.add( new SSIContestTypeView( SSIContest.CONTEST_TYPE_STACK_RANK, contestTypeName, valueBean.getDescriptionSr() ) );
    }
    if ( ssiPromotion.isAwardThemNowSelected() )
    {
      String contestTypeName = getContestTypeName( contestTypesFromPcikList, SSIContestType.AWARD_THEM_NOW );
      contestTypes.add( new SSIContestTypeView( SSIContest.CONTEST_TYPE_AWARD_THEM_NOW, contestTypeName, valueBean.getDescriptionAtn() ) );
    }

  }

  private String getContestTypeName( List<SSIContestType> contestTypesFromPcikList, String code )
  {
    for ( SSIContestType ssiContestType : contestTypesFromPcikList )
    {
      if ( ssiContestType.getCode().equals( code ) )
      {
        return ssiContestType.getName();
      }
    }
    return null;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage( String language )
  {
    this.language = language;
  }

  public String getContestGuideUrl()
  {
    return contestGuideUrl;
  }

  public void setContestGuideUrl( String contestGuideUrl )
  {
    this.contestGuideUrl = contestGuideUrl;
  }

  public List<SSIContestTypeView> getContestTypes()
  {
    return contestTypes;
  }

  public void setContestTypes( List<SSIContestTypeView> contestTypes )
  {
    this.contestTypes = contestTypes;
  }

}
