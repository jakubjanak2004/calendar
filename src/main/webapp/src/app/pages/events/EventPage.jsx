import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {http} from "../../../lib/http.jsx";

export default function EventPage() {
    const { eventId } = useParams();
    const [title, setTitle] = useState("")
    const [description, setDescription] = useState("")

    async function loadEvent() {
        const res = await http.client.get(`events/${eventId}`)
        const data = res.data
        setTitle(data.title)
        setDescription(data.description)
    }

    useEffect(() => {
        loadEvent().finally()
    }, [eventId]);

    return <>
        <h1>Update Event</h1>
        <form>
            <input
                type="text"
                value={title}
                placeholder={"Title"}
                onChange={e => setTitle(e.target.value)}
                required
            />
            <input
                type="text"
                value={description}
                placeholder={"Description"}
                onChange={e => setDescription(e.target.value)}
            />
            <input type="submit" value="Update Event"/>
        </form>
    </>
}