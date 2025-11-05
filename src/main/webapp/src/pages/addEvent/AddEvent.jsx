import {Link, useNavigate, useParams} from "react-router-dom";
import {useState} from "react";
import {http} from "../../requests/http.jsx";

function toLocalInputValue(date) {
    const pad = n => String(n).padStart(2, "0");
    const d = new Date(date);
    const yyyy = d.getFullYear();
    const mm = pad(d.getMonth() + 1);
    const dd = pad(d.getDate());
    const hh = pad(d.getHours());
    const mi = pad(d.getMinutes());
    return `${yyyy}-${mm}-${dd}T${hh}:${mi}`;
}

const toIso = (local) => new Date(local).toISOString();

export function AddEvent() {
    const [title, setTitle] = useState("")
    const [description, setDescription] = useState("")
    const [startTime, setStartTime] = useState(toLocalInputValue(new Date()));
    const [endTime, setEndTime] = useState(toLocalInputValue(new Date()));
    const { eventOwnerId } = useParams();

    async function createNewEvent(e) {
        e.preventDefault()
        console.log(eventOwnerId)
        const res = await http.client.post(`/eventOwners/${eventOwnerId}/events`,
            {
                title,
                description,
                startTime: toIso(startTime),
                endTime: toIso(endTime)
            }
        )
        console.log(res)
        // todo later add the event into the global event handling
        // todo navigate back to calendar
    }

    return <>
        <Link to={"/dashboard"}>Dashboard</Link>
        <h1>Create New Event</h1>
        <form id={"add-event"} onSubmit={createNewEvent}>
            <input
                type="text"
                value={title}
                onChange={e => setTitle(e.target.value)}
                placeholder={"Title"}
                required
            />
            <input
                type="text"
                value={description}
                onChange={e => setDescription(e.target.value)}
                placeholder={"Description"}
            />
            <input
                type="datetime-local"
                value={startTime}
                onChange={e => setStartTime(e.target.value)}
                required
            />
            <input
                type="datetime-local"
                value={endTime}
                onChange={e => setEndTime(e.target.value)}
                required
            />
            <input type="submit" value={"Create"}/>
        </form>
    </>
}