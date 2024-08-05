import React, { useState } from 'react';


import './App.css';


function LoginPage() {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };


    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        //console.log('Login with:', username, password);

    };

    return (
        
        <div className = "login-page">
            <form onSubmit = {handleSubmit} className = "login-form">
                <div ><h2 className="loginTitle">Welcome</h2></div>
                <div className="username-section">
                    <label className= "login-username">Username: </label>
                    <input className= "username-input" id ="username"  value={username} onChange={handleUsernameChange} />  
                </div>
                <div className="password-section">
                    <label className= "login-password">Password: </label>
                    <input className= "password-input" type= "password" value={password} onChange ={handlePasswordChange} />
                </div >
                 <button className ="button" type ="submit"> Login</button>
            </form>
        </div>
    );

}
export default LoginPage;
