package pl.vdx.spaceshipfleet.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class RequestCounterService {

    private final AtomicLong counter = new AtomicLong(0);

    public void increment() {
        counter.incrementAndGet();
    }

    public long getCount() {
        return counter.get();
    }

    public void reset() {
        counter.set(0);
    }
}
