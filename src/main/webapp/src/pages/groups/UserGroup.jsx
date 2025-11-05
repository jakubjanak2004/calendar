import {useState} from "react";
import {Link} from "react-router-dom";

export default function UserGroup({group}) {
    const groupId = group.id
    const [groupName, setGroupName] = useState(group.name)

    return <li className={"group-li"}>
        <Link to={`${groupId}`} state={{ groupName: group.name }}>
            <p>{groupName}</p>
        </Link>
    </li>
}