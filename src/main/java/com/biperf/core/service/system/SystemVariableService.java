/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/system/SystemVariableService.java,v $
 */

package com.biperf.core.service.system;

import java.util.List;

import com.biperf.core.dao.system.SystemVariableDAO;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

public interface SystemVariableService extends SAO
{
  /**
   * name referenced in Spring applicationConfig.xml
   */
  public static String BEAN_NAME = "systemVariableService";

  /**
   * PID
   */
  public static String PROJECT_ID = "project.id";

  /**
   * is product claims module installed?
   */
  public static String INSTALL_PRODUCTCLAIMS = "install.productclaims";

  /**
   * is recognition installed?
   */
  public static String INSTALL_RECOGNITION = "install.recognition";

  /**
   * is quizzes installed?
   */
  public static String INSTALL_QUIZZES = "install.quizzes";

  /**
   * is SSI installed?
   */
  public static String INSTALL_SSI = "install.ssi";

  /**
   * is quizzes installed?
   */
  public static String INSTALL_ENGAGEMENT = "install.engagement";

  /**
   * is nominations installed?
   */
  public static String INSTALL_NOMINATIONS = "install.nominations";

  /**
   * is SURVEY installed?
   */
  public static String INSTALL_SURVEYS = "install.survey";

  /**
   * is GOALQUEST installed?
   */
  public static String INSTALL_GOAL_QUEST = "install.goalquest";

  /**
   * is CHALLENGEPOINT installed?
   */
  public static String INSTALL_CHALLENGEPOINT = "install.challengepoint";

  /**
   * is THROWDOWN installed?
   */
  public static String INSTALL_THROWDOWN = "install.throwdown";

  /**
   *
   * client name
   */
  public static String CLIENT_NAME = "client.name";

  /**
  *
  * client name
  */
  public static String CLIENT_PREFIX = "import.client.prefix";

  /**
   * client website url to access Beacon
   */
  public static String CLIENT_URL = "client.url";

  /**
   * contact us page url
   */
  public static String CLIENT_CONTACT_URL = "client.contact.url";

  /**
   * system variables name to look up the email address which the contact us screen will use as a
   * destination address
   */
  public static final String CONTACT_US_EMAIL_ADDRESS_KEY = "contact.us.email";

  /**
   * system variable name of the specified pattern to use when generating a default random password
   */
  public static final String PASSWORD_PATTERN = "password.pattern";

  /**
   * system variable name of where to find the minimum password length
   */
  public static final String PASSWORD_MIN_LENGTH = "password.min.length";

  /**
   * system variable name - true/false whether or not we should check the password for mixed
   * character sets (i.e. some mixture of lower case, upper case, numbers, punctuation)
   */
  public static final String PASSWORD_SHOULD_USE_REGEX = "password.should.use.regex";

  /**
   * system variable name of where to find the regex for a pattern the password should *not* match
   */
  public static final String PASSWORD_MUST_NOT_MATCH_REGEX = "password.not.match.regex";

  /**
   * system variable name of the variations of 'password' not allowed for password
   */
  public static final String PASSWORD_VARIATIONS_NOT_ALLOWED = "password.variations.notallowed";

  /**
   * system variable name for the password expired period
   */
  public static final String PASSWORD_EXPIRED_PERIOD = "password.expired.period";

  public static final String PASSWORD_LOCKOUT_DURATION_MINUTES = "password.lockout.timeout.minutes";

  /**
   * The minimum number of distinct character types need for a password (upper, lower, number,
   * special characters)
   */
  public static final String PASSWORD_DISTINCT_CHARACTER_TYPES = "password.num.of.char.types.required";

  /**
   * A comma separated list of character classes available. I.e. if it is upper,lower,number then special characters
   * are not required
   */
  public static final String PASSWORD_CHARACTER_TYPES_AVAILABLE = "password.character.types.available";

  /**
   * system variable name for the password force reset
   */
  public static final String PASSWORD_FORCE_RESET = "password.force.reset";

  /**
   * system variable name of where to find true/false if users can re-use their last password when
   * doing a change password
   */
  public static final String PASSWORD_CAN_REUSE = "password.can.reuse";

  public static final String HOMEPAGE_GENERICBANNER_AD = "home.genericbanners";

  public static final String AWARDBANQ_SOURCE_SYSTEM = "awardbanq.source.system";
  public static final String AWARDBANQ_SOURCE_SYSTEM_USER_ID = "awardbanq.realtime.userid";
  public static final String AWARDBANQ_ORGANIZATION_NUMBER = "awardbanq.organization.number";
  public static final String AWARDBANQ_MODE = "awardbanq.mode";
  public static final String AWARDBANQ_CONVERTCERT_IS_USED = "awardbanq.convertcert.used";
  public static final String AWARDBANQ_DEPOSIT_RETRY_COUNT = "awardbanq.deposit.retry.count";
  public static final String AWARDBANQ_DEPOSIT_RETRY_DELAY = "awardbanq.deposit.retry.delay";

