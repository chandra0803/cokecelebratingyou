<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   ##cp_header_css_image##
	<!-- start template -->
	<div>Dear <strong>##first_name## ##last_name##</strong>,<br /><br />
	
	##program_name## FINAL PROGRESS UPDATE<br/><br/>
	
	<xsl:if test="/StrongMail/MGR!=''">			
		Thank you for participating in the ##program_name## program.
			<xsl:if test="/StrongMail/AWARD_PERQS!=''">  
				You have earned ##totalChallengepointValue## Points for participating in the ##program_name## Challengepoint Program
			</xsl:if>
	<br/><br/>					
	</xsl:if>
	
	<xsl:if test="/StrongMail/NONMGR!=''">			
	Thank you for participating in the ##program_name## program. Your final results as reported to us were ##actualResults##. Per these results you have earned Points both by passing your earnings starting point and by achieving your personally selected Challengepoint!
	The final results show that you have achieved ##actualResults##. Per the program rules this means you earned a total of ##primaryEarnings## in the basic portion of the program. ##interimAmount## Points were deposited in your account at an earlier date(s). The remaining ##remainingPoints## Points were deposited in your account on ##calculationapprovedate##.
	
			<xsl:if test="/StrongMail/AWARD_PERQS!=''">  
				In addition, you achieved your selected Challengepoint of ##challengepointLevel##.
				This means you have earned an additional ##rewardAmount## Points!
			</xsl:if>
			<xsl:if test="/StrongMail/MERCHANDISETRAVELTYPE!=''">  
				In addition, you achieved your selected Challengepoint of ##challengepointLevel##. To confirm your award choice and place your Challengepoint award order please click on the link provided below. Simply log into the website and 
				you will be taken to an order confirmation page. Merchandise orders must be place no later than admin type in order end date!
			</xsl:if>			
	<br/><br/>	
	
		In summary, your program earnings are:
	
		Total Points earned in the basic awards portion   ##primaryEarnings##
			<xsl:if test="/StrongMail/AWARD_PERQS!=''">  
				Points earned for Challengepoint achievement   ##rewardAmount##	<br/><br/>
				
				Total Points Earnings  ##totalRewardAmount##	<br/><br/>
			</xsl:if> 

	Previous Points deposits   ##totalPreviousDeposit## 	<br/><br/>
	
	Points deposited on ##calculationapprovedate##. ##totalRewardAmount##	<br/><br/>
						
	</xsl:if>
		
	We would like to thank you for your efforts and wish you all the best. If you disagree with the final results we received please contact your manager directly.	<br/><br/>
	
	Thank you again for your participation.	<br/><br/>
	
	
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/SITE_LINK"/>
		</xsl:attribute>
		click here to access the website 	<br/><br/>				
	</xsl:element>
						
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/CONTACT_US_URL"/>
		</xsl:attribute>
		If you have any questions please click here to contact the Program Administrator<br /><br/>				
	</xsl:element>
		
	Sincerely,<br /><br />The <strong>##program_name##</strong> Challengepoint Team!</div>
	<br />
 	<!-- end template -->	
   ##cp_footer1## 						
  </xsl:template>
</xsl:stylesheet>



