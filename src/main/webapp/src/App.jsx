import {Navigate, Route, Routes} from "react-router-dom";
import {LoginPage} from "./pages/auth/LoginPage.jsx";
import {Dashboard} from "./pages/dashboard/Dashboard.jsx";
import {RequireAuth} from "./pages/auth/RequireAuth.jsx";
import {NotFound} from "./pages/NotFound.jsx";
import {SignupPage} from "./pages/auth/SignupPage.jsx";
import {AddEvent} from "./pages/dashboard/eventCRUD/AddEvent.jsx";

function App() {
    return <Routes>
        {/* public */}
        <Route path="/login" element={<LoginPage/>}/>
        <Route path="/signup" element={<SignupPage/>}/>

        <Route path="/" element={<RequireAuth/>}>
            <Route index element={<Navigate to="dashboard" replace/>}/>
            <Route path="dashboard" element={<Dashboard/>} />
            <Route path={"addEvent"} element={<AddEvent/>} />
        </Route>

        {/* 404 */}
        <Route path="*" element={<NotFound/>}/>
    </Routes>
}

export default App
