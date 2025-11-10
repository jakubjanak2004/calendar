import { Link, useParams } from "react-router-dom";
import { useCallback, useEffect, useMemo, useState } from "react";
import { http } from "../../../lib/http.jsx";
import CalendarUser from "../../../components/users/CalendarUser.jsx";
import { useAuth } from "../../../features/AuthContext.jsx";

export default function ManageMembersPage() {
    const { groupId } = useParams();
    const { userId: currentUserId } = useAuth();

    // --- Members & Invited (from backend) ---
    const [members, setMembers] = useState([]);
    const [invited, setInvited] = useState([]);
    const [loadingMembers, setLoadingMembers] = useState(false);
    const [loadingInvited, setLoadingInvited] = useState(false);

    // Sets for fast filtering
    const memberIdSet = useMemo(() => new Set(members.map(u => u.id)), [members]);
    const invitedIdSet = useMemo(() => new Set(invited.map(u => u.id)), [invited]);

    const otherMembers = useMemo(
        () => members.filter(m => m.id !== currentUserId),
        [members, currentUserId]
    );

    // Loaders
    const getMembers = useCallback(async () => {
        setLoadingMembers(true);
        try {
            // If you expose /members, use that:
            // const res = await http.client.get(`groups/${groupId}/members`);
            // If you still use /users for "members only", keep this:
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
    const removeMember = useCallback(
        async (userId) => {
            try {
                await http.client.delete(`groups/${groupId}/users/${userId}`);
                setMembers(cur => cur.filter(m => m.id !== userId));
            } catch (e) {
                console.error(e);
                alert("Failed to remove member.");
            }
        },
        [groupId]
    );

    // --- Pageable full users directory (invite candidates) ---
    const [users, setUsers] = useState([]); // current page of directory
    const [page, setPage] = useState(0);
    const [size] = useState(10);
    const [last, setLast] = useState(false);
    const [loadingUsers, setLoadingUsers] = useState(false);
    const [inviting, setInviting] = useState(null); // userId being invited

    const fetchUsers = useCallback(
        async (pageNum) => {
            setLoadingUsers(true);
            try {
                const res = await http.client.get(`/users`, { params: { page: pageNum, size } });
                const { content = [], last: isLast = true } = res.data ?? {};
                setUsers(content);
                setLast(isLast);
                setPage(pageNum);
            } finally {
                setLoadingUsers(false);
            }
        },
        [size]
    );

    // Invite: create/upgrade membership to INVITED
    const inviteUser = useCallback(
        async (userId) => {
            setInviting(userId);
            try {
                await http.client.post(`groups/${groupId}/users/${userId}`);
                // Update UI:
                setInvited(cur => [...cur, users.find(u => u.id === userId)].filter(Boolean)); // add to invited list
                setUsers(cur => cur.filter(u => u.id !== userId)); // hide from pager
            } catch (e) {
                console.error(e);
                alert("Failed to invite user.");
            } finally {
                setInviting(null);
            }
        },
        [groupId, users]
    );

    // Initial loads
    useEffect(() => {
        getMembers().catch(console.error);
        getInvited().catch(console.error);
    }, [getMembers, getInvited]);

    useEffect(() => {
        fetchUsers(0).catch(console.error);
    }, [fetchUsers]);

    // Filter candidates: exclude me, members, invited
    const inviteCandidates = useMemo(
        () =>
            users.filter(
                u => u.id !== currentUserId && !memberIdSet.has(u.id) && !invitedIdSet.has(u.id)
            ),
        [users, currentUserId, memberIdSet, invitedIdSet]
    );

    return (
        <>
            <div className="mb-3">
                <Link to={`/groups/${groupId}`} className="underline">Back to Group</Link>
            </div>

            <h2 className="text-xl font-semibold mb-2">Current members</h2>
            <ul className="space-y-2 mb-6">
                {loadingMembers && <li className="text-sm text-gray-500">Loading members…</li>}
                {!loadingMembers && otherMembers.length === 0 && (
                    <li className="text-sm text-gray-500">No other members.</li>
                )}
                {otherMembers.map(member => (
                    <li key={member.id} className="flex items-center gap-3">
                        <CalendarUser user={member} />
                        <button
                            onClick={() => removeMember(member.id)}
                            className="px-3 py-1 rounded bg-red-600 text-white"
                        >
                            Remove
                        </button>
                    </li>
                ))}
            </ul>

            <h3 className="text-lg font-semibold mb-2">Pending invitations</h3>
            <ul className="space-y-2 mb-6">
                {loadingInvited && <li className="text-sm text-gray-500">Loading invites…</li>}
                {!loadingInvited && invited.length === 0 && (
                    <li className="text-sm text-gray-500">No pending invites.</li>
                )}
                {invited.map(u => (
                    <li key={u.id} className="flex items-center justify-between">
                        <CalendarUser user={u} />
                        <span className="text-sm text-amber-700">Invited</span>
                        {/* Optional: add "Cancel invite" */}
                        {/* <button className="px-3 py-1 rounded border" onClick={() => cancelInvite(u.id)}>Cancel</button> */}
                    </li>
                ))}
            </ul>

            <h2 className="text-xl font-semibold mb-2">Invite users</h2>
            <div className="border rounded p-3">
                <ul className="space-y-2">
                    {inviteCandidates.map(u => (
                        <li key={u.id} className="flex items-center justify-between">
                            <CalendarUser user={u} />
                            <button
                                className="px-3 py-1 rounded bg-blue-600 text-white disabled:opacity-60"
                                onClick={() => inviteUser(u.id)}
                                disabled={inviting === u.id}
                            >
                                {inviting === u.id ? "Inviting…" : "Invite"}
                            </button>
                        </li>
                    ))}
                    {!loadingUsers && inviteCandidates.length === 0 && (
                        <li className="text-sm text-gray-500">No users to invite on this page.</li>
                    )}
                </ul>

                <div className="flex items-center justify-between mt-4">
                    <button
                        className="px-3 py-1 rounded border"
                        onClick={() => fetchUsers(Math.max(0, page - 1))}
                        disabled={loadingUsers || page === 0}
                    >
                        Previous
                    </button>
                    <span className="text-sm">Page {page + 1}</span>
                    <button
                        className="px-3 py-1 rounded border"
                        onClick={() => fetchUsers(page + 1)}
                        disabled={loadingUsers || last}
                    >
                        Next
                    </button>
                </div>
            </div>
        </>
    );
}
