# Software Development Capstone Project

## Developed using Android Studio 4.0

### About

This is an Android mobile application that uses the [Room](https://developer.android.com/topic/libraries/architecture/room) architecture and the ViewModel pattern. It is developed for a fictional, local religious organization for the purposes of Customer Relationship Management(CRM) for church staff as well as an informational portal for users. Data is stored on local SQLite storage, but can be modified to be accessed from external server. The application allows for tracking of the following:

- Current and past members, prospective members/contacts
- Intraorganizational groups
- Donations to the church
- Upcoming church events

### Features

- Separate experiences for admin/power user, church member, and guest user
- Direct connection to device Call/SMS/email capability
- Full Insert/Update/Delete capability
- Scrollable lists (using RecyclerView)
- Screen orientation change handling (portrait <-> landscape)
- Login authentication
- Error and exception handling
- Keyword search functionality for events (by title or description) and member (by first or last name)
- Data filtering for events (by week/month) and members (by membership status)

### Instructions For Use
*Note: Application needs to be installed on either an Android emulator or a physical device (min SDK version 18). A full User Guide can be found on page 13 of the* [summary document](Extra_Documents/Task2_SectionC.docx).

1. Install the [.apk](app-release.apk) file on preferred device.
2. Run application on device.
3. On the login screen, click the hamburger menu and add sample data.
4. Enter login credentials:
    - **Admin**: `test@wgu.edu/123456`
    - **Authorized Member**: `sallydoe@gmail.com/123456`
    - **Guest Access**: Click the "Not a Member?" link near the bottom of the screen
5. Navigate through and interact as desired.
6. Logout by pressing the device back button twice on the member home screen.

### Examples

1. Searching for an event by keyword

<img src="Extra_Documents/Screenshots/event_search.gif" width="250" height="500" />
