package url;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Test;

public class SimpleURLTest {

    @Test
    public void testSimpleParsing() throws MalformedURLException {
        SimpleURL url = new SimpleURL("https://graph.facebook.com/750198471659552/accounts/test-users?method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4");
        assertEquals("https", url.getProtocol());
        assertEquals("graph.facebook.com", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/750198471659552/accounts/test-users", url.getPath());
        assertEquals("method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4", url.getQuery());
    }

    @Test
    public void testRootRelativeURIWithRootContext() throws MalformedURLException {

        SimpleURL context = new SimpleURL("https://graph.facebook.com");
        
        SimpleURL url = new SimpleURL(context, "/750198471659552/accounts/test-users?method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4");
        
        assertEquals("https", url.getProtocol());
        assertEquals("graph.facebook.com", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/750198471659552/accounts/test-users", url.getPath());
        assertEquals("method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4", url.getQuery());
    }
    
    @Test
    public void testRootRelativeURIWithNonRootContext() throws MalformedURLException {

        SimpleURL context = new SimpleURL("https://graph.facebook.com/foo/bar");
        
        SimpleURL url = new SimpleURL(context, "/750198471659552/accounts/test-users?method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4");
        
        assertEquals("https", url.getProtocol());
        assertEquals("graph.facebook.com", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/750198471659552/accounts/test-users", url.getPath());
        assertEquals("method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4", url.getQuery());
    }
    
    @Test
    public void testAbsoluteURIWithContext() throws MalformedURLException {

        SimpleURL context = new SimpleURL("https://hello.com/foo/bar");
        
        SimpleURL url = new SimpleURL(context, "https://graph.facebook.com/750198471659552/accounts/test-users?method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4");
        
        assertEquals("https", url.getProtocol());
        assertEquals("graph.facebook.com", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/750198471659552/accounts/test-users", url.getPath());
        assertEquals("method=get&access_token=750198471659552lleveCvbUu_zqBa9tkT3tcgaPh4", url.getQuery());
    }
}

