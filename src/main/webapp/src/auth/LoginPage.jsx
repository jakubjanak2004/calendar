import {Link, useNavigate} from "react-router-dom";
import {http} from "../requests/http.jsx";
import {useState} from "react";
import {useAuth} from "./AuthContext.jsx";

export function LoginPage() {
    const {login} = useAuth()
    let [username, setUsername] = useState("")
    let [error, setError] = useState("")
    const [password, setPassword] = useState("")
    const navigate = useNavigate()

    function handleUnauthorized() {
        setError("Username or password is wrong")
    }

    async function handleLogin(e) {
        e.preventDefault()
        try {
            const res = await http.client.post("auth/login", {username, password})
            const token = res.data.token
            const userId = res.data.userId
            const firstName = res.data.firstName
            const lastName = res.data.lastName
            login(token, userId, username, firstName, lastName)
            navigate("/", {replace: true})
        } catch (error) {
            if (error.response.status === 401) {
                handleUnauthorized()
            }
        }
    }

    return <>
        <header>
            <Link to={"/signup"}>Sign Up</Link>
        </header>
        <h1 className={"main-header"}>Login Page</h1>
        <form onSubmit={e => handleLogin(e)} className={"auth-form"}>
            <input
                type="text"
                placeholder={"username"}
                value={username}
                onChange={e => setUsername(e.target.value)}
                autoComplete="username"
                required
            />
            <input
                type="password"
                placeholder={"password"}
                onChange={e => setPassword(e.target.value)}
                autoComplete="password"
                required
            />
            <input type="submit" value={"LogIn"}/>
        </form>
        <p role="alert">
            {error}
        </p>
    </>
}