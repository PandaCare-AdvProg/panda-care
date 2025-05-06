package id.ac.ui.cs.advprog.pandacare.observer;
import id.ac.ui.cs.advprog.pandacare.model.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


class ConsultationObserverTest {

    private ConsultationObserver observer;
    private Consultation consultation;

    @BeforeEach
    void setUp() {
        observer = mock(ConsultationObserver.class);
        consultation = mock(Consultation.class);
    }

    @Test
    void testUpdateIsCalledWithCorrectParameters() {
        String message = "Consultation updated";

        observer.update(consultation, message);

        verify(observer, times(1)).update(consultation, message);
    }
}
