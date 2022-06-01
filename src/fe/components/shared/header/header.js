import React from 'react';

import './header.scss';

const Header = ( props ) => {
	const { altText, skin } = props;
	return (
		<header id="header" className="site-header">
			<h1 className="logo">
				<a href="homePage.do">
					<img src={ 'assets/skins/' + skin + '/img/logo-primary.png' } alt={ altText } />
				</a>
			</h1>
		</header>
	);
};

export default Header;
