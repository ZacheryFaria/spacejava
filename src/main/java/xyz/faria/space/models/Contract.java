package xyz.faria.space.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.EmbeddedColumnNaming;
import xyz.faria.space.spaceapi.model.ContractTerms;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Enumerated(EnumType.ORDINAL)
    private xyz.faria.space.spaceapi.model.Contract.TypeEnum type;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "fulfilled")
    private Boolean fulfilled;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @Column(name = "deadline_to_accept")
    private LocalDateTime deadlineToAccept;

    @Embedded
    @EmbeddedColumnNaming("terms_%s")
    private ContractTerms terms;
}
