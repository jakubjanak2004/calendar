import {Link, useNavigate} from "react-router-dom";
import {useAuth} from "../../../features/AuthContext.jsx";
import Calendar from "../../../components/calendar/Calendar.jsx";

export function Dashboard() {
    const navigate = useNavigate();
    const {logout, userId} = useAuth()

    function handleSignOut(e) {
        e.preventDefault();
        logout()
        navigate("/", { replace: true });
    }

    return <>
        <header>
            <button onClick={handleSignOut}>Sign Out</button>
        </header>
        <Link to={"/groups"}>Groups</Link><br/>
        <Link to={"/invitations"}>Invitations</Link>
        <Calendar eventOwnerId={userId}/>
    </>
}