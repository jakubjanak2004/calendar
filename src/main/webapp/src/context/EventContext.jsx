import {createContext, useContext, useState} from "react";


const EventContext = createContext(null);

export function EventProvider({children}) {
    const [eventsByOwner, setEventsByOwner] = useState({});

    function findEventById(eventId) {
        for (const ownerId of Object.keys(eventsByOwner)) {
            const event = eventsByOwner[ownerId]?.[eventId];
            if (event) return event;
        }
        return null;
    }

    function getEventsForOwner(ownerId) {
        const ownerEvents = eventsByOwner[ownerId] || {};
        return Object.values(ownerEvents);
    }

    function setEventsForOwner(ownerId, events) {
        setEventsByOwner(prev => ({
            ...prev,
            [ownerId]: Object.fromEntries(events.map(e => [e.id, e])),
        }));
    }

    function addEventsForOwner(ownerId, events) {
        const safeEvents = Array.isArray(events) ? events : [];

        setEventsByOwner(prev => {
            const existing = prev[ownerId] || {};
            const additions = Object.fromEntries(
                safeEvents.map(e => [e.id, e])
            );
            return {
                ...prev,
                [ownerId]: {
                    ...existing,
                    ...additions,
                },
            };
        });
    }

    function updateEvent(event) {
        if (!event || !event.id) {
            console.warn("updateEvent called without a valid event.id");
            return;
        }

        setEventsByOwner(prev => {
            let changed = false;
            const next = {...prev};

            for (const ownerId of Object.keys(prev)) {
                const ownerEvents = prev[ownerId];
                if (ownerEvents && ownerEvents[event.id]) {
                    next[ownerId] = {
                        ...ownerEvents,
                        [event.id]: event,
                    };
                    changed = true;
                }
            }

            return changed ? next : prev;
        });
    }

    function removeEventForOwner(ownerId, eventId) {
        setEventsByOwner(prev => {
            const ownerEvents = {...(prev[ownerId] || {})};
            delete ownerEvents[eventId];
            return {...prev, [ownerId]: ownerEvents};
        });
    }

    return <EventContext.Provider
        value={{eventsByOwner, getEventsForOwner, setEventsForOwner, addEventsForOwner, updateEvent, removeEventForOwner, findEventById}}
    >
        {children}
    </EventContext.Provider>
}

export function useEvents() {
    const ctx = useContext(EventContext)
    if (!ctx) throw new Error("useEvents must be used within <EventProvider>");
    return ctx;
}