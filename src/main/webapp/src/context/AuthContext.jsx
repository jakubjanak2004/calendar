import {createContext, useContext, useEffect, useMemo, useState} from "react";
import {http} from "../lib/http.jsx";

const AuthCtx = createContext(null);

export function AuthProvider({children}) {
    const [token, setToken] = useState(null);
    const [userId, setUserId] = useState("");
    const [username, setUsername] = useState("")
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")
    const [color, setColor] = useState("")

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
        color,
        login: (t, userId, username, firstName, lastName, color) => {
            setToken(t)
            setUserId(userId)
            setUsername(username)
            setFirstName(firstName)
            setLastName(lastName)
            setColor(color)
        },
        logout: () => {
            setToken(null)
        },
        updateUser: async (newFirstName, newLastName, newColor) => {
            await http.client.put('users/me', {
                firstName: newFirstName,
                lastName: newLastName,
                color: {
                    color: newColor
                }
            });
            setFirstName(newFirstName);
            setLastName(newLastName);
            setColor(newColor);
        }
    }), [token, username, firstName, lastName, color]);

    return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export function useAuth() {
    const ctx = useContext(AuthCtx);
    if (!ctx) throw new Error("useAuth must be used within <AuthProvider>");
    return ctx;
}
