package id.ac.ui.cs.advprog.pandacare.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    private Long consultationId;
    private int score;
    private String review;
}