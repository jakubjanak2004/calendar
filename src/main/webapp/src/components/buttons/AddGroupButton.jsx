import {Link} from "react-router-dom";

export default function AddGroupButton() {
    return <>
        <Link to={"/groups/addGroup"}>
            <div className={"button"}>
                AddGroup
            </div>
        </Link>
    </>
}