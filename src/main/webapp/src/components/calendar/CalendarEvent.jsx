import {useState} from "react";
import {http} from "../../lib/http.jsx";

const timeFmt = new Intl.DateTimeFormat(undefined, {
    hour: "2-digit", minute: "2-digit", hour12: false,
});
const pad = (n) => String(n).padStart(2, "0");
export const toInputTime = (d) => `${pad(d.getHours())}:${pad(d.getMinutes())}`;
export const applyTime = (date, hhmm) => {
    const [h, m] = hhmm.split(":").map(Number);
    const d = new Date(date);
    d.setHours(h, m, 0, 0);
    return d;
};

export default function CalendarEvent({event, handleDelete}) {
    const eventId = event.uuid
    const [title, setTitle] = useState(event.title)
    const [description, setDescription] = useState(event.description)
    const [startTime, setStartTime] = useState(new Date(event.startTime))
    const [endTime, setEndTime] = useState(new Date(event.endTime))

    async function onSubmit(e) {
        e.preventDefault();
        const res = await http.client.put(`/events/${eventId}`, {title, description, startTime, endTime})
        console.log(res)
    }

    async function deleteEvent() {
        await http.client.delete(`/events/${eventId}`)
        handleDelete(eventId)
    }

    return <ul>
        <form key={eventId} className="calendar-event" onSubmit={onSubmit}>
            <input
                type="text"
                value={title}
                onChange={e => setTitle(e.target.value)}
                placeholder={"Title"}
            />
            <input
                type="text"
                value={description}
                onChange={e => setDescription(e.target.value)}
                placeholder={"Description"}
            />
            <input
                type="time"
                value={toInputTime(startTime)}
                onChange={(e) => setStartTime(applyTime(startTime, e.target.value))}
            />
            <input
                type="time"
                value={toInputTime(endTime)}
                onChange={(e) => setEndTime(applyTime(endTime, e.target.value))}
            />
            <input
                type="submit"
                value={"Update Event"}
            />
        </form>
        <button onClick={deleteEvent}>Delete</button>
    </ul>
}