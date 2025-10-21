import {Link, useNavigate} from "react-router-dom";
import {http} from "../../requests/http.jsx";
import {useState} from "react";
import {useAuth} from "./AuthContext.jsx";

export function LoginPage() {
    let [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const navigate = useNavigate()
    const {login} = useAuth()

    async function handleLogin(e) {
        e.preventDefault()
        const res = await http.post("auth/login", {username, password})
        const token = res.data.token
        const firstName = res.data.firstName
        const lastName = res.data.lastName
        login(token, username, firstName, lastName)
        navigate("/", {replace: true})
    }

    return <>
        <Link to={"/signup"}>Sign Up</Link>
        <h1>Login Page</h1>
        <form onSubmit={e => handleLogin(e)}>
            <input
                type="text"
                placeholder={"username"}
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder={"password"}
                onChange={(e) => setPassword(e.target.value)}
            />
            <input type="submit" value={"LogIn"}/>
        </form>
    </>
}