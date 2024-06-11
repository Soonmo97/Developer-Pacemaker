package project.developer_pacemaker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "archivedTodo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchivedTodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aSeq")
    private long aSeq;

    @Column(name = "pSeq", nullable = false)
    private long pSeq;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "duration", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float duration;

    @Column(name = "isCompleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isCompleted;
}
