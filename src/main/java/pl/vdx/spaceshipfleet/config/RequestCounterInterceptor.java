package pl.vdx.spaceshipfleet.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pl.vdx.spaceshipfleet.service.RequestCounterService;

@Component
@RequiredArgsConstructor
public class RequestCounterInterceptor implements HandlerInterceptor {

    private final RequestCounterService counterService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        counterService.increment();
        return true;
    }
}
