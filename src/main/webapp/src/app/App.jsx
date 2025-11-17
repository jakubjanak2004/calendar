import {Navigate, Route, Routes} from "react-router-dom";
import {LoginPage} from "./pages/auth/LoginPage.jsx";
import {Dashboard} from "./pages/dashboard/Dashboard.jsx";
import {RequireAuth} from "../config/RequireAuth.jsx";
import {NotFoundPage} from "./pages/NotFoundPage.jsx";
import {SignupPage} from "./pages/auth/SignupPage.jsx";
import {AddEventPage} from "./pages/events/AddEventPage.jsx";
import GroupsPage from "./pages/groups/GroupsPage.jsx";
import UserGroupDetail from "../components/groups/UserGroupDetail.jsx";
import ManageMembersPage from "./pages/groups/ManageMembersPage.jsx";
import Invitations from "./pages/dashboard/Invitations.jsx";
import AddGroupPage from "./pages/groups/AddGroupPage.jsx";
import "react-big-calendar/lib/css/react-big-calendar.css";
import UpdateEventPage from "./pages/events/UpdateEventPage.jsx";
import CalendarUserDetail from "../components/users/CalendarUserDetail.jsx";

function App() {
    return <Routes>
        {/* public */}
        <Route path="/login" element={<LoginPage/>}/>
        <Route path="/signup" element={<SignupPage/>}/>

        <Route path="/" element={<RequireAuth/>}>
            <Route index element={<Navigate to="dashboard" replace/>}/>
            <Route path="dashboard" element={<Dashboard/>}>
                <Route path="event/:eventId" element={<UpdateEventPage/>}/>
                <Route path="eventOwner/:eventOwnerId/addEvent" element={<AddEventPage/>}/>
            </Route>
            <Route path="invitations" element={<Invitations/>}></Route>
            <Route path="/:eventOwnerId/addEvent" element={<AddEventPage/>}/>
            <Route path="groups" element={<GroupsPage/>}/>
            <Route path={"/groups/addGroup"} element={<AddGroupPage/>}/>
            <Route path="groups/:groupId" element={<UserGroupDetail/>}>
                <Route path="event/:eventId" element={<UpdateEventPage/>}/>
                <Route path="eventOwner/:eventOwnerId/addEvent" element={<AddEventPage/>}/>
            </Route>
            <Route path="groups/:groupId/manageMembers" element={<ManageMembersPage/>}/>
            <Route path="user/:userId" element={<CalendarUserDetail/>}/>
        </Route>

        {/* 404 */}
        <Route path="*" element={<NotFoundPage/>}/>
    </Routes>
}

export default App
