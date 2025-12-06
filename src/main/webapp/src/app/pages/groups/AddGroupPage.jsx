import { http } from "../../../lib/http.jsx";
import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import {useAuth} from "../../../context/AuthContext.jsx";
import GroupsButton from "../../../components/buttons/GroupsButton.jsx";

export default function AddGroupPage() {
    const [name, setName] = useState("");
    const [usersIdsList, setUsersIdsList] = useState([]);
    const {userId} = useAuth()

    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);
    const [size] = useState(10);
    const [last, setLast] = useState(true);
    const [loadingUsers, setLoadingUsers] = useState(false);

    const navigate = useNavigate();

    const fetchUsers = useCallback(
        async (pageNum) => {
            setLoadingUsers(true);
            try {
                const res = await http.client.get("/users", {
                    params: { page: pageNum, size },
                });

                const data = res.data ?? {};
                const rawContent = data.content ?? [];
                const content = rawContent.filter((user) => user.id !== userId);

                const isLast =
                    typeof data.last === "boolean"
                        ? data.last
                        : rawContent.length < size; // use rawContent for paging logic

                setUsers(content);
                setLast(isLast);
                setPage(typeof data.number === "number" ? data.number : pageNum);
            } catch (err) {
                console.error(err);
            } finally {
                setLoadingUsers(false);
            }
        },
        [size, userId]
    );


    useEffect(() => {
        fetchUsers(0).catch(console.error);
    }, [fetchUsers]);

    function toggleUser(userId) {
        setUsersIdsList((prev) =>
            prev.includes(userId)
                ? prev.filter((id) => id !== userId)
                : [...prev, userId]
        );
    }

    async function createNewGroup(e) {
        e.preventDefault();
        try {
            await http.client.post("groups", {
                name,
                usersIdsList,
            });
            navigate(-1);
        } catch (err) {
            console.error(err);
        }
    }

    return (
        <>
            <header>
                <GroupsButton/>
            </header>
            <h1>Add a new Group</h1>
            <form onSubmit={createNewGroup}>
                <input
                    type="text"
                    placeholder="Group name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />

                <input type="submit" value="Create new Group"/>

                <h2>Select users</h2>
                <p>Selected users: {usersIdsList.length}</p>
                {loadingUsers && <p>Loading users…</p>}

                <ul>
                    {users.map((u) => (
                        <li key={u.id}>
                            <label>
                                <input
                                    type="checkbox"
                                    checked={usersIdsList.includes(u.id)}
                                    onChange={() => toggleUser(u.id)}
                                />
                                {u.username || u.email || u.name || u.id}
                            </label>
                        </li>
                    ))}

                    {!loadingUsers && users.length === 0 && (
                        <li>No users on this page.</li>
                    )}
                </ul>

                <div>
                    <button
                        type="button"
                        onClick={() => fetchUsers(Math.max(0, page - 1))}
                        disabled={loadingUsers || page === 0}
                    >
                        Previous
                    </button>
                    <span> Page {page + 1} </span>
                    <button
                        type="button"
                        onClick={() => fetchUsers(page + 1)}
                        disabled={loadingUsers || last}
                    >
                        Next
                    </button>
                </div>
            </form>
        </>
    );
}
