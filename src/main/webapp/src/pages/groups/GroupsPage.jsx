import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { http } from "../../requests/http.jsx";
import UserGroup from "./UserGroup.jsx";

export default function GroupsPage() {
    const [content, setContent] = useState([]);
    const [page, setPage] = useState(0);      // 0-based
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    // const [error, setError] = useState("");

    async function getUserGroups(p = page, s = size) {
        setLoading(true);
        // setError("");
        try {
            const res = await http.client.get("/groups", {
                params: { page: p, size: s, sort: "name,asc" }, // adjust sort as you like
            });
            const data = res.data;
            setContent(data.content ?? []);
            setPage(data.number ?? p);
            setSize(data.size ?? s);
            setTotalPages(data.totalPages ?? 0);
        } catch (e) {
            console.error(e);
            // setError("Failed to load groups.");
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
            <Link to="/dashboard">Dashboard</Link>
            <h1>Groups page</h1>

            <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
                <button onClick={() => setPage(0)} disabled={!hasPrev}>⏮ First</button>
                <button onClick={() => setPage(p => Math.max(p - 1, 0))} disabled={!hasPrev}>◀ Prev</button>
                <span>Page {page + 1} / {Math.max(totalPages, 1)}</span>
                <button onClick={() => setPage(p => (hasNext ? p + 1 : p))} disabled={!hasNext}>Next ▶</button>
                <button onClick={() => setPage(totalPages - 1)} disabled={!hasNext}>Last ⏭</button>

                <label style={{ marginLeft: 12 }}>
                    Page size:&nbsp;
                    <select value={size} onChange={e => { setPage(0); setSize(Number(e.target.value)); }}>
                        {[5, 10, 20, 50].map(n => <option key={n} value={n}>{n}</option>)}
                    </select>
                </label>
            </div>

            {loading && <p>Loading…</p>}
            {/*{error && <p style={{ color: "crimson" }}>{error}</p>}*/}

            <h2>Your groups:</h2>
            <ul>
                {content.map(group => (
                    <UserGroup key={group.id} group={group} />
                ))}
                {!loading && content.length === 0 && <li>No groups yet.</li>}
            </ul>
        </>
    );
}