  public static final String SHOPPING_INTERNAL_REMOTE_URL_PREFIX = "shop.internalremoteurl";
  public static final String GOALQUEST_MERCHLINQ_DEST_ENV_PREFIX = "goalquest.merchlinqdestenv";
  public static final String GOALQUEST_MERCHLINQ_SSO_URL_PREFIX = "goalquest.merchlinqsso.url";

  public static final String WEBSERVICES_URL_PREFIX = "webservices.url";

  public static final String MACHINE_LANGUAGE_ALLOW_TRANSLATION = "machine.language.allow.translation";

  public static final String SITE_URL_PREFIX = "site.url";
  /**
   * system variable name that specifies the login failure count for User Lockout
   */
  public static final String LOCKOUT_FAILURE_COUNT = "lockout.failure.count";

  public static final String BOOLEAN_INCLUDE_BALANCE = "boolean.include.balance";

  public static final String PARTICIPANT_ALLOW_ESTATEMENTS = "participant.allow.estatements";
  public static final String PARTICIPANT_ALLOW_CONTACTS = "participant.allow.contacts";

  public static final String DISCRETIONARY_AWARD_MIN = "discretionary.award.min";
  public static final String DISCRETIONARY_AWARD_MAX = "discretionary.award.max";

  // System variable to show hide leaderboard module
  public static final String LEADERBOARD_SHOW_HIDE = "install.leaderboard";

  // System variable to show default dashboard charts

  public static final String DEFAULT_DASHBOARD_CHARTS = "default.dashboard.charts";

  public static final String NOMINATION_REPORT_COMMENT_AVAILABLE = "nomination.report.comment.available";

  /**
   * system variable name to check if batch processing of claims are allowed
   */
  public static final String CLAIM_PROCESSING_ALLOW_BATCH = "claim.processing.allow.batch";

  /**
   * The name of the system variable whose value is the date on which a particular client's version
   * of this application was launched.
   */
  public static final String CLIENT_LAUNCH_DATE = "client.launch.date";

  /**
   * system variable name that indicates whether or not to enable ip-based access restrictions.
   * (should be true/false)
   */
  public static final String SHOULD_RESTRICT_ACCESS_BY_IP = "should.restrict.by.ip";

  /**
   * if should.restrict.by.ip is true, this is a comma separated list of IPs to allow, in the form
   * of regular expressions. I.E. 172.16.*,208.51.23.2,10.*
   */
  public static final String IPS_TO_ALLOW_CSV_REGEX = "ips.to.allow.csv.regex";

  /**
   * default mask for Pax SSNs - options are VIEW_NONE, VIEW_MASK or VIEW_FULL
   */
  public static final String SSN_DEFAULT_VIEW = "ssn.default.view";

  public static final String ESCALATION_HIERARCHY_ID = "escalation.hierarchy.id";

  // To fix the bug # 20668
  public static final String IMPORT_PAGE_SIZE = "import.page.size";

  /**
   * System receiver email address
   */
  public static final String SYSTEM_EMAIL_ADDRESS = "email.address.system";

  /**
   * System sender email address
   */
  public static final String SYSTEM_SENDER_EMAIL_ADDRESS = "email.notification.senderaddr";

  /**
   * Additional System email address
   */
  public static final String SYSTEM_EMAIL_ADDRESS_ADDL = "email.address.system.addl";

  /**
   * Incentive email address (the address from which user e-mails will be sent from)
   */
  public static final String SYSTEM_EMAIL_ADDRESS_INCENTIVE = "email.address.system.incentive";

  /**
   * Subject prefix (to distinguish e-mails during testing)
   */
  public static final String SUBJECT_PREFIX = "email.subject.prefix";

  /**
   * Subject prefix (to distinguish e-mails during testing)
   */
  public static final String SUBJECT_PREFIX_DISPLAY = "email.subject.prefix.display";

  /**
   * System "personal" display name - the user friendly string displayed in From field in e-mails
   */
  public static final String SYSTEM_PERSONAL_DISPLAY_NAME = "email.address.system.personaldisplay";

  /**
   * Incentive "personal" display name - the user friendly string displayed in From field in e-mails
   * This will be used if none is otherwise specified
   */
  public static final String INCENTIVE_PERSONAL_DISPLAY_NAME = "email.address.incentive.personaldisplay";

  /**
   * If true, the Bounce Back Email Verification process has verified that the email address
   * specified in the system variable has replied to us indicating it is a deliverable email address
   */
  public static final String BOUNCEBACK_EMAIL_VERIFIED = "email.bounceback.verified";

  /**
   * Prepended to HTML emails to put a client wrapper around the text body.
   */
  public static final String EMAIL_WRAPPER_HEADER = "email.wrapper.header";

  /**
   * Appended to HTML emails to put a client wrapper around the text body.
   */
  public static final String EMAIL_WRAPPER_FOOTER = "email.wrapper.footer";

  /**
   * Used to control whether the client program requires participants to accept Terms & Conditions
   * before accessing the site
   */
  public static final String TERMS_CONDITIONS_USED = "termsAndConditions.used";

  /**
   * Used to control whether client required participant email address upon login for the first time
   */
  public static final String FIRST_TIME_LOGIN_REQUIRED_EMAIL = "firstTimeLogin.required.email";

