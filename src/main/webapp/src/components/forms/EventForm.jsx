import {useEffect, useState} from "react";
import {http} from "../../lib/http.jsx";

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

export default function EventForm({onSubmitCallback, eventId, submitButtonValue, defaultStartTime, defaultEndTime}) {
    const [title, setTitle] = useState("")
    const [description, setDescription] = useState("")
    const [startTime, setStartTime] = useState(toLocalInputValue(defaultStartTime || new Date()));
    const [endTime, setEndTime] = useState(toLocalInputValue(defaultEndTime || new Date()));

    async function onSubmitHandle(e) {
        e.preventDefault()
        onSubmitCallback(title, description, toIso(startTime), toIso(endTime))
    }

    // todo determine if loading the event is necessary, it would be better to load it from local event store
    async function loadEvent() {
        const res = await http.client.get(`events/${eventId}`)
        const data = res.data
        setTitle(data.title)
        setDescription(data.description)
        setStartTime(toLocalInputValue(new Date(data.startTime)))
        setEndTime(toLocalInputValue(new Date(data.endTime)))
    }

    useEffect(() => {
        if (!eventId) return;
        loadEvent().finally();
    }, [])

    useEffect(() => {
        if (toIso(endTime) < toIso(startTime)) {
            setEndTime(startTime)
        }
    }, [startTime]);

    return <>
        <form id={"add-event"} onSubmit={onSubmitHandle}>
            <input
                type="text"
                value={title}
                onChange={e => setTitle(e.target.value)}
                placeholder={"Title"}
                required
                autoFocus
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
            <input type="submit" value={submitButtonValue}/>
        </form>
    </>
}