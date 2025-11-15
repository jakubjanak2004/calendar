import {Link, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {http} from "../../../lib/http.jsx";
import BackButton from "../../../components/buttons/BackButton.jsx";

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

export function AddEventPage() {
    const [title, setTitle] = useState("")
    const [description, setDescription] = useState("")
    const [startTime, setStartTime] = useState(toLocalInputValue(new Date()));
    const [endTime, setEndTime] = useState(toLocalInputValue(new Date()));
    const { eventOwnerId } = useParams();
    const navigate = useNavigate();

    // when start time changed and end time is before start time shift it to start time
    useEffect(() => {
        if (toIso(endTime) < toIso(startTime)) {
            setEndTime(startTime)
        }
    }, [startTime]);

    async function createNewEvent(e) {
        e.preventDefault()
        const res = await http.client.post(`/eventOwners/${eventOwnerId}/events`,
            {
                title,
                description,
                startTime: toIso(startTime),
                endTime: toIso(endTime)
            }
        )
        // todo later add the event into the global event handling
        navigateBack()
    }

    function navigateBack() {
        navigate(-1)
    }

    return <>
        <header>
            <BackButton />
        </header>
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