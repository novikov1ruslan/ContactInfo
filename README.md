# ContactInfo
A utility that gets a phone number and returns a Contact if exists.
An activity that has input for entering a phone number and a display area for a phone Contact. 
After entering the number, if a contact exists, it is displayed on the screen in a neat way.

# Requirements
The phone number can be in any format (with or without country code, with dashes, spaces etc)
The utility should return a Contact if exists a contact with the "most probable" same number.
If several contacts with the same number exist - return the one that has a thumbnail, otherwise - any.
Design the Contact class to contain the necessary data for display.
The utility should implement a static cache to improve performance.

# NOTES:
The app should be managed with Gradle/Maven.
Third party library can (or even should) be used, but Not libraries that handle Android contacts.
The performance of the utility is a high priority.
The handling of different phone number formats is of high priority.
The utility should Not load all phone contacts to memory for caching.
The code should be commented (only for the key business logic parts).
