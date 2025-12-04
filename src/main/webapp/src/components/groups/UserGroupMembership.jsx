import {Link, useNavigate} from "react-router-dom";
import UserGroupSVG from "../svg/UserGroupSVG.jsx";
import {useState} from "react";
import {http} from "../../lib/http.jsx";

export default function UserGroupMembership({groupMembership}) {
    const groupId = groupMembership.id
    const groupName = groupMembership.groupName
    const navigate = useNavigate()
    const [color, setColor] = useState(groupMembership.color)

    async function navigateToGroup() {
        navigate(`/groups/${groupId}`)
    }

    async function handleColorBlur() {
        await http.client.put(`groupMemberships/${groupId}/me/color`, {
            color: color,
        })
    }

    return <>
        <div className={"li-element"} >
            <div className={"li-div"} onClick={navigateToGroup}>
                <UserGroupSVG/>
                <p>{groupName}</p>
            </div>
            <input
                type="color"
                value={color}
                onChange={e => setColor(e.target.value)}
                onBlur={handleColorBlur}
            />
        </div>
    </>
}