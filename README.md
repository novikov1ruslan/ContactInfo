# Contact Info
Uses:
*Gson* for objects serialization,
*Apache Commons* for validators

##Tread Safeness:
Application in general is not thread safe and some concurrnecy contracts do not hold.

##Cache and Performance:
Cache is implemented using android.util.LruCache and set to 2 entries by default. To change it set CACHE_ENTRIES in AsyncTaskContactChooser. To simulate long queries uncomment *debug_simulate_long_query()* call in AsyncTaskContactChooser.

##Correctness:
Provided the queries can take long content provider operations are performed in AsyncTask. But the lifecycle of listener is not taken into account. This is documented in the class description.

##Extras:
Pallete is calcualated on the fly for a thumnail (if exists).
Some logs are added, primarily for the developer. Query time is mesured in those logs.
