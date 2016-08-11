# SyncAdapterExample
Android SyncAdapter can be hard for beginners. Here is small example project

##Steps

###1. Create Stub Authenticator

Even if you have no plans of using Authentication in your app, you MUST have Authenticator implementation.
It can be dummy (stub) but it has to be there.

You will create it in 4 simple steps:
    
1. Extend AbstractAccountAuthenticator and implement empty methods
2. Create service that will run implementatuion of Authenticator: AuthenticatorService
3. In "xml" directory in your "res" create authenticator.xml with following content

	
```xml
   <?xml version="1.0" encoding="utf-8"?>
	   <account-authenticator 
			xmlns:android="http://schemas.android.com/apk/res/android"
            android:accountType="com.eutechpro.syncadapterexample"
            android:icon="@drawable/ic_launcher"
            android:smallIcon="@drawable/ic_launcher"
            android:label="@string/app_name" />
```

		
	Please, pay attention to **android:accountType="com.eutechpro.syncadapterexample"**



4. Define *StubAuthenticatorService* in *AndroidManifest.xml* file
   
   
```XML
   	<service android:name="com.eutechpro.syncadapterexample.StubAuthenticatorService">
            <intent-filter>
                <action android:name="android.accounts.AccountAuthenticator" />
            </intent-filter>
            <meta-data
                android:name="android.accounts.AccountAuthenticator"
                android:resource="@xml/authenticator" />
        </service>
```
   	


###2. Create Stub ContentProvider

Again, the same story as for Authenticator. Even if you do not need ContentProvider, you MUST have one.
Steps to have it:

1. Extend *ContentProvider* and implement empty methods
2. Define ContentProvider i *AndroidManifest.xml* as any other CntentProvider

	
```XML
	<provider
            android:name="com.eutechpro.syncadapterexample.StubContentProvider"
            android:authorities="com.eutechpro.syncadapterexample.provider"
            android:exported="false"
            android:syncable="true"/>
```       	
       
Here, pay attention to authorities value: 			
**android:authorities="com.eutechpro.syncadapterexample.provider"**



###3. Create SyncAdapter

Now, we are getting to the real deal. Create SyncAdapter extending AbstractThreadedSyncAdapter.
Method **onPerformSync** will be called upon successfull sync call, so there should be your magic code.

But, that is not all. You need few more steps.

First, add xml settings. Afain, in "xml" directory add **syncadapter.xml** with following code:

```XML
	<?xml version="1.0" encoding="utf-8"?>
	<sync-adapter xmlns:android="http://schemas.android.com/apk/res/android"
              android:accountType="com.eutechpro.syncadapterexample"
              android:allowParallelSyncs="false"
              android:contentAuthority="com.eutechpro.syncadapterexample.provider"
              android:isAlwaysSyncable="true"
              android:supportsUploading="false"
              android:userVisible="true" />
```


Pay attention on

```XML
	android:accountType="com.eutechpro.syncadapterexample"
	android:contentAuthority="com.eutechpro.syncadapterexample.provider"
```

For the rest of attributes, please refer to [documentation](https://developer.android.com/training/sync-adapters/creating-sync-adapter.html#CreateSyncAdapterMetadata)

And final step is to create Service that will run your SyncAdapter: **SyncAdapterService**.

Of course, define it in Manifset file

```XML
		<service
            android:name="ch.teleboy.sync_app_settings.SyncAdapterService"
            android:exported="true"
            android:process=":sync">
            <intent-filter>
                <action android:name="android.content.SyncAdapter" />
            </intent-filter>
            <meta-data
                android:name="android.content.SyncAdapter"
                android:resource="@xml/syncadapter" />
        </service>
```


###4. Syncing

Starting Sync process is simple.

1. Define account. It doesn't metter if you already created same account
		
		
```Java
		public static final String AUTHORITY    = "com.eutechpro.syncadapterexample.provider";
    	public static final String ACCOUNT_TYPE = "com.eutechpro.syncadapterexample";
    	public static final String ACCOUNT      = "dummyaccount";

		Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
		accountManager.addAccountExplicitly(newAccount, null, null)
```
		
2. Start SyncAdapter
	
	
```Java
		ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
```

3. If you want to force Sync process, call

		
```Java
		Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(newAccount, AUTHORITY, bundle);
```


###5. Add permissions

At the and, add permissions in manifest

```XML
	<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS" />
    	<uses-permission android:name="android.permission.READ_SYNC_SETTINGS" />
    	<uses-permission android:name="android.permission.WRITE_SYNC_SETTINGS" />
    	<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS" />
```


###5.Enjoy!
