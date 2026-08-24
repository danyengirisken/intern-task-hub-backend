package com.danyengirisken.interntaskhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Görev (task) modülü ana varligi. Carbon domain entity konvansiyonu
 * (prefiks + sequence ID + Auditable) referans alinmistir.
 *
 *  status   : TODO | IN_PROGRESS | DONE
 *  priority : LOW | MEDIUM | HIGH
 */
@Entity
@Table(name = "T_TASK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Task extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_TASK")
    @SequenceGenerator(name = "SEQ_T_TASK", sequenceName = "SEQ_T_TASK", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    /** Gorevin ait oldugu proje (zorunlu). */
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    /** Gorevin alindigi sprint; null ise gorev backlog'dadir. */
    @Column(name = "sprint_id")
    private Long sprintId;

    /**
     * Gorevin ait oldugu partner (tenant). Kayit sirasinda projeden turetilir,
     * istemciden alinmaz — boylece proje ile partner asla ayrisamaz.
     */
    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "due_date")
    private LocalDate dueDate;
}
