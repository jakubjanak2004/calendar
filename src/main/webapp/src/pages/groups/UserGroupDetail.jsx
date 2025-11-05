import {Link, useLocation, useParams} from "react-router-dom";
import Calendar from "../../components/calendar/Calendar.jsx";

export default function UserGroupDetail() {
    const { groupId } = useParams();
    const location = useLocation();
    const groupName = location.state?.groupName;

    return <>
        <Link to={"/groups"}>Groups</Link>
        <h1>{groupName}</h1>
        <Calendar eventOwnerId={groupId}/>
    </>
}