// IBackService.aidl
package smartmall.socket;

// Declare any non-default types here with import statements

interface IBackService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void initServer(String deviceIdHex);
    boolean sendMessage(String message);
    boolean startSendHeartMessage(String deviceIdHex);
    boolean removeSendHeartMessage();
    boolean hasHeartBeat();
}
