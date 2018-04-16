package c.calvinc.appstorelist.repository;

/**
 * 2018-04-13
 *
 * @author calvinc
 */
public class NetworkState {
    private final Status status;
    private final String msg;

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isFinished() {
        return this.status == Status.SUCCESS || this.status == Status.FAILED;
    }
}