  /**
   * Used to control whether a User (e.g. admin) can accept the Terms & Conditions on behalf of the
   * Participant
   */
  public static final String TERMS_CONDITIONS_USER_CAN_ACCEPT = "termsAndConditions.usercanaccept";

  /**
   * Used to control whether the Terms & Conditions link (which takes you to the view only version
   * of the Terms & Conditions page) should be displayed on the Login page
   */
  public static final String TERMS_CONDITIONS_DISPLAY_LOGIN = "termsAndConditionsView.display";

  // BugFix 17933
  /**
   * Login page "personal" display name - the user friendly welocome string displayed in the login page
  */
  public static final String LOGIN_PAGE_PERSONAL_DISPLAY_NAME = "login.welcome.message";

  /**
   * Used to see if it is  Email Batch Enabled
   */
  public static final String EMAIL_BATCH_ENABLE = "email.batch.enable";

  /**
   * system variable name for the strongmail
   */
  public static final String EMAIL_USE_STRONGMAIL = "email.use.strongmail";

  public static final String STRONGMAIL_ORG_NAME = "strongmail.organization.name";

  public static final String STRONGMAIL_SUB_ORG_ID = "strongmail.sub.organization.id";

  public static final String STRONGMAIL_USER_NAME = "strongmail.user.name";

  public static final String STRONGMAIL_PASSWORD = "strongmail.password.name";

  /**
   * Merchlinq ordering configuration.
   */
  public static final String MERCHLINQ_ORDER_URL_PREFIX = "merchlinq.url";
  public static final String MERCHLINQ_ORDER_PHONE = "merchorder.phone";

  // bugfix 17593
  public static final String SELF_ENROLL_ALLOWED = "self.enrollment.enabled";

  public static final String DISPLAY_TABLE_MAX_PER_SINGLE_PAGE = "display.table.max.per.single.page";
  public static final String DISPLAY_TABLE_MAX_PER_MULTI_PAGE = "display.table.max.per.multi.page";

  public static final String MERCHANDISE_BILLING_CODE_CHAR = "merchandise.billing.code.char";

  public static final String AWARDBANQ_PAX_UPDATE_PROCESS_BATCH_SIZE = "awardbanq.pax.update.process.batch.size";

  public static final String MESSENGER_APPLICATION_CODE = "messenger.app.code";

  public static final String SECONDARY_LOGO_ENABLE = "secondary.logo.enable";

  public static final String GOOGLE_ANALYTICS_ACCOUNT = "google.analytics.account";
  public static final String FILELOAD_ADC_TOKEN = "fileload.token";

  // External SSO
  public static final String SSO_UNIQUE_ID = "sso.unique.id";
  public static final String SSO_TIME_LAG_ALLOWED = "sso.timelag.allowed";
  public static final String SSO_DATE_FORMAT = "sso.date.format";
  public static final String SSO_SECRET_KEY = "sso.secret.key";
  public static final String SSO_AES256_KEY = "sso.aes256.key";
  public static final String SSO_INIT_VECTOR = "sso.init.vector";
  public static final String SSO_SENDER_TIME_ZONE = "sso.sender.time.zone";

  // PURL properties
  public static final String PURL_AVAILABLE = "purl.available";
  public static final String PURL_ALLOW_FACEBOOK = "purl.allow.facebook";
  public static final String PURL_ALLOW_TWITTER = "purl.allow.twitter";
  public static final String PURL_ALLOW_LINKED_IN = "purl.allow.linkedin";
  public static final String PURL_ALLOW_CHATTER = "purl.allow.chatter";
  public static final String PURL_DAYS_TO_EXP = "purl.days.to.expiration";
  public static final String SYSTEM_IMG_SIZE_LIMIT = "system.image.upload.size.limit";
  public static final String PURL_COMMENT_SIZE_LIMIT = "purl.comment.size.limit";
  public static final String PURL_URL_PREFIX = "purl.url";

  // Quiz File upload properties
  public static final String SYSTEM_VIDEO_SIZE_LIMIT = "system.video.upload.size.limit";
  public static final String SYSTEM_PDF_SIZE_LIMIT = "system.pdf.upload.size.limit";

  // Public Recognition Properties
  public static final String PUBLIC_RECOG_DEFAULT_TAB_NAME = "public.recog.default.tab.name";
  public static final String PUBLIC_RECOG_ACTIVE_TABS = "public.recog.active.tabs";
  public static final String PUBLIC_RECOG_ALLOW_FACEBOOK = "public.recog.allow.facebook";
  public static final String PUBLIC_RECOG_ALLOW_TWITTER = "public.recog.allow.twitter";
  public static final String PUBLIC_RECOG_ALLOW_LINKED_IN = "public.recog.allow.linkedin";
  public static final String PUBLIC_RECOG_ALLOW_CHATTER = "public.recog.allow.chatter";

  // Purl Celebration Properties
  public static final String PURL_CELEBRATION_DEFAULT_TAB_NAME = "purl.celeb.default.tab.name";
  public static final String PURL_CELEBRATION_ACTIVE_TABS = "purl.celeb.active.tabs";

