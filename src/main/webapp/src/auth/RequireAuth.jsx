import {Navigate, Outlet} from "react-router-dom";
import {useAuth} from "./AuthContext.jsx";

export function RequireAuth() {
    const {loggedIn} = useAuth()
    if (loggedIn) {
        return <Outlet/>;
    }
    return <Navigate to="/login"/>
}
