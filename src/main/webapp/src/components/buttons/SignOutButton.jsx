import {useNavigate} from "react-router-dom";
import {useAuth} from "../../features/AuthContext.jsx";

export default function SignOutButton() {
    const navigate = useNavigate();
    const {logout} = useAuth()

    function handleSignOut(e) {
        const ok = confirm("Do you really want to sign out?");
        if (!ok) return;

        e.preventDefault();
        logout()
        navigate("/", {replace: true});
    }

    return <>
        <button className={"button"} onClick={handleSignOut}>
            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                 fill="block">
                <path
                    d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h280v80H200v560h280v80H200Zm440-160-55-58 102-102H360v-80h327L585-622l55-58 200 200-200 200Z"/>
            </svg>
        </button>
    </>
}