  // Public Recognition Properties
  public static final String SMACK_TALK_DEFAULT_TAB_NAME = "smack.talk.default.tab.name";
  public static final String SMACK_TALK_ACTIVE_TABS = "smack.talk.active.tabs";

  // FACEBOOK setup properties
  public static final String FACEBOOK_API_KEY = "facebook.api.key";
  public static final String FACEBOOK_APP_SECRET = "facebook.api.secret";
  public static final String FACEBOOK_APP_ID = "facebook.api.id";

  // TWITTER setup properties
  public static final String TWITTER_CONSUMER_KEY = "twitter.consumer.key";
  public static final String TWITTER_CONSUMER_SECRET = "twitter.consumer.secret";

  // Chatter setup properties
  public static final String CHATTER_CONSUMER_KEY = "chatter.consumer.key";
  public static final String CHATTER_CONSUMER_SECRET = "chatter.consumer.secret";
  public static final String CHATTER_CALLBACK_URL = "chatter.callback.url";

  // public static final String WELLNESS_URL_PREFIX = "wellness.url";

  public static final String ROSTER_MGMT_AVAILABLE = "roster.management.available";
  public static final String BUDGET_TRANSFER_SHOW = "budget.transfer.show";

  public static final String DEFAULT_LANGUAGE = "default.language";
  public static final String DEFAULT_COUNTRY = "default.country";
  public static final String RUN_CAMPAIGN_TRANSFER_PROCESS = "run.campaign.transfer.process";
  public static final String TEXT_EDITOR_DICTIONARIES = "texteditor.dictionaries";

  /**
   * Used to see whether Global Standalone is enabled or not
   */
  public static final String ENABLE_GLOBAL_STANDALONE = "boolean.enable.global.standalone";
  public static final String GLOBAL_FILE_PROCESSING_WEBDAV = "global.fileprocessing.webdav";
  public static final String GLOBAL_FILE_PROCESSING_WORKWIP = "global.fileprocessing.workwip";
  public static final String GLOBAL_FILE_PROCESSING_PREFIX = "global.fileprocessing.prefix";
  public static final String GLOBAL_FILE_PROCESSING_SUBFOLDER = "global.fileprocessing.subfolder";

  public static final String DEPOSIT_PROCESS_SEND_PAX_DEPOSIT_MAIL = "deposit.process.send.pax.deposit.email";
  public static final String EMAIL_SERVERS = "email.servers";
  public static final String EMAIL_SERVERS_PORT = "email.servers.port";
  public static final String CLIENT_LARGE_AUDIENCE = "client.large.audience";
  public static final String MANAGER_SEND_ALERT = "manager.send.alrt";
  public static final String REPORT_LARGE_AUDIENCE = "large.audience.report.generation.enabled";
  public static final String REPORT_START_DATE_ROLLBACK_DAYS = "report.startDate.rollback.days";

  public static final String PAX_SEARCH_ROWS_LIMIT = "pax.search.limit";
  public static final String BANNER_AD_LIMIT = "banner.ad.limit";

  public static final String URLSHORTNER_URL = "urlshortner.url";
  public static final String URLSHORTNER_SIGNATURE = "urlshortner.signature";

  public static final String ENABLE_LARGE_AUDIENCE_REPORT_GENERATION = "large.audience.report.generation.enabled";
  public static final String SERVER_INSTANCE = "server.instance.suffix";

  public static final String INSTALL_BADGES = "install.badges";
  public static final String PROFILE_FOLLOWLIST_SHOW = "profile.followlist.tab.show";
  public static final String ALLOW_DELEGATE = "sidebar.allow.delegate";

  public static final String PLATEAU_AWARDS_REMINDER_ENABLED = "plateau.awards.reminder";
  public static final String PLATEAU_AWARDS_REMINDER_DAYS = "plateau.awards.reminder.days";
  public static final String PLATEAU_PLATFORM_ONLY = "drive.enabled";

  public static final String SURVEY_REPORT_RESPONSE_COUNT = "survey.report.response.count";

  public static final String ALLOW_PASSWORD_FIELD_AUTO_COMPLETE = "allow.password.field.auto.complete";

  public static final String GOOGLE_CLOUD_MESSAGING_SERVER_ID = "google.cloud.messaging.server.id";

  public static final String DIY_AUDIENCE_EXPORT_LIMIT = "diy.audience.export.limit";

  // SSI
  public static final String SSI_PROGRESS_UPLOAD_SIZE_LIMIT = "ssi.progress.upload.size.limit";

  // ENROLLMENT CHARACTERISTICS
  public static final String BANK_ENROLLMENT_CHARACTERISTIC1 = "bankenrollment.characteristic1";
  public static final String BANK_ENROLLMENT_CHARACTERISTIC2 = "bankenrollment.characteristic2";

  // WORKHAPPIER
  public static final String WORK_HAPPIER = "work.happier";
  // external survey url(quantum survey url)
  public static final String EXTERNAL_SURVEY_ENDPOINT = "external.survey.endpoint";
  public static final String EXTERNAL_SURVEY_AES256_KEY = "external.survey.aes256.key";
  public static final String EXTERNAL_SURVEY_INIT_VECTOR = "external.survey.init.vector";

