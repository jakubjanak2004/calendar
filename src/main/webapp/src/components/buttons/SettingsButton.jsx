import {Link} from "react-router-dom";

export default function SettingsButton() {
    return <>
        <Link
            to={`/settings`}
            title="Settings"
            className="button"
        >
            Settings
        </Link>
    </>
}