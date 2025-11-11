import {useMemo, useState} from "react";
import CalendarDay from "./CalendarDay.jsx";
import {Link} from "react-router-dom";

export default function Calendar({eventOwnerId, canAddEvents}) {
    const [monthDateStart, setMonthDateStart] = useState(() => {
        const now = new Date();
        return new Date(now.getFullYear(), now.getMonth(), 1);
    })

    const year = monthDateStart.getFullYear();
    const month = monthDateStart.getMonth();
    const last = new Date(year, month + 1, 0).getDate();

    const changeMonth = (offset) => {
        setMonthDateStart(d => new Date(d.getFullYear(), d.getMonth() + offset, 1));
    }

    const days = useMemo(() => {
        return Array.from({length: last}, (_, i) => new Date(year, month, i + 1));
    }, [year, month])

    const firstDayIndex = useMemo(() => {
        return (monthDateStart.getDay() + 6) % 7;
    }, [year, month]);

    return <div id={"dashboard"}>
        {/* Add event */}
        {canAddEvents && <Link to={`/${eventOwnerId}/addEvent`}>Add Event</Link>}

        {/* Change Month of the displayed calendar */}
        <div id={"month-info"}>
            <button className={"arrow-button"} onClick={() => changeMonth(-1)}>←</button>
            <h1 id={"month-name"}>{new Date(year, month).toLocaleString('default', {month: 'long', year: 'numeric'})}</h1>
            <button className={"arrow-button"} onClick={() => changeMonth(1)}>→</button>
        </div>

        {/* Get all days in month */}
        <ul id={"calendar"}>
            <li className={"day"}>Mon</li>
            <li className={"day"}>Tue</li>
            <li className={"day"}>Wed</li>
            <li className={"day"}>Thu</li>
            <li className={"day"}>Fri</li>
            <li className={"day"}>Sat</li>
            <li className={"day"}>Sun</li>

            {/* leading blanks */}
            {Array.from({length: firstDayIndex}).map((_, i) => (
                <li key={`pad-${i}`} className="empty" aria-hidden="true"/>
            ))}

            {days.map(date =>
                <CalendarDay key={date.toDateString()} date={date} eventOwnerId={eventOwnerId}/>
            )}
        </ul>
    </div>
}