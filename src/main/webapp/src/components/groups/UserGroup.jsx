import {Link} from "react-router-dom";

export default function UserGroup({group}) {
    const groupId = group.id
    const groupName = group.name

    return <>
        <Link className={"li-element"} to={`${groupId}`} state={{groupName: group.name}}>
            <p>{groupName}</p>
        </Link>
    </>
}