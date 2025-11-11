import {useEffect, useState} from "react";
import {http} from "../../../lib/http.jsx";
import {Link} from "react-router-dom";

export default function Invitations() {
    const [invitations, setInvitations] = useState([])

    async function getInvitations() {
        const res = await http.client.get("groups/invitations")
        setInvitations(res.data)
    }

    async function acceptInvite(groupId) {
        await http.client.post(`groups/${groupId}/users/me`)
        setInvitations(prev => prev.filter(inv => inv.id !== groupId));
    }

    useEffect(() => {
        getInvitations().finally()
    }, []);

    return <>
        <Link to={"/dashboard"}>Dashboard</Link>
        <h1>Invitations</h1>
        <ul>
            {invitations.map(userGroup => (
                <li key={userGroup.id}>
                    <p>
                        {userGroup.name}
                    </p>
                    <button onClick={() => acceptInvite(userGroup.id)}>Accept</button>
                </li>
            ))}
        </ul>
    </>
}