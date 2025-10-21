import {useNavigate} from "react-router-dom";
import {useAuth} from "../auth/AuthContext.jsx";

export function Dashboard() {
    const navigate = useNavigate();
    const {logout, username, firstName, lastName} = useAuth()

    function handleSignOut(e) {
        e.preventDefault();
        logout()
        navigate("/", { replace: true });
    }

    return <>
        <button onClick={handleSignOut}>Sign Out</button>
        <h1>Dashboard</h1>
        <h2>username: {username}</h2>
        <h2>name: {firstName} {lastName}</h2>
    </>
}