package url;

import java.net.URI;
import java.net.URL;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.logic.BlackHole;

public class UrlParserBenchmark {

    @State(Scope.Thread)
    public static class ThreadState {
        private int i = -1;

        public int next() {
            i++;
            if (i == Data.URLS.length)
                i = 0;
            return i;
        }
    }

    @GenerateMicroBenchmark
    public void parseSimpleURL(ThreadState state, BlackHole bh) throws Exception {
        int i = state.next();
        bh.consume(new SimpleURL(Data.URLS[i]));
    }

    @GenerateMicroBenchmark
    public void parseURI(ThreadState state, BlackHole bh) throws Exception {
        int i = state.next();
        bh.consume(URI.create(Data.URLS[i]));
    }
    
    @GenerateMicroBenchmark
    public void parseURL(ThreadState state, BlackHole bh) throws Exception {
        int i = state.next();
        bh.consume(new URL(Data.URLS[i]));
    }
}
