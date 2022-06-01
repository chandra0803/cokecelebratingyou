<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   ##header_css_image##

						Dear ##first_name##,<br/><br/>
						
						Welcome to GoalQuest!<br/><br/>
						
						Now is the time to challenge yourself with ##program_name## -- only you know what you can do!  In order to participate in ##program_name## you will need to visit the GoalQuest web site and select your personal goal for this program.<br/><br/>
						
						<xsl:element name="a">
							<xsl:attribute name="href">
								<xsl:value-of select="/StrongMail/SITE_LINK"/>
							</xsl:attribute>
							Click here to access the website. 					
						</xsl:element>
						To access the GoalQuest website you will need to enter your unique login ID and Password that you were supplied with during the last program you participated in. If you have forgotten this information, please access the website and click on either the "forgot user id" or the "forgot password" option. Please retain this information in a secure place for future use.<br/><br/>
						
						In order to participate in ##program_name## you must select your personal goal. If you do not select a goal you will not be eligible for any awards in this program.  Selection of goals will begin ##registration_start_date## and you must submit your goal before ##registration_end_date##. The program contest period will begin on ##promotion_start_date##
						<xsl:if test="/StrongMail/PROMOTION_END_DATE!= ''">			
							and will end on ##promotion_end_date##
						</xsl:if>
						.  All eligible activities must occur during these official program dates.<br/><br/>
						
						
						Once at the GoalQuest website you will be able to pick your personal goal from the three choices listed below.<br/><br/>
						<xsl:if test="/StrongMail/BASE!= ''">
					 		Your base is: ##base##.<br/><br/>
					 	</xsl:if>
						Your goal levels and available rewards are:<br/><br/>
						<xsl:if test="/StrongMail/GOAL_LEVEL1!= ''">			
							* Goal Level 1 the performance required to achieve this goal is ##goal_level1##, which equals ##goal_value1##
								<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
									and the reward is ##goal_reward1##
								</xsl:if>
								.
						<br/><br/>					
						</xsl:if>
			
						<xsl:if test="/StrongMail/GOAL_LEVEL2!= ''">	
							* Goal Level 2 the performance required to achieve this goal is ##goal_level2##, which equals ##goal_value2##
								<xsl:if test="/StrongMail/AWARD_PERQS!= ''"> 
									and the reward is ##goal_reward2##
								</xsl:if>
								.
						<br/><br/>					
						</xsl:if>
						<xsl:if test="/StrongMail/GOAL_LEVEL3!= ''">
							* Goal Level 3 the performance required to achieve this goal is ##goal_level3##, which equals ##goal_value3##
							<xsl:if test="/StrongMail/AWARD_PERQS!= ''">
								and the reward is ##goal_reward3##
							</xsl:if>
							.
						<br/><br/>				
						</xsl:if>
						<xsl:if test="/StrongMail/GOAL_LEVEL4!= ''">
							* Goal Level 4 the performance required to achieve this goal is ##goal_level4##, which equals ##goal_value4##
							<xsl:if test="/StrongMail/AWARD_PERQS!= ''"> 
								and the reward is ##goal_reward4##
							</xsl:if>
							.
						<br/><br/>				
						</xsl:if>
			
						<xsl:if test="/StrongMail/GOAL_LEVEL5!= ''">
							* Goal Level 5 the performance required to achieve this goal is ##goal_level5##, which equals ##goal_value5##
							<xsl:if test="/StrongMail/AWARD_PERQS!= ''">
								and the reward is ##goal_reward5##
							</xsl:if>
							.
							<br/><br/>
						</xsl:if>
			
						 The GoalQuest web site also features links to your program rules and progress updates.
						<xsl:if test="/StrongMail/AWARD_PERQS!= ''"> 
							The website will also provide information about Points if you are not familiar with this popular award media.
						</xsl:if> PLEASE READ the RULES at the website before you select your goal so that you make the best goal selection for YOU!<br/><br/>
						
						If you have any questions or comments, please 
						<xsl:element name="a">
							<xsl:attribute name="href">
								<xsl:value-of select="/StrongMail/CONTACT_URL"/>
							</xsl:attribute>
							click here  					
						</xsl:element>
						to contact program administration or email us at: goalquest@biworldwide.com.<br/><br/>
						
						Best wishes and good luck!<br/><br/>
						
						Sincerely,<br/><br/>
						The ##program_name## GoalQuest Team!<br/><br/><br/><br/>
      ##footer1## 						

  </xsl:template>
</xsl:stylesheet>



