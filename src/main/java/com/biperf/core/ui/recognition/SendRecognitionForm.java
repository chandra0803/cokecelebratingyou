
package com.biperf.core.ui.recognition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.CMMessage;
import com.biperf.core.ui.MethodMap;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.recognition.state.PurlContributorBean;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.biperf.core.value.promotion.CustomFormStepElementsView;

public class SendRecognitionForm extends BaseForm implements Serializable
{
  public static final String CARD_TYPE_DRAWING = "drawing";
  public static final String CARD_TYPE_CERTIFICATE = "cert";
  public static final String CARD_TYPE_UPLOAD = "upload";
  public static final String CARD_TYPE_CARD = "card";

  public static final String PROMO_TYPE_RECOGNITION = "recognition";
  public static final String PROMO_TYPE_NOMINATION = "nomination";

  private Long promotionId;

  private String promotionType;
  private Long nodeId;

  private Long claimId;

  private String navigationTabJSON;

  // nomination only
  private String teamName;
  private String individualOrTeam;

  private String selectedBehavior;

  // ecard
  private String cardType;
  private Long cardId;
  private boolean cardCanEdit;
  private String cardUrl;
  private String cardData;
  private String ownCardName;
  private String drawingData;
  private String videoImageUrl;
  private String videoUrl;

  private String comments;

  private boolean sendCopyToManager;
  private boolean sendCopyToMe;
  private String sendCopyToOthers;
  private boolean makeRecPrivate;

  private String recipientSendDate;

  private final Map<Integer, RecipientBean> claimRecipientFormBeans = new HashMap<>();

  private final Map<Integer, PurlContributorBean> claimContributorFormBeans = new HashMap<>();
  private boolean preselectedContributorsLocked;
  private Long recipientId;
  private String recipientNodeId;
  private Long purlRecipientId;
  private String purlReturnUrl;
  private PurlRecipient purlRecipient;

  private List<CMMessage> errors;

  private String initializationJson = "";

  private String initializationJsonStr = "";

  private String claimFormAsset;
  private Long claimFormId;
  private Long claimFormStepId;
  private Map<Long, List<ClaimElementForm>> claimElementForms;
  private final Map<Integer, ClaimElementForm> claimElements = new HashMap<>();

  private MethodMap claimElementValue;
  private String contributorTeamsSearchFilters;

  private Integer anniversaryYears;
  private Integer anniversaryDays;

  private List<ClaimElement> newClaimElementsList = new ArrayList<ClaimElement>();
  

  // Client customization for WIP #39189 starts
  private final Map<Integer, TcccClaimFileValueBean> claimUploadFormBeans = new HashMap<>();
  //private Map<Integer, String> uploadDescription = new HashMap<Integer, String>();
  //private Map<Integer, String> uploadUrl = new HashMap<Integer, String>();
  // Client customization for WIP #39189 ends

  private String idJson;

  private CustomFormStepElementsView customElements = new CustomFormStepElementsView();

  public boolean recognitionEdit = false;

  public int getClaimRecipientFormBeansCount()
  {
    if ( claimRecipientFormBeans == null || claimRecipientFormBeans.isEmpty() )
    {
      return 0;
    }
    return claimRecipientFormBeans.size();
  }

  public RecipientBean getClaimRecipientFormBeans( int index )
  {
    RecipientBean bean = claimRecipientFormBeans.get( index );
    if ( bean == null )
    {
      bean = new RecipientBean();
      claimRecipientFormBeans.put( index, bean );
    }
    return bean;
  }

  public void setClaimRecipientFormBeansForPurl( int index, RecipientBean recipientBean )
  {
    claimRecipientFormBeans.clear();
    claimRecipientFormBeans.put( 0, recipientBean );
  }

  public List<ClaimElement> getNewClaimElementsList()
  {
    return newClaimElementsList;
  }

  public void setNewClaimElementsList( List<ClaimElement> newClaimElementsList )
  {
    this.newClaimElementsList = newClaimElementsList;
  }

  public boolean isCardCanEdit()
  {
    return cardCanEdit;
  }

  public void setCardCanEdit( boolean cardCanEdit )
  {
    this.cardCanEdit = cardCanEdit;
  }

