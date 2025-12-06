import {useEffect, useState} from "react";
import {useEvents} from "../../context/EventContext.jsx";

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
    const {findEventById} = useEvents();
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [startTime, setStartTime] = useState(toLocalInputValue(defaultStartTime || new Date()));
    const [endTime, setEndTime] = useState(toLocalInputValue(defaultEndTime || new Date()));

    async function onSubmitHandle(e) {
        e.preventDefault()
        onSubmitCallback(title, description, toIso(startTime), toIso(endTime))
    }

    async function loadEvent() {
        const event = findEventById(eventId);
        setTitle(event.title)
        setDescription(event.description)
        setStartTime(toLocalInputValue(new Date(event.startTime)))
        setEndTime(toLocalInputValue(new Date(event.endTime)))
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