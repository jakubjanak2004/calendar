import {Link} from "react-router-dom";

export default function AddGroupButton() {
    return <>
        <Link
            to={"/groups/addGroup"}
            title="Create new group"
        >
            <div className={"button"}>
                AddGroup
            </div>
        </Link>
    </>
}