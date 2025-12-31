package xyz.faria.space.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import xyz.faria.space.spaceapi.model.ContractTerms;

@Getter
@Setter
@Entity
@Table(name = "contract")
public class Contract {

    @javax.annotation.Nonnull
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @javax.annotation.Nonnull
    @Column(name = "faction_symbol", nullable = false)
    private String factionSymbol;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @javax.annotation.Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private xyz.faria.space.spaceapi.model.Contract.TypeEnum type;


    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "deadline", column = @Column(name = "terms_deadline", nullable = false)),
        @AttributeOverride(name = "payment.onAccepted", column = @Column(name = "terms_payment_on_accepted", nullable = false)),
        @AttributeOverride(name = "payment.onFulfilled", column = @Column(name = "terms_payment_on_fulfilled", nullable = false))
    })
    private ContractTerms terms;

    @javax.annotation.Nonnull
    @Column(name = "accepted", nullable = false)
    private Boolean accepted = false;

    @javax.annotation.Nonnull
    @Column(name = "fulfilled", nullable = false)
    private Boolean fulfilled = false;

    @javax.annotation.Nullable
    @Column(name = "deadline_to_accept")
    private LocalDateTime deadlineToAccept;

}
