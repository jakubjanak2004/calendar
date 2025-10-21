import {createContext, useContext, useEffect, useMemo, useState} from "react";
import {http} from "../../requests/http.jsx";

const AuthCtx = createContext(null);

export function AuthProvider({children}) {
    const [token, setToken] = useState(null);
    const [username, setUsername] = useState("")
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")

    useEffect(() => {
        if (token) {
            http.interceptors.request.use((config) => {
                const {token} = useAuth()
                if (token) config.headers.Authorization = `Bearer ${token}`;
                return config;
            })
        }
    }, [token])

    const value = useMemo(() => ({
        loggedIn: !!token,
        token,
        username,
        firstName,
        lastName,
        login: (t, username, firstName, lastName) => {
            setToken(t)
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
