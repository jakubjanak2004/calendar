# Calendar Application

This is a calendar application. The documentation starts with the problem analysis and then continues with the app design.

## Functional Requirements
FR1 — Authentication
* A user can log in to their account using a username and password.
* The system must return an access and refresh token on successful login.

FR2 — Personal Calendar (read)
* A user can view their personal calendar, which aggregates their personal events and events from groups they belong to.
* The calendar supports day and month views and a time-zone–aware agenda list.

FR3 — Personal Events (create/update/delete)
* A user can create, edit, and delete personal events with fields: title, description, start, end, all-day flag, location, recurrence.
* Overlapping events are allowed but the UI indicates conflicts.

FR4 — Groups (create & manage)
* A user can create a group and becomes its admin.
* A group admin can invite users to the group; invitees must accept or decline.
* A group admin can remove members.
* A group admin can grant or revoke the Editor role for any group member.

FR5 — Group Events (permissions)
* A group admin and editor can create, edit, and delete events for the group.
* Group members can view all group events.

## Nonfunctional Requirements
1. System should be accessible through REST API.
2. System should be containerized for easy deployment.

## List of Users

## Use-Case Diagrams

## Class Diagram