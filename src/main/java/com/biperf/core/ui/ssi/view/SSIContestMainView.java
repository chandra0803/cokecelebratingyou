
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestDocumentValueBean;
import com.biperf.core.value.ssi.SSIContestLanguageValueBean;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.domain.Content;

/**
 * 
 * SSIContestMainView.
 * 
 * @author kandhi
 * @since Nov 10, 2014
 * @version 1.0
 */
public class SSIContestMainView
{
  private static final String URL_SAVE_GENERAL_INFO = "/ssi/saveGeneralInfo.do";
  private static final String CONTEST_LIST_PAGE = "/ssi/creatorContestList.do?method=display";

  private int currentStep;
  private String defaultLanguage;
  private String startDate;
  private String endDate;
  private String tileStartDate;
  private List<SSIContestLanguageValueBean> languages;
  private List<SSIContestApproverView> contestApprovers;
  private List<SSIContestDescriptionValueBean> descriptions = new ArrayList<SSIContestDescriptionValueBean>();
  private List<SSIContestNameValueBean> names = new ArrayList<SSIContestNameValueBean>();
  private List<SSIContestDocumentValueBean> documents = new ArrayList<SSIContestDocumentValueBean>();
  private List<SSIContestMessageValueBean> messages = new ArrayList<SSIContestMessageValueBean>();
  private int namesTranslationCount = 0;
  private int descriptionsTranslationCount = 0;
  private int documentsTranslationCount = 0;
  private int messagesTranslationCount = 0;
  private boolean includeMessage;
  private String billTo;
  private boolean billCodeRequired;
  private boolean contestApprovalRequired;
  private String contestType;
  private String ssiContestClientState;
  private Long contestId;
  private String saveAsDraftForwardUrl;
  private String nextUrl;
  private boolean allowSpreadsheet;
  private List<SSIContestBillCodeView> billCodes;

  public SSIContestMainView( SSIContest ssiContest,
                             SSIPromotion ssiPromotion,
                             String contestClientState,
                             String contestType,
                             SSIContestValueBean valueBean,
                             List localeItems,
                             String sysUrl,
                             List<SSIContestBillCodeView> billCodes )
  {
    this.currentStep = 1;
    this.contestId = ssiContest.getId();
    this.setContestType( contestType );
    populateContest( ssiContest, ssiPromotion, valueBean, localeItems );
    this.ssiContestClientState = contestClientState;
    this.saveAsDraftForwardUrl = sysUrl + CONTEST_LIST_PAGE;
    this.nextUrl = sysUrl + URL_SAVE_GENERAL_INFO;
    this.allowSpreadsheet = Boolean.TRUE;
    this.billCodes = billCodes;
  }

  private void populateContest( SSIContest ssiContest, SSIPromotion ssiPromotion, SSIContestValueBean valueBean, List localeItems )
  {
    if ( ssiContest.getStartDate() != null )
    {
      this.startDate = DateUtils.toDisplayString( ssiContest.getStartDate() );
    }

    if ( ssiContest.getEndDate() != null )
    {
      this.endDate = DateUtils.toDisplayString( ssiContest.getEndDate() );
    }
    if ( ssiContest.getDisplayStartDate() != null )
    {
      this.tileStartDate = DateUtils.toDisplayString( ssiContest.getDisplayStartDate() );
    }

    this.billCodeRequired = ssiPromotion.isBillCodesActive();
    if ( this.billCodeRequired )
    {
      if ( ssiContest.getBillPayoutCodeType() != null )
      {
        this.billTo = ssiContest.getBillPayoutCodeType().getCode();
      }
    }
    this.contestApprovalRequired = ssiPromotion.getRequireContestApproval();
    this.defaultLanguage = "en_US";
    populateSystemLanguages( localeItems );
    populateContestValues( ssiContest, valueBean );
  }