  public Long getCardId()
  {
    return cardId;
  }

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  public String getCardType()
  {
    return cardType;
  }

  public void setCardType( String cardType )
  {
    this.cardType = cardType;
  }

  public String getCardUrl()
  {
    return cardUrl;
  }

  public void setCardUrl( String cardUrl )
  {
    this.cardUrl = cardUrl;
  }

  public String getCardData()
  {
    return cardData;
  }

  public void setCardData( String cardData )
  {
    this.cardData = cardData;
  }

  public String getOwnCardName()
  {
    return ownCardName;
  }

  public void setOwnCardName( String ownCardName )
  {
    this.ownCardName = ownCardName;
  }

  public String getDrawingData()
  {
    return drawingData;
  }

  public void setDrawingData( String drawingData )
  {
    this.drawingData = drawingData;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String getVideoImageUrl()
  {
    return videoImageUrl;
  }

  public void setVideoImageUrl( String videoImageUrl )
  {
    this.videoImageUrl = videoImageUrl;
  }

  public List<RecipientBean> getRecipients()
  {
    return new ArrayList<>( claimRecipientFormBeans.values() );
  }

  public String getComments()
  {
    return (String)StringUtil.replaceApostrophe( StringUtil.escapeHTML( comments ) );
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public String getRecipientSendDate()
  {
    return recipientSendDate;
  }

  public void setRecipientSendDate( String recipientSendDate )
  {
    this.recipientSendDate = recipientSendDate;
  }

  public String getSelectedBehavior()
  {
    return selectedBehavior;
  }

  public void setSelectedBehavior( String selectedBehavior )
  {
    this.selectedBehavior = selectedBehavior;
  }

  public boolean getSendCopyToManager()
  {
    return sendCopyToManager;
  }

  public String getSendCopyToManagerForDataForm()
  {
    return sendCopyToManager ? "on" : "";
  }

  public void setSendCopyToManager( boolean sendCopyToManager )
  {
    this.sendCopyToManager = sendCopyToManager;
  }

  public boolean getSendCopyToMe()
  {
    return sendCopyToMe;
  }

  public String getSendCopyToMeForDataForm()
  {
    return sendCopyToMe ? "on" : "";
  }

  public void setSendCopyToMe( boolean sendCopyToMe )
  {
    this.sendCopyToMe = sendCopyToMe;
  }

  public String getSendCopyToOthers()
  {
    return sendCopyToOthers;
  }

  public void setSendCopyToOthers( String sendCopyToOthers )
  {
    this.sendCopyToOthers = sendCopyToOthers;
  }

  public boolean isMakeRecPrivate()
  {
    return makeRecPrivate;
  }

  public void setMakeRecPrivate( boolean makeRecPrivate )
  {
    this.makeRecPrivate = makeRecPrivate;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public int getClaimContributorFormBeansCount()
  {
    if ( claimContributorFormBeans == null || claimContributorFormBeans.isEmpty() )
    {
      return 0;
    }
    return claimContributorFormBeans.size();
  }

  public List<PurlContributorBean> getContributors()
  {
    return new ArrayList<>( claimContributorFormBeans.values() );
  }

  public void populateContributorMap( List<PurlContributorBean> contributors )
  {
    claimContributorFormBeans.clear();
    for ( int i = 0; i < contributors.size(); i++ )
    {
      claimContributorFormBeans.put( i, contributors.get( i ) );
    }
  }

  public PurlContributorBean getClaimContributorFormBeans( int index )
  {
    PurlContributorBean bean = claimContributorFormBeans.get( index );
    if ( bean == null )
    {
      bean = new PurlContributorBean();
      claimContributorFormBeans.put( index, bean );
    }
    return bean;
  }

  public void setPreselectedContributorsLocked( boolean preselectedContributorsLocked )
  {
    this.preselectedContributorsLocked = preselectedContributorsLocked;
  }

  public boolean isPreselectedContributorsLocked()
  {
    return preselectedContributorsLocked;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public String getRecipientNodeId()
  {
    return recipientNodeId;
  }

  public void setRecipientNodeId( String recipientNodeId )
  {
    this.recipientNodeId = recipientNodeId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlReturnUrl( String purlReturnUrl )
  {
    this.purlReturnUrl = purlReturnUrl;
  }

  public String getPurlReturnUrl()
  {
    return purlReturnUrl;
  }

  public void setPurlRecipient( PurlRecipient purlRecipient )
  {
    this.purlRecipient = purlRecipient;
  }

  public PurlRecipient getPurlRecipient()
  {
    return purlRecipient;
  }

  public List<CMMessage> getErrors()
  {
    return errors;
  }

  public void setErrors( List<CMMessage> errors )
  {
    this.errors = errors;
  }

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public String getClaimFormAsset()
  {
    return claimFormAsset;
  }

  public void setClaimFormAsset( String claimFormAsset )
  {
    this.claimFormAsset = claimFormAsset;
  }

  public Long getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public Long getClaimFormStepId()
  {
    return claimFormStepId;
  }

  public void setClaimFormStepId( Long claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  public Integer getAnniversaryYears()
  {
    return anniversaryYears;
  }

  public void setAnniversaryYears( Integer anniversaryYears )
  {
    this.anniversaryYears = anniversaryYears;
  }

  public Integer getAnniversaryDays()
  {
    return anniversaryDays;
  }

  public void setAnniversaryDays( Integer anniversaryDays )
  {
    this.anniversaryDays = anniversaryDays;
  }

  public void addClaimElementForms( Long promotionId, List<ClaimElementForm> claimFormStepElements )
  {
    if ( claimElementForms == null )
    {
      claimElementForms = new HashMap<>();
    }
    claimElementForms.put( promotionId, claimFormStepElements );
  }

  public Map<Long, List<ClaimElementForm>> getClaimElementForms()
  {
    return claimElementForms;
  }

  public int getClaimElementsCount()
  {
    if ( claimElements == null || claimElements.isEmpty() )
    {
      return 0;
    }
    return claimElements.size();
  }

  // named this way for Struts
  public ClaimElementForm getClaimElement( int index )
  {
    ClaimElementForm bean = claimElements.get( index );
    if ( bean == null )
    {
      bean = new ClaimElementForm();
      claimElements.put( index, bean );
    }
    return bean;
  }

  public List<ClaimElementForm> getClaimElementsList()
  {
    return new ArrayList<>( claimElements.values() );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List<ClaimElementForm> getSortedClaimElementsList( List<ClaimElementForm> sortedList )
  {
    if ( sortedList != null && !sortedList.isEmpty() )
    {
      Collections.sort( sortedList, new Comparator()
      {
        @Override
        public int compare( Object object, Object object1 )
        {
          ClaimElementForm message1 = (ClaimElementForm)object;
          ClaimElementForm message2 = (ClaimElementForm)object1;

          return message1.getClaimFormStepElementId().compareTo( message2.getClaimFormStepElementId() );
        }
      } );
    }

    return sortedList;
  }

  public Locale getCalendarLocale()
  {
    return UserManager.getLocale();
  }

  public String getCalendarStartDate()
  {
    return DateUtils.toDisplayString( new Date() );
  }

  public MethodMap getClaimElementValue()
  {
    if ( claimElementValue == null )
    {
      claimElementValue = new MethodMap()
      {
        @Override
        public Object get( Object key )
        {
          ClaimElementForm cef = (ClaimElementForm)key;

          ClaimElementForm match = createDefaultClaimElementForm();
          for ( ClaimElementForm claimElementForm : claimElements.values() )
          {
            if ( cef.getClaimFormStepElementId().equals( claimElementForm.getClaimFormStepElementId() ) )
            {
              match = claimElementForm;
              break;
            }
          }

          return match;
        }
      };
    }
    return claimElementValue;
  }

  private ClaimElementForm createDefaultClaimElementForm()
  {
    ClaimElementForm form = new ClaimElementForm();
    form.setValue( "" );

    AddressFormBean afb = new AddressFormBean();
    afb.setAddr1( "" );
    afb.setAddr2( "" );
    afb.setAddr3( "" );
    afb.setCity( "" );
    afb.setCountryCode( "" );
    afb.setCountryName( "" );
    afb.setPostalCode( "" );
    afb.setStateTypeCode( "" );

    form.setMainAddressFormBean( afb );

    return form;
  }

  public String getContributorTeamsSearchFilters()
  {
    return contributorTeamsSearchFilters;
  }

  public void setContributorTeamsSearchFilters( String contributorTeamsSearchFilters )
  {
    this.contributorTeamsSearchFilters = contributorTeamsSearchFilters;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getNavigationTabJSON()
  {
    return navigationTabJSON;
  }

  public void setNavigationTabJSON( String navigationTabJSON )
  {
    this.navigationTabJSON = navigationTabJSON;
  }

  public String getIdJson()
  {
    StringBuilder r = new StringBuilder( "{ " ).append( "promotionId:" ).append( promotionId ).append( "," ).append( " claimId:" ).append( claimId ).append( " , " ).append( " selectedNodeId:" )
        .append( nodeId ).append( " , " ).append( " recognitionEdit:" ).append( recognitionEdit ).append( " }" );

    return r.toString();
  }

  public CustomFormStepElementsView getCustomElements()
  {
    return customElements;
  }

  public void setCustomElements( CustomFormStepElementsView customElements )
  {
    this.customElements = customElements;
  }

  public String getIndividualOrTeam()
  {
    return individualOrTeam;
  }

  public void setIndividualOrTeam( String individualOrTeam )
  {
    this.individualOrTeam = individualOrTeam;
  }

  public String getInitializationJsonStr()
  {
    return "{\"formSetup\": " + getInitializationJson() + " }";
  }

  public void setInitializationJsonStr( String initializationJsonStr )
  {
    this.initializationJsonStr = initializationJsonStr;
  }

  public boolean isRecognitionEdit()
  {
    return recognitionEdit;
  }

  public void setRecognitionEdit( boolean recognitionEdit )
  {
    this.recognitionEdit = recognitionEdit;
  }

//Client customization for WIP #39189 starts
 public int getClaimUploadFormBeansCount()
 {
   if ( claimUploadFormBeans == null || claimUploadFormBeans.isEmpty() )
   {
     return 0;
   }
   return claimUploadFormBeans.size();
 }

 public List<TcccClaimFileValueBean> getClaimUploads()
 {
   return new ArrayList<>( claimUploadFormBeans.values() );
 }

 public void populateClaimUploadMap( List<TcccClaimFileValueBean> claimUploads )
 {
   claimUploadFormBeans.clear();
   for ( int i = 0; i < claimUploads.size(); i++ )
   {
     claimUploadFormBeans.put( i, claimUploads.get( i ) );
   }
 }

 public TcccClaimFileValueBean getClaimUploadFormBeans( int index )
 {
   TcccClaimFileValueBean bean = claimUploadFormBeans.get( index );
   if ( bean == null )
   {
     bean = new TcccClaimFileValueBean();
     claimUploadFormBeans.put( index, bean );
   }
   return bean;
 }

 /*
 public void setUploadDescription( Map<Integer, String> uploadDescription )
 {
   this.uploadDescription = uploadDescription;
 }

 public Map<Integer, String> getUploadDescription()
 {
   return uploadDescription;
 }

 public void setUploadDescription( int index, String uploadDesc )
 {
   this.uploadDescription.put( index, uploadDesc );
 }

 public String getUploadDescription( int index )
 {
   String uploadDesc = uploadDescription.get( index );
   if ( uploadDesc == null )
   {
     uploadDesc = new String();
     uploadDescription.put( index, uploadDesc );
   }
   return uploadDesc;
 }

 public void setUploadUrl( Map<Integer, String> uploadUrl )
 {
   this.uploadUrl = uploadUrl;
 }

 public Map<Integer, String> getUploadUrl()
 {
   return uploadUrl;
 }

 public void setUploadUrl( int index, String uploadUrl )
 {
   this.uploadUrl.put( index, uploadUrl );
 }

 public String getUploadUrl( int index )
 {
   String url = uploadUrl.get( index );
   if ( url == null )
   {
     url = new String();
     uploadUrl.put( index, url );
   }
   return url;
 }
 */
 // Client customization for WIP #39189 ends
}