  public static final String SHOW_SEND_ALL_QUESTION_ON_ESTATEMENT_PROCESS = "show.sendall.estatement.process";
  /**
   * Shared Services Security Key - encrypted
   */
  public static final String SHARED_SERVICES_KEY = "shared.services.security.key";

  public static final String ESTATEMENT_STARTING_USER_ID = "estatement.starting.user.id";

  public static final String PUBLIC_RECOG_WALL_SP_PAGECOUNT = "public.rec.wall.feed.page.count";
  public static final String PUBLIC_RECOG_WALL_FEED_ENABLED = "public.rec.wall.feed.enabled";

  public static final String SMTP_HOST_USERNAME = "smtp.host.username";

  public static final String SMPT_HOST_PASSWORD = "smtp.host.password";

  public static final String AES_KEY = "126,-117,6,26,51,-12,13,106,-66,-71,105,59,-47,100,98,-40,107,26,56,73,-71,-127,-95,-121,26,-13,-66,98,-48,-120,85,80";
  public static final String AES_INIT_VECTOR = "46,40,-89,40,-126,11,13,17,-127,-45,27,-16,119,5,-71,-107";

  // AWS webdav credentials
  public static final String AWS_GLOBAL_FILE_PROCESSING_WEBDAV_USERNAME = "aws.global.fileproc.webdav.username";
  public static final String AWS_GLOBAL_FILE_PROCESSING_WEBDAV_PASSWORD = "aws.global.fileproc.webdav.password";

  // AWS ADC File transfer script
  public static final String AWS_FILE_LOAD_TRANSFER_CGI = "aws.fileload.transer.cgi";
  public static final String AWS_FILE_LOAD_TRANSFER_EXECUTION_CMD = "aws.fileload.transer.execution.cmd";
  // SEA System properties
  public static final String SEA_SECURITY_SALT = "sea.security.salt";
  public static final String SEA_DAYS_TO_ABANDONED = "sea.days.to.abandoned";
  public static final String SEA_EMAIL_PASSWORD = "sea.email.password";
  public static final String SEA_EMAIL_ACCOUNT  = "sea.email.account";

  // Post Process Variables
  /**
   * Number of attempts to retry post process
   */
  public static final String POST_PROCESS_RETRY_ATTEMPTS = "mailing.post.process.retry.attempts";

  public static final String SSO_DETAILED_LOGGING_ON = "sso.detailed.logging.on";
  public static final String SSO_LOGOUT_REDIRECT_URL = "sso.logout.redirect.url";
  public static final String SSO_LOGIN_TYPE = "login.type";
  public static final String LOGOUT_TIMEOUT_LIKE_SSO = "logout.timeout.like.sso";
  public static final String NOT_AUTH_TIMEOUT_REDIRECT_URL = "noauth.timeout.redirect.url";
  public static final String NOT_AUTH_TIMEOUT_REDIRECT_INTERNAL_URL = "noauth.timeout.internal.redirect.url";

  // Elastic search configurations
  public static final String ELASTICSEARCH_CREDENTIALS_USERNAME = "elasticsearch.creds.username";
  public static final String ELASTICSEARCH_CREDENTIALS_PASSWORD = "elasticsearch.creds.password";
  public static final String ELASTICSEARCH_INDEX_BATCH_SIZE = "elasticsearch.index.batch.size";
  public static final String AUTOCOMPLETE_ES_URL = "autocomplete.es.url";
  public static final String AUTOCOMPLETE_SEARCH_DELAY_MILLIS = "autocomplete.delay.millis";
  public static final String AUTOCOMPLETE_ES_READTIMEOUT = "autocomplete.es.readtimeout";
  public static final String AUTOCOMPLETE_ES_PROXY_URL = "autocomplete.es.proxy.url";
  public static final String ELASTICSEARCH_ENABLED = "elastic.search.enabled";
  public static final String AUTOCOMPLETE_ES_PROXY_PORT = "autocomplete.es.proxy.port";
  public static final String AUTOCOMPLETE_ELASTIC_SEARCH_MAX_ALLOWED_TO_RECOGNIZE = "autocomplete.es.max.allowed.recognize";
  public static final String AUTOCOMPLETE_ELASTIC_SEARCH_MAX_RESULT_SIZE = "autocomplete.es.max.result.size";
  public static final String AUTOCOMPLETE_ES_AWS_REGION = "autocomplete.es.aws.region";
  // Elastic search configurations

  // DEBUG ASSET
  public static final String CMS_ASSET_DEBUG = "asset.debug";

  // Instant Poll
  public static final String INSTANT_POLL = "instantpoll";

  // Celebration Alert
  public static final String CELEBRATION = "celebration";

  // recognition report comment enable
  public static final String RECOGNITION_REPORT_COMMENT_ENABLED = "report.recognition.comment.display";
  public static final String USER_PASSWORD_HISTORY_CHECK_COUNT = "password.history.count";
  public static final String USER_OTP_PASSWORD_EXPIRY_DAYS = "password.otp.expiry.days";
  public static final String TRANSLATION_BILL_CODE = "translation.bill.code";
  public static final String TRANSLATION_SERVICE_URL_PREFIX = "translation.service.url";

