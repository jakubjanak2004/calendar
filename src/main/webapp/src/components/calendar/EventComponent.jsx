export default function EventComponent({event}) {
    return <>
        <div className="event-content">
            <div className="event-title">{event.title}</div>
            {event.description && (
                <div className="event-description">{event.description}</div>
            )}
        </div>
    </>
}