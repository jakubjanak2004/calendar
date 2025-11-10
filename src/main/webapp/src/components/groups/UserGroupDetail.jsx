import {Link, useLocation, useNavigate, useParams} from "react-router-dom";
import Calendar from "../calendar/Calendar.jsx";
import {useEffect, useState} from "react";
import {http} from "../../lib/http.jsx";
import CalendarUser from "../users/CalendarUser.jsx";
import {useAuth} from "../../features/AuthContext.jsx";

export default function UserGroupDetail() {
    const { groupId } = useParams();
    const [groupMembers, setGroupMembers] = useState([])
    const [userRole, setUserRole] = useState("")
    const location = useLocation();
    const groupName = location.state?.groupName;
    const navigate = useNavigate()


    const canEdit = userRole === "EDITOR" || userRole === "ADMIN";
    const canManageMembership = userRole === "ADMIN";

    async function getGroupMembers() {
        let res = await http.client.get(`groups/${groupId}/users`)
        setGroupMembers(res.data)
    }

    async function getUserRole() {
        const res = await http.client.get(`groups/${groupId}/users/me/membershipRole`)
        setUserRole(res.data)
        console.log(userRole)
    }

    async function leaveGroup() {
        await http.client.delete(`groups/${groupId}/users/me`)
        navigate('/groups', {replace: true})
    }

    useEffect(() => {
        getGroupMembers().finally()
        getUserRole().finally()
    }, [groupId]);

    return <>
        <Link to={"/groups"}>Groups</Link>
        <h1>{groupName}</h1>
        <button onClick={leaveGroup}>Leave Group</button>
        {canManageMembership && (
            <Link to={'manageMembers'}>Manage Members</Link>
        )}
        <ul>
            {groupMembers.map(member => (
                <li>
                    <CalendarUser user={member}/>
                </li>
            ))}
        </ul>
        <Calendar eventOwnerId={groupId}/>
    </>
}