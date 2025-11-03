import {Route, Routes} from "react-router-dom";
import {LoginPage} from "./pages/auth/LoginPage.jsx";
import {Dashboard} from "./pages/dashboard/Dashboard.jsx";
import {RequireAuth} from "./pages/auth/RequireAuth.jsx";
import {NotFound} from "./pages/NotFound.jsx";
import {SignupPage} from "./pages/auth/SignupPage.jsx";

function App() {
    return  <Routes>
        {/* public */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

        {/* protected wrapper ON "/" */}
        <Route path="/" element={<RequireAuth />}>
            <Route index element={<Dashboard />} />
            {/* more protected routes: <Route path="settings" element={<Settings/>} /> */}
        </Route>

        {/* 404 */}
        <Route path="*" element={<NotFound />} />
    </Routes>
}

export default App
