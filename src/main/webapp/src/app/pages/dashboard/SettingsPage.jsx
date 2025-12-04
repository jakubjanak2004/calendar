import {useAuth} from "../../../context/AuthContext.jsx";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import DashboardButton from "../../../components/buttons/DashboardButton.jsx";

export default function SettingsPage() {
    const {
        firstName: authFirstName,
        lastName: authLastName,
        username: authUsername,
        color: authColor,
        updateUser,
    } = useAuth();

    const [firstName, setFirstName] = useState(authFirstName);
    const [lastName, setLastName] = useState(authLastName);
    const [color, setColor] = useState(authColor)
    const username = authUsername;
    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault();
        updateUser(firstName, lastName, color).then(() => {
            navigate('/dashboard');
        })
    };

    return <>
        <header>
            <DashboardButton/>
        </header>
        <h1>Settings Page</h1>
        <h2>{username}</h2>

        <form onSubmit={handleSubmit}>
            <div>
                <label>
                    First name:{" "}
                    <input
                        type="text"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        required
                    />
                </label>
            </div>

            <div>
                <label>
                    Last name:{" "}
                    <input
                        type="text"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        required
                    />
                </label>
            </div>

            <div>
                <label>
                    Color:{" "}
                    <input
                        type="color"
                        value={color}
                        onChange={(e) => setColor(e.target.value)}
                        required
                    />
                </label>
            </div>

            <button type="submit">Update</button>
        </form>
    </>
}
