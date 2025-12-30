package xyz.faria.space.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "agent", indexes = {@Index(name = "idx_agent_symbol", columnList = "symbol,reset_date", unique = true)})
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "headquarters", nullable = false)
    private String headquarters;

    @Column(name = "starting_faction", nullable = false)
    private String startingFaction;

    @Column(name = "credits", nullable = false)
    private Long credits;

    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(name = "reset_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date resetDate;
}