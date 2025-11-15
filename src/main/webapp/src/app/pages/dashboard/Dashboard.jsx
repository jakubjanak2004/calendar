import AppCalendar from "../../../components/calendar/AppCalendar.jsx";
import GroupsButton from "../../../components/buttons/GroupsButton.jsx";
import InvitationsButton from "../../../components/buttons/InvitationsButton.jsx";
import SignOutButton from "../../../components/buttons/SignOutButton.jsx";
import {useAuth} from "../../../features/AuthContext.jsx";

export function Dashboard() {
    const {userId} = useAuth()
    return <>
        <header>
            <nav>
                <SignOutButton />
                <GroupsButton />
                <InvitationsButton />
            </nav>
        </header>
        <AppCalendar eventOwnerId={userId} canAddEvents={true}/>
    </>
}