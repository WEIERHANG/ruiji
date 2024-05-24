import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

var container = document.getElementById('root');
var root = ReactDOM.createRoot(container);

root.render(<App />);

module.hot.accept()
