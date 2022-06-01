import React from 'react';
import classNames from 'classnames';

import LoginForm from './forms/login-form';
import ForgotUserId from './forms/forgot-user-id';
import ForgotPassword from './forms/forgot-password';
import MethodOfContact from './forms/shared/method-of-contact';
import MessageWindow from './forms/shared/message-window';
import CodeVerification from './forms/code-verification';
import ResetPassword from './forms/reset-password';
import Activation from './forms/activation';
import ActivationAttr from './forms/activation-attr';
import CreatePassword from './forms/create-password';
import Recovery from './forms/recovery';
import AccountLock from './forms/account-lock';
import AccountLockConfirm from './forms/account-lock-confirm';

import Header from '../../shared/header/header';
import Footer from '../../shared/footer/footer';
import Spinner from '../../shared/spinner/spinner';
import Modal from '../../shared/modal/modal';

import './loginReact.scss';

class LoginPage extends React.Component {

	constructor(props) {
		super(props);
		const token = new RegExp('[\?&]userToken=([^&#]*)').exec(this.getLocation());
		const recovery = new RegExp('[\?&]recovery=([^&#]*)').exec(this.getLocation());
		const forgotUser = new RegExp('[\?&]forgotUser=([^&#]*)').exec(this.getLocation());
		const welcomeToken = new RegExp('[\?&]welcomeToken=([^&#]*)').exec(this.getLocation());
		const accountLock = new RegExp('[\?&]accountLock=([^&#]*)').exec(this.getLocation());
		const accountLockConfirm = new RegExp('[\?&]accountLockConfirm=([^&#]*)').exec(this.getLocation());
		const accountLockConfirmKey = new RegExp('key=([^&]*)').exec(this.getLocation());
		const isEmailCheck = new RegExp('isEmail=(.*)').exec(this.getLocation());
		const activationPage = new RegExp('[\?&]activationPage=([^&#]*)').exec(this.getLocation());
		const forgotPass = new RegExp('[\?&]forgotPass=([^&#]*)').exec(this.getLocation());
		if (token && token[1]) {
			this.state = {
				currentForm: 'codeVerification',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
			this.props.changeFormFunc(this.state.currentForm);
		} else if (welcomeToken && welcomeToken[1]) {
			this.state = {
				currentForm: 'codeVerification',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
		} else if (recovery && recovery[1]) {
			this.state = {
				currentForm: 'recovery',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
		} else if (forgotUser && forgotUser[1]) {
			this.state = {
				currentForm: 'forgotUserId',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
			this.props.changeFormFunc(this.state.currentForm);
		} else if (accountLock && accountLock[1]) {
			this.state = {
				currentForm: 'accountLock',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
			if (accountLockConfirmKey && accountLockConfirmKey[0]) {
				if ((accountLockConfirmKey[0].split("=")[0] === "key") && (accountLockConfirmKey[0].split("=")[1] === "false")) {
					this.state = {
						...this.state,
						userOptedLock: true
					}
				} else {
					this.state = {
						...this.state,
						userOptedLock: false
					}					
				}
				if (isEmailCheck && (isEmailCheck[0].split("=")[1] === "true")) {
					this.state = {
						...this.state,
						emailFrom: true
					}
				} else {
					this.state = {
						...this.state,
						emailFrom: false
					}
				}

			}
			this.props.changeFormFunc(this.state.currentForm);
		} else if (accountLockConfirm && accountLockConfirm[1]) {
			this.state = {
				currentForm: 'accountLockConfirm',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
			if (accountLockConfirmKey && accountLockConfirmKey[0]) {
				this.state = {
					...this.state,
					keyRef: accountLockConfirmKey[0]
				}
			}
			this.props.changeFormFunc(this.state.currentForm);
		} else if (activationPage && activationPage[1]) {
			this.state = {
				currentForm: 'activation',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
			this.props.changeFormFunc(this.state.currentForm);
		} else if (forgotPass && forgotPass[1]) {
			this.state = {
				currentForm: 'forgotPassword',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
			this.props.changeFormFunc(this.state.currentForm);
		} else {
			this.state = {
				currentForm: 'login',
				errors: [],
				content: this.props.cmStringsArrayToObject(this.props.login.content)
			};
			this.props.changeFormFunc(this.state.currentForm);
		}

	}
	getLocation = () => {
		if (window.location.url) {
			return window.location.url;
		} else {
			return window.location.href;
		}
	}
	setLanguage = () => {
		const localeCode = this.languageParam('cmsLocaleCode') || 'en_US';
		this.setState({
			languageSelected: localeCode
		});
	}
	languageParam = (name) => {
		const results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
		if (results === null) {
			return null;
		}
		else {
			return results[1];
		}
	}
	changeLanguage = (value) => {

		this.setState({
			showSpinner: true
		});
		const localeCode = value;
		let url = window.location.href;

		if (url.indexOf('cmsLocaleCode') >= 0) {
			url = url.replace(/cmsLocaleCode=([a-z|A-Z|_])+/i, 'cmsLocaleCode=' + localeCode);
		}
		else {
			url = url + (url.indexOf('?') >= 0 ? '&' : '?') + 'cmsLocaleCode=' + localeCode;
		}

		window.location.href = url;
	}
	componentWillMount() {
		if (!this.props.recovery.length && this.state.currentForm === 'recovery') {
			const { countryCodeUrl } = this.props.login;
			this.props.fetchDataCountries(countryCodeUrl);
		}
		if (!this.props.passConfirm.passwordRules) {
			const { pwRulesUrl } = this.props.login;
			this.props.fetchPasswordRules(pwRulesUrl);
		}


	}
	render() {
		const {
			selfEnrollment
		} = this.state;
		const {
			currentForm,
			skin
		} = this.props.login;
		const pageClasses = classNames({
			'page-wrapper': true
		});

		const pageClassnames = classNames({
			'page-content': true,
			'page': true,
			'selfEnrollment': selfEnrollment
		});

		return (
			<div className={pageClasses}>
				<Header altText="login screen primary logo" skin={skin} />
				<section id="contents">
					<div className="container">
						<div className={pageClassnames} id="loginPage" style={{ visibility: 'visible' }}>

							{
								currentForm === 'login' &&
								<LoginForm
									handleFormChange={this.props.changeFormFunc}
									changeLanguage={this.changeLanguage}
									setLanguage={this.setLanguage}
									{...this.props} {...this.state} />
							}
							{
								currentForm === 'forgotUserId' &&
								<ForgotUserId handleFormChange={this.props.changeFormFunc} fetchData={this.props.fetchData}{...this.props}{...this.state} />
							}
							{
								currentForm === 'forgotPassword' &&
								<ForgotPassword handleFormChange={this.props.changeFormFunc} fetchDataForgotPassword={this.props.fetchDataForgotPassword}{...this.props} {...this.state} />
							}
							{
								currentForm === 'methodOfContact' &&
								<MethodOfContact handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'messageWindow' &&
								<MessageWindow handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'codeVerification' &&
								<CodeVerification handleFormChange={this.props.changeFormFunc}  {...this.props} {...this.state} />
							}
							{
								currentForm === 'resetPassword' &&
								<ResetPassword handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'activation' &&
								<Activation handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'activAttr' &&
								<ActivationAttr handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'createPass' &&
								<CreatePassword handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'recovery' &&
								<Recovery handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'accountLock' &&
								<AccountLock userOpted={this.state.userOptedLock} userFromEmail={this.state.emailFrom} handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}
							{
								currentForm === 'accountLockConfirm' &&
								<AccountLockConfirm handleKeyValue={this.state.keyRef} handleFormChange={this.props.changeFormFunc} {...this.props} {...this.state} />
							}

						</div>
					</div>
					{this.props.showSpinner &&
						<Spinner />
					}
					{this.props.login.toggleModal &&
						<Modal {...this.state} {...this.props} />
					}
				</section>
				<Footer {...this.state} {...this.props} />
			</div>
		);

	}


}


export default LoginPage;
