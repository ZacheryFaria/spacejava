package xyz.faria.space.services;

import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import xyz.faria.space.models.Contract;
import xyz.faria.space.models.Ship;
import xyz.faria.space.repositories.ContractRepository;
import xyz.faria.space.spaceapi.api.ContractsApi;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.converters.ContractConverter;
import xyz.faria.space.spaceapi.model.DeliverContractRequest;
import xyz.faria.space.spaceapi.model.ShipCargoItem;
import xyz.faria.space.spaceapi.model.TradeSymbol;

@Service
public class ContractService {

    private static final Logger logger = Logger.getLogger(ContractService.class.getName());

    private final ContractRepository contractRepository;
    private final ShipService shipService;

    public ContractService(ContractRepository contractRepository, ShipService shipService) {
        this.contractRepository = contractRepository;
        this.shipService = shipService;
    }

    public void negotiateContract(Ship ship) throws ApiException {
        var fleetApi = ship.getFleetApi();

        var response = fleetApi.negotiateContract(ship.getSymbol());
        var contract = response.getData().getContract();

        var contractModel = ContractConverter.fromApiContract(contract);
        contractModel.setAgent(ship.getAgent());
        contractRepository.save(contractModel);
    }

    public void acceptContract(Contract contract) throws ApiException {
        var contractApi = new ContractsApi(contract.getAgent().getAgentClient());
        var response = contractApi.acceptContract(contract.getId());
        ContractConverter.fromApiContract(contract, response.getData().getContract());
        contractRepository.save(contract);
    }

    public void fulfillContract(Contract contract) throws ApiException {
        var contractApi = new ContractsApi(contract.getAgent().getAgentClient());
        var response = contractApi.fulfillContract(contract.getId());
        ContractConverter.fromApiContract(contract, response.getData().getContract());
        contractRepository.save(contract);
    }

    public void deliverContract(Contract contract, Ship ship) throws ApiException {
        var contractApi = new ContractsApi(contract.getAgent().getAgentClient());
        var deliverRequest = createDeliverContractRequest(contract, ship);
        var response = contractApi.deliverContract(contract.getId(), deliverRequest);
        ContractConverter.fromApiContract(contract, response.getData().getContract());
        contractRepository.save(contract);
    }

    private DeliverContractRequest createDeliverContractRequest(Contract contract, Ship ship) {
        var terms = contract.getTerms();

        ShipCargoItem itemToTransfer = null;

        for (var termItem : terms.getDeliver()) {
            try {
                TradeSymbol symbol = TradeSymbol.valueOf(termItem.getTradeSymbol());
                var item = ship.getCargo().findItem(symbol);
                if (item.isPresent()) {
                    itemToTransfer = item.get();
                    break;
                }
            } catch (IllegalArgumentException e) {
                logger.warning(
                    "Invalid trade symbol in contract delivery: " + termItem.getTradeSymbol());
            }
        }

        if (itemToTransfer == null) {
            throw new IllegalStateException("No item to deliver in contract");
        }

        return new DeliverContractRequest()
            .tradeSymbol(itemToTransfer.getSymbol().toString())
            .shipSymbol(ship.getSymbol())
            .units(itemToTransfer.getUnits());
    }
}
