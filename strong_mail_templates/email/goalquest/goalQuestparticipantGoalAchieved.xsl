<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
		##header_css_image## 

						Dear ##first_name## ##last_name##,<br/><br/>
			
						CONGRATULATIONS!<br/><br/>
			
						<xsl:if test="/StrongMail/MGR = 'true'">
							<xsl:choose>
								<xsl:when test="/StrongMail/AWARD_PERQS!= ''"> 
						         You have earned ##reward_amount## Points for participating in the ##program_name## GoalQuest Program.
								 To spend your points or for more information visit the ##program_name## 
									<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="/StrongMail/SITE_LINK"/>
										</xsl:attribute>
										website				
									</xsl:element>
								, and click on Shop menu.								 
							   </xsl:when>
							   <xsl:otherwise>
									You have earned your choice of one item from the ##goal_level## catalog for participating in the ##program_name## GoalQuest Program.<br/><br/>
									To spend your points or for more information visit the ##program_name##
									<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="/StrongMail/SITE_LINK"/>
										</xsl:attribute>
										website				
									</xsl:element>
								, and click on Confirm and order your award.
						       </xsl:otherwise>
							</xsl:choose>
						</xsl:if>
			
						<xsl:if test="/StrongMail/MGR = ''">			
							At the start of the ##program_name## incentive program you selected a personal goal at the program website. You selected ##goal_level## that had an achievement requirement of ##goal_level_amount## which equals ##total_goal_value##.<br/><br/>
							
							The final results reported to us show that you have achieved ##actual_results## which is ##percent_to_goal## % of your personal goal!!
							
							<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  Your reward will be ##reward_amount## in Points.<br/><br/>
							
									<xsl:if test="/StrongMail/PARTNER_NAMES != ''">
										Because of your goal achievement,the following partners will be eligible to receive Points.
										##partner_names##. <br/><br/>
									</xsl:if>
			
							
							To spend your points or for more information visit the ##program_name##
							
									<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="/StrongMail/SITE_LINK"/>
										</xsl:attribute>
										website				
									</xsl:element> 
							, and click on Shop menu.
							</xsl:if>				
							<xsl:if test="/StrongMail/AWARD_PERQS = ''"> 
							Your reward will be your choice of one item from the ##goal_level## catalog.<br/><br/>
							In order to confirm your item selection and shipping information:<br/><br/>
							<ul>
							<li>Return to the GoalQuest website. To get to the web site, simply click on this
													<xsl:element name="a">
														<xsl:attribute name="href">
															<xsl:value-of select="/StrongMail/SITE_LINK"/>
														</xsl:attribute>
														link				
													</xsl:element>
							or type in ##site_link## </li>
							<li>Click on Confirm and order your award.</li>
							<li>Follow the instructions for selecting your award and confirming the Shipping address.</li>
							<li>Your award must come from the catalog Level you selected for your program.</li>
							</ul>
							
							<br/>
							</xsl:if>
						</xsl:if>
			
			
						Thank you for helping to make the ##program_name## a success!<br/><br/>
						
						Sincerely,<br/><br/>
						
						The ##program_name## GoalQuest Team
						<br/><br/><br/><br/>
      ##footer1## 
  </xsl:template>
</xsl:stylesheet>

