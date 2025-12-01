import {useEffect, useState} from "react";
import {http} from "../../../lib/http.jsx";
import UserGroupMembership from "../../../components/groups/UserGroupMembership.jsx";
import DashboardButton from "../../../components/buttons/DashboardButton.jsx";
import AddGroupButton from "../../../components/buttons/AddGroupButton.jsx";

export default function GroupsPage() {
    const [content, setContent] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);

    async function getUserGroups(p = page, s = size) {
        setLoading(true);
        try {
            const res = await http.client.get("/groupMemberships/me", {
                params: {page: p, size: s, sort: "groupName,asc"},
            });
            const data = res.data;
            setContent(data.content ?? []);
            setPage(data.number ?? p);
            setSize(data.size ?? s);
            setTotalPages(data.totalPages ?? 0);
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        getUserGroups(page, size).finally()
    }, [page, size]);

    const hasPrev = page > 0;
    const hasNext = page + 1 < totalPages;

    return (
        <>
            <header>
                <nav>
                    <DashboardButton/>
                    <AddGroupButton/>
                </nav>
                <h1>Your Groups</h1>
            </header>

            <div className={"pagination"}>
                <button onClick={() => setPage(0)} disabled={!hasPrev}>⏮ First</button>
                <button onClick={() => setPage(p => Math.max(p - 1, 0))} disabled={!hasPrev}>◀ Prev</button>
                <span>Page {page + 1} / {Math.max(totalPages, 1)}</span>
                <button onClick={() => setPage(p => (hasNext ? p + 1 : p))} disabled={!hasNext}>Next ▶</button>
                <button onClick={() => setPage(totalPages - 1)} disabled={!hasNext}>Last ⏭</button>

                <label style={{marginLeft: 12}}>
                    Page size:&nbsp;
                    <select value={size} onChange={e => {
                        setPage(0);
                        setSize(Number(e.target.value));
                    }}>
                        {[5, 10, 20, 50].map(n => <option key={n} value={n}>{n}</option>)}
                    </select>
                </label>
            </div>

            {loading && <p>Loading…</p>}
            <ul>
                {content.map((groupMembership, i) => (
                    <li key={i}>
                        <UserGroupMembership key={groupMembership.id} groupMembership={groupMembership}/>
                    </li>
                ))}
                {!loading && content.length === 0 && <li>No groups yet.</li>}
            </ul>
        </>
    );
}