  // MEPlus properties
  public static final String MEPLUS_ENABLED = "meplus.enabled";

  // recognition-only properties
  public static final String RECOGNITION_ONLY_ENABLED = "recognition-only.enabled";

  public static final String ADMIN_IP_RESTRICTIONS = "admin.ip.restrictions";

  // Under Armour Properties
  public static final String UA_WEBSERVICES_URL_PREFIX = "bi.uaws.endpoint.url";
  public static final String UA_WEBSERVICES_AUTHORIZE_URL_PREFIX = "bi.uaws.authorize.url";
  public static final String UNDERARMOUR_CODE = "bi.ua.microservice.code";
  public static final String UNDERARMOUR_CLIENT_ID = "bi.ua.microservice.client.id";
  public static final String UNDERARMOUR_ENCRYPTION_SALT = "bi.ua.microservice.encryption.salt";
  public static final String UNDERARMOUR_MICROSERVICE_ENABLED = "bi.ua.microservice.enabled";
  public static final String UNDERARMOUR_REPO = "bi.ua.microservice.repo";
  public static final String UA_PAST_DAYS = "bi.ua.microservice.past.days";
  public static final String UNDERARMOUR_SESSION_LOGOUT_URL_PREFIX = "bi.ua.session.logout.url";

  // Opt-out of receiving Awards

  public static final String ENABLE_OPT_OUT_AWARDS = "enable.opt.out.awards";

  public static final String ENABLE_OPT_OUT_PROGRAM = "enable.opt.out.program";

  public static final String PDF_SERVICE_URL = "pdf.service.url";

  public static final String SALES_MAKER = "salesmaker.enabled";
  
  public static final String EXPERIENCE_SYSTEM_VARIABLE = "experience.marketplace.url";

  public static final String HC_CONFIG_URL = "honeycomb.config.url";
  public static final String HC_CLIENT_CODE = "honeycomb.client.code";
  public static final String HC_GATEWAY_URL = "honeycomb.gateway.url";
  public static final String HC_SYNC_BATCH_SIZE = "honeycomb.sync.batch.size";

  // Honeycomb Gateway endpoints
  public static final String HC_ENDPOINT_ACCOUNT_SYNC = "honeycomb.endpoint.account.sync";
  public static final String HC_ENDPOINT_GOALQUEST_DETAILS = "honeycomb.endpoint.goalquest.details";
  public static final String HC_ENDPOINT_GOALQUEST_MANAGERPROGRAMS = "honeycomb.endpoint.gq.managerprograms";
  public static final String HC_ENDPOINT_SSO_PARAMETERS = "honeycomb.endpoint.sso.parameters";
  public static final String HC_ENDPOINT_CLIENT_ID = "honeycomb.endpoint.client.id";

  // public static final String REISSUE_SEND_PASSWORD_ON = "reissue.send.password.on";
  // public static final String REISSUE_SEND_PASSWORD_MAX_USERS = "reissue.send.password.max.users";
  // public static final String REISSUE_SEND_PASSWORD_DAYS_VALID =
  // "reissue.send.password.days.valid";
  // public static final String REISSUE_SEND_PASSWORD_VAILID_IPS =
  // "reissue.send.password.valid.ips";

  // UserToken constraints
  public static final String USER_TOKEN_LENGTH_WELCOME_EMAIL = "user.token.length.welcome.email";
  public static final String USER_TOKEN_LENGTH_EMAIL = "user.token.length.email";
  public static final String USER_TOKEN_LENGTH_PHONE = "user.token.length.phone";
  public static final String USER_TOKEN_LENGTH_EMAIL_VERIFICATION = "user.token.length.email.verify";
  public static final String USER_TOKEN_LENGTH_PHONE_VERIFICATION = "user.token.length.phone.verify";

  public static final String USER_TOKEN_EXPIRATION_DAYS_WELCOME_EMAIL = "user.token.expire.days.welcome.email";
  public static final String USER_TOKEN_EXPIRATION_HOURS_EMAIL = "user.token.expire.hours.email";
  public static final String USER_TOKEN_EXPIRATION_MINUTES_PHONE = "user.token.expire.minutes.phone";
  public static final String USER_TOKEN_EXPIRATION_HOURS_EMAIL_VERIFICATION = "user.token.expire.hours.email.verify";
  public static final String USER_TOKEN_EXPIRATION_MINUTES_PHONE_VERIFICATION = "user.token.expire.hours.phone.verify";
  // Client customization for WIP #39189 starts
  public static final String COKE_UPLOAD_FILE_SIZE_LIMIT = "coke.upload.file.size.limit";
  public static final String COKE_UPLOAD_FILE_TYPES = "coke.upload.file.types";
  public static final String COKE_UPLOAD_FILE_REPALCE_CHARS = "coke.upload.file.replace.chars";
  // Client customization for WIP #39189 ends
  
  // Plateau Redemption role
  public static final String ROLES_FOR_BIW_ONLY = "roles.for.biw.only";

