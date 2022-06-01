import React from 'react';
import PropTypes from 'prop-types';
import { CheckmarkCircleIcon, CloseCircleIcon } from '../../shared/icons/icons.js';
import PasswordInput from '../../shared/password-input/password-input.js';

import './password-helper.scss';

class PasswordHelper extends React.Component {

    static propTypes = {
        passwordRules: PropTypes.object.isRequired
    };

    constructor( props ) {
        super( props );

        this.state = {
            focused: false,
            passwordRulesChecks: {
                isLowerCaseRequired: null,
                isUpperCaseRequired: null,
                isSpecialCharRequired: null,
                isNumberRequired: null,
                hasValidLength: null
            }
        };

    }

    lengthTest = ( passwordValue, minLength ) => {
        return passwordValue.length >= minLength;
    };

    handleFocus = event => {
        this.setState( { focused: true } );
    };

    handleBlur = event => {
        this.setState( { focused: false } );
    };

    componentWillReceiveProps( nextProps ) {
        const passwordValue = nextProps.value;
        const {
            isLowerCaseRequired,
            isUpperCaseRequired,
            isSpecialCharRequired,
            isNumberRequired,
            minLength
        } = this.props.passwordRules;
        const { minRules } = this.props;
        this.setState( {
            passwordRulesChecks: {
                isLowerCaseRequired: isLowerCaseRequired ? /^(?=.*[a-z]).+$/.test( passwordValue ) : true,
                isUpperCaseRequired: isUpperCaseRequired ? /^(?=.*[A-Z]).+$/.test( passwordValue ) : true,
                isSpecialCharRequired: isSpecialCharRequired ? /[-!$%#@^&*()_+|~=`{}[\]:";'<>?,./]/.test ( passwordValue ) : true,
                isNumberRequired: isNumberRequired ? /[0-9]/.test( passwordValue ) : true
            }
        } );
        const passValidate = {
            passwordRulesChecks: {
                isLowerCaseRequired: isLowerCaseRequired ? /^(?=.*[a-z]).+$/.test( passwordValue ) : true,
                isUpperCaseRequired: isUpperCaseRequired ? /^(?=.*[A-Z]).+$/.test( passwordValue ) : true,
                isSpecialCharRequired: isSpecialCharRequired ? /[-!$%#@^&*()_+|~=`{}[\]:";'<>?,./]/.test ( passwordValue ) : true,
                isNumberRequired: isNumberRequired ? /[0-9]/.test( passwordValue ) : true
            }
        };
        const offset = 4 - this.props.passwordRules.numUsed;
        let rulePass = 0;
        Object.keys( passValidate.passwordRulesChecks ).forEach( function ( key ) {
            if( passValidate.passwordRulesChecks[ key ] === true ) {
                rulePass++;
            }
        } );
        rulePass -= offset;
        if( rulePass >= minRules ) {
                this.setState( { rulePass: true } );
            } else {
                this.setState( { rulePass: false } );
            }
            this.setState( { minLength: this.lengthTest( passwordValue, minLength ) } );
    }

    render() {

        const {
            passwordRules,
            passwordLabels,
            onChange,
            onKeyPress,
            label,
            name,
            value,
            tabIndex,
            disable,
            autoFocus,
            minRulesLabel,
            ignore
        } = this.props;
        const { passwordRulesChecks, rulePass, minLength } = this.state;
        const passwordRulesArray = [];

        for( const key in passwordRules ) {
            if ( key !== 'minLength' ) {
                passwordRulesArray.push( key );
            }
        }
        return (
            <div className={
                this.state.focused
                ? 'password-helper-input focused'
                : 'password-helper-input'
            }>
                <PasswordInput
                    label={ label }
                    name={ name }
                    value={ value }
                    autoFocus={ autoFocus }
                    disabled= { disable }
                    tabIndex={ tabIndex }
                    onChange={ onChange }
                    onKeyPress={ onKeyPress }
                    onFocus={ this.handleFocus }
                    onBlur={ this.handleBlur }
                />
                {!ignore &&
                    <div className={
                        this.state.focused
                        ? 'password-rules-container'
                        : 'password-rules-container hide'
                    }>
                    <div className={
                        passwordRules
                        ? 'rule'
                        : 'hide' }>

                        <div className={
                            minLength
                            ? 'rule-icon checkmark-icon success'
                            : 'hide' }>
                            <CheckmarkCircleIcon/>
                        </div>

                        <div className={
                            !minLength && passwordRulesChecks.hasValidLength !== null
                            ? 'rule-icon close-circle-icon'
                            : 'hide' }>
                            <span><CloseCircleIcon/></span>
                        </div>

                        <span>{ passwordLabels.length }</span>
                    </div>
                    <div className={ 'rule' }>

                        <div className={
                            rulePass
                            ? 'rule-icon checkmark-icon success'
                            : 'hide' }>
                            <CheckmarkCircleIcon/>
                        </div>

                        <div className={
                            !rulePass
                            ? 'rule-icon close-circle-icon'
                            : 'hide' }>
                            <span><CloseCircleIcon/></span>
                        </div>

                        <span>{ minRulesLabel }</span>
                    </div>
                    {
                        passwordRulesArray.map( ( rule, index ) => {
                            if( rule != 'numUsed' ) {
                                return(
                                    <div key={ index } className={
                                        passwordRules[ rule ]
                                        ? 'rule child-label'
                                        : 'hide' } >
                                        <div className={
                                            passwordRulesChecks[ rule ]
                                            ? 'rule-icon checkmark-icon success'
                                            : 'hide' }>
                                            <CheckmarkCircleIcon/>
                                        </div>

                                        <div className={
                                            !passwordRulesChecks[ rule ] && passwordRulesChecks[ rule ] !== null
                                            ? 'rule-icon close-circle-icon'
                                            : 'hide' }>
                                            <span className="circle"></span>
                                        </div>

                                        <span>{ passwordLabels[ rule ] }</span>
                                    </div>
                                );
                            }
                        } )
                    }


                </div>
                }
            </div>
        );
    }
}

export default PasswordHelper;
