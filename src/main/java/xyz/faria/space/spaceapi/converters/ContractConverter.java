package xyz.faria.space.spaceapi.converters;

import xyz.faria.space.models.Contract;

public class ContractConverter {
    public static Contract fromApiContract(Contract contract, xyz.faria.space.spaceapi.model.Contract apiContract) {
        contract.setId(apiContract.getId());
        contract.setAccepted(apiContract.getAccepted());
        contract.setFulfilled(apiContract.getFulfilled());
        contract.setType(apiContract.getType());
        contract.setTerms(apiContract.getTerms());
        contract.setFactionSymbol(apiContract.getFactionSymbol());
        if (apiContract.getDeadlineToAccept() != null) {
            contract.setDeadlineToAccept(apiContract.getDeadlineToAccept().toLocalDateTime());
        }

        return contract;
    }

    public static Contract fromApiContract(xyz.faria.space.spaceapi.model.Contract apiContract) {
        return fromApiContract(new Contract(), apiContract);
    }
}