  // Daily tip
  public static final String TIP_DAY_ROTATE_SECONDS = "tip.day.rotate.seconds";

  public static final String INSTALL_WIZARD_ENVIRONMENT_URL = "install.wizard.environment";

  // PS v2 SSO system variables
  public static final String PARTNER_SERVICES_CATALOG_SSO_CUSTOMER_ID = "partnersrvc.consolidated.customer.id";
  public static final String PARTNER_SERVICES_CATALOG_SSO_CATALOG_TYPE = "partnersrvc.catalogsso.catalog.type";
  public static final String PARTNER_SERVICES_CATALOG_SSO_WS_URL_PREFIX = "partnersrvc.catalogsso.wsdl.url";

  public static final String PARTNERSRVC_CLIENT_KEY_PASSWORD = "partnersrvc.client.key.password";
  public static final String PARTNERSRVC_CLIENT_KEYSTORE_PASSWORD = "partnersrvc.client.store.password";

  public static final String PARTNERSRVC_SERVER_KEYSTORE_PASSWORD = "partnersrvc.server.store.password";

  public static final String PARTNERSRVC_AES_KEY = "partnersrvc.aes256.key";
  public static final String PARTNERSRVC_AES_INIT_VECTOR = "partnersrvc.init.vector";

  // constants
  public static final String APP_PREFIX = "g";
  public static final String PARTNERSRVC_NON_PROD_ENV = "nonprd";

  // Placing The Product Client Name in T&C
  public static final String PRODUCT_CLIENT_NAME = "client.name";

  // RA Constants
  public static final String RA_ENABLE = "ra.enabled";
  public static final String RA_LEVELS_CHECK = "ra.recog.levels.check";
  public static final String RA_NUMBER_OF_DAYS_NEW_HIRE_TO_REGULAR_EMPLOYEE = "ra.numberofdays.newhiretoregularemployee";
  public static final String RA_NUMBER_OF_DAYS_EMPLOYEE_REMINDER = "ra.numberofdays.employee.reminder";
  public static final String RA_SEND_REMINDER_TO_RECOGNIZE_NEW_HIRE = "ra.sendreminder.torecognize.newhire";
  public static final String RA_SEND_REMINDER_TO_RECOGNIZE_TEAM_MEMBER = "ra.sendreminder.torecognize.teammember";

  // Facility for the termed user to do the account activation, to redeem his/her points.
  public static final String TERMED_USER_ALLOW_REDEEM = "termed.user.allow.redeem";

  // Fixed for the pure SSO client.
  public static final String SSO_CLIENT_PURE_CLIENT = "sso.only";
  public static final String SSO_LOGIN_URL = "sso.login.url";

  // Roster constants
  public static final String ROSTER_COMPANY_ID = "roster.company.id";

  // Enable New Service Anniversary Service From Nackle.
  public static final String NEW_SERVICE_ANNIVERSARY = "new.service.anniversary.enabled";

  // Nackle Mesh Services Host Base Url
  public static final String NACKLE_MESH_SERVICES_HOST_BASE_URL = "nackle.mesh.services.base.endpoint";

  // Amazon Kinesis Stream Name
  public static final String AWS_KINESIS_STREAMNAME = "aws.kinesis.streamname";

  // Amazon Kinesis Region
  public static final String AWS_KINESIS_REGION = "aws.kinesis.region";

  // Amazon account number
  public static final String AWS_ACCOUNT_NO = "aws.kinesis.account.no";

  // JWT Secret for Roster
  public static final String JWT_SECRET_ROSTER = "jwt.secret.roster";

  // JWT Issuer for Roster
  public static final String JWT_ISSUER_ROSTER = "jwt.issuer.roster";

  // Budget notused notification
  public static final String BUDGET_NOTUSED_NOTIFICATION_ENABLD = "ra.Program.NotUsed.Notification.Enabled";

  // Nackle Mesh Credentials
  public static final String NACKLE_MESH_SERVICES_CLIENT_ID = "nackle.mesh.services.client.id";
  public static final String NACKLE_MESH_SERVICES_SECRET_KEY = "nackle.mesh.services.secret.key";

  // DM application context name
  public static final String DM_CONTEXT_NAME = "dm.context.name";

  // Security - CSRF Exclude URL paths;
  public static final String EXCLUDE_CSRF_URLS = "exclude.csrf.urls";
  
//Client customizations for wip #23129 starts
 public static final String COKE_GIFT_CODE_SWEEP_MIN_DAYS = "coke.gift.code.sweep.minimum.days";
 public static final String COKE_GIFT_CODE_SWEEP_TRANS_DESC = "coke.gift.code.sweep.trans.desc";
 //Client customizations for wip #23129 ends
 
 /* WIP 37311 customization starts */
 public static final String COKE_GIFTCODE_SWEEP_BILLCODE_LIST = "coke.gift.code.sweep.bill.code.list";
 /* WIP 37311 customization ends */
 
 //client customization for WIP-44575
 public static final String COKE_USER_CHAR_WORK_COUNTRY = "coke.user.char.work.country";
 
