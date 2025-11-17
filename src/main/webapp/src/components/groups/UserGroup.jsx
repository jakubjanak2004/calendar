import {Link} from "react-router-dom";
import UserGroupSVG from "../svg/UserGroupSVG.jsx";

export default function UserGroup({group}) {
    const groupId = group.id
    const groupName = group.name

    return <>
        <Link className={"li-element"} to={`/groups/${groupId}`}>
            <UserGroupSVG />
            <p>{groupName}</p>
        </Link>
    </>
}