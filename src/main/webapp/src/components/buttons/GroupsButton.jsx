import {Link} from "react-router-dom";
import UserGroupSVG from "../svg/UserGroupSVG.jsx";

export default function GroupsButton() {
    return <>
        <Link
            className={"button"}
            to={"/groups"}
            title="See your Groups"
        >
            <UserGroupSVG />
        </Link>
    </>
}