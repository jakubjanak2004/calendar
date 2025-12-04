import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './app/App.jsx'
import {BrowserRouter} from "react-router-dom";
import {AuthProvider} from "./context/AuthContext.jsx";
import "./styles/style.css"
import {EventProvider} from "./context/EventContext.jsx";

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <BrowserRouter>
            <AuthProvider>
                <EventProvider>
                    <App/>
                </EventProvider>
            </AuthProvider>
        </BrowserRouter>
    </StrictMode>,)