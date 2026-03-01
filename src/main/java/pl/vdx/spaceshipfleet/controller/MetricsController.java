package pl.vdx.spaceshipfleet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.vdx.spaceshipfleet.service.RequestCounterService;  // ← TEN import

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final RequestCounterService requestCounterService;

    public MetricsController(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @GetMapping("/requests-count")
    public long getRequestsCount() {
        return requestCounterService.getCount();
    }
}
