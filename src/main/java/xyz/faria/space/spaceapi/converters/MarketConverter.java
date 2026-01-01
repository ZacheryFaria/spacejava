package xyz.faria.space.spaceapi.converters;

import xyz.faria.space.models.Market;

public class MarketConverter {

    public static Market fromApiMarket(Market market,
        xyz.faria.space.spaceapi.model.Market apiMarket) {
        market.setExchange(apiMarket.getExchange());
        market.setExports(apiMarket.getExports());
        market.setImports(apiMarket.getImports());
        market.setSymbol(apiMarket.getSymbol());
        market.setTradeGoods(apiMarket.getTradeGoods());
        market.setTransactions(apiMarket.getTransactions());
        return market;
    }

    public static Market fromApiMarket(xyz.faria.space.spaceapi.model.Market apiMarket) {
        return fromApiMarket(new Market(), apiMarket);
    }
}