  private void populateSystemLanguages( List localeItems )
  {
    languages = new ArrayList<SSIContestLanguageValueBean>();
    for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      if ( content != null )
      {
        String localeCode = (String)content.getContentDataMap().get( "CODE" );
        LanguageType type = LanguageType.lookup( "" + localeCode );
        if ( null != type )
        {
          SSIContestLanguageValueBean language = new SSIContestLanguageValueBean( localeCode, type.getName() );
          languages.add( language );
        }
      }
    }
  }

  private void populateContestValues( SSIContest ssiContest, SSIContestValueBean valueBean )
  {
    messages = new ArrayList<SSIContestMessageValueBean>();
    names = new ArrayList<SSIContestNameValueBean>();
    descriptions = new ArrayList<SSIContestDescriptionValueBean>();
    documents = new ArrayList<SSIContestDocumentValueBean>();
    SSIContestNameValueBean nameVB = new SSIContestNameValueBean( defaultLanguage, valueBean.getContestName() );
    names.add( nameVB );
    SSIContestDescriptionValueBean descriptionVB = new SSIContestDescriptionValueBean( defaultLanguage, valueBean.getDescription() );
    descriptions.add( descriptionVB );
    if ( ssiContest.getIncludePersonalMessage() != null && ssiContest.getIncludePersonalMessage() )
    {
      SSIContestMessageValueBean messageVB = new SSIContestMessageValueBean( defaultLanguage, valueBean.getMessage() );
      messages.add( messageVB );
      this.includeMessage = Boolean.TRUE;
    }
    else
    {
      this.includeMessage = Boolean.FALSE;
    }
    SSIContestDocumentValueBean documentVB = new SSIContestDocumentValueBean( defaultLanguage,
                                                                              valueBean.getAttachmentUrl(),
                                                                              valueBean.getAttachmentTitle(),
                                                                              valueBean.getAttachmentUrl(),
                                                                              valueBean.getAttachmentOriginalName() );
    documents.add( documentVB );
  }

  public int getCurrentStep()
  {
    return currentStep;
  }

  public void setCurrentStep( int currentStep )
  {
    this.currentStep = currentStep;
  }

  public String getDefaultLanguage()
  {
    return defaultLanguage;
  }

  public void setDefaultLanguage( String defaultLanguage )
  {
    this.defaultLanguage = defaultLanguage;
  }

  public List<SSIContestLanguageValueBean> getLanguages()
  {
    return languages;
  }

  public void setLanguages( List<SSIContestLanguageValueBean> languages )
  {
    this.languages = languages;
  }

  public int getNamesTranslationCount()
  {
    return namesTranslationCount;
  }

  public void setNamesTranslationCount( int namesTranslationCount )
  {
    this.namesTranslationCount = namesTranslationCount;
  }

  public List<SSIContestNameValueBean> getNames()
  {
    return names;
  }

  public void setNames( List<SSIContestNameValueBean> names )
  {
    this.names = names;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getTileStartDate()
  {
    return tileStartDate;
  }

  public void setTileStartDate( String tileStartDate )
  {
    this.tileStartDate = tileStartDate;
  }

  public int getDescriptionsTranslationCount()
  {
    return descriptionsTranslationCount;
  }

  public void setDescriptionsTranslationCount( int descriptionsTranslationCount )
  {
    this.descriptionsTranslationCount = descriptionsTranslationCount;
  }

  public List<SSIContestDescriptionValueBean> getDescriptions()
  {
    return descriptions;
  }

  public void setDescriptions( List<SSIContestDescriptionValueBean> descriptions )
  {
    this.descriptions = descriptions;
  }

  public int getDocumentsTranslationCount()
  {
    return documentsTranslationCount;
  }

  public void setDocumentsTranslationCount( int documentsTranslationCount )
  {
    this.documentsTranslationCount = documentsTranslationCount;
  }

  public List<SSIContestDocumentValueBean> getDocuments()
  {
    return documents;
  }

  public void setDocuments( List<SSIContestDocumentValueBean> documents )
  {
    this.documents = documents;
  }

  public boolean isIncludeMessage()
  {
    return includeMessage;
  }

  public void setIncludeMessage( boolean includeMessage )
  {
    this.includeMessage = includeMessage;
  }

  public int getMessagesTranslationCount()
  {
    return messagesTranslationCount;
  }

  public void setMessagesTranslationCount( int messagesTranslationCount )
  {
    this.messagesTranslationCount = messagesTranslationCount;
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public List<SSIContestApproverView> getContestApprovers()
  {
    return contestApprovers;
  }

  public void setContestApprovers( List<SSIContestApproverView> contestApprovers )
  {
    this.contestApprovers = contestApprovers;
  }

  public String getBillTo()
  {
    return billTo;
  }

  public void setBillTo( String billTo )
  {
    this.billTo = billTo;
  }

  public boolean isBillCodeRequired()
  {
    return billCodeRequired;
  }

  public void setBillCodeRequired( boolean billCodeRequired )
  {
    this.billCodeRequired = billCodeRequired;
  }

  public boolean isContestApprovalRequired()
  {
    return contestApprovalRequired;
  }

  public void setContestApprovalRequired( boolean contestApprovalRequired )
  {
    this.contestApprovalRequired = contestApprovalRequired;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getSsiContestClientState()
  {
    return ssiContestClientState;
  }

  public void setSsiContestClientState( String ssiContestClientState )
  {
    this.ssiContestClientState = ssiContestClientState;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public String getSaveAsDraftForwardUrl()
  {
    return saveAsDraftForwardUrl;
  }

  public void setSaveAsDraftForwardUrl( String saveAsDraftForwardUrl )
  {
    this.saveAsDraftForwardUrl = saveAsDraftForwardUrl;
  }

  public String getNextUrl()
  {
    return nextUrl;
  }

  public void setNextUrl( String nextUrl )
  {
    this.nextUrl = nextUrl;
  }

  @JsonProperty( "allowSpreadsheet" )
  public boolean isAllowSpreadsheet()
  {
    return allowSpreadsheet;
  }

  public void setAllowSpreadsheet( boolean allowSpreadsheet )
  {
    this.allowSpreadsheet = allowSpreadsheet;
  }

  public List<SSIContestBillCodeView> getBillCodes()
  {
    return billCodes;
  }

  public void setBillCodes( List<SSIContestBillCodeView> billCodes )
  {
    this.billCodes = billCodes;
  }

}
