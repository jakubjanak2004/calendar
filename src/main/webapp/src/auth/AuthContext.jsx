import {createContext, useContext, useEffect, useMemo, useState} from "react";
import {http} from "../requests/http.jsx";

const AuthCtx = createContext(null);

export function AuthProvider({children}) {
    const [token, setToken] = useState(null);
    const [userId, setUserId] = useState("");
    const [username, setUsername] = useState("")
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")

    useEffect(() => {
        http.setToken(token)
    }, [token])

    const value = useMemo(() => ({
        loggedIn: !!token,
        token,
        userId,
        username,
        firstName,
        lastName,
        login: (t, userId, username, firstName, lastName) => {
            setToken(t)
            setUserId(userId)
            setUsername(username)
            setFirstName(firstName)
            setLastName(lastName)
        },
        logout: () => {
            setToken(null)
        },
    }), [token, username, firstName, lastName]);

    return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export function useAuth() {
    const ctx = useContext(AuthCtx);
    if (!ctx) throw new Error("useAuth must be used within <AuthProvider>");
    return ctx;
}
