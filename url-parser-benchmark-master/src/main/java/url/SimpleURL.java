/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package url;

import java.net.MalformedURLException;

public class SimpleURL {

    private final String originalUrl;
    private String protocol;
    private String host;
    private int port = -1;
    private String query;
    private String authority;
    private String path;
    private String userInfo;
    private String ref;

    public SimpleURL(String url) throws MalformedURLException {
        this(null, url);
    }

    public SimpleURL(SimpleURL context, final String originalUrl) throws MalformedURLException {
        this.originalUrl = originalUrl;

        // local vars
        String url = originalUrl;
        String protocol = null;
        String host = null;
        int port = -1;
        String query = null;
        String authority = null;
        String path = null;
        String userInfo = null;
        String ref = null;

        int i, limit, c;
        int start = 0;
        boolean aRef = false;
        boolean isRelative = false;

        try {
            limit = originalUrl.length();
            while (limit > 0 && originalUrl.charAt(limit - 1) <= ' ') {
                limit--; // eliminate trailing whitespace
            }
            while (start < limit && originalUrl.charAt(start) <= ' ') {
                start++; // eliminate leading whitespace
            }

            if (originalUrl.regionMatches(true, start, "url:", 0, 4)) {
                start += 4;
            }

            /*
             * we're assuming this is a ref relative to the context URL. This
             * means protocols cannot start w/ '#', but we must parse ref URL's
             * like: "hello:there" w/ a ':' in them.
             */
            aRef = start < originalUrl.length() && originalUrl.charAt(start) == '#';

            for (i = start; !aRef && i < limit && (c = originalUrl.charAt(i)) != '/'; i++) {
                if (c == ':') {

                    String s = originalUrl.substring(start, i).toLowerCase();
                    if (isValidProtocol(s)) {
                        protocol = s;
                        start = i + 1;
                    }
                    break;
                }
            }

            // Only use our context if the protocols match.
            if (context != null && (protocol == null || protocol.equalsIgnoreCase(context.protocol))) {

                // If the context is a hierarchical URL scheme and the spec
                // contains a matching scheme then maintain backwards
                // compatibility and treat it as if the spec didn't contain
                // the scheme; see 5.2.3 of RFC2396
                if (context.path != null && context.path.startsWith("/"))
                    protocol = null;

                if (protocol == null) {
                    protocol = context.protocol;
                    authority = context.authority;
                    userInfo = context.userInfo;
                    host = context.host;
                    port = context.port;
                    path = context.path;
                    isRelative = true;
                }
            }

            if (protocol == null) {
                throw new MalformedURLException("no protocol: " + originalUrl);
            }

            i = originalUrl.indexOf('#', start);
            if (i >= 0) {
                ref = originalUrl.substring(i + 1, limit);
                limit = i;
            }

            /*
             * Handle special case inheritance of query and fragment implied by
             * RFC2396 section 5.2.2.
             */
            if (isRelative && start == limit) {
                query = context.query;
                if (ref == null) {
                    ref = context.ref;
                }
            }

            boolean isRelPath = false;
            boolean queryOnly = false;

            // FIX: should not assume query if opaque
            // Strip off the query part
            if (start < limit) {
                int queryStart = url.indexOf('?');
                queryOnly = queryStart == start;
                if ((queryStart != -1) && (queryStart < limit)) {
                    query = url.substring(queryStart + 1, limit);
                    if (limit > queryStart)
                        limit = queryStart;
                    url = url.substring(0, queryStart);
                }
            }

            i = 0;
            // Parse the authority part if any
            boolean isUNCName = (start <= limit - 4) && (url.charAt(start) == '/') && (url.charAt(start + 1) == '/') && (url.charAt(start + 2) == '/')
                    && (url.charAt(start + 3) == '/');
            if (!isUNCName && (start <= limit - 2) && (url.charAt(start) == '/') && (url.charAt(start + 1) == '/')) {
                start += 2;
                i = url.indexOf('/', start);
                if (i < 0) {
                    i = url.indexOf('?', start);
                    if (i < 0)
                        i = limit;
                }

                host = authority = url.substring(start, i);

                int ind = authority.indexOf('@');
                if (ind != -1) {
                    userInfo = authority.substring(0, ind);
                    host = authority.substring(ind + 1);
                } else {
                    userInfo = null;
                }
                if (host != null) {
                    // If the host is surrounded by [ and ] then its an IPv6
                    // literal address as specified in RFC2732
                    if (host.length() > 0 && (host.charAt(0) == '[')) {
                        if ((ind = host.indexOf(']')) > 2) {

                            String nhost = host;
                            host = nhost.substring(0, ind + 1);
                            if (!SimpleIPAddressUtil.isIPv6LiteralAddress(host.substring(1, ind))) {
                                throw new IllegalArgumentException("Invalid host: " + host);
                            }

                            port = -1;
                            if (nhost.length() > ind + 1) {
                                if (nhost.charAt(ind + 1) == ':') {
                                    ++ind;
                                    // port can be null according to RFC2396
                                    if (nhost.length() > (ind + 1)) {
                                        port = Integer.parseInt(nhost.substring(ind + 1));
                                    }
                                } else {
                                    throw new IllegalArgumentException("Invalid authority field: " + authority);
                                }
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid authority field: " + authority);
                        }
                    } else {
                        ind = host.indexOf(':');
                        port = -1;
                        if (ind >= 0) {
                            // port can be null according to RFC2396
                            if (host.length() > (ind + 1)) {
                                port = Integer.parseInt(host.substring(ind + 1));
                            }
                            host = host.substring(0, ind);
                        }
                    }
                } else {
                    host = "";
                }
                if (port < -1)
                    throw new IllegalArgumentException("Invalid port number :" + port);
                start = i;
                // If the authority is defined then the path is defined by the
                // spec only; See RFC 2396 Section 5.2.4.
                if (authority != null && authority.length() > 0)
                    path = "";
            }

            if (host == null) {
                host = "";
            }

            // Parse the file path if any
            if (start < limit) {
                if (url.charAt(start) == '/') {
                    path = url.substring(start, limit);
                } else if (path != null && path.length() > 0) {
                    isRelPath = true;
                    int ind = path.lastIndexOf('/');
                    String seperator = "";
                    if (ind == -1 && authority != null)
                        seperator = "/";
                    path = path.substring(0, ind + 1) + seperator + url.substring(start, limit);

                } else {
                    String seperator = (authority != null) ? "/" : "";
                    path = seperator + url.substring(start, limit);
                }
            } else if (queryOnly && path != null) {
                int ind = path.lastIndexOf('/');
                if (ind < 0)
                    ind = 0;
                path = path.substring(0, ind) + "/";
            }
            if (path == null)
                path = "";

            if (isRelPath) {
                // Remove embedded /./
                while ((i = path.indexOf("/./")) >= 0) {
                    path = path.substring(0, i) + path.substring(i + 2);
                }
                // Remove embedded /../ if possible
                i = 0;
                while ((i = path.indexOf("/../", i)) >= 0) {
                    /*
                     * A "/../" will cancel the previous segment and itself,
                     * unless that segment is a "/../" itself i.e. "/a/b/../c"
                     * becomes "/a/c" but "/../../a" should stay unchanged
                     */
                    if (i > 0 && (limit = path.lastIndexOf('/', i - 1)) >= 0 && (path.indexOf("/../", limit) != 0)) {
                        path = path.substring(0, limit) + path.substring(i + 3);
                        i = 0;
                    } else {
                        i = i + 3;
                    }
                }
                // Remove trailing .. if possible
                while (path.endsWith("/..")) {
                    i = path.indexOf("/..");
                    if ((limit = path.lastIndexOf('/', i - 1)) >= 0) {
                        path = path.substring(0, limit + 1);
                    } else {
                        break;
                    }
                }
                // Remove starting .
                if (path.startsWith("./") && path.length() > 2)
                    path = path.substring(2);

                // Remove trailing .
                if (path.endsWith("/."))
                    path = path.substring(0, path.length() - 1);
            }

            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.userInfo = userInfo;
            this.path = path;
            this.ref = ref;
            this.query = query;
            this.authority = authority;

        } catch (MalformedURLException e) {
            throw e;
        } catch (Exception e) {
            MalformedURLException exception = new MalformedURLException(e.getMessage());
            exception.initCause(e);
            throw exception;
        }
    }

    private boolean isValidProtocol(String protocol) {
        int len = protocol.length();
        if (len < 1)
            return false;
        char c = protocol.charAt(0);
        if (!Character.isLetter(c))
            return false;
        for (int i = 1; i < len; i++) {
            c = protocol.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '.' && c != '+' && c != '-') {
                return false;
            }
        }
        return true;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getQuery() {
        return query;
    }

    public String getPath() {
        return path;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public String getAuthority() {
        return authority;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getRef() {
        return ref;
    }
}
