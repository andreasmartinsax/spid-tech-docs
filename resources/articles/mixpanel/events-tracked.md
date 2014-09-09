:title Events tracked by SPiD

:aside

## Read more about analytics in SPiD:
- [Managing User-Specific Properties and Traits](/mixpanel/managing-properties-and-traits/)
- [Mixpanel Analytics](/mixpanel/analytics/)
- [Mixpanel Page Viewed Event](/mixpanel/page-viewed-event/)
- [Implementing Mixpanel tracking](/mixpanel/implementing-tracking/)

## Full event and property descriptions:
[Please see externally hosted spreadsheet](https://docs.google.com/spreadsheet/ccc?key=0An7r5IwHGo5MdG04bjlRVE5sUFNNc0JPdV9BUGtEWWc&usp=sharing)

## Visual maps of events in SPiD-flows:
- [Authentication](https://docs.google.com/drawings/d/1ltS8Yqt3pgoD2ymTJwm66KRZLEFbJTdFJbC_d6i6DBw/edit?usp=sharing)
- Payment (to be announced)
- Profile (to be announced)


:body

# Server side events sent by SPiD

- Accept agreement
- Verify email
- Login
- Logout
- Migration completed
- Migration started
- Verify phone
- Signup completed
- Signup started
- Verify email sent
- Choose payment method
- Choose product
- Order complete
- Order authorized
- Confirm * payment identifier
- Denied purchase, multisale disallowed
- Prior access to product
- Receipt sent
- Password changed
- Change password notification email sent
- Secondary email added
- Secondary email added notification email sent
- Device fingerprint tracked on user
- hasProduct


# Browser side events sent by SPiD:

- Back to client
- Click to view agreement
- Field filled
- Page viewed
- Time on page


# Properties added to all server side events:

SPiD will automatically add some profile data to events. These properties should
not be added as traits by the client, as they will be ignored (in favor of the
user's actual profile data). This keeps profile data in events from going stale.

- Version (Platform version)
- Revision (Platform revision)
- SPiD ID (The SPiD user id)
- Client (Client value based on "service alias" in SPiD client setup)
- ip (User's ip in order for Mixpanel to derive country, city, etc)
- User agent (Not used by Mixpanel on server side requests, but we send it anyway)
- Age
- Gender
- [User status](/user-status/) 
- Registered (When the user registered)
- Tracking ref ([Client provided visitor reference](/tracking-parameters/))
- Tracking tag ([Client provided tag parameter](/tracking-parameters/), used for custom order tracking)
- Flow (payment or signup)
- device (Device of user determined by user agent, mapped to device model by SPiD)
