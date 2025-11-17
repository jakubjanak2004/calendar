import {Link, Outlet, useNavigate, useParams} from "react-router-dom";
import AppCalendar from "../calendar/AppCalendar.jsx";
import {useEffect, useState} from "react";
import {http} from "../../lib/http.jsx";
import GroupsButton from "../buttons/GroupsButton.jsx";

export default function UserGroupDetail() {
    const {groupId} = useParams();
    const [group, setGroup] = useState({})
    const [groupMembers, setGroupMembers] = useState([])
    const [userRole, setUserRole] = useState("")
    const navigate = useNavigate()

    const canAddEvents = userRole === "EDITOR" || userRole === "ADMIN";
    const canManageMembership = userRole === "ADMIN";

    async function getGroupMembers() {
        let res = await http.client.get(`groups/${groupId}/users`)
        setGroupMembers(res.data)
    }

    async function getUserRole() {
        const res = await http.client.get(`groups/${groupId}/users/me/membershipRole`)
        setUserRole(res.data)
    }

    async function fetchGroupInfo() {
        const res = await http.client.get(`groups/${groupId}`)
        setGroup(res.data)
    }

    async function leaveGroup() {
        const ok = confirm(`Do you really want to leave ${group.name}?`)
        if (!ok) return
        await http.client.delete(`groups/${groupId}/users/me`)
        navigate('/groups', {replace: true})
    }

    useEffect(() => {
        getGroupMembers().finally()
        getUserRole().finally()
    }, [groupId]);

    useEffect(() => {
       fetchGroupInfo().finally()
    }, [groupId]);

    return <>
        <header className={"sticky"}>
            <nav>
                <GroupsButton/>
                <button className={"button"} onClick={leaveGroup}>Leave Group</button>
                {canManageMembership && (
                    <Link className={"button"} to={'manageMembers'}>Manage Members</Link>
                )}
            </nav>
            <h1>{group.name}</h1>
        </header>
        {/*<ul>*/}
        {/*    {groupMembers.map((member, i) => (*/}
        {/*        <li key={i}>*/}
        {/*            <CalendarUser user={member}/>*/}
        {/*        </li>*/}
        {/*    ))}*/}
        {/*</ul>*/}
        <AppCalendar eventOwnerId={groupId} canManageEvents={canAddEvents}/>
        <Outlet/>
    </>
}