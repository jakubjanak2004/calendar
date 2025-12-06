import {useNavigate} from "react-router-dom";
import UserSVG from "../svg/UserSVG.jsx";

export default function CalendarUser({user, children}) {
    const navigate = useNavigate()

    function navigateToUser() {
        navigate(`/user/${user.id}`, {
            state: {
                user
            }
        })
    }

    return <>
            <div className={"li-element"} onDoubleClick={navigateToUser}>
                <UserSVG />
                <p>{user.username}</p>
                {children}
            </div>
    </>
}