// src/components/Header.js
import React from 'react';
import './Header.css';
import SignOutButton from './SignOutButton';

const Header = ({ userName, onSignOut }) => {
  return (
    <header className="header">
      <div className="header-content">
        <h1>{userName}</h1>
        <SignOutButton onSignOut={onSignOut} />
      </div>
    </header>
  );
};

export default Header;
