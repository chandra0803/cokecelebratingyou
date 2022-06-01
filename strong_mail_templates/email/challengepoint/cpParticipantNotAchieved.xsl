<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   ##cp_header_css_image##
	<!-- start template -->
	<div>Dear <strong>##first_name## ##last_name##</strong>,<br /><br />
	
	##program_name## FINAL PROGRESS UPDATE
	
	<xsl:if test="/StrongMail/notAchieved!=''">			
		Thank you for participating in the ##program_name## program. Your final results as reported to us were ##actualResults##. 
		This was below your earnings starting point of ##threshold## and, as a result, you did not qualify for any earnings from the program.
	<br/><br/>					
	</xsl:if>

	<xsl:if test="/StrongMail/hasAward!=''">			
		Thank you for participating in the ##program_name## program. Your final results as reported to us were ##actualResults##. 
		Per these results you have earned Points by passing your earnings starting point!<br /><br/>
		The final results show that you have achieved ##actualResults##. Per the program rules this means you earned a total of
		##primaryEarnings## . ##interimAmount## Points were deposited in your account at an earlier date(s). The remaining ##remainingPoints## Points were deposited in your account on ##calculationapprovedate##.<br /><br/>
		Regarding your selected Challengepoint of ##challengepointLevel##, your final results were below the target and you did not qualify for any earnings from this portion of the program.<br /><br/>

	<br/><br/>					
	</xsl:if>

	
We would like to thank you for your efforts and wish you all the best. If you disagree with the final results we received please contact your manager directly.

Thank you again for your participation.
	
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



