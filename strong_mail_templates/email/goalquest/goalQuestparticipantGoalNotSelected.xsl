<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
		##header_css_image## 
			
						Dear ##first_name## ##last_name##,<br/><br/>
						IMPORTANT! PLEASE READ<br/><br/>
						The goal selection period for your ##program_name## program will be ending soon! In order to participate in the program you must select your goal by midnight (central time zone) on ##goal_selection_end_date##. If no goal is selected by that time you will not be eligible for any awards.<br/><br/>
						
						If you have any questions or comments, Please email us at:goalquest@biworldwide.com<br/><br/>
						
						
									<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="/StrongMail/SITE_LINK"/>
										</xsl:attribute>
										Click here to access the website				
									</xsl:element> 
						
						<br/><br/>
						
						Best wishes and good luck!<br/><br/>
						
						Sincerely,<br/><br/>
						
						The ##program_name## GoalQuest Team!<br/><br/>
			
						<br/><br/>
      ##footer1## 
  </xsl:template>
</xsl:stylesheet>


