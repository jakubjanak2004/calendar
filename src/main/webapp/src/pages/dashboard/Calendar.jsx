import {useMemo, useState} from "react";
import {CalendarDay} from "./CalendarDay.jsx";

export function Calendar() {
    const [monthDate, setMonthDate] = useState(() => {
        const now = new Date();
        return new Date(now.getFullYear(), now.getMonth(), 1);
    });

    const year = monthDate.getFullYear();
    const month = monthDate.getMonth();

    const changeMonth = (offset) => {
        setMonthDate(d => new Date(d.getFullYear(), d.getMonth() + offset, 1));
    };

    const days = useMemo(() => {
        const last = new Date(year, month + 1, 0).getDate();
        return Array.from({length: last}, (_, i) => new Date(year, month, i + 1));
    }, [year, month]);

    return <>
        <h1>Calendar</h1>

        {/* Change Month of the displayed calendar */}
        <div>
            <button onClick={() => changeMonth(-1)}>←</button>
            <span>{new Date(year, month).toLocaleString('default', {month: 'long', year: 'numeric'})}</span>
            <button onClick={() => changeMonth(1)}>→</button>
        </div>

        {/* Get all days in month */}
        <ul>
            {days.map((date) => (
                <CalendarDay key={date.toDateString()} date={date}/>
            ))}
        </ul>

    </>
}