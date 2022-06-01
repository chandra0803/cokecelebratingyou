<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   ##cp_header_css_image##
	<!-- start template -->
	<div>Dear <strong>##first_name## ##last_name##</strong>,<br /><br />
	Welcome to Challengepoint!<br /><br />
	Now is the time to challenge yourself with <strong>##program_name##</strong> -- only you know what you can do! In order to participate in 
	<strong>##program_name##</strong> you will need to visit the Challengepoint website and select your personal Challengepoint for this program.<br /><br />
	
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/SITE_LINK"/>
		</xsl:attribute>
		Click here to access the website. 					
	</xsl:element>
	
	<br /><br />To access the Challengepoint website you will need to enter your unique Login ID and Password that you were supplied with during the last 
	program you participated in. If you have forgotten this information, please access the website and click on either the "forgot user id" or the 
	"forgot password" option. <br /><br />In order to participate in <strong>##program_name##</strong> you must select your personal Challengepoint. If you 
	do not select a Challengepoint you will not be eligible for any awards in this program. Selection of Challengepoints will begin <strong>##registration_start_date## </strong>
	and you must submit your&nbsp;Challengepoint before <strong>##registration_end_date##</strong>. The program contest period will begin on 
	<strong>##promotion_start_date##</strong> and will end on <strong>##promotion_end_date##</strong>. All eligible activities must occur during these official 
	program dates.<br /><br />Once at the Challengepoint website you will be able to pick your personal Challengepoint from the three choices listed 
	below.<br />
	
	<xsl:if test="/StrongMail/BASE!= ''">
	Your base is:<strong> ##base##</strong>.
	</xsl:if>
	<br />Your Challengepoint levels and available rewards are:<br /><br />
	
	<xsl:if test="/StrongMail/challengepoint1!= ''">			
	* Challengepoint Level 1 the performance required to achieve this Challengepoint is <strong>##challengepointLevel1##,</strong> which equals <strong>
	##challengepointValue1##</strong>
			<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
				and the reward is <strong>${challengepointReward1}</strong>.
			</xsl:if>
	<br/><br/>					
	</xsl:if>
						
	<xsl:if test="/StrongMail/challengepoint2!= ''">			
	* Challengepoint Level 2 the performance required to achieve this Challengepoint is <strong>##challengepointLevel2##,</strong> which equals <strong>
	##challengepointValue2##</strong>
			<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
				and the reward is <strong>${challengepointReward2}</strong>.
			</xsl:if>
	<br/><br/>					
	</xsl:if>
	
	
	<xsl:if test="/StrongMail/challengepoint3!= ''">			
	* Challengepoint Level 3 the performance required to achieve this Challengepoint is <strong>##challengepointLevel3##,</strong> which equals <strong>
	##challengepointValue3##</strong>
			<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
				and the reward is <strong>${challengepointReward3}</strong>.
			</xsl:if>
	<br/><br/>					
	</xsl:if>
	
	
	The Challengepoint website features links to your program rules and progress updates.<br />
	
	<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
		The website will also provide information about Points if you are not familiar with this popular award media.		
	</xsl:if> 
	
	PLEASE READ the RULES 
	at the website before you select your Challengepoint so that you make the best Challengepoint selection for YOU!<br /><br />
	
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/SITE_LINK"/>
		</xsl:attribute>
		click here to access the website 					
	</xsl:element>
						
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/CONTACT_URL"/>
		</xsl:attribute>
		If you have any questions please click here to contact the Program Administrator<br />				
	</xsl:element>
							

	Best wishes and good luck!<br /><br />Sincerely,<br /><br />The <strong>##program_name##</strong> Challengepoint Team!</div>
	<br />
 	<!-- end template -->	
   ##cp_footer1## 						
  </xsl:template>
</xsl:stylesheet>



