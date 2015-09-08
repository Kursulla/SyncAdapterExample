package ch.teleboy.sync_app_settings;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service that do "background work" for authenticating user for SyncAdapter
 *
 * Created by Kursulla on 07/09/15.
 */
public class StubAuthenticatorService extends Service {
    private StubAuthenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new StubAuthenticator(this);
    }

    /*
    * When the system binds to this Service to make the RPC call
    * return the authenticator’s IBinder.
    */
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
