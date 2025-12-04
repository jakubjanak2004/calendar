import {useEffect, useMemo, useState} from "react";
import {Calendar, Views} from "react-big-calendar";
import {localizer} from "./calendarLocalizer";
import {http} from "../../lib/http.jsx";
import {Link, useNavigate} from "react-router-dom";
import EventComponent from "./EventComponent.jsx";
import {useEvents} from "../../context/EventContext.jsx";
import {NONE} from "react-big-calendar/lib/utils/Resources.js";

function darkenHex(hex, amount) {
    // Remove "#" if present
    hex = hex.replace(/^#/, "");

    // Parse RGB
    let r = parseInt(hex.slice(0, 2), 16);
    let g = parseInt(hex.slice(2, 4), 16);
    let b = parseInt(hex.slice(4, 6), 16);

    // Darken each channel
    r = Math.max(0, Math.floor(r * (1 - amount)));
    g = Math.max(0, Math.floor(g * (1 - amount)));
    b = Math.max(0, Math.floor(b * (1 - amount)));

    // Convert back to hex
    const toHex = (c) => c.toString(16).padStart(2, "0");
    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
}

// todo refactor the logic here as it got a bit too messy, maybe instead of membership role use can edit event boolean
export default function AppCalendar({memberships, canAddEvents}) {
    const {setEventsForOwner, eventsByOwner} = useEvents();
    const [date, setDate] = useState(() => new Date());
    const [view, setView] = useState(Views.MONTH);
    const navigate = useNavigate()
    const firstOwnerId = memberships.length > 0 ? memberships[0].id : null;

    const events = useMemo(() => {
        const all = [];

        for (let calendarOwner of memberships) {
            const ownerEvents = eventsByOwner[calendarOwner.id] || {};
            const ownerEventsArray = Object.values(ownerEvents).map(e => {
                const color = calendarOwner.color;
                return {
                canManage: calendarOwner.canManage,
                title: e.title,
                description: e.description,
                start: new Date(e.startTime),
                end: new Date(e.endTime),
                allDay: e.allDay ?? false, // todo right now we are not using the all day
                color: color,
                selectedColor: darkenHex(color, .1),
                resource: e,
            }});

            all.push(...ownerEventsArray);
        }

        return all;
    }, [memberships, eventsByOwner]);

    const fetchEventsForRange = async (eventOwnerId, startDate, endDate) => {
        const start = startDate.toISOString();
        const end = endDate.toISOString();

        const res = await http.client.get(
            `eventOwners/${eventOwnerId}/events/${encodeURIComponent(start)}/${encodeURIComponent(end)}`
        );

        setEventsForOwner(eventOwnerId, res.data)
    };

    useEffect(() => {
        const year = date.getFullYear();
        const month = date.getMonth();

        const startDate = new Date(year, month, 1, 0, 0, 0, 0);
        const endDate = new Date(year, month + 1, 0, 23, 59, 59, 999);

        for(let eventOwner of memberships) {
            fetchEventsForRange(eventOwner.id, startDate, endDate).finally()
        }
    }, [firstOwnerId, date]);

    const handleNavigate = (newDate) => {
        setDate(newDate);
    };

    const handleViewChange = (newView) => {
        setView(newView);
    };

    function setColor(event, start, end, isSelected) {
        let backgroundColor;
        if (isSelected) {
            backgroundColor = event.selectedColor;
        } else {
            backgroundColor = event.color;
        }

        return {
            style: {
                backgroundColor,
            },
        };
    }

    const handleDoubleClickEvent = (event) => {
        if (!event.canManage) return;
        const id = event.resource.id;
        navigate(`event/${id}`);
    };

    const handleSelectSlot = ({ start, action }) => {
        if (!canAddEvents) return;
        if (action !== "doubleClick") return;

        // end is always one hour after start
        const end = new Date(start.getTime() + 60 * 60 * 1000);

        navigate(`eventOwner/${firstOwnerId}/addEvent`, {
            state: {
                startTime: start,
                endTime: end,
            }
        });
    };

    return <>
        {canAddEvents &&
            <div className={"add-events-button"}>
                <Link
                    to={`eventOwner/${firstOwnerId}/addEvent`}
                    className={"add-events-button"}
                    title="Add Event"
                >
                    <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px"
                         fill="black">
                        <path d="M440-440H200v-80h240v-240h80v240h240v80H520v240h-80v-240Z"/>
                    </svg>
                </Link>
            </div>
        }

        <Calendar
            localizer={localizer}
            events={events}
            selectable={canAddEvents}
            onSelectSlot={handleSelectSlot}
            step={60}
            timeslots={1}
            date={date}
            view={view}
            onNavigate={handleNavigate}
            onView={handleViewChange}
            views={[Views.MONTH, Views.WEEK, Views.DAY]}
            startAccessor="start"
            endAccessor="end"
            components={{
                event: EventComponent,
            }}
            eventPropGetter={setColor}
            onDoubleClickEvent={canAddEvents ? handleDoubleClickEvent : undefined}
        />
    </>
}