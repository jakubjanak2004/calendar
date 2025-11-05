import { Link, useNavigate } from "react-router-dom";
import { http } from "../requests/http.jsx";
import { useAuth } from "./AuthContext.jsx";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

async function isUsernameAvailable(username) {
    const res = await http.client.get("/auth/usernameTaken", { params: {username} });
    return Boolean(!res.data);
}

const usernameSchema = z
    .string()
    .trim()
    .min(5, "Min 5 chars")
    .max(20, "Max 20 chars")
    .superRefine(async (u, ctx) => {
        const val = u.trim();
        if (val.length < 5) return; // let length rule handle it
        try {
            const ok = await isUsernameAvailable(val);
            if (!ok) {
                ctx.addIssue({
                    code: z.ZodIssueCode.custom,
                    message: "Username is already taken",
                }); // attaches to "username" itself
            }
        } catch {
            // Optional: show a soft error instead of blocking
            // ctx.addIssue({ code: z.ZodIssueCode.custom, message: "Couldn't verify username right now" });
        }
    });

// --- object schema with sync password match
const schema = z
    .object({
        firstName: z.string().trim().max(100, "Minimal length is 100 characters").min(1, "First name is required"),
        lastName: z.string().trim().max(100, "Maximal length is 100 characters").min(1, "Last name is required"),
        username: usernameSchema,
        password: z.string().min(5, "Minimal length is 5 characters").max(50, "Maximal length is 50 characters"),
        passwordRepeat: z.string().min(5, "Minimal length is 5 characters"),
    })
    .refine((d) => d.password === d.passwordRepeat, {
        path: ["passwordRepeat"],
        message: "Passwords do not match",
    });

export function SignupPage() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const {
        register,
        handleSubmit,
        reset,
        trigger,
        formState: { errors, isSubmitting }
    } = useForm({
        resolver: zodResolver(schema, undefined, { async: true }), // enable async zod
        defaultValues: {
            firstName: "",
            lastName: "",
            username: "",
            password: "",
            passwordRepeat: ""
        },
        mode: "onSubmit",
        reValidateMode: "onSubmit",
    });

    const usernameReg = register("username");

    async function onSubmit(data) {
        const res = await http.client.post("auth/signup", {
            firstName: data.firstName,
            lastName: data.lastName,
            username: data.username,
            password: data.password
        });
        const token = res.data.token;
        login(token, data.username, data.firstName, data.lastName);
        reset();
        navigate("/", { replace: true });
    }

    return (
        <>
            <header><Link to="/login">Log in</Link></header>
            <h1 className="main-header">Signup page</h1>

            <form onSubmit={handleSubmit(onSubmit)} className="auth-form" noValidate>
                <input type="text" placeholder="first name" {...register("firstName")} />
                {errors.firstName && <p role="alert">{errors.firstName.message}</p>}

                <input type="text" placeholder="last name" {...register("lastName")} />
                {errors.lastName && <p role="alert">{errors.lastName.message}</p>}

                <input
                    type="text"
                    placeholder="username"
                    autoComplete="username"
                    {...usernameReg}
                    onBlur={(e) => {
                        usernameReg.onBlur(e)
                        trigger("username")
                    }}
                />
                {errors.username && <p role="alert">{errors.username.message}</p>}

                <input type="password" placeholder="password" autoComplete="new-password" {...register("password")} />
                {errors.password && <p role="alert">{errors.password.message}</p>}

                <input type="password" placeholder="repeat password" {...register("passwordRepeat")} />
                {errors.passwordRepeat && <p role="alert">{errors.passwordRepeat.message}</p>}

                <input type="submit" value={isSubmitting ? "Signing up…" : "Sign In"} disabled={isSubmitting} />
            </form>
        </>
    );
}
