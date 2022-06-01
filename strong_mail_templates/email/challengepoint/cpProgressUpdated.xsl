<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   ##cp_header_css_image##
	<!-- start template -->
	<div>Dear <strong>##first_name## ##last_name##</strong>,<br /><br />
	##program_name## PROGRESS UPDATE!<br /><br />

	At the start of the ##program_name## program you selected a personal Challengepoint at the program website.

	<xsl:if test="/StrongMail/challengepointLevel!=''">			
		You selected Level ##challengepointLevel## that had an achievement requirement of ##challengepointLevelDescription## which equals ##totalChallengepointValue##.
	<br/><br/>					
	</xsl:if>
	
	If you achieve your Challengepoint by the end of the program on ##promotion_end_date## you will receive ##challengepointLevelAward## Points in addition to Points earned at the rate of ##primaryAwardIncrement# Points per ##primaryAward##. 
 
	As of ##date_1## results reported to us show that you have achieved ##actual_results##

	<xsl:if test="/StrongMail/challengepointLevel!=''">			
		which is ##percentToChallengepoint## % of your personal Challengepoint.
	<br/><br/>					
	</xsl:if>
	
	For more information visit the ##program_name## website and click on View Progress Details on the Home Page.
	
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/SITE_LINK"/>
		</xsl:attribute>
		click here to access the website 					
	</xsl:element><br/><br/>
	
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/CONTACT_US_URL"/>
		</xsl:attribute>
		If you have any questions, please click here to contact Program Administrator
	</xsl:element>
	<br/><br/>
		
	Good luck during the remainder of the program!<br/><br/>

	Sincerely,<br /><br />The <strong>##program_name##</strong> Challengepoint Team!</div>
	<br />
 	<!-- end template -->	
   ##cp_footer1## 						
  </xsl:template>
</xsl:stylesheet>



