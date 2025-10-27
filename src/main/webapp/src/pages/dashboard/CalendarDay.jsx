import {useEffect, useMemo, useState} from "react";
import {http} from "../../requests/http.jsx";

export function CalendarDay({date}) {
    const [events, setEvents] = useState([]);
    const now = new Date();

    const isToday =
        date.getFullYear() === now.getFullYear() &&
        date.getMonth() === now.getMonth() &&
        date.getDate() === now.getDate();

    const [start, end] = useMemo(() => {
        const y = date.getFullYear();
        const m = date.getMonth();
        const d = date.getDate();
        const startLocal = new Date(y, m, d, 0, 0, 0, 0);
        const endLocal = new Date(y, m, d + 1, 0, 0, 0, 0); // next midnight
        return [startLocal.toISOString(), endLocal.toISOString()];
    }, [date.getFullYear(), date.getMonth(), date.getDate()]);

    // todo move this into the calendar, here it calls too many requests
    useEffect(() => {
        http.get(`/events/${start}/${end}`)
            .then(({data}) => {
                setEvents(Array.isArray(data) ? data : []);
            })
            .catch((err) => {
                console.error(err);
                setEvents([]);
            });
    }, [start, end])

    return <li>
        {date.toDateString()}
        {isToday && " today"}
        {events.length}
        {events.map(event => (
            // todo handle the event key
            <div key={event.id ?? event.startTime}>
                <div><strong>{event.title}</strong></div>
                <div>
                    {event.startTime} – {event.endTime}
                </div>
                <div>{event.description}</div>
            </div>
        ))}
    </li>
}