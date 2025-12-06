import AppCalendar from "../../../components/calendar/AppCalendar.jsx";
import GroupsButton from "../../../components/buttons/GroupsButton.jsx";
import InvitationsButton from "../../../components/buttons/InvitationsButton.jsx";
import SignOutButton from "../../../components/buttons/SignOutButton.jsx";
import {useAuth} from "../../../context/AuthContext.jsx";
import {Outlet} from "react-router-dom";
import {useEffect, useState} from "react";
import {http} from "../../../lib/http.jsx";
import SettingsButton from "../../../components/buttons/SettingsButton.jsx";

export function Dashboard() {
    const {userId, color} = useAuth()
    const [eventOwners, setEventOwners] = useState([])

    async function loadEventOwners() {
        const res = await http.client.get('groupMemberships/me')
        const data = res.data.map(membership => {
            membership.canManage = membership.membershipRole === "ADMIN" || membership.membershipRole === "EDITOR";
            membership.id = membership.groupId
            return membership
        })
        setEventOwners([{
            id: userId,
            canManage: true,
            color: color,
        }, ...data])
    }

    useEffect(() => {
        loadEventOwners().finally()
    }, []);

    return <>
        <header>
            <nav>
                <SignOutButton/>
                <GroupsButton/>
                <InvitationsButton/>
                <SettingsButton />
            </nav>
        </header>
        <AppCalendar memberships={eventOwners} canAddEvents={true}/>
        <div>
            <Outlet/>
        </div>
    </>
}