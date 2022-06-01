import React from 'react';
import classNames from 'classnames';

import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';
import 'whatwg-fetch';
import { Alert } from 'component-library';

import '../loginReact.scss';

class ActivationAttr extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            focusedField: '',
            disabled: true,
        };
    }

    handleField = ( event ) => {
        const name = event.target.name;
        const disabled = event.target.value.length ? false : true;
        const newState = Object.assign( {}, this.state, { [ name ]: event.target.value, disabled: disabled } );
        this.setState( newState );


    }

    onSubmit = () => {
        let fields;
        if( this.props.activation.activation ) {
            fields = this.props.activation.activation.activationFields.map( ( value, index ) => {
            const values = {};
            const label = value.label;
            values.participantIdentifierId = value.participantIdentifierId;
            values.value = this.state[ label ];
            return values;
            } );
        } else {
            fields = this.props.verification.activation.activationFields.map( ( value, index ) => {
            const values = {};
            const label = value.label;
            values.participantIdentifierId = value.participantIdentifierId;
            values.value = this.state[ label ];
            return values;
        } );
        }
        const {
            fetchCollectionData } = this.props;
        const {
            activAttrUrl } = this.props.login;
            let userId;
            let token;
            if( this.props.activation.id ) {
                userId = this.props.activation.id;
                token = '';
            } else {
                userId = this.props.verification.activation.userName;
                token = this.props.verification.token;
            }
        const url = activAttrUrl.replace( '(userId)', encodeURIComponent( userId ) );

        fetchCollectionData( fields, url, token );

    }

    checkEnter = ( event ) => {
        const enterPressed = event.charCode === 13;
        const length = event.target.value.length;
        if ( enterPressed && length ) {
            event.preventDefault();
            this.onSubmit();
        } else if( enterPressed ) {
            event.preventDefault();
        }
    }

  render() {
	const loggingIn = false;
    const {
        content,
        activation,
        verification
    } = this.props;
    const {
        message,
        toggleModal
    } = this.props.login;


    const userIdFieldClasses = classNames( {
        'email-field': true,
        'input-wrap': true,
        'focused': true,
        'activation': true
    } );
    const clientLogo = '';

    return (
		<NarrowForm page="login" clientLogo={ clientLogo } method="post">
            <p>
                <a tabIndex="9" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
            <h3>{ content[ 'login.loginpage.we_found_you' ] }</h3>

            <p>{ content[ 'login.loginpage.activation_att_confirm' ] }</p>
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


                <div className={ userIdFieldClasses }>

                {activation.activation &&
                        activation.activation.activationFields.map( ( attr, index ) =>
                        <div className="activation-field-desc" key={ attr.participantIdentifierId }>
                            <div>{attr.description}</div>
                        <label htmlFor={ 'formUserId' + attr.participantIdentifierId } className="control-label">{ attr.label }</label>
                        {index == 0 &&
                            <input
                                type="text"
                                name={ attr.label }
                                tabIndex={ index + 1 }
                                id={ 'formUserId' + attr.participantIdentifierId }
                                className="gq-input"
                                autoFocus
                                value={ this.state.value }
                                onChange={ this.handleField }
                                onKeyPress={ this.checkEnter } />

                        }
                        {
                            index > 0 &&
                            <input
                                type="text"
                                name={ attr.label }
                                tabIndex={ index + 1 }
                                id={ 'formUserId' + attr.participantIdentifierId }
                                className="gq-input"
                                value={ this.state.value }
                                onChange={ this.handleField }
                                onKeyPress={ this.checkEnter } />
                        }
                    </div>
                    )
                }
                {verification.activation &&
                        verification.activation.activationFields.map( ( attr, index ) =>
                        <div className="activation-field-desc" key={ attr.participantIdentifierId }>
                            <div>{attr.description}</div>
                        <label htmlFor={ 'formUserId' + attr.participantIdentifierId } className="control-label">{ attr.label }</label>
                        {index == 0 &&
                            <input
                                type="text"
                                name={ attr.label }
                                tabIndex={ index + 1 }
                                id={ 'formUserId' + attr.participantIdentifierId }
                                className="gq-input"
                                autoFocus
                                value={ this.state.value }
                                onChange={ this.handleField }
                                onKeyPress={ this.checkEnter } />

                        }
                        {
                            index > 0 &&
                            <input
                                type="text"
                                name={ attr.label }
                                tabIndex={ index + 1 }
                                id={ 'formUserId' + attr.participantIdentifierId }
                                className="gq-input"
                                value={ this.state.value }
                                onChange={ this.handleField }
                                onKeyPress={ this.checkEnter } />
                        }
                    </div>
                    )
                }
            </div>
                    <LoaderButton
                        fetching={ loggingIn }
                        handleClick={ this.onSubmit }
                        disabled={ this.state.disabled }
                        customClass="btn btn-block btn-primary form-action-btn"
                        tabIndex={ activation.activation ? activation.activation.activationFields.length + 1 : verification.activation.activationFields.length + 1 }
                        done={ this.props.done }>
                        {content[ 'system.button.submit' ]}
                    </LoaderButton>
                    <p className="help">
                        <a tabIndex="3" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'help' ) }>
                            <span>{ content[ 'login.loginpage.help' ] }</span>
                        </a>
                    </p>

		</NarrowForm>
    );
  }
}


export default ActivationAttr;
