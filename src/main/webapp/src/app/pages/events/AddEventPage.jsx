import {useNavigate, useParams} from "react-router-dom";
import {http} from "../../../lib/http.jsx";
import EventForm from "../../../components/forms/EventForm.jsx";
import Draggable from "react-draggable";
import {useRef} from "react";
import {useEvents} from "../../../context/EventContext.jsx";

export function AddEventPage() {
    const {eventOwnerId} = useParams();
    const {addEventsForOwner} = useEvents()
    const navigate = useNavigate();

    async function createNewEvent(title, description, startTime, endTime) {
        const res = await http.client.post(`/eventOwners/${eventOwnerId}/events`, {
            title, description, startTime, endTime
        })
        const event = res.data;
        addEventsForOwner(eventOwnerId, [event]);
        navigate(-1);
    }

    const nodeRef = useRef(null);

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
                        <h1>Create New Event</h1>
                        <button onClick={() => navigate(-1)}>X</button>
                    </div>

                    <EventForm onSubmitCallback={createNewEvent} submitButtonValue={"Create"}/>
                </div>
            </Draggable>
        </div>
    </>
}