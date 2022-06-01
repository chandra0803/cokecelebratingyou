import React from 'react';
import classNames from 'classnames';

import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';

import { Alert } from 'component-library';
import '../loginReact.scss';

import { run, runValidation } from '../../../utils/form-validation';

import { required, validEmail } from '../../../utils/form-validation-rules';

const fieldValidations = [
  runValidation( 'username', 'First Name', required ),
  //runValidation( 'username', 'First Name', minLength( 6 ) ),
  runValidation( 'email', 'Email Address', required ),
  runValidation( 'email', 'Email Address', validEmail )

];

class Recovery extends React.Component {

    constructor( props ) {
        super( props );
            this.state = {
                phone: '',
                email: '',
                validationErrors: { },
                errors: [],
                countryCode: this.props.recovery.countries.countryPhones[ 0 ].countryId,
                fetchCountries: true
            };


    }

    handleField = ( event ) => {
        if( event.target.name === 'phone' ) {
            this.validatePhone( event );
        }
        const name = event.target.name;
        const newState = Object.assign( {}, this.state, { [ name ]: event.target.value } );
        newState.validationErrors = run( newState, fieldValidations );
        this.setState( newState );
    }

    errorFor = ( field ) => {
        return this.state.validationErrors[ field ] || '';
    }
    onSubmit = () => {
        const {
                fetchDataCollectRecovery
            } = this.props;
        let userId;
        if( this.props.activation && this.props.activation.id ) {
            userId = this.props.activation.id;
        } else if ( this.props.forgotPass && this.props.forgotPass.details ) {
            userId = this.props.forgotPass.details.userName;
        } else {
            userId = null;
        }
        let tokenValidation;
        if( this.props.attributes.attributes && this.props.attributes.attributes.tokenValidation ) {
            tokenValidation = this.props.attributes.attributes.tokenValidation;
        } else {
            tokenValidation = null;
        }
        const { phone, email, countryCode } = this.state;
        const { recoveryMethodUrl } = this.props.login;
        const contact = { countryId: countryCode, contactPhone: phone, contactEmail: email, userName: userId, tokenValidation: tokenValidation };
        let final;
        if( Object.keys( this.props.attributes ).length || ( this.props.forgotPass && this.props.forgotPass.details && this.props.forgotPass.details.nonPax === true ) ) {
            final = false;
        } else {
            final = true;
        }
        fetchDataCollectRecovery( contact, recoveryMethodUrl, final );
    }

    countryChange = ( event ) => {
        this.setState( {
            countryCode: event.target.value
        } );
    }
    checkEnter = ( event ) => {
        const enterPressed = event.charCode === 13;
        const { phone, email } = this.state;

        if ( ( enterPressed && phone.length ) || ( enterPressed && email.length ) ) {
            this.onSubmit();
        }
    }
    validatePhone = ( event ) => {
        event = event || window.event;
        const inputVal = event.target.value,
            allowOnlyNumeric = /[^\d]/g; // Regex pattern to match any non numeric character

        if ( allowOnlyNumeric.test( inputVal ) ) {
            // Replacing any non numeric character with empty string during key input
            event.target.value = inputVal.replace( allowOnlyNumeric, '' );
        }
    }

  render() {

    const {
        content
    } = this.props;

    const {
        phone,
        email,
    } = this.state;

    const {
        message,
        toggleModal
    } = this.props.login;
    const {
        countryPhones
    } = this.props.recovery.countries;
    const phoneFieldClasses = classNames( {
        'phone-field': true,
        'input-wrap': true,
        'focused': true
    } );
    let linkText = content[ 'login.forgotpwd.mblox_tnc' ].template( { linkOpen: '<a>', linkClose: '<a>' } );
    linkText = linkText.split( '<a>' );
    // EMAIL
    let validEmailAddress = false;
    if ( email.length && this.errorFor( 'email' ) ) {
        validEmailAddress = true;
    }
    const emailFieldClasses = classNames( {
        'email-field': true,
        'input-wrap': true,
        'focused': true,
        'hasValidationError': validEmailAddress
    } );

    const clientLogo = '';
    const loginErrors = [];
    return (
		<NarrowForm page="login" clientLogo={ clientLogo } errorsExist={ loginErrors.length > 0 }>
            <p>
                <a tabIndex="1" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
			<h3>{ content[ 'login.loginpage.recovery_information' ] }</h3>

            <p>{content[ 'login.forgotpwd.info_email_phone_dtls' ]}</p>
            <em>{content[ 'login.forgotpwd.acc_communication_assistance' ]} </em><em>{linkText[ 0 ]} <a className="tncLink" tabIndex="2" onClick={ ()=>this.props.toggleModalDisplay( !toggleModal, 'tnc' ) }>{linkText[ 1 ]} </a>{linkText[ 2 ]}</em>

            {
                message &&
                message.error &&
                        <Alert type={ 'error' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.error } }/>
                        </Alert>

            }
            <div className={ emailFieldClasses }>
                <label className="control-label" htmlFor="email">{ content[ 'participant.participant.email_address' ] }</label>
                <input
                    type= "email"
                    name="email"
                    tabIndex={ 3 }
                    id="email"
                    className="gq-input"
                    value={ email }
                    onChange={ this.handleField }
                    onKeyPress={ this.checkEnter } />
                    <div className="validation-error"><span className="text">{ content[ 'login.forgotpwd.email_req' ] }</span></div>
            </div>
            <div className="andor"><span className="and-or-hr"><hr /></span><span className="and-or-text">{ content[ 'login.loginpage.and_or' ] }</span><span className="and-or-hr"><hr /></span></div>
            <div className={ phoneFieldClasses }>
                <label htmlFor="countryCode" className="control-label">{ content[ 'login.loginpage.country_code' ] }</label>
				<select id="countryCode" tabIndex={ 4 } className="selectpicker gq-input" name="countryCode" onChange={ this.countryChange }>
					{
						countryPhones.map( ( country, idx ) => {
							return <option key={ idx } value={ country.countryId }>{ country.label }</option>;
						} )
					}
				</select>
            </div>
            <div className={ phoneFieldClasses }>
                <label htmlFor="formUserId" className="control-label">{ content[ 'login.password.reset.phone_number' ] }</label>
                    <input
                    type="tel"
                    name="phone"
                    tabIndex={ 5 }
                    id="formUserId"
                    className="gq-input"
                    value={ phone }
                    onChange={ this.handleField }
                    onKeyPress={ this.checkEnter } />
                    <div className="validation-error"><span className="text">{this.errorFor( 'phone' )}</span></div>
            </div>


			<LoaderButton
				handleClick={ this.onSubmit }
				disabled={ ( !phone.length && !email.length ) || validEmailAddress ? true : false }
				customClass="btn btn-block btn-primary form-action-btn"
				tabIndex={ 6 }>
				{ content[ 'system.button.submit' ] }
			</LoaderButton>

            <p className="help">
                <a tabIndex="7" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'help' ) }>
                    <span>{ content[ 'login.loginpage.help' ] }</span>
                </a>
            </p>

		</NarrowForm>
    );
  }
}


export default Recovery;
