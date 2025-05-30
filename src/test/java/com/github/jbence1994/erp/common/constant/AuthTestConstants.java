package com.github.jbence1994.erp.common.constant;

public interface AuthTestConstants {
    String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    String MALFORMED_BEARER_TOKEN = "NotBearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    String INVALID_BEARER_TOKEN = "Bearer invalidToken";
    String JWT_SECRET_KEY = "I9HI974VGqzWMeZ4S4cxgAQXbcwOZhF5Pv+c6B0bVY22T0aNOz6R11+zIMvKW1y/BEtk6ggMgkuFUZILQUcKLaJMuJaA6cGEKpOyS2iQ4oE=";
    int ACCESS_TOKEN_EXPIRATION = 300;
    int REFRESH_TOKEN_EXPIRATION = 604800;
    String MALFORMED_TOKEN = "this.is.not.a.jwt";
}
