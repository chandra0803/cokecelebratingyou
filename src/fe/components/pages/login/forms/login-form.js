import React from 'react';
import classNames from 'classnames';

import {
    EyeOpenIcon,
    EyeClosedIcon } from '../../../shared/icons/icons';
import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';
import { Alert } from 'component-library';
import 'whatwg-fetch';

import '../loginReact.scss';


const Content = ( props ) => {

    const key = [ props.view, props.component, props.tag ].join( '.' );
    return (
    		<span>
    		{
    			<span dangerouslySetInnerHTML={ { __html: props.content[ key ] || key } }/>	
    		}  
    		</span>
    );
    
};

class LoginForm extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            username: '',
            password: '',
            passwordVisible: false,
            focusedField: ''
        };
    }

    passwordVisibility = () => {
        const { passwordVisible } = this.props.login;
        this.props.togglePasswordVisible( !passwordVisible );
    }

    handleField = ( event ) => {
        const name = event.target.name;
        this.setState( { [ name ]: event.target.value } );
    }

	componentDidMount() {
		this.props.setLanguage();

	}

    onSubmit = () => {
        const {
            username,
            password } = this.state;
        const {
            loginFunc } = this.props;
        const {
            strutsToken,
            loginUrl } = this.props.login;

        loginFunc( username, password, loginUrl, strutsToken );

    }

    checkEnter = ( event ) => {
        const enterPressed = event.charCode === 13;
        const { username, password } = this.state;

        if ( enterPressed && username.length && password.length ) {
            this.onSubmit();
        }
    }
    languageChange = ( event ) => {
        const { changeLanguage } = this.props;
        changeLanguage( event.target.value );
    }

  render() {
	const loggingIn = false;
    const {
        message,
        locales,
        passwordVisible,
        selfEnrollment,
        toggleModal
    } = this.props.login;
    const {
        content
    } = this.props;

    const {
        username,
        password,
        locale,
        focusedField
    } = this.state;

	const languageFieldClasses = classNames( {
        'hide': locales.length === 0,
		'language-select': true,
		'input-wrap': true,
		'focused': focusedField === 'language' || !!locale
	} );

    const userFieldClasses = classNames( {
        'username-field': true,
		'login-form': true,
        'input-wrap': true,
        'focused': true
    } );

    const passwordFieldClasses = classNames( {
        'password-field': true,
        'input-wrap': true,
        'focused': true
    } );

    const clientLogo = '';
    const loginErrors = [];

    return (

		<NarrowForm page="login" clientLogo={ clientLogo } errorsExist={ loginErrors.length > 0 } method="post">
            <div className="row-fluid">   
            {
            	 <h2 dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.title' ] } }/>  

            }
            {
            	 <p dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.intro_para_one' ] } } />
            }           

            {
                content[ 'login.loginpage.intro_para_two' ] &&
                <p dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.intro_para_two' ] } } />    
                
            }
            {
                message &&
                message.error &&
                        <Alert type={ 'error' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.error } }/>
                        </Alert>

            }
            {
                message &&
                message.success &&
                        <Alert type={ 'success' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.success } }/>
                        </Alert>

            }
            {
                message &&
                !message.error &&
                !message.success &&
                <Alert type={ message.type } key={ message.id }>
                    <p dangerouslySetInnerHTML={ { __html: message.name } }/>
                    <p dangerouslySetInnerHTML={ { __html: message.text } }/>
                </Alert>

            }
			<div className={ languageFieldClasses }>
                <label htmlFor="languageSelect" className="control-label"></label>
				<select id="language-select" className="selectpicker gq-input" value={ this.props.languageSelected } onChange={ this.languageChange }>
					{
						locales.map( ( locale, idx ) => {
							return <option key={ idx } value={ locale.code }>{ locale.label }</option>;
						} )
					}
				</select>
            </div>


			<div className={ userFieldClasses }>
                <a tabIndex="5" className="forgot-link login-input-label" onClick={ e => this.props.handleFormChange( 'forgotUserId' ) }>
                    <Content
                        view="login"
                        component="password.reset"
                        tag="forgot_login_id"
                        content={ content } />
                </a>
                <label htmlFor="formUserId" className="control-label">
                    <Content
                        view="login"
                        component="loginpage"
                        tag="username"
                        content={ content } />
                </label>
                <span className="userId"><span className="icon-info"></span><span className="instructions">{ content[ 'login.loginpage.instructions' ] }</span></span>
                <input
                    type="text"
                    name="username"
                    tabIndex="1"
                    autoFocus
                    id="formUserId"
                    className="gq-input"
                    value={ username }
                    onChange={ this.handleField }
                    onKeyPress={ this.checkEnter } />
            </div>

            <div className={ passwordFieldClasses }>
                <a tabIndex="5" className="forgot-link login-input-label" onClick={ ( e ) => this.props.handleFormChange( 'forgotPassword' ) }>
                    <Content
                        view="login"
                        component="loginpage"
                        tag="forgot_password"
                        content={ content } />

                </a>
                <label className="control-label" htmlFor="formPassword">
                    <Content
                        view="login"
                        component="loginpage"
                        tag="password"
                        content={ content } />
                </label>
                <span className="eye" onClick={ this.passwordVisibility }>
                    { passwordVisible ? <EyeOpenIcon/> : <EyeClosedIcon/> }
                </span>
                <input
                    type={ passwordVisible ? 'text' : 'password' }
                    name="password"
                    tabIndex="2"
                    id="formPassword"
                    className="gq-input"
                    value={ password }
                    onChange={ this.handleField }
                    onKeyPress={ this.checkEnter } />
            </div>
                <div className="activation">
                    <p>{ content[ 'login.loginpage.no_password' ] } <a onClick={ ( e )=>this.props.handleFormChange( 'activation' ) }>{ content[ 'login.loginpage.activate_account' ] }</a></p>
                </div>
			<LoaderButton
				fetching={ loggingIn }
				handleClick={ this.onSubmit }
				disabled={ !username.length || !password.length ? true : false }
				customClass="btn btn-block btn-primary form-action-btn"
                tabIndex={ 3 }
                buttonId="login-page-submit"
				done={ this.props.done }>
				<Content
                    view="login"
                    component="loginpage"
                    tag="log_in"
                    content={ content } />
			</LoaderButton>
            {selfEnrollment &&
                <p className="self-enroll" >
                    {content[ 'login.loginpage.no_login_id_or_password' ] + ' '}
                    <a onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'register' ) }>{content[ 'login.loginpage.register_new_account' ]}</a>
                </p>
            }
        </div>
		</NarrowForm>
    );
  }
}


export default LoginForm;
