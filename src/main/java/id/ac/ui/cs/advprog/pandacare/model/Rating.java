package id.ac.ui.cs.advprog.pandacare.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @OneToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;
    
    @Column(name = "score", nullable = false)
    private int score;
    
    @Column(name = "review", columnDefinition = "TEXT")
    private String review;
    
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp  
    private LocalDateTime updatedAt;

    public void setScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5");
        }
        this.score = score;
    }
    
    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", doctor=" + (doctor != null ? doctor.getName() : "null") +
                ", patient=" + (patient != null ? patient.getName() : "null") +
                ", score=" + score +
                ", review='" + review + '\'' +
                '}';
    }
}