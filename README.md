# Foreground Service
 A non killable foregroung service to run a task continously. 
 
 A foreground service is a service, which works exactly same as a normal service (background service) and the only difference is it has a notification attached to it in the notification tray of the device.
 
What is the use case of a foreground service in an Android application?

The main advantage of having a foreground service is its higher priority than its background version. Android OS implemented restriction to background execution to save battery and memory usages, i.e., it kills least used apps (or processes) to retain the memory and battery.
To save battery it puts unused apps components into Doze mode and to save memory it kills background services to regain memory for its foreground apps.

There are many use cases in an Android app to have a foreground service. Main advantages the are followings,
1.	Higher priority hence less likely to be killed by OS
2.	Indication to user to inform about tasks under process.
3.	Accessibility to the app features like in music player apps.