 public static final String COKE_DAY_EMAIL_WORK_COUNTRIES= "coke.day.email.work.countries";
 
 public static final String COKE_DAY_EMAIL_DAYS_PRIOR = "coke.day.email.days.prior";

 // customization starts wip 42702
 public static final String COKE_CASH_DEFAULT_APPROVER = "coke.cash.default.approver";
 // customization end wip 42702
 
 // Client customization for WIP #42701 starts
 public static final String COKE_USER_PAYROLL_AREA_CHAR = "coke.user.char.payroll.area";
 public static final String COKE_USER_CURRENCY_CHAR = "coke.user.char.currency";
 public static final String COKE_USER_DIVISION_KEY_CHAR = "coke.user.char.division.key";
 public static final String COKE_USER_JOB_GRADE_CHAR = "coke.user.char.job.grade";
 public static final String COKE_CASH_JOB_GRADE_AND_BELOW = "coke.cash.job.grade.and.below";
 public static final String COKE_CASH_PERCENT_SPLIT = "coke.cash.percent.split";
 // Client customization for WIP #42701 ends
 
 /* TCCC  - customization start - wip 46975 */
 public static final String COKE_RECOG_ANYONE_CARD_ID_LIST  = "coke.recog.anyone.card.id.list";
 public static final String COKE_RECOG_ANYONE_COPY_SENDER   = "coke.recog.anyone.copy.sender";
 public static final String COKE_CUSTOME_REPORT_AUDIENCE_ID = "coke.custom.report.audience.id";
 /* TCCC  - customization end */
 /* TCCC  - customization start - wip 51838 */
 public static final String COKE_RECOGNIZE_ANY_AVAILABLE="coke.recog.anyone.available";
 public static final String COKE_RECOGNIZE_ANY_AUDIENCE_IDS="coke.recog.anyone.audience";
 /* TCCC  - customization end */
 // Client customization for WIP 58122
 public static final String COKE_TRAINING_CONTACT_EMAIL = "coke.training.contact.email";
 
 // Client customizations for wip #26532 starts
 public static final String COKE_ACCEPTABLE_DOMAINS = "coke.acceptable.domains";
 // Client customizations for wip #26532 ends
 //client customization start - wip 52159
 public static final String COKE_PURL_BU_INVITE = "coke.purl.bu.invite";
 public static final String COKE_DIV_KEY_PROMO_IDS ="coke.noms.save.div.promo.ids";
 public static final String COKE_BUDGET_VALUE_PER_PERSON = "coke.budget.value.per.person";
 public static final String COKE_BUDGET_NODE_TYPE = "coke.budget.node.type";
 public static final String COKE_EXCLUDE_FROM_HEADCOUND_COUNTRY_CODES = "coke.exclude.from.hc.country.codes";
 
 public static final String COKE_INACTIVE_DEPOSIT_PROMO_IDS = "coke.inactive.deposit.promo.ids"; 
 public static final String COKE_BUNCHBALL_API_URL = "coke.bunchball.api.url";
 public static final String COKE_BUNCHBALL_API_CLIENT_ID = "coke.bunchball.api.client.id";
 public static final String COKE_BUNCHBALL_API_SECRET = "coke.bunchball.api.secret";
 public static final String COKE_SIDEBAR_SHOW_BUNCHBALL_MODULE = "coke.sidebar.show.bunchball.module";
 public static final String COKE_MEME_FONT_SIZES = "coke.meme.font.sizes";
 public static final String COKE_CHEERS_PROMO_ID = "coke.cheers.promo.id";
  
  /**
   * setter for SystemVariableDAO
   *
   * @param systemVariableDAO
   */
  public void setSystemVariableDAO( SystemVariableDAO systemVariableDAO );

  /**
   * get all system variables.
   *
   * @return List
   */
  public List getAllProperties();

  /**
   * Get a propertySetItem from OSPropertySet by name.
   *
   * @param propertyName
   * @return PropertySetItem
   */
  public PropertySetItem getPropertyByName( String propertyName );

  /**
   * Get a propertySetItem from OSPropertySet by name and environment. Appends the environment to
   * the propertyName to find the Property
   *
   * @param propertyName
   * @return PropertySetItem
   */
  public PropertySetItem getPropertyByNameAndEnvironment( String propertyName );

  /**
   * Save a PropertySetItem in OS_PROPERTYSET.
   *
   * @param prop a PropertySetItem
   * @throws ServiceErrorException
   */
  public void saveProperty( PropertySetItem prop ) throws ServiceErrorException;

  /**
   * Save the value of an existing PropertySetItem in OS_PROPERTYSET.
   *
   * @param prop a PropertySetItem
   */
  public void savePropertyValue( PropertySetItem prop );

  /**
   * Delete a PropertySet from OS_PROPERTYSET.
   *
   * @param prop
   */
  public void deleteProperty( PropertySetItem prop );

  public void setContextName( String contextName );

  public String getContextName();

  public String getPrefix();

  public boolean isGDEV();

  public String getAESDecryptedValue( String encryptedValue );

  public PropertySetItem getDefaultLanguage();

  public String getAESDecryptedValue( String encryptedValue, String aesKey, String iv );

}
