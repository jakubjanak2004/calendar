import AppCalendar from "../../../components/calendar/AppCalendar.jsx";
import GroupsButton from "../../../components/buttons/GroupsButton.jsx";
import InvitationsButton from "../../../components/buttons/InvitationsButton.jsx";
import SignOutButton from "../../../components/buttons/SignOutButton.jsx";
import {useAuth} from "../../../context/AuthContext.jsx";
import {Outlet} from "react-router-dom";

export function Dashboard() {
    const {userId} = useAuth()
    return <>
        <header>
            <nav>
                <SignOutButton/>
                <GroupsButton/>
                <InvitationsButton/>
            </nav>
        </header>
        <AppCalendar eventOwnerId={userId} canManageEvents={true}/>
        <div>
            <Outlet/>
        </div>
    </>
}