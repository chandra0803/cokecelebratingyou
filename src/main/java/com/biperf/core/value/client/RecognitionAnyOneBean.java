package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.RecognitionBean.DrawToolSettings;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public final class RecognitionAnyOneBean {

	private boolean ecardsActive = true;
	private boolean bannersActive =true;
	private boolean messageActive = true;
	private String bannerImage="img/recognizeAnyone/recognizeAnyone.jpg";
	private List<RecipientAnyoneBean> invitees = new ArrayList<RecipientAnyoneBean>();
	private List<EcardBean> eCards = new ArrayList<EcardBean>();
	private DrawToolSettings drawToolSettings;
	private String senderEmail;

	public RecognitionAnyOneBean(String senderEmail,List allCardList , String siteUrlPrefix){
		RecipientAnyoneBean recipientAnyoneBean = new RecipientAnyoneBean();
		recipientAnyoneBean.setEmailId("");
		recipientAnyoneBean.setFirstName("");
		recipientAnyoneBean.setLastName("");
		this.senderEmail = senderEmail;
		
		invitees.add(recipientAnyoneBean);
		 if ( allCardList != null && allCardList.size() > 0 )
		    {
			 int i = 0;
		      Iterator cardListIterator = allCardList.iterator();
		      while ( cardListIterator.hasNext() )
		      {
		    	  	if(i==9) break;
			        Card cardImage = (Card)cardListIterator.next();
			        eCards.add(new EcardBean(siteUrlPrefix,cardImage.getId(), cardImage.getName(),cardImage.getSmallImageName(),cardImage.getLargeImageName(),"card",true));
			        i++;

		      }
		    }
		 drawToolSettings = new DrawToolSettings( true, true );
	}

	
	public boolean isEcardsActive() {
		return ecardsActive;
	}

	public void setEcardsActive(boolean ecardsActive) {
		this.ecardsActive = ecardsActive;
	}

	public boolean isBannersActive() {
		return bannersActive;
	}

	public void setBannersActive(boolean bannersActive) {
		this.bannersActive = bannersActive;
	}

	public boolean isMessageActive() {
		return messageActive;
	}

	public void setMessageActive(boolean messageActive) {
		this.messageActive = messageActive;
	}

	public String getBannerImage() {
		return bannerImage;
	}

	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}

	public List<EcardBean> geteCards() {
		return eCards;
	}

	public void seteCards(List<EcardBean> eCards) {
		this.eCards = eCards;
	}

	public DrawToolSettings getDrawToolSettings() {
		return drawToolSettings;
	}

	public void setDrawToolSettings(DrawToolSettings drawToolSettings) {
		this.drawToolSettings = drawToolSettings;
	}

	public List<RecipientAnyoneBean> getInvitees() {
		return invitees;
	}

	public void setInvitees(List<RecipientAnyoneBean> invitees) {
		this.invitees = invitees;
	}

	
	
	public String getSenderEmail() {
		return senderEmail;
	}


	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}



	public static class EcardBean extends NameableBean
	  {
	    private String smallImage;
	    private String largeImage;
	    private String cardType;
	    private boolean canEdit;
	    
	    EcardBean(String siteUrlPrefix, Long id, String name ,String smallImage,String largeImage,String cardType,boolean canEdit){
	    	
	    	setId(id);
	    	setName(name);
	        this.smallImage = siteUrlPrefix + ECard.CARDS_FOLDER + smallImage;
	        this.largeImage = siteUrlPrefix + ECard.CARDS_FOLDER + largeImage;
	    	this.cardType = cardType;
	    	this.canEdit = canEdit;
	    }
		public String getSmallImage() {
			return smallImage;
		}
		public String getLargeImage() {
			return largeImage;
		}
		public String getCardType() {
			return cardType;
		}
		public boolean isCanEdit() {
			return canEdit;
		}
	  }
	
}
