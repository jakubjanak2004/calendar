import {useRef} from "react";
import Draggable from "react-draggable";
import {useNavigate, useParams} from "react-router-dom";
import {http} from "../../../lib/http.jsx";
import EventForm from "../../../components/forms/EventForm.jsx";
import "./UpdateEventPage.css";
import {useEvents} from "../../../context/EventContext.jsx";

export default function UpdateEventPage() {
    const {eventId} = useParams();
    const {updateEvent} = useEvents();
    const navigate = useNavigate();

    async function updateEventOnSubmit(title, description, startTime, endTime) {
        await http.client.put(`/events/${eventId}`, {
            title, description, startTime, endTime,
        });
        updateEvent({
            id: eventId, title, description, startTime, endTime
        })
        navigate(-1);
    }

    const nodeRef = useRef(null);

    // todo abstract this into a window component, DraggableWindow
    return <>
        <div className={"window-background"}>
            <Draggable
                nodeRef={nodeRef}
                handle=".edit-event-header"
            >
                <div
                    ref={nodeRef}
                    className="edit-event-window"
                >
                    <div className="edit-event-header">
                        <button onClick={() => navigate(-1)}>X</button>
                        <h1>Update Event</h1>
                    </div>

                    <EventForm
                        onSubmitCallback={updateEventOnSubmit}
                        eventId={eventId}
                        submitButtonValue="Update"
                    />
                </div>
            </Draggable>
        </div>
    </>
}
