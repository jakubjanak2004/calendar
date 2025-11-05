import {Navigate, Route, Routes} from "react-router-dom";
import {LoginPage} from "./auth/LoginPage.jsx";
import {Dashboard} from "./pages/dashboard/Dashboard.jsx";
import {RequireAuth} from "./auth/RequireAuth.jsx";
import {NotFound} from "./pages/NotFound.jsx";
import {SignupPage} from "./auth/SignupPage.jsx";
import {AddEvent} from "./pages/addEvent/AddEvent.jsx";
import GroupsPage from "./pages/groups/GroupsPage.jsx";
import UserGroupDetail from "./pages/groups/UserGroupDetail.jsx";

function App() {
    return <Routes>
        {/* public */}
        <Route path="/login" element={<LoginPage/>}/>
        <Route path="/signup" element={<SignupPage/>}/>

        <Route path="/" element={<RequireAuth/>}>
            <Route index element={<Navigate to="dashboard" replace/>}/>
            <Route path="dashboard" element={<Dashboard/>} />
            <Route path="/:eventOwnerId/addEvent" element={<AddEvent/>} />
            <Route path="groups" element={<GroupsPage />} />
            <Route path="groups/:groupId" element={<UserGroupDetail />} />
        </Route>

        {/* 404 */}
        <Route path="*" element={<NotFound/>}/>
    </Routes>
}

export default App
