import {useLocation} from "react-router-dom";
import './CalendarUserDetail.css'

export default function CalendarUserDetail() {
    const location = useLocation();
    const user = location.state?.user
    return <div id={"user-info"}>
        <h1>User detail</h1>
        <p>{user.firstName}</p>
        <p>{user.lastName}</p>
        <p>{user.username}</p>
    </div>
}