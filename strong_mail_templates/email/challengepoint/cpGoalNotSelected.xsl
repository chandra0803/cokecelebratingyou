<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   ##cp_header_css_image##
			
						Dear ##first_name## ##last_name##,<br/><br/>
						IMPORTANT! PLEASE READ<br/><br/>
						The time period for you to select your Challengepoint for the <strong>##promotion_name##</strong> program is ending in just a few days! In order to participate in the program you must select your Challengepoint by midnight (central time zone) on <strong>##cp_selection_end_date##</strong>.
						<strong>If no Challengepoint is selected by that time you will not be eligible for any awards.</strong><br /><br />
						
						<xsl:element name="a">
							<xsl:attribute name="href">
								<xsl:value-of select="/StrongMail/SITE_LINK"/>
							</xsl:attribute>
							Click here to access the website				
						</xsl:element> 
						<br/><br/>
						
						<xsl:element name="a">
							<xsl:attribute name="href">
								<xsl:value-of select="/StrongMail/CONTACT_US_URL"/>
							</xsl:attribute>
							If you have any questions please click here to contact the Program Administrator			
						</xsl:element>
						<br/><br/>						
						
						Best wishes and good luck!<br/><br/>
						
						Sincerely,<br/><br/>
						
						The ##promotion_name## Challengepoint Team!<br/><br/>
			
						<br/><br/>
      <!-- end template -->	
   ##cp_footer1## 						
  </xsl:template>
</xsl:stylesheet>


