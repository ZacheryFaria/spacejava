package xyz.faria.space.models;


import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import xyz.faria.space.spaceapi.model.MarketTradeGood;
import xyz.faria.space.spaceapi.model.MarketTransaction;
import xyz.faria.space.spaceapi.model.TradeGood;

@Getter
@Setter
@Entity
@Table(name = "market")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Waypoint waypoint;

    @javax.annotation.Nonnull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @javax.annotation.Nonnull
    @ElementCollection
    private List<TradeGood> exports = new ArrayList<>();

    @javax.annotation.Nonnull
    @ElementCollection
    private List<TradeGood> imports = new ArrayList<>();

    @javax.annotation.Nonnull
    @ElementCollection
    private List<TradeGood> exchange = new ArrayList<>();

    @javax.annotation.Nullable
    @ElementCollection
    private List<MarketTransaction> transactions = new ArrayList<>();

    @javax.annotation.Nullable
    @ElementCollection
    private List<MarketTradeGood> tradeGoods = new ArrayList<>();
}
