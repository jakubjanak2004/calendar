import {useEffect, useState} from "react";
import {http} from "../../../lib/http.jsx";
import DashboardButton from "../../../components/buttons/DashboardButton.jsx";

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
        <header>
            <DashboardButton />
        </header>
        <h1>Invitations</h1>
        {invitations.length === 0 &&
            <p>You have no invitations</p>
        }
        <ul>
            {invitations.map(userGroup => (
                <li className={"li-element"} key={userGroup.id}>
                    <p>
                        {userGroup.name}
                    </p>
                    <button
                        className={"invite-button"}
                        onClick={() => acceptInvite(userGroup.id)}>
                        Accept
                    </button>
                </li>
            ))}
        </ul>
    </>
}