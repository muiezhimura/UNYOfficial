package id.ac.uny.unyofficial;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by groovyle on 2018-01-14.
 */

public class NetworkInfoUtility {
    public static final String DEFAULT_HOST = "uny.ac.id";

    public Boolean checkConnectionToHost() throws UnknownHostException {
        return this.checkConnectionToHost(DEFAULT_HOST);
    }

    public Boolean checkConnectionToHost(String host) throws UnknownHostException {
        final InetAddress address = InetAddress.getByName(host);
        return !address.equals("");
    }
}
