import {Link, useNavigate} from "react-router-dom";
import {useState} from "react";
import {http} from "../../requests/http.jsx";
import {useAuth} from "./AuthContext.jsx";

export function SignupPage() {
    let [firstName, setFirstName] = useState("")
    let [lastName, setLastName] = useState("")
    let [username, setUsername] = useState("")
    let [password, setPassword] = useState("")
    let [passwordRepeat, setPasswordRepeat] = useState("")
    const navigate = useNavigate();
    const {login} = useAuth()

    async function handleSignup(e) {
        e.preventDefault()
        if (password !== passwordRepeat) {
            console.error("Passwords do not match")
            return;
        }
        try {
            const res = await http.post("auth/signup", {firstName, lastName, username, password})
            const token = res.data.token
            login(token, username, firstName, lastName)
            navigate("/", {replace: true})
        } catch (err) {
            console.error(err)
        }
    }

    return <>
        <Link to={"/login"}>Log in</Link>
        <h1>Signup page</h1>
        <form onSubmit={handleSignup}>
            <input
                type="text"
                placeholder={"first name"}
                value={firstName}
                onChange={e => setFirstName(e.target.value)}
            />
            <input
                type="text"
                placeholder={"last name"}
                value={lastName}
                onChange={e => setLastName(e.target.value)}
            />
            <input
                type="text"
                placeholder={"username"}
                value={username}
                onChange={e => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder={"password"}
                value={password}
                onChange={e => setPassword(e.target.value)}
            />
            <input
                type="password"
                placeholder={"repeat password"}
                value={passwordRepeat}
                onChange={e => setPasswordRepeat(e.target.value)}
            />
            <input
                type="submit"
                value={"Sign In"}
            />
        </form>
    </>
}