package xyz.faria.space.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import xyz.faria.space.spaceapi.client.ApiClient;

@Getter
@Setter
@Entity
@Table(name = "agent", indexes = {
    @Index(name = "idx_agent_symbol", columnList = "symbol,reset_id", unique = true)})
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

    @ManyToOne
    @JoinColumn(name = "reset_id")
    private Reset reset;

    public ApiClient getAgentClient() {
        return ApiClient.getAgentApiClient(this.getToken());
    }
}