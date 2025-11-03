import {useEffect, useMemo, useState} from "react";
import {http} from "../../../requests/http.jsx";
import CalendarEvent from "./CalendarEvent.jsx";

export default function CalendarDay({date}) {
    const [events, setEvents] = useState([]);
    const [selected, setSelected] = useState(false);
    const now = new Date();

    const isToday = date.getFullYear() === now.getFullYear() && date.getMonth() === now.getMonth() && date.getDate() === now.getDate();

    const [start, end] = useMemo(() => {
        const y = date.getFullYear();
        const m = date.getMonth();
        const d = date.getDate();
        const startLocal = new Date(y, m, d, 0, 0, 0, 0)
        const endLocal = new Date(y, m, d + 1, 0, 0, 0, 0)
        return [startLocal.toISOString(), endLocal.toISOString()];
    }, [date.getFullYear(), date.getMonth(), date.getDate()]);

    async function getEventsForDay() {
        try {
            const res = await http.client.get(`/events/${start}/${end}`)
            setEvents(res.data)
        } catch (err) {
            console.error(err)
        }
    }

    const handleDeleted = id => setEvents(prev => prev.filter(e => e.uuid !== id));

    useEffect(() => {
        getEventsForDay().finally()
    }, [start, end])

    const className = ["calendar-day", isToday ? "today" : "", events.length ? "has-events" : "", selected ? "selected" : "",].filter(Boolean).join(" ");

    return <>
        <li
            className={className}
            onClick={() => !selected && setSelected(true)}
        >
            <button className={"arrow-button"} onClick={() => setSelected(false)}>←</button>
            <div className={"day-number"}>
                {date.getDate()}
            </div>

            <div className={"content"}>
                <ul className={"events-row"}>
                    {events.map((event, index) => (
                        <CalendarEvent key={index} event={event} handleDelete={handleDeleted}/>))}
                </ul>
            </div>
        </li>
    </>
}