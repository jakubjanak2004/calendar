import {useNavigate} from "react-router-dom";
import {useAuth} from "../auth/AuthContext.jsx";
import Calendar from "./calendar/Calendar.jsx";

export function Dashboard() {
    const navigate = useNavigate();
    const {logout} = useAuth()

    function handleSignOut(e) {
        e.preventDefault();
        logout()
        navigate("/", { replace: true });
    }

    return <>
        <header>
            <button onClick={handleSignOut}>Sign Out</button>
        </header>
        <Calendar/>
    </>
}