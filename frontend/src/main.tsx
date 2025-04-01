import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';  // Import the AuthProvider
import App from './App';

const rootElement = document.getElementById('root');

if (rootElement) {
  const root = ReactDOM.createRoot(rootElement);
  root.render(
    <React.StrictMode>
      <BrowserRouter>
        <AuthProvider>  {/* Wrap AuthProvider inside BrowserRouter */}
          <App />
        </AuthProvider>
      </BrowserRouter>
    </React.StrictMode>
  );
}
