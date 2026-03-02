package pl.vdx.spaceshipfleet.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestCounterServiceTest {

    @Test
    void increment_shouldIncreaseCount() {
        // given
        RequestCounterService service = new RequestCounterService();

        // when
        service.increment();
        service.increment();

        // then
        assertThat(service.getCount()).isEqualTo(2L);
    }

    @Test
    void reset_shouldSetCountToZero() {
        // given
        RequestCounterService service = new RequestCounterService();
        service.increment();
        service.increment();

        // when
        service.reset();

        // then
        assertThat(service.getCount()).isEqualTo(0L);
    }
}
