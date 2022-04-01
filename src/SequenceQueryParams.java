/**
 * Created by ravikanthk on 3/31/22.
 */
public class SequenceQueryParams {
    private String orgId;
    private String userId;
    private String sessionId;
    private String pageId;
    private String seq;
    private String pageStart;
    private String prevBundleTime;
    private String lastActivity;
    private String isNewSession;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getPageStart() {
        return pageStart;
    }

    public void setPageStart(String pageStart) {
        this.pageStart = pageStart;
    }

    public String getPrevBundleTime() {
        return prevBundleTime;
    }

    public void setPrevBundleTime(String prevBundleTime) {
        this.prevBundleTime = prevBundleTime;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getIsNewSession() {
        return isNewSession;
    }

    public void setIsNewSession(String isNewSession) {
        this.isNewSession = isNewSession;
    }
}
