
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.biperf.core.ui.recognition.SendRecognitionForm;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean;

public class NominationSubmitForm extends SendRecognitionForm
{

  private static final long serialVersionUID = 1L;

  private NominationSubmitDataPromotionValueBean promotion = new NominationSubmitDataPromotionValueBean();
  private List<NominationsParticipantDataValueBean.ParticipantValueBean> participants;
  private boolean draft;
  private Long groupId;

  private FormFile nominationLink;
  private String attachedDoc;

  public NominationSubmitDataPromotionValueBean getPromotion()
  {
    return promotion;
  }

  public void setPromotion( NominationSubmitDataPromotionValueBean promotion )
  {
    this.promotion = promotion;
  }

  @SuppressWarnings( "unchecked" )
  public List<NominationsParticipantDataValueBean.ParticipantValueBean> getParticipants()
  {
    if ( participants == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationsParticipantDataValueBean.ParticipantValueBean();
        }
      };
      participants = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return participants;
  }

  public void setParticipants( List<NominationsParticipantDataValueBean.ParticipantValueBean> participants )
  {
    this.participants = participants;
  }

  public boolean isDraft()
  {
    return draft;
  }

  public void setDraft( boolean draft )
  {
    this.draft = draft;
  }

  public Long getGroupId()
  {
    return groupId;
  }

  public void setGroupId( Long groupId )
  {
    this.groupId = groupId;
  }

  @Override
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    if ( CollectionUtils.isNotEmpty( this.participants ) )
    {
      this.participants.clear();
    }

  }

  public String getAttachedDoc()
  {
    return attachedDoc;
  }

  public void setAttachedDoc( String attachedDoc )
  {
    this.attachedDoc = attachedDoc;
  }

  public FormFile getNominationLink()
  {
    return nominationLink;
  }

  public void setNominationLink( FormFile nominationLink )
  {
    this.nominationLink = nominationLink;
  }

}
