import {useParams} from "react-router-dom";
import {useCallback, useEffect, useMemo, useState} from "react";
import {http} from "../../../lib/http.jsx";
import CalendarUser from "../../../components/users/CalendarUser.jsx";
import {useAuth} from "../../../features/AuthContext.jsx";
import BackButton from "../../../components/buttons/BackButton.jsx";

// todo add membership role administration, that an admin can change usersMembershipRoles
export default function ManageMembersPage() {
    const {groupId} = useParams();
    const {userId: currentUserId} = useAuth();

    // --- Members & Invited (from backend) ---
    const [members, setMembers] = useState([]);
    const [invited, setInvited] = useState([]);
    const [loadingMembers, setLoadingMembers] = useState(false);
    const [loadingInvited, setLoadingInvited] = useState(false);

    // Sets for fast filtering
    const memberIdSet = useMemo(() => new Set(members.map(u => u.id)), [members]);
    const invitedIdSet = useMemo(() => new Set(invited.map(u => u.id)), [invited]);

    const otherMembers = useMemo(() => members.filter(m => m.id !== currentUserId), [members, currentUserId]);

    // Loaders
    const getMembers = useCallback(async () => {
        setLoadingMembers(true);
        try {
            const res = await http.client.get(`groups/${groupId}/users`);
            setMembers(res.data ?? []);
        } finally {
            setLoadingMembers(false);
        }
    }, [groupId]);

    const getInvited = useCallback(async () => {
        setLoadingInvited(true);
        try {
            // new endpoint you mentioned:
            const res = await http.client.get(`groups/${groupId}/users/invited`);
            setInvited(res.data ?? []);
        } finally {
            setLoadingInvited(false);
        }
    }, [groupId]);

    // Remove member (admin action)
    const removeMember = useCallback(async (user) => {
        const ok = confirm(`Do you really want to remove ${user.username} from group?`)
        if (!ok) return
        try {
            await http.client.delete(`groups/${groupId}/users/${user.id}`);
            setMembers(cur => cur.filter(m => m.id !== user.id));
            fetchUsers(page).finally()

        } catch (e) {
            console.error(e);
            alert("Failed to remove member.");
        }
    }, [groupId]);

    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);
    const [size] = useState(10);
    const [last, setLast] = useState(false);
    const [loadingUsers, setLoadingUsers] = useState(false);
    const [inviting, setInviting] = useState(null);

    const fetchUsers = useCallback(async (pageNum) => {
        setLoadingUsers(true);
        try {
            const res = await http.client.get(`/users`, {
                params: {page: pageNum, size, sort: "username,asc"}
            });
            const {content = []} = res.data ?? {};
            setUsers(content);
            setLast(pageNum >= res.data.page.totalPages - 1);
            setPage(pageNum);
        } finally {
            setLoadingUsers(false);
        }
    }, [size]);

    const inviteUser = useCallback(async (userId) => {
        setInviting(userId);
        try {
            await http.client.post(`groups/${groupId}/users/${userId}`);
            setInvited(cur => [...cur, users.find(u => u.id === userId)].filter(Boolean));
            setUsers(cur => cur.filter(u => u.id !== userId));
        } catch (e) {
            console.error(e);
            alert("Failed to invite user.");
        } finally {
            setInviting(null);
        }
    }, [groupId, users]);

    const cancelInvitation = useCallback(async (user) => {
        const ok = confirm(`Do you really want to cancel invitation for ${user.username}?`)
        if (!ok) return
        const userId = user.id
        setInviting(userId);
        try {
            await http.client.delete(`groups/${groupId}/users/${userId}`);
            const user = invited.find(user => user.id === userId)
            setInvited(cur => cur.filter(u => u.id !== userId));
            fetchUsers(page).finally()
        } catch (e) {
            console.error(e);
            alert("Failed to invite user.");
        } finally {
            setInviting(null);
        }
    }, [groupId, users]);

    // Initial loads
    useEffect(() => {
        getMembers().catch(console.error);
        getInvited().catch(console.error);
    }, [getMembers, getInvited]);

    useEffect(() => {
        fetchUsers(0).catch(console.error);
    }, [fetchUsers]);

    const inviteCandidates = useMemo(() => users.filter(u => u.id !== currentUserId && !memberIdSet.has(u.id) && !invitedIdSet.has(u.id)), [users, currentUserId, memberIdSet, invitedIdSet]);

    return <>
        <header>
            <BackButton/>
        </header>
        <h2 className="text-xl font-semibold mb-2">Current members</h2>
        <ul className="space-y-2 mb-6">
            {loadingMembers && <li className="text-sm text-gray-500">Loading members…</li>}
            {!loadingMembers && otherMembers.length === 0 && (
                <li className="text-sm text-gray-500">No other members.</li>)}
            {otherMembers.map(member => (<li key={member.id} className="flex items-center gap-3">
                <CalendarUser user={member}>
                    <button
                        onClick={() => removeMember(member)}
                        className="remove-button"
                    >
                        Remove
                    </button>
                </ CalendarUser>
            </li>))}
        </ul>

        <h3>Pending invitations</h3>
        <ul>
            {loadingInvited && <li>Loading invites…</li>}
            {!loadingInvited && invited.length === 0 && (<li>No pending invites.</li>)}
            {invited.map(u => (<li key={u.id}>
                <CalendarUser user={u}>
                    <button
                        onClick={() => cancelInvitation(u)}
                        className="remove-button"
                    >
                        Cancel Invitation
                    </button>
                </CalendarUser>
            </li>))}
        </ul>

        <h2>Invite users</h2>
        <div>
            <button
                onClick={() => fetchUsers(Math.max(0, page - 1))}
                disabled={loadingUsers || page === 0}
            >
                Previous
            </button>
            <span>Page {page + 1}</span>
            <button
                onClick={() => fetchUsers(page + 1)}
                disabled={loadingUsers || last}
            >
                Next
            </button>
        </div>
        <div>
            <ul>
                {inviteCandidates.map(u => (<li key={u.id}>
                    <CalendarUser user={u}>
                        <button
                            className={"invite-button"}
                            onClick={() => inviteUser(u.id)}
                            disabled={inviting === u.id}
                        >
                            {inviting === u.id ? "Inviting…" : "Invite"}
                        </button>
                    </CalendarUser>
                </li>))}
                {!loadingUsers && inviteCandidates.length === 0 && (<li>No users to invite on this page.</li>)}
            </ul>
        </div>
    </>
}
