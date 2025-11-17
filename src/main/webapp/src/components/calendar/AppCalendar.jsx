import {useEffect, useState} from "react";
import {Calendar, Views} from "react-big-calendar";
import {localizer} from "./calendarLocalizer";
import {http} from "../../lib/http.jsx";
import {Link, useNavigate} from "react-router-dom";
import EventComponent from "./EventComponent.jsx";
import {useEvents} from "../../context/EventContext.jsx";

export default function AppCalendar({eventOwnerId, canManageEvents}) {
    const {setEventsForOwner, eventsByOwner} = useEvents();
    const [date, setDate] = useState(() => new Date());
    const [view, setView] = useState(Views.MONTH);
    const navigate = useNavigate()

    let userEvents = eventsByOwner[eventOwnerId] || {};
    let eventsArray = Object.values(userEvents)
        .map(e => ({
            title: e.title,
            description: e.description,
            start: new Date(e.startTime),
            end: new Date(e.endTime),
            allDay: e.allDay ?? false,
            color: "#0650bc",
            selectedColor: "#205db6",
            resource: e,
        }))

    const fetchEventsForRange = async (startDate, endDate) => {
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

        fetchEventsForRange(startDate, endDate).finally();
    }, [eventOwnerId, date]);

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
            // className: "my-event", // optional extra CSS class
        };
    }

    const handleDoubleClickEvent = (event) => {
        const id = event.resource.id;
        navigate(`event/${id}`);
    };

    return <>
        {canManageEvents &&
            <div className={"add-events-button"}>
                <Link
                    to={`eventOwner/${eventOwnerId}/addEvent`}
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
            events={eventsArray}
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
            onDoubleClickEvent={canManageEvents ? handleDoubleClickEvent : undefined}
        />
    </>
}