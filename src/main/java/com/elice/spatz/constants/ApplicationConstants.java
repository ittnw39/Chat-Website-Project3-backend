package com.elice.spatz.constants;

public final class ApplicationConstants {

    public static final String JWT_SECRET_KEY = "JWT_SECRET_KEY";
    public static final String JWT_SECRET_DEFAULT_VALUE = "dpffltmchlwhdvmfhwprxmqlalfzldlqslek.dkanehwjqrmsgkftndjqttmqslek.";
    public static final String JWT_HEADER = "Authorization";

    // Access Token Expiration time : 30 minutes
    public static final Long ACCESS_TOKEN_EXPIRATION = 1800000L;
    // Refresh Token Expiration time : 24 hours
    public static final Long REFRESH_TOKEN_EXPIRATION = 86400000L;
    // Refresh Token ReIssue limit : 5
    public static final int REFRESH_TOKEN_RE_ISSUE_LIMIT = 5;
}